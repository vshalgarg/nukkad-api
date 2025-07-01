import AsyncStorage from "@react-native-async-storage/async-storage";
import { createContext, useContext, useEffect, useState } from "react";

const AddressContext = createContext();

export const AddressProvider = ({ children }) => {
  const [mode, setMode] = useState("add");
  const [addressData, setAddressData] = useState(null);
  const [address, setAddress] = useState([]);
  const [selectedAddressId, setSelectedAddressId] = useState(null);

  useEffect(() => {
    const loadData = async () => {
      try {
        const storedAddress = await AsyncStorage.getItem("address");
        const storedSelectedId = await AsyncStorage.getItem(
          "selectedAddressId"
        );

        if (storedAddress) setAddress(JSON.parse(storedAddress));
        if (storedSelectedId) setSelectedAddressId(storedSelectedId);
      } catch (err) {
        console.warn("Error loading address data", err);
      }
    };
    loadData();
  }, []);

  useEffect(() => {
    AsyncStorage.setItem("address", JSON.stringify(address));
  }, [address]);

  useEffect(() => {
    if (selectedAddressId)
      AsyncStorage.setItem("selectedAddressId", selectedAddressId);
  }, [selectedAddressId]);

  const addAddress = async (newAddress) => {
    const updated = [...address, newAddress];
    setAddress(updated);
    setSelectedAddressId(newAddress.id);

    await AsyncStorage.setItem("address", JSON.stringify(updated));
    await AsyncStorage.setItem("selectedAddressId", newAddress.id);
  };
  

  const updateAddress = async (updated) => {
    const updatedList = address.map((addr) =>
      addr.id === updated.id ? updated : addr
    );
    setAddress(updatedList);
    await AsyncStorage.setItem("address", JSON.stringify(updatedList));
  };

  const resetAddress = async () => {
    setAddress([]);
    await AsyncStorage.removeItem("address");
    setSelectedAddressId(null);
    await AsyncStorage.removeItem("selectedAddressId");
  };

  const deleteAddress = async (id) => {
    const updated = address.filter(addr => `${addr.id}` !== `${id}`);
    setAddress(updated);
    await AsyncStorage.setItem('address', JSON.stringify(updated));

    if (`${id}` === `${selectedAddressId}`) {
      setSelectedAddressId(null);
      await AsyncStorage.removeItem('selectedAddressId');
    }
  };
  
  
  return (
    <AddressContext.Provider
      value={{
        mode,
        setMode,
        addressData,
        setAddressData,
        address,
        setAddress,
        addAddress,
        updateAddress,
        selectedAddressId,
        setSelectedAddressId,
        deleteAddress,
        resetAddress,
      }}
    >
      {children}
    </AddressContext.Provider>
  );
};

export const useAddress = () => useContext(AddressContext);
