import Ionicons from 'react-native-vector-icons/Ionicons';
import {
  FlatList,
  Pressable,
  SafeAreaView,
  StyleSheet,
  Text,
  Alert,
  View,
} from 'react-native';
import {
  useNavigation,
  useRoute,
  useFocusEffect,
} from '@react-navigation/native';
import { useCallback } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';

import AddressCard from '../../components/AddressCard';
import BackButton from '../../components/BackButton';
import { useAddress } from '../../contexts/addressContext';
import styles from '../../styles/globalStyles';
import Fonts from '../../styles/font';
import Colors from '../../styles/colors';
import { markAddressAsDefault } from '../../services/customer/addressService';

const Address = () => {
  const {
    setMode,
    setAddressData,
    address,
    selectedAddressId,
    setSelectedAddressId,
    deleteAddress,
    setAddress,
    markAsDefault,
    syncAddressesFromServer,
  } = useAddress();

  const navigation = useNavigation();
  const route = useRoute();
  const fromCart = route.params?.fromCart === 'true';
  const hideDelete = fromCart;

  const handleSelectAddress = async id => {
    setSelectedAddressId(String(id));
    await AsyncStorage.setItem('selectedAddressId', String(id));
  };
  
  

  const handleAddAddress = () => {
    setMode('add');
    setAddressData(null);
    navigation.navigate('AddressForm');
  };

  const handleEditAddress = address => {
    setMode('edit');
    setAddressData(address);
    navigation.navigate('AddressForm');
  };

  const handleDeleteAddress = item => {
    Alert.alert(
      'Delete Address',
      'Are you sure you want to delete this address?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Delete',
          style: 'destructive',
          onPress: async () => {
            const isDeletingDefault = item.id === selectedAddressId;

            deleteAddress(item.id);

            const remainingAddresses = address.filter(a => a.id !== item.id);
            if (isDeletingDefault && remainingAddresses.length > 0) {
              const fallback = remainingAddresses[0];
              await handleSelectAddress(fallback.id);
            } else if (remainingAddresses.length === 0) {
              setSelectedAddressId(null);
              await AsyncStorage.removeItem('selectedAddressId');
            }
          },
        },
      ],
    );
  };

  useFocusEffect(
    useCallback(() => {
      const load = async () => {
        try {
          await syncAddressesFromServer();

          const selectedAddress = await AsyncStorage.getItem('selectedAddressId');
          console.log(selectedAddress)
          if (selectedAddress) {
            setSelectedAddressId(selectedAddress);
          } else {
            const storedList = await AsyncStorage.getItem('address');
            const parsedList = storedList ? JSON.parse(storedList) : [];

            const defaultAddr = parsedList.find(a => a.isDefault);
            console.log("defaultAddr",defaultAddr)

            if (defaultAddr) {
              setSelectedAddressId(String(defaultAddr.id));
              await AsyncStorage.setItem(
                'selectedAddressId',
                String(defaultAddr.id),
              );
            } else if (parsedList.length > 0) {
              const first = parsedList[0];
              setSelectedAddressId(String(first.id));
              await AsyncStorage.setItem('selectedAddressId', String(first.id));
            } else {
              setSelectedAddressId(null);
              await AsyncStorage.removeItem('selectedAddressId');
            }
          }

          // ✅ Clear fromCart param after initial mount (to hide Change Address later)
          if (route.params?.fromCart) {
            navigation.setParams({ fromCart: undefined });
          }
        } catch (err) {
          console.warn('⚠️ Failed to load default address:', err.message);
        }
      };

      load();
    }, []),
  );
  
  

  console.log(
    'Address List (sorted):',
    [...address].sort((a, b) => b.id - a.id),
  );

  const handleMarkAsDefault = async item => {
    try {
      await markAsDefault(item.id); 
      await syncAddressesFromServer();
      await handleSelectAddress(item.id); 
    } catch (error) {
      console.error('Error marking address as default:', error);
    }
  };
  
  return (
    <SafeAreaView style={[styles.pageContainer, { flex: 1 }]}>
      <View style={[styles.pageContainer, { flex: 1 }]}>
        <BackButton title="Delivery Address" />

        <FlatList
          data={[...address].sort((a, b) => b.id - a.id)}
          keyExtractor={item => item.id.toString()}
          renderItem={({ item }) => (
            <AddressCard
              item={item}
              onEdit={handleEditAddress}
              onDelete={handleDeleteAddress}
              onSelect={
                fromCart ? () => handleSelectAddress(item.id) : undefined
              }
              onMarkDefault={() => handleMarkAsDefault(item)}
              isSelected={String(item.id) === String(selectedAddressId)}
              hideDelete={hideDelete || address.length === 1}
              source={fromCart ? 'cart' : 'sidebar'}
            />
          )}
          contentContainerStyle={{
            paddingHorizontal: 20,
            paddingBottom: 20,
            flexGrow: 1,
            justifyContent: address.length === 0 ? 'center' : 'flex-start',
          }}
          showsVerticalScrollIndicator={false}
          ListEmptyComponent={() => (
            <View style={innerStyle.emptyContainer}>
              <Text style={innerStyle.emptyText}>No address added</Text>
              <Pressable
                style={innerStyle.addButton}
                onPress={handleAddAddress}
              >
                <Ionicons
                  name="add-circle-outline"
                  size={24}
                  color={Colors.bgClr}
                />
                <Text style={innerStyle.addButtonText}>Add Address</Text>
              </Pressable>
            </View>
          )}
        />

        {address.length > 0 && (
          <View style={innerStyle.container}>
            <Pressable style={innerStyle.addButton} onPress={handleAddAddress}>
              <Ionicons
                name="add-circle-outline"
                size={24}
                color={Colors.bgClr}
              />
              <Text style={innerStyle.addButtonText}>Add Address</Text>
            </Pressable>
          </View>
        )}
      </View>
    </SafeAreaView>
  );
};

const innerStyle = StyleSheet.create({
  container: {
    alignItems: 'center',
    marginVertical: 20,
  },
  emptyContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 30,
    paddingHorizontal: 20,
  },
  emptyText: {
    fontSize: Fonts.sizes.base,
    color: Colors.secondaryText,
    marginBottom: 20,
    textAlign: 'center',
  },
  addButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Colors.primary,
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 50,
  },
  addButtonText: {
    color: Colors.bgClr,
    fontSize: Fonts.sizes.base,
    fontWeight: '500',
    marginLeft: 8,
  },
});

export default Address;
