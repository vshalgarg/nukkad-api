// hooks/useSafeRouter.js
import { useCallback, useRef } from 'react';
import { useNavigation } from '@react-navigation/native';

export const useSafeRouter = (delay = 1000) => {
  const navigation = useNavigation();
  const isNavigating = useRef(false);

  const safePush = useCallback(
    (screenName, params) => {
      if (isNavigating.current) return;
      isNavigating.current = true;
      navigation.navigate(screenName, params);
      setTimeout(() => {
        isNavigating.current = false;
      }, delay);
    },
    [delay, navigation],
  );

  const safeReplace = useCallback(
    (screenName, params) => {
      if (isNavigating.current) return;
      isNavigating.current = true;
      navigation.replace(screenName, params);
      setTimeout(() => {
        isNavigating.current = false;
      }, delay);
    },
    [delay, navigation],
  );

  return { safePush, safeReplace };
};
