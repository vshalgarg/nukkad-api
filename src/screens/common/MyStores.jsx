import React, { useEffect, useState } from 'react';
import {
  Alert,
  FlatList,
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useRoute } from '@react-navigation/native';
import AntDesign from 'react-native-vector-icons/AntDesign';
import Ionicons from 'react-native-vector-icons/Ionicons';

import BackButton from '../../components/BackButton';
import CustomButton from '../../components/CustomButton';
import { useStore } from '../../contexts/storeContext';
import Colors from '../../styles/colors';
import styles from '../../styles/globalStyles';
import { useSafeRouter } from '../../hooks/useSafeRouter';
import Fonts from '../../styles/font';

const STORAGE_KEY = '@scanned_stores';

export default function MyStores() {
  const { safePush } = useSafeRouter();
  const route = useRoute();
  const scannedData = route.params?.scannedData;

  const [stores, setStores] = useState([]);
  const [selectedStoreIndex, setSelectedStoreIndex] = useState(null);
  const { saveStore } = useStore();

  useEffect(() => {
    const loadAndAddStore = async () => {
      try {
        const jsonValue = await AsyncStorage.getItem(STORAGE_KEY);
        const savedStores = jsonValue ? JSON.parse(jsonValue) : [];

        let updatedStores = [...savedStores];

        if (scannedData) {
          let newStore;
          try {
            newStore = JSON.parse(scannedData);
          } catch (e) {
            console.warn('Invalid scanned data JSON', e);
            return;
          }

          const exists = savedStores.some(
            store =>
              store.shopName === newStore.shopName &&
              store.address === newStore.address,
          );

          if (!exists) {
            updatedStores.push(newStore);
            await AsyncStorage.setItem(
              STORAGE_KEY,
              JSON.stringify(updatedStores),
            );
          }
        }

        setStores(updatedStores);

        if (updatedStores.length > 0 && selectedStoreIndex === null) {
          setSelectedStoreIndex(0);
        }
      } catch (error) {
        console.error('Error handling scanned store data', error);
      }
    };

    loadAndAddStore();
  }, []);

  const handleAddStore = () => {
    safePush('AddStore');
  };

  const handleDelete = itemToDelete => {
    Alert.alert(
      'Delete Store',
      `Are you sure you want to delete "${itemToDelete.shopName}"?`,
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Delete',
          style: 'destructive',
          onPress: () => performDelete(itemToDelete),
        },
      ],
      { cancelable: true },
    );
  };

  const performDelete = async itemToDelete => {
    try {
      const filteredStores = stores.filter(
        store =>
          !(
            store.shopName === itemToDelete.shopName &&
            store.address === itemToDelete.address
          ),
      );

      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(filteredStores));
      setStores(filteredStores);

      if (
        selectedStoreIndex !== null &&
        stores[selectedStoreIndex].shopName === itemToDelete.shopName &&
        stores[selectedStoreIndex].address === itemToDelete.address
      ) {
        if (filteredStores.length > 0) {
          setSelectedStoreIndex(0);
        } else {
          setSelectedStoreIndex(null);
        }
      } else if (selectedStoreIndex > filteredStores.length - 1) {
        setSelectedStoreIndex(filteredStores.length - 1);
      }
    } catch (error) {
      console.error('Error deleting store:', error);
    }
  };

  const handleSubmit = () => {
    if (selectedStoreIndex !== null) {
      const selected = stores[selectedStoreIndex];
      saveStore(selected);
      safePush('CustomerDashboard');
    } else {
      console.log('⚠️ No store selected');
    }
  };

  const renderItem = ({ item, index }) => (
    <Pressable
      style={[
        innerStyle.card,
        selectedStoreIndex === index && innerStyle.selectedCard,
      ]}
      onPress={() => setSelectedStoreIndex(index)}
    >
      <View style={innerStyle.radioContainer}>
        <View style={innerStyle.iconColumn}>
          <Text style={innerStyle.shopName}>{item.shopName}</Text>
          <Text style={innerStyle.address}>{item.address}</Text>
        </View>
        <View style={innerStyle.iconColumn}>
          <Pressable onPress={() => handleDelete(item)}>
            <AntDesign name="delete" size={20} color="red" />
          </Pressable>
        </View>
      </View>
    </Pressable>
  );

  return (
    <View style={styles.pageContainer}>
      <BackButton title="My Stores" />
      <View style={innerStyle.container}>
        {stores.length === 0 ? (
          <View
            style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}
          >
            <Text style={innerStyle.noStores}>No stores scanned yet</Text>
            <View style={innerStyle.addStoreContainer}>
              <Pressable style={innerStyle.button} onPress={handleAddStore}>
                <Ionicons name="add-circle-outline" size={24} color="black" />
                <Text style={innerStyle.buttonText}> Add store</Text>
              </Pressable>
            </View>
          </View>
        ) : (
          <>
            <FlatList
              data={stores}
              keyExtractor={(item, index) => item.shopName + index}
              renderItem={renderItem}
              contentContainerStyle={{ paddingBottom: 20 }}
            />
            <View style={innerStyle.btnContainer}>
              <CustomButton
                onPress={handleAddStore}
                title="Add Store"
                className="bg-white"
                textClassName="text-black"
              />
              <CustomButton onPress={handleSubmit} title="Set As Default" />
            </View>
          </>
        )}
      </View>
    </View>
  );
}

const innerStyle = StyleSheet.create({
  container: {
    flex: 1,
    padding: 15,
    backgroundColor: 'white',
  },
  noStores: {
    fontSize: Fonts.sizes.lg,
    color: 'gray',
    marginTop: 20,
    textAlign: 'center',
  },
  addStoreContainer: {
    alignItems: 'center',
    padding: 5,
    marginVertical: 20,
  },
  button: {
    flexDirection: 'row',
    backgroundColor: Colors.primary,
    padding: 8,
    paddingHorizontal: 15,
    borderRadius: 50,
    alignItems: 'center',
  },
  buttonText: {
    fontSize: Fonts.sizes.base,
    marginLeft: 5,
  },
  card: {
    marginTop: 16,
    padding: 16,
    borderRadius: 12,
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#ddd',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  selectedCard: {
    borderColor: Colors.primary,
  },
  radioContainer: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    height: 70,
  },
  iconColumn: {
    justifyContent: 'space-around',
    height: '100%',
  },
  shopName: {
    fontSize: Fonts.sizes.lg,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 4,
  },
  address: {
    fontSize: Fonts.sizes.sm,
    color: '#666',
  },
  btnContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center',
    marginTop: 20,
    marginBottom: 20,
  },
});
