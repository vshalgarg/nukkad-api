import { createContext, useContext, useEffect, useState } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';

const StoreContext = createContext();

const STORE_KEY = '@selected_store'; // ✅ New, clear key

export const StoreProvider = ({ children }) => {
  const [storeData, setStoreData] = useState(null);
  const [isStoreLoaded, setIsStoreLoaded] = useState(false);

  // ✅ Load selected store on app start
  useEffect(() => {
    const loadSelectedStore = async () => {
      try {
        const jsonValue = await AsyncStorage.getItem(STORE_KEY);
        if (jsonValue) {
          const parsedStore = JSON.parse(jsonValue);
          setStoreData(parsedStore);
          console.log(
            '✅ Loaded selected store from AsyncStorage:',
            parsedStore,
          );
        }
      } catch (err) {
        console.error('❌ Failed to load selected store:', err.message);
      } finally {
        setIsStoreLoaded(true);
      }
    };

    loadSelectedStore();
  }, []);

  // ✅ Save selected store persistently
  const saveStore = async store => {
    try {
      console.log('✅ saveStore called with:', store);
      setStoreData(store);
      await AsyncStorage.setItem(STORE_KEY, JSON.stringify(store));
    } catch (err) {
      console.error('❌ Failed to save selected store:', err.message);
    }
  };

  // ✅ Clear selected store
  const resetStore = async () => {
    try {
      await AsyncStorage.removeItem(STORE_KEY);
    } catch (err) {
      console.error('❌ Failed to reset selected store:', err.message);
    }
    setStoreData(null);
  };

  if (!isStoreLoaded) {
    return null; // Or show a splash screen / loader
  }

  return (
    <StoreContext.Provider
      value={{ storeData, setStoreData, saveStore, resetStore }}
    >
      {children}
    </StoreContext.Provider>
  );
};

export const useStore = () => useContext(StoreContext);
