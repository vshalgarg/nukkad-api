import React from 'react';
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

const MAX_IMAGES = 4;

const StoreImageUploader = ({ images, setImages }) => {
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
    if (images.length >= MAX_IMAGES) return;

    const hasPermission = await requestGalleryPermission();
    if (!hasPermission) {
      Alert.alert(
        'Permission Denied',
        'Gallery access is required to upload images.',
      );
      return;
    }

    const result = await launchImageLibrary({
      mediaType: 'photo',
      maxWidth: 800,
      quality: 0.7,
      selectionLimit: 1,
    });

    if (result?.assets && result.assets.length > 0) {
      setImages([...images, result.assets[0].uri]);
    }
  };

  const removeImage = uri => {
    Alert.alert('Remove Image', 'Do you want to remove this image?', [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Remove',
        onPress: () => {
          const updated = images.filter(img => img !== uri);
          setImages(updated);
        },
      },
    ]);
  };

  const renderBox = index => {
    if (index < images.length) {
      return (
        <View style={styles.imageBox} key={images[index]}>
          <Image source={{ uri: images[index] }} style={styles.image} />
          <TouchableOpacity
            style={styles.deleteIcon}
            onPress={() => removeImage(images[index])}
          >
            <Ionicons name="close-circle" size={20} color="red" />
          </TouchableOpacity>
        </View>
      );
    } else {
      return (
        <TouchableOpacity
          key={index}
          style={styles.imageBox}
          onPress={pickImage}
        >
          <Ionicons name="add" size={30} color="#888" />
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
    backgroundColor: '#eee',
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
    backgroundColor: '#fff',
    borderRadius: 10,
  },
});

export default StoreImageUploader;
