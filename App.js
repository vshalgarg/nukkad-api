import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { SafeAreaView } from 'react-native-safe-area-context';
import Toast from 'react-native-toast-message';
import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';

import AppNavigator from './src/navigation/AppNavigator';
import { store, persistor } from './src/store/store';

import { AuthProvider } from './src/contexts/authContext'; 
import { ProfileProvider } from './src/contexts/profileContext';
import { StoreProvider } from './src/contexts/storeContext';
import { AddressProvider } from './src/contexts/addressContext';
import { StorekeeperAddressProvider } from './src/contexts/storekeeperAddressContext';

import { toastConfig } from './src/utils/toastConfig';

export default function App() {
  return (
    <AuthProvider>
      <Provider store={store}>
        <PersistGate loading={null} persistor={persistor}>
          <ProfileProvider>
            <AddressProvider>
              <StoreProvider>
                <StorekeeperAddressProvider>
                  <SafeAreaView style={{ flex: 1 }}>
                    <NavigationContainer>
                      <AppNavigator />
                    </NavigationContainer>
                    <Toast config={toastConfig} topOffset={2} />
                  </SafeAreaView>
                </StorekeeperAddressProvider>
              </StoreProvider>
            </AddressProvider>
          </ProfileProvider>
        </PersistGate>
      </Provider>
    </AuthProvider>
  );
}
