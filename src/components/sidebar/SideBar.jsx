import Ionicons from 'react-native-vector-icons/Ionicons';
import { useEffect, useRef } from 'react';
import {
  Alert,
  Animated,
  Dimensions,
  Image,
  Linking,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';

import AsyncStorage from '@react-native-async-storage/async-storage';
import ProfileImage from '../../../assets/images/ProfileImage.svg';
import { useProfile } from '../../contexts/profileContext.js';
import { useSafeRouter } from '../../hooks/useSafeRouter.js';

import { useDispatch } from 'react-redux';
import { clearCart } from '../../store/cartSlice.js';
import { resetOrdersFromFile } from '../../store/storekeeperOrdersSlice.js';
import { resetUser } from '../../store/userSlice.js';

import { useAddress } from '../../contexts/addressContext.js';
import { useStore } from '../../contexts/storeContext.js';
import Fonts from '../../styles/font.js';
import Colors from '../../styles/colors.js';
import { persistor } from '../../store/store.js';

const screenWidth = Dimensions.get('window').width;

const SideBar = ({ isVisible, onClose }) => {
  const { safePush, safeReplace } = useSafeRouter();
  const slideAnimation = useRef(new Animated.Value(-screenWidth)).current;
  const { profile } = useProfile();
  const role = profile?.role;
  const imageUri = profile?.image;
  const name = `${profile?.firstName ?? ''} ${profile?.lastName ?? ''}`.trim();
  const email = profile?.email ?? '';

  const dispatch = useDispatch();
  const { resetProfile } = useProfile();
  const { resetAddress } = useAddress();
  const { resetStore } = useStore();

  useEffect(() => {
    Animated.timing(slideAnimation, {
      toValue: isVisible ? 0 : -screenWidth,
      duration: 300,
      useNativeDriver: true,
    }).start();
  }, [isVisible]);

  const openLink = async url => {
    try {
      console.log('Trying to open:', url);
      await Linking.openURL(url); // skip canOpenURL
    } catch (error) {
      console.warn('Failed to open URL:', url, error);
    }
  };
  

  const baseMenuItems = [
    { name: 'My Stores', icon: 'storefront' },
    ...(role !== 'storekeeper'
      ? [
          { name: 'Add Store', icon: 'add-circle-sharp' },
          { name: 'Addresses', icon: 'location-sharp' },
        ]
      : []),
    { name: 'Notifications', icon: 'notifications' },
    { name: 'Settings', icon: 'settings-sharp' },
    { name: 'Refer to Store', icon: 'share-social-sharp' },
    { name: 'Refer to Customer', icon: 'person' },
    { name: 'Help and Support', icon: 'help-circle' },
    { name: 'Privacy Policy', icon: 'shield-half' },
    { name: 'Terms & Conditions', icon: 'document' },
    ...(role !== 'storekeeper' ? [{ name: 'Rate Store', icon: 'star' }] : []),
    { name: 'Logout', icon: 'log-out' },
  ];

  const roleBasedItem =
    role === 'customer'
      ? { name: 'My Orders', icon: 'bag-add' }
      : { name: 'Order History', icon: 'time' };
  const storekeeperExtraItems =
    role === 'storekeeper' ? [{ name: 'Payment Options', icon: 'card' }] : [];

  const menuItems = [roleBasedItem, ...storekeeperExtraItems, ...baseMenuItems];

  const getRouteForMenuItem = (menuName, role) => {
    const routes = {
      ...(role !== 'storekeeper' && {
        'Add Store': 'AddStore',
        Addresses: 'Address',
      }),
      Notifications: 'Notification',
      Settings: 'settings',
      // 'Help and Support': 'Help',
      'Refer to Customer': 'ReferToCustomer',
      'Rate Store': 'RateStore',
      'Payment Options': 'PaymentOptions',
    };

    if (menuName === 'My Stores') {
      return role === 'storekeeper' ? 'StoreDetail' : 'MyStores';
    }
    if (menuName === 'My Orders' || menuName === 'Order History') {
      return 'Orders';
    }

    return routes[menuName];
  };

  const externalLinks = {
    'Help and Support': 'https://support.google.com',
    'Privacy Policy': 'https://policies.google.com/privacy',
    'Terms & Conditions': 'https://policies.google.com/terms',
    'Refer to Store': 'https://www.google.com/',
  };
  

  const handleOptionClick = async menuName => {
    onClose();


    if (externalLinks[menuName]) {
      await openLink(externalLinks[menuName]);
      return;
    }

    if (menuName === 'Logout') {
      Alert.alert('Logout', 'Are you sure you want to logout?', [
        {
          text: 'Cancel',
          style: 'cancel',
        },
        {
          text: 'Logout',
          style: 'destructive',
          onPress: async () => {
            try {
              await persistor.purge(); // ✅ Clears persisted Redux state cleanly
              await AsyncStorage.removeItem('authToken');
              await AsyncStorage.removeItem('userRole');
              dispatch(clearCart());
              dispatch(resetUser());
              dispatch(resetOrdersFromFile());
              resetProfile();
              resetAddress();
              resetStore();
              safeReplace('Home'); // 👈 your CLI entry screen name
            } catch (error) {
              console.error('Logout failed:', error);
            }
          },
        },
      ]);
      return;
    }

    const routeName = getRouteForMenuItem(menuName, role);
    if (routeName) {
      safePush(routeName);
    } else {
      console.warn('No route found for menu item:', menuName);
    }
  };

  if (!isVisible) return null;

  return (
    <>
      <TouchableOpacity style={styles.overlay} onPress={onClose} />
      <Animated.View
        style={[
          styles.sidebar,
          { transform: [{ translateX: slideAnimation }] },
        ]}
      >
        <View style={styles.profileContainer}>
          {imageUri ? (
            <Image
              source={{ uri: imageUri }}
              style={styles.profileImage}
              resizeMode="cover"
            />
          ) : (
            <View style={{ overflow: 'hidden', borderRadius: 30 }}>
              <ProfileImage height={60} width={60} />
            </View>
          )}

          <View style={styles.profileTextContainer}>
            <Text style={styles.profileName}>{name}</Text>
            <Text style={styles.profileEmail}>
              {role === 'storekeeper' ? profile?.storeName : email}
            </Text>
          </View>

          <TouchableOpacity onPress={() => safeReplace('ProfileSetting')}>
            <Ionicons name="settings-sharp" size={24} color={Colors.secondaryText} />
          </TouchableOpacity>
        </View>

        <ScrollView showsVerticalScrollIndicator={false}>
          {menuItems.map(item => (
            <TouchableOpacity
              key={item.name}
              onPress={() => handleOptionClick(item.name)}
              style={styles.menuItem}
              accessibilityRole="button"
            >
              <Ionicons
                name={item.icon}
                size={22}
                color={Colors.secondary}
                style={styles.menuIconLeft}
              />
              <Text style={styles.menuText}>{item.name}</Text>
              {item.name !== 'Logout' && (
                <Ionicons
                  name="chevron-forward-outline"
                  size={20}
                  color={Colors.secondaryText}
                />
              )}
            </TouchableOpacity>
          ))}
        </ScrollView>
      </Animated.View>
    </>
  );
};

export default SideBar;

const styles = StyleSheet.create({
  sidebar: {
    position: 'absolute',
    top: 0,
    left: 0,
    height: '100%',
    width: Dimensions.get('window').width * 0.8,
    backgroundColor:Colors.bgClr,
    paddingTop: 60,
    paddingHorizontal: 20,
    zIndex: 1000,
    elevation: 5,
  },
  overlay: {
    position: 'absolute',
    top: 0,
    left: 0,
    height: '100%',
    width: '100%',
    backgroundColor: 'rgba(0,0,0,0.3)',
    zIndex: 100,
  },
  profileContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 20,
    gap: 10,
    width: '100%',
  },
  profileImage: {
    width: 60,
    height: 60,
    borderRadius: 30,
  },
  profileTextContainer: {
    flex: 1,
    width: '70%',
    flexShrink: 1,
    flexWrap: 'wrap',
  },
  profileName: {
    fontSize: Fonts.sizes.base,
    fontWeight: 'bold',
    color: Colors.secondary,
    flexShrink: 1,
  },
  profileEmail: {
    fontSize: Fonts.sizes.sm,
    color: Colors.secondaryText,
    flexShrink: 1,
  },
  menuItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 14,
    borderBottomWidth: 1,
    borderBottomColor: Colors.borderColor,
  },
  menuIconLeft: {
    width: 26,
    marginRight: 12,
  },
  menuText: {
    flex: 1,
    fontSize: Fonts.sizes.base,
  },
});
