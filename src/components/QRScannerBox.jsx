import { useEffect, useState } from 'react';
import {
  PermissionsAndroid,
  Platform,
  StyleSheet,
  View,
  Text,
} from 'react-native';
import { Camera } from 'react-native-camera-kit';
import { showToast } from '../utils/toastUtils';
import Fonts from '../styles/font';

export default function QRScannerBox({ onScan }) {
  const [hasPermission, setHasPermission] = useState(Platform.OS === 'ios');
  const [scanned, setScanned] = useState(false);
  
  useEffect(() => {
    if (scanned) {
      const timer = setTimeout(() => setScanned(false), 3000);
      return () => clearTimeout(timer);
    }
  }, [scanned]);
  

  useEffect(() => {
    const requestCameraPermission = async () => {
      if (Platform.OS === 'android') {
        try {
          const granted = await PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.CAMERA,
            {
              title: 'Camera Permission',
              message: 'App needs access to your camera to scan QR codes.',
              buttonNeutral: 'Ask Me Later',
              buttonNegative: 'Cancel',
              buttonPositive: 'OK',
            },
          );
          if (granted === PermissionsAndroid.RESULTS.GRANTED) {
            setHasPermission(true);
          } else {
            showToast('error', 'Camera permission denied');
          }
        } catch (err) {
          console.warn(err);
          showToast('error', 'Failed to request permission');
        }
      }
    };

    requestCameraPermission();
  }, []);

  const handleBarcodeScanned = event => {
    if (scanned) return;
    setScanned(true);
    const data = event?.nativeEvent?.codeStringValue;
    if (onScan) {
      onScan({ data });
    }
  };

  if (!hasPermission) {
    return (
      <View style={styles.permissionFallback}>
        <Text style={styles.permissionText}>
          Camera permission is required to scan QR codes.
        </Text>
      </View>
    );
  }

  return (
    <View style={styles.cameraBox}>
      <Camera
        style={StyleSheet.absoluteFillObject}
        scanBarcode={true}
        onReadCode={handleBarcodeScanned}
        laserColor="blue"
        frameColor="yellow"
      />
    </View>
  );
}

const styles = StyleSheet.create({
  cameraBox: {
    height: '100%',
    width: '100%',
    overflow: 'hidden',
    position: 'relative',
  },
  permissionFallback: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
    backgroundColor: '#fefefe',
  },
  permissionText: {
    textAlign: 'center',
    color: '#333',
    fontSize: Fonts.sizes.base,
  },
});
