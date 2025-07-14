import React, { useState } from 'react';
import {
  View,
  Image,
  TouchableOpacity,
  StyleSheet,
  Alert,
  Platform,
  PermissionsAndroid,
} from 'react-native';
import { launchImageLibrary } from 'react-native-image-picker';
import Ionicons from 'react-native-vector-icons/Ionicons';
import Colors from '../styles/colors';

const MAX_IMAGES = 4;

const StoreImageUploader = ({ images, setImages }) => {
  const [picking, setPicking] = useState(false); // to prevent double picker open

  const requestGalleryPermission = async () => {
    if (Platform.OS !== 'android') return true;

    try {
      const granted = await PermissionsAndroid.request(
        Platform.Version >= 33
          ? PermissionsAndroid.PERMISSIONS.READ_MEDIA_IMAGES
          : PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
      );
      return granted === PermissionsAndroid.RESULTS.GRANTED;
    } catch (err) {
      console.warn('Permission error:', err);
      return false;
    }
  };

  const pickImage = async () => {
    if (picking || images.length >= MAX_IMAGES) return;

    setPicking(true); // block further taps

    const hasPermission = await requestGalleryPermission();
    if (!hasPermission) {
      Alert.alert(
        'Permission Denied',
        'Gallery access is required to upload images.',
      );
      setPicking(false);
      return;
    }

    const result = await launchImageLibrary({
      mediaType: 'photo',
      maxWidth: 800,
      quality: 0.7,
      selectionLimit: 1,
    });

    if (result?.assets && result.assets.length > 0) {
      const selectedImage = result.assets[0];
      setImages(prev => [...prev, selectedImage]); // Store full image object
    }

    setPicking(false); // allow next tap
  };

  const removeImage = uri => {
    Alert.alert('Remove Image', 'Do you want to remove this image?', [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Remove',
        onPress: () => {
          const updated = images.filter(img => img.uri !== uri);
          setImages(updated);
        },
      },
    ]);
  };

  const renderBox = index => {
    if (index < images.length) {
      return (
        <View style={styles.imageBox} key={images[index].uri}>
          <Image source={{ uri: images[index].uri }} style={styles.image} />
          <TouchableOpacity
            style={styles.deleteIcon}
            onPress={() => removeImage(images[index].uri)}
          >
            <Ionicons name="close-circle" size={20} color={Colors.reject} />
          </TouchableOpacity>
        </View>
      );
    } else {
      return (
        <TouchableOpacity
          key={index}
          style={[styles.imageBox, picking && { opacity: 0.5 }]} // Optional: visual feedback
          onPress={pickImage}
          disabled={picking}
        >
          <Ionicons name="add" size={30} color={Colors.secondary} />
        </TouchableOpacity>
      );
    }
  };

  return (
    <View style={styles.grid}>
      {[...Array(MAX_IMAGES)].map((_, i) => renderBox(i))}
    </View>
  );
};

const styles = StyleSheet.create({
  grid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 10,
    justifyContent: 'center',
    marginTop: 10,
  },
  imageBox: {
    width: 70,
    height: 70,
    backgroundColor: Colors.bgClr,
    borderWidth: 2,
    borderColor: Colors.borderColor,
    borderRadius: 10,
    justifyContent: 'center',
    alignItems: 'center',
    position: 'relative',
    overflow: 'hidden',
  },
  image: {
    width: '100%',
    height: '100%',
    borderRadius: 10,
  },
  deleteIcon: {
    position: 'absolute',
    top: 2,
    right: 2,
    backgroundColor: Colors.bgClr,
    borderRadius: 10,
  },
});

export default StoreImageUploader;
