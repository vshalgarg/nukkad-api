import { configureStore } from '@reduxjs/toolkit';
import { persistReducer, persistStore } from 'redux-persist';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { combineReducers } from '@reduxjs/toolkit';

import userReducer from './userSlice';
import cartReducer from './cartSlice';
import orderReducer from './orderSlice'; // ✅ Import
import storekeeperOrdersReducer from './storekeeperOrdersSlice';

const persistConfig = {
  key: 'root',
  storage: AsyncStorage,
  whitelist: ['cart', 'order', 'storekeeperOrders'], // ✅ Add 'order'
};

const rootReducer = combineReducers({
  user: userReducer,
  cart: cartReducer,
  order: orderReducer, // ✅ Add here
  storekeeperOrders: storekeeperOrdersReducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
});

export const persistor = persistStore(store);
