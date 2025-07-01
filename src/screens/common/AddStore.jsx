// CenterBoxCamera.js
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useState } from 'react';
import { Dimensions, StyleSheet, Text, TextInput, View } from 'react-native';
import { useNavigation } from '@react-navigation/native';

import BackButton from '../../components/BackButton';
import CustomButton from '../../components/CustomButton';
import QRScannerBox from '../../components/QRScannerBox';
import { useStore } from '../../contexts/storeContext';
import Colors from '../../styles/colors';
import styles from '../../styles/globalStyles';
import { showToast } from '../../utils/toastUtils';
import { useSafeRouter } from '../../hooks/useSafeRouter';
import Fonts from '../../styles/font';

const STORAGE_KEY = '@scanned_stores';

export default function AddStore() {
  const [storeId, setStoreID] = useState('');
  const navigation = useNavigation();
  const { saveStore } = useStore();
  const {safePush}= useSafeRouter();

  const handleBarcodeScanned = async e => {
    let parsedData;
    try {
      parsedData = JSON.parse(e.data);
    } catch (e) {
      showToast('error', 'Invalid QR Code', 'Scan a valid QR code');
      return;
    }

    try {
      const jsonValue = await AsyncStorage.getItem(STORAGE_KEY);
      const savedStores = jsonValue ? JSON.parse(jsonValue) : [];

      const exists = savedStores.some(
        store =>
          store.shopName === parsedData.shopName &&
          store.address === parsedData.address,
      );

      if (!exists) {
        const updatedStores = [...savedStores, parsedData];
        await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(updatedStores));
      }

      saveStore(parsedData);
      showToast('success', 'Store Added Successfully');
      safePush('CustomerDashboard', { scannedData: parsedData });
    } catch (err) {
      console.error('Error saving scanned store', err);
      showToast('error', 'Error occurred in adding store');
    }
  };

  const handleAddStore = () => {
    if (!storeId) {
      showToast('error', 'Please enter a valid Store ID.');
      return;
    }

    saveStore(storeId);
    navigation.navigate('CustomerDashboard', {
      scannedData: storeId,
    });
  };

  const handleSkip = async () => {
    try {
      const jsonValue = await AsyncStorage.getItem(STORAGE_KEY);
      const savedStores = jsonValue ? JSON.parse(jsonValue) : [];

      if (savedStores.length > 0) {
        const lastStore = savedStores[savedStores.length - 1];
        saveStore(lastStore);
      }

      navigation.navigate('CustomerDashboard');
    } catch (error) {
      console.error('Failed to handle skip', error);
      showToast('error', 'Unexpected Error Found');
    }
  };

  return (
    <View style={styles.pageContainer}>
      <BackButton backgroundColor="#eeeeee55" title={`Add Store`} />

      <View style={innerStyle.container}>
        <Text style={innerStyle.text}>Scan QR to Add Store</Text>

        <View style={innerStyle.cameraBox}>
          <QRScannerBox onScan={handleBarcodeScanned} />
        </View>

        <Text style={{ fontWeight: '700', fontSize: Fonts.sizes.xxl }}>Or</Text>

        <View style={innerStyle.manual}>
          <View style={innerStyle.StoreIdContainer}>
            <Text style={innerStyle.label}>Add Store Manually By Id</Text>

            <TextInput
              placeholder="Add Store Id"
              value={storeId}
              onChangeText={setStoreID}
              style={innerStyle.TextInputStyle}
            />
          </View>
        </View>
      </View>

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
    backgroundColor: 'white',
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
  TextInputStyle: {
    backgroundColor: '#FAFAFA',
    borderRadius: 20,
    paddingHorizontal: 10,
    fontSize: Fonts.sizes.sm,
    paddingVertical: 15,
    textAlign: 'center',
    textAlignVertical: 'center',
  },
  btnContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginBottom: '10%',
  },
});
