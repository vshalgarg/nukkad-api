import AsyncStorage from "@react-native-async-storage/async-storage";
import { createContext, useContext, useEffect, useState } from "react";

const StorekeeperAddressContext = createContext();

export const StorekeeperAddressProvider = ({ children }) => {
  const [storekeeperAddress, setStorekeeperAddress] = useState(null);

  useEffect(() => {
    const loadData = async () => {
      const stored = await AsyncStorage.getItem("storekeeperAddress");
      if (stored) setStorekeeperAddress(JSON.parse(stored));
    };
    loadData();
  }, []);

  useEffect(() => {
    if (storekeeperAddress) {
      AsyncStorage.setItem(
        "storekeeperAddress",
        JSON.stringify(storekeeperAddress)
      );
    }
  }, [storekeeperAddress]);

  const saveStorekeeperAddress = (address) => {
    setStorekeeperAddress(address);
  };

  const resetStorekeeperAddress = async () => {
    await AsyncStorage.removeItem("storekeeperAddress");
    setStorekeeperAddress(null);
  };

  return (
    <StorekeeperAddressContext.Provider
      value={{
        storekeeperAddress,
        saveStorekeeperAddress,
        resetStorekeeperAddress,
      }}
    >
      {children}
    </StorekeeperAddressContext.Provider>
  );
};

export const useStorekeeperAddress = () =>
  useContext(StorekeeperAddressContext);
