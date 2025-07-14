// hooks/useBackHandlerControl.js
import { useEffect } from 'react';
import { Alert } from 'react-native';
import { useNavigation } from '@react-navigation/native';

/**
 * @param {Object} options
 * @param {boolean} options.blockBack - If true, back is fully blocked
 * @param {boolean} options.confirmBack - If true, ask confirmation before back
 */
export default function useBackHandlerControl({
  blockBack = false,
  confirmBack = false,
} = {}) {
  const navigation = useNavigation();

  useEffect(() => {
    const unsubscribe = navigation.addListener('beforeRemove', e => {
      if (blockBack) {
        // Fully block back
        e.preventDefault();
        return;
      }

      if (confirmBack) {
        // Show confirmation before navigating back
        e.preventDefault();
        Alert.alert('Confirm Exit', 'Are you sure you want to go back?', [
          { text: 'Cancel', style: 'cancel', onPress: () => {} },
          {
            text: 'Yes',
            style: 'destructive',
            onPress: () => navigation.dispatch(e.data.action),
          },
        ]);
      }

      // Else: allow back normally
    });

    return unsubscribe;
  }, [navigation, blockBack, confirmBack]);
}
