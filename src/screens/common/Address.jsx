import Ionicons from 'react-native-vector-icons/Ionicons';
import {
  FlatList,
  Pressable,
  SafeAreaView,
  StyleSheet,
  Text,
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

const Address = () => {
  const {
    setMode,
    setAddressData,
    address,
    selectedAddressId,
    setSelectedAddressId,
    deleteAddress,
    setAddress,
  } = useAddress();

  const navigation = useNavigation();
  const route = useRoute();
  const fromCart = route.params?.fromCart === 'true';
  const hideDelete = fromCart;

  const handleSelectAddress = id => {
    setSelectedAddressId(id);
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
    // Show confirmation alert
    Alert.alert(
      'Delete Address',
      'Are you sure you want to delete this address?',
      [
        {
          text: 'Cancel',
          style: 'cancel',
        },
        {
          text: 'Delete',
          style: 'destructive',
          onPress: () => {
            const isDeletingDefault = item.id === selectedAddressId;

            deleteAddress(item.id);

            // Select new default if needed
            if (isDeletingDefault) {
              const remainingAddresses = address.filter(a => a.id !== item.id);
              if (remainingAddresses.length > 0) {
                setSelectedAddressId(remainingAddresses[0].id);
                AsyncStorage.setItem(
                  'selectedAddressId',
                  remainingAddresses[0].id,
                );
              } else {
                setSelectedAddressId(null);
                AsyncStorage.removeItem('selectedAddressId');
              }
            }
          },
        },
      ],
    );
  };
  

  useFocusEffect(
    useCallback(() => {
      const fetchAddress = async () => {
        const storedAddress = await AsyncStorage.getItem('address');
        const storedSelectedId = await AsyncStorage.getItem(
          'selectedAddressId',
        );

        if (storedAddress) setAddress(JSON.parse(storedAddress));
        if (storedSelectedId) setSelectedAddressId(storedSelectedId);
      };

      fetchAddress();
    }, []),
  );

  return (
    <SafeAreaView style={{ flex: 1 }}>
      <View style={[styles.pageContainer, { flex: 1 }]}>
        <BackButton title="Delivery Address" />

        <FlatList
          data={address}
          keyExtractor={item => item.id}
          renderItem={({ item }) => (
            <AddressCard
              item={item}
              onEdit={handleEditAddress}
              onDelete={handleDeleteAddress}
              isSelected={item.id === selectedAddressId}
              onSelect={() => handleSelectAddress(item.id)}
              hideDelete={hideDelete}
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
                <Ionicons name="add-circle-outline" size={24} color="#fff" />
                <Text style={innerStyle.addButtonText}>Add Address</Text>
              </Pressable>
            </View>
          )}
        />

        {address.length > 0 && (
          <View style={innerStyle.container}>
            <Pressable style={innerStyle.addButton} onPress={handleAddAddress}>
              <Ionicons name="add-circle-outline" size={24} color="white" />
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
    color: '#888',
    marginBottom: 20,
    textAlign: 'center',
  },
  addButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#4CAF50',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 50,
  },
  addButtonText: {
    color: '#fff',
    fontSize: Fonts.sizes.base,
    fontWeight: '500',
    marginLeft: 8,
  },
});

export default Address;
