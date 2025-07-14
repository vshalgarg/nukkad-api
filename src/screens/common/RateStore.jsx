// No change in imports
import React, { useRef, useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Modal,
  Keyboard,
  Platform,
} from 'react-native';
import { useNavigation } from '@react-navigation/native';
import Ionicons from 'react-native-vector-icons/Ionicons';
import Toast from 'react-native-toast-message';

import BackButton from '../../components/BackButton';
import CustomButton from '../../components/CustomButton';
import Colors from '../../styles/colors';
import Fonts from '../../styles/font';
import { rateStore } from '../../services/customer/ratingService';
import { useAuth } from '../../contexts/authContext';

// ✅ SVG for success modal
import TickIcon from '../../../assets/images/review.svg';
import { useStore } from '../../contexts/storeContext';

const RateStore = () => {
  const [rating, setRating] = useState(0);
  const [feedback, setFeedback] = useState('');
  const [showThankYou, setShowThankYou] = useState(false);
  const feedbackRef = useRef(null);
  const navigation = useNavigation();

  const handleStarPress = value => setRating(value);

  const { token } = useAuth();
  const{storeData}=useStore();

  const handleSubmitReview = async () => {
    if (rating === 0 || feedback.trim() === '') {
      Toast.show({
        type: 'error',
        text1: 'Please provide both rating and feedback.',
      });
      return;
    }

    try {
      const storeKeeperId = storeData?.storekeeperId;
      console.log(storeKeeperId)

      await rateStore(
        {
          storeKeeperId,
          review: feedback,
          rating,
        },
        token,
      );

      Keyboard.dismiss();
      feedbackRef.current?.blur();
      setShowThankYou(true);
    } catch (error) {
      Toast.show({
        type: 'error',
        text1: 'Failed to submit rating.',
        text2: error.message,
      });
    }
  };

  const handleDone = () => {
    setShowThankYou(false);
    feedbackRef.current?.blur();
    navigation.navigate('CustomerDashboard');
  };

  return (
    <View style={styles.pageContainer}>
      <BackButton title="Rate Store" backgroundColor={Colors.backbuttonColor} />
      <View style={styles.container}>

        <View style={styles.starsContainer}>
          {[1, 2, 3, 4, 5].map(value => (
            <TouchableOpacity
              key={value}
              style={styles.starButton}
              onPress={() => handleStarPress(value)}
            >
              <Ionicons
                name="star"
                size={40}
                color={value <= rating ? Colors.primary : Colors.diabledText}
              />
            </TouchableOpacity>
          ))}
        </View>

        <TextInput
          ref={feedbackRef}
          style={styles.textArea}
          placeholder="Write your feedback here..."
          multiline
          value={feedback}
          onChangeText={setFeedback}
        />

        <Text style={styles.wordCount}>{feedback.length} characters</Text>

        <View style={styles.btnContainer}>
          <CustomButton title="Submit Review" onPress={handleSubmitReview} />
        </View>
      </View>

      <Modal transparent visible={showThankYou} animationType="fade">
        <View style={styles.modalOverlay}>
          <View style={styles.modalBox}>
            <TickIcon width={80} height={80} />
            <Text style={styles.modalTitle}>Thank you!</Text>
            <Text style={styles.modalMessage}>
              We appreciate your feedback. It helps us improve your experience.
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
    backgroundColor: Colors.bgClr,
  },
  container: {
    flex: 1,
    padding: 24,
    marginTop:'5%'
  },
  heading: {
    fontSize: Fonts.sizes.lg + 2,
    fontWeight: 'bold',
    color: Colors.primaryText,
    textAlign: 'center',
    marginTop: 10,
    marginBottom: 20,
  },
  starsContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginBottom: 20,
    gap: 10,
  },
  starButton: {
    padding: 5,
  },
  textArea: {
    borderWidth: 1,
    borderColor: Colors.secondary,
    borderRadius: 10,
    padding: 12,
    fontSize: Fonts.sizes.base,
    textAlignVertical: 'top',
    height: 160,
    backgroundColor: Colors.white,
  },
  wordCount: {
    textAlign: 'right',
    marginVertical: 8,
    fontSize: 12,
    color: Colors.secondaryText,
  },
  btnContainer: {
    marginTop: '15%',
    alignItems: 'center',
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.4)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  modalBox: {
    backgroundColor: Colors.bgClr,
    padding: 30,
    borderRadius: 16,
    width: '85%',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.2,
    shadowRadius: 5,
    elevation: 6,
  },
  modalTitle: {
    fontSize: Fonts.sizes.lg,
    fontWeight: '600',
    color: Colors.primary,
    marginVertical: 15,
  },
  modalMessage: {
    fontSize: Fonts.sizes.base,
    color: Colors.primaryText,
    textAlign: 'center',
    marginBottom: 25,
    lineHeight: 20,
  },
});
