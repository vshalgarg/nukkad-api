import AsyncStorage from '@react-native-async-storage/async-storage';
import { createContext, useContext, useEffect, useState } from 'react';
import {
  getAllAddresses,
  addNewAddress,
  updateExistingAddress,
  deleteAddressFromServer,
  markAddressAsDefault,
} from '../services/customer/addressService';

const AddressContext = createContext();

export const AddressProvider = ({ children }) => {
  const [address, setAddress] = useState([]);
  const [selectedAddressId, setSelectedAddressId] = useState(null);
  const [defaultAddress, setDefaultAddress] = useState(null);
  const [mode, setMode] = useState('add');
  const [addressData, setAddressData] = useState(null);

  useEffect(() => {
    const load = async () => {
      const stored = await AsyncStorage.getItem('address');
      const storedId = await AsyncStorage.getItem('selectedAddressId');
      if (stored) {
        const parsed = JSON.parse(stored);
        setAddress(parsed);
        const def = parsed.find(a => a.default);
        if (def) setDefaultAddress(def);
      }
      if (storedId) setSelectedAddressId(storedId);
    };
    load();
  }, []);

  useEffect(() => {
    AsyncStorage.setItem('address', JSON.stringify(address));
    const def = address.find(a => a.default);
    setDefaultAddress(def || null);
  }, [address]);

  useEffect(() => {
    if (selectedAddressId)
      AsyncStorage.setItem('selectedAddressId', String(selectedAddressId));
  }, [selectedAddressId]);

  const addAddress = async data => {
    const saved = await addNewAddress(data);
    const updatedList = [saved, ...address];
    setAddress(updatedList);
    setSelectedAddressId(String(saved.id));
  };

  const updateAddress = async updated => {
    const saved = await updateExistingAddress(updated.id, updated);
    const updatedList = address.map(a => (a.id === saved.id ? saved : a));
    setAddress(updatedList);
  };

  const deleteAddress = async id => {
    await deleteAddressFromServer(id);
    const filtered = address.filter(a => a.id !== id);
    setAddress(filtered);

    if (String(id) === String(selectedAddressId)) {
      setSelectedAddressId(null);
      await AsyncStorage.removeItem('selectedAddressId');
    }
  };

  const markAsDefault = async id => {
    await markAddressAsDefault(id);
    const updatedList = address.map(a => ({
      ...a,
      default: a.id === id,
    }));
    setAddress(updatedList);
    setSelectedAddressId(String(id));
  };

  const syncAddressesFromServer = async () => {
    const fresh = await getAllAddresses();
    setAddress(fresh);
    const def = fresh.find(a => a.default);
    if (def) {
      setDefaultAddress(def);
      setSelectedAddressId(String(def.id));
    }
  };

  const resetAddress = async () => {
    setAddress([]);
    setSelectedAddressId(null);
    setDefaultAddress(null);
    await AsyncStorage.removeItem('address');
    await AsyncStorage.removeItem('selectedAddressId');
  };

  return (
    <AddressContext.Provider
      value={{
        address,
        selectedAddressId,
        setSelectedAddressId,
        defaultAddress,
        addAddress,
        updateAddress,
        deleteAddress,
        markAsDefault,
        syncAddressesFromServer,
        resetAddress, 
        mode,
        setMode,
        addressData,
        setAddressData,
      }}
    >
      {children}
    </AddressContext.Provider>
  );
};

export const useAddress = () => useContext(AddressContext);
