import { useFocusEffect, useRoute } from '@react-navigation/native';
import { useCallback } from 'react';
import { Alert, BackHandler } from 'react-native';

export default function useCustomBackHandler({
  confirmExitScreens = [], // e.g. ['Home', 'Dashboard']
  disableBackScreens = [], // e.g. ['Splash', 'Loading']
}) {
  const route = useRoute();
  const currentScreen = route.name;

  useFocusEffect(
    useCallback(() => {
      const onBackPress = () => {
        if (confirmExitScreens.includes(currentScreen)) {
          Alert.alert('Exit App', 'Are you sure you want to exit?', [
            { text: 'Cancel', style: 'cancel' },
            { text: 'Yes', onPress: () => BackHandler.exitApp() },
          ]);
          return true;
        }
        if (disableBackScreens.includes(currentScreen)) {
          return true; // Block back action
        }
        return false; // Allow default back behavior
      };

      const backHandler = BackHandler.addEventListener(
        'hardwareBackPress',
        onBackPress,
      );

      return () => backHandler.remove();
    }, [currentScreen]),
  );
}
