  import AsyncStorage from '@react-native-async-storage/async-storage';
  import {
    Dimensions,
    ScrollView,
    StyleSheet,
    Text,
    View,
    BackHandler,
    KeyboardAvoidingView,
    Platform,
  } from 'react-native';
  import {
    useNavigation,
    useIsFocused,
    useFocusEffect,
  } from '@react-navigation/native';
  import { useEffect, useRef, useState, useCallback } from 'react';
  import { useRoute } from '@react-navigation/native';

  import BackButton from '../../components/BackButton';
  import CustomButton from '../../components/CustomButton';
  import QRScannerBox from '../../components/QRScannerBox.jsx';
  import { useStore } from '../../contexts/storeContext';
  import Colors from '../../styles/colors';
  import globalStyles from '../../styles/globalStyles';
  import Fonts from '../../styles/font';
  import CustomInput from '../../components/CustomInput';
  import { showToast } from '../../utils/toastUtils';
  import { useSafeRouter } from '../../hooks/useSafeRouter';
  import { addCustomerStore } from '../../services/customer/addStoreService.js';
  import textStyles from '../../styles/textStyles.js';

  const STORAGE_KEY = '@scanned_stores';

  export default function AddStore() {
    const [storeId, setStoreID] = useState('');
    const [showScanner, setShowScanner] = useState(true); // controls visibility
    const navigation = useNavigation();
    const isFocused = useIsFocused();
    const { saveStore } = useStore();
    const { safePush } = useSafeRouter();
    const scannerRef = useRef();
    const route= useRoute();
    const hideBack=route.params?.hideBackButton

    const persistStoreIfNew = async store => {
      if (!store?.storekeeperId) return;

      const jsonValue = await AsyncStorage.getItem(STORAGE_KEY);
      const savedStores = jsonValue ? JSON.parse(jsonValue) : [];

      const exists = savedStores.some(
        s => s.storekeeperId === store.storekeeperId,
      );

      if (!exists) {
        await AsyncStorage.setItem(
          STORAGE_KEY,
          JSON.stringify([...savedStores, store]),
        );
      }
    };

    const stopCameraAndNavigate = async callback => {
      try {
        await scannerRef.current?.stopCamera?.();
        callback?.(); // 👈 trigger navigation first
        setTimeout(() => {
          setShowScanner(false); // 👈 unmount AFTER navigation delay
        }, 400); // give time for nav transition
      } catch (error) {
        console.warn('Camera stop error:', error);
        callback?.();
        setTimeout(() => {
          setShowScanner(false);
        }, 400);
      }
    };

    useFocusEffect(
      useCallback(() => {
        setShowScanner(true); // reset when screen refocuses

        const handleBack = () => {
          stopCameraAndNavigate(() => navigation.goBack());
          return true;
        };

        const unsubNav = navigation.addListener('beforeRemove', e => {
          e.preventDefault();
          stopCameraAndNavigate(() => {
            navigation.dispatch(e.data.action);
          });
        });

        const backHandlerSubscription = BackHandler.addEventListener(
          'hardwareBackPress',
          handleBack,
        );

        return () => {
          backHandlerSubscription.remove();
          unsubNav();
        };
      }, [navigation]),
    );

    const handleBarcodeScanned = async e => {
      try {
        const parsedData = JSON.parse(e.data);
        const storeQrId = parsedData?.storeQrId;

        if (!storeQrId || !/^STR\d+$/.test(storeQrId)) {
          return showToast(
            'error',
            'Invalid QR Code',
            'Missing or invalid storeQrId',
          );
        }

        const store = await addCustomerStore(storeQrId); // API should throw error or return message

        if (!store?.storekeeperId) {
          stopCameraAndNavigate(() => safePush('CustomerDashboard'));
          return;
        }

        await persistStoreIfNew(store);
        saveStore(store);
        stopCameraAndNavigate(() =>
          safePush('CustomerDashboard', { scannedData: store }),
        );
      } catch (err) {
        console.error('❌ QR Scan Error:', err);
        const message =
          err?.response?.data?.message || err.message || 'Invalid QR';
        showToast('error', 'Failed to Add Store', message);
      }
    };

    const handleAddStore = async () => {
      const id = storeId.trim().toUpperCase();

      try {
        const store = await addCustomerStore(id); 

        await persistStoreIfNew(store);
        saveStore(store);
        if (!store?.storekeeperId) {
          stopCameraAndNavigate(() => navigation.navigate('CustomerDashboard'));
          return;
        }

        requestAnimationFrame(() => {
          stopCameraAndNavigate(() =>
            safePush('CustomerDashboard', { scannedData: store }),
          );
        });
        
      } catch (err) {
        const message =
          err?.response?.data?.message || err.message || 'Store addition failed';
        showToast('error', 'Failed to Add Store', message);
      }
    };

    const handleSkip = async () => {
      try {
        const jsonValue = await AsyncStorage.getItem(STORAGE_KEY);
        const savedStores = jsonValue ? JSON.parse(jsonValue) : [];

        if (savedStores.length > 0) {
          const lastStore = savedStores[savedStores.length - 1];
          saveStore(lastStore);
        }

        stopCameraAndNavigate(() => {
          navigation.navigate('CustomerDashboard');
        });
      } catch (error) {
        console.error('Failed to handle skip', error);
        showToast('error', 'Unexpected Error Found');
      }
    };

    const handleBackPress = () => {
      stopCameraAndNavigate(() => navigation.goBack());
    };

    return (
      <View style={globalStyles.pageContainer}>
        <View style={{ height: 60 }}>
          {!hideBack && (
            <BackButton title="Add Store" onPress={handleBackPress} />
          )}
          <Text
            style={[
              textStyles.subheading,
              {
                textAlign: 'center',
                marginVertical: '2%',
                textAlignVertical: 'center',
              },
            ]}
          >
            Add Store
          </Text>
        </View>

        <KeyboardAvoidingView
          behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
          style={{ flex: 1 }}
        >
          <ScrollView keyboardShouldPersistTaps="handled">
            <View style={innerStyle.container}>
              <Text style={innerStyle.text}>Scan QR to Add Store</Text>

              {showScanner && (
                <View style={innerStyle.cameraBox}>
                  <QRScannerBox ref={scannerRef} onScan={handleBarcodeScanned} />
                </View>
              )}

              <Text style={innerStyle.orText}>Or</Text>

              <View style={innerStyle.manual}>
                <View style={innerStyle.StoreIdContainer}>
                  <Text style={innerStyle.label}>Add Store Manually By Id</Text>
                  <CustomInput
                    placeholder="Add Store Id"
                    value={storeId}
                    onTextChange={setStoreID}
                    fixedPrefix="STR"
                  />
                </View>
              </View>
            </View>
          </ScrollView>
        </KeyboardAvoidingView>

        <View style={innerStyle.btnContainer}>
          <CustomButton title="Add Store" onPress={handleAddStore} />
          <CustomButton title="Skip" onPress={handleSkip} />
        </View>
      </View>
    );
  }

  const { height } = Dimensions.get('window');
  const boxHeight = height / 3;

  const innerStyle = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: Colors.bgClr,
      alignItems: 'center',
      justifyContent: 'center',
      gap: 10,
      paddingBottom: height * 0.15,
    },
    text: {
      fontWeight: '600',
      fontSize: Fonts.sizes.base,
      marginBottom: 10,
    },
    cameraBox: {
      height: boxHeight,
      width: boxHeight,
      borderWidth: 4,
      borderColor: Colors.primary,
      borderRadius: 12,
      marginBottom: '5%',
      overflow: 'hidden',
      zIndex: 1,
    },
    orText: {
      fontWeight: '700',
      fontSize: Fonts.sizes.xxl,
    },
    manual: {
      width: '100%',
      paddingHorizontal: '15%',
      marginTop: '5%',
    },
    StoreIdContainer: {
      justifyContent: 'flex-start',
    },
    label: {
      fontSize: Fonts.sizes.base,
      marginBottom: 15,
    },
    btnContainer: {
      flexDirection: 'row',
      justifyContent: 'space-around',
      marginBottom: 20,
    },
  });
