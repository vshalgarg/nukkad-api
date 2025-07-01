import React, { useRef, useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  Image,
  StyleSheet,
  Modal,
  Keyboard,
} from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { launchCamera, launchImageLibrary } from 'react-native-image-picker';
import Ionicons from 'react-native-vector-icons/Ionicons';
import Toast from 'react-native-toast-message';

import BackButton from '../../components/BackButton';
import CustomButton from '../../components/CustomButton';
import Colors from '../../styles/colors';

// ✅ Make sure these SVGs are set up properly with react-native-svg-transformer
import CameraIcon from '../../../assets/images/add-image-camera.svg';
import GalleryIcon from '../../../assets/images/add-image-gallary.svg';
import TickIcon from '../../../assets/images/review.svg';
import Fonts from '../../styles/font';

const RateStore = () => {
  const [rating, setRating] = useState(0);
  const [feedback, setFeedback] = useState('');
  const [image, setImage] = useState(null);
  const [showThankYou, setShowThankYou] = useState(false);
  const feedbackRef = useRef(null);
  const navigation = useNavigation();

  const handleStarPress = value => setRating(value);

  const handleSubmitReview = () => {
    if (rating === 0 || feedback.trim() === '') {
      Toast.show({
        type: 'error',
        text1: 'Please provide both rating and feedback.',
      });
      return;
    }
    Keyboard.dismiss();
    feedbackRef.current?.blur();
    setShowThankYou(true);
  };

  const handlePickImage = () => {
    launchImageLibrary({ mediaType: 'photo', quality: 0.7 }, response => {
      if (
        !response.didCancel &&
        !response.errorCode &&
        response.assets?.length
      ) {
        setImage(response.assets[0].uri);
      }
    });
  };

  const handleOpenCamera = () => {
    launchCamera({ mediaType: 'photo', quality: 0.7 }, response => {
      if (
        !response.didCancel &&
        !response.errorCode &&
        response.assets?.length
      ) {
        setImage(response.assets[0].uri);
      }
    });
  };

  const handleDone = () => {
    setShowThankYou(false);
    feedbackRef.current?.blur();
    navigation.navigate('CustomerDashboard'); // ✅ Match with your route name
  };

  return (
    <View style={styles.pageContainer}>
      <BackButton title="Rate Store" backgroundColor="#eee" />
      <View style={innerStyle.container}>
        <View>
          <View style={innerStyle.starsContainer}>
            {[1, 2, 3, 4, 5].map(value => (
              <TouchableOpacity
                key={value}
                onPress={() => handleStarPress(value)}
              >
                <Ionicons
                  name="star"
                  size={36}
                  color={value <= rating ? Colors.primary : '#ccc'}
                />
              </TouchableOpacity>
            ))}
          </View>

          <TextInput
            ref={feedbackRef}
            style={innerStyle.textArea}
            placeholder="Write your feedback..."
            multiline
            value={feedback}
            onChangeText={setFeedback}
          />

          <Text style={innerStyle.wordCount}>{feedback.length} characters</Text>

          <View style={innerStyle.buttonsRow}>
            <TouchableOpacity
              style={innerStyle.actionButton}
              onPress={handlePickImage}
            >
              <GalleryIcon />
            </TouchableOpacity>
            <TouchableOpacity
              style={innerStyle.actionButton}
              onPress={handleOpenCamera}
            >
              <CameraIcon />
            </TouchableOpacity>
          </View>

          {image && (
            <Image source={{ uri: image }} style={innerStyle.preview} />
          )}
        </View>

        <View style={innerStyle.btnContainer}>
          <CustomButton title="Submit Review" onPress={handleSubmitReview} />
        </View>
      </View>

      <Modal transparent visible={showThankYou} animationType="fade">
        <View style={innerStyle.modalOverlay}>
          <View style={innerStyle.modalBox}>
            <TickIcon height={81} width={81} />
            <Text style={innerStyle.modalText}>
              Thank you for your feedback!
            </Text>
            <Text style={innerStyle.modalTextArea}>
              We appreciate your feedback. We’ll use it to improve your
              experience.
            </Text>
            <CustomButton title="Done" onPress={handleDone} />
          </View>
        </View>
      </Modal>

      <Toast />
    </View>
  );
};

export default RateStore;

const styles = StyleSheet.create({
  pageContainer: {
    flex: 1,
    backgroundColor: '#fff',
  },
});

const innerStyle = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    justifyContent: 'space-between',
  },
  starsContainer: {
    flexDirection: 'row',
    marginVertical: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  textArea: {
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 8,
    padding: 10,
    textAlignVertical: 'top',
    fontSize: Fonts.sizes.base,
    height: 200,
  },
  wordCount: {
    textAlign: 'right',
    marginTop: 5,
    marginBottom: 20,
    color: '#888',
  },
  buttonsRow: {
    flexDirection: 'row',
    marginBottom: 20,
    gap: 12,
  },
  actionButton: {
    padding: 10,
    borderRadius: 8,
    backgroundColor: Colors.primary,
    marginRight: 10,
  },
  preview: {
    width: '100%',
    height: 200,
    borderRadius: 10,
    resizeMode: 'cover',
  },
  btnContainer: {
    alignItems: 'center',
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  modalBox: {
    backgroundColor: 'white',
    padding: 30,
    borderRadius: 12,
    width: '80%',
    alignItems: 'center',
  },
  modalTextArea: {
    textAlign: 'center',
    paddingHorizontal: 10,
    marginBottom: 20,
  },
  modalText: {
    fontSize: Fonts.sizes.lg,
    fontWeight: '500',
    marginBottom: 20,
    textAlign: 'center',
  },
});
