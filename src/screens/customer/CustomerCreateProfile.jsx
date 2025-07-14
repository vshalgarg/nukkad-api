import DateTimePicker from '@react-native-community/datetimepicker';
import React, { useEffect, useState } from 'react';
import {
  Keyboard,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';

import CustomButton from '../../components/CustomButton';
import CustomInput from '../../components/CustomInput';
import { useAddress } from '../../contexts/addressContext';
import { useProfile } from '../../contexts/profileContext';
import styles from '../../styles/globalStyles';
import Colors from '../../styles/colors';
import { showToast } from '../../utils/toastUtils';
import { useSafeRouter } from '../../hooks/useSafeRouter';
import textStyles from '../../styles/textStyles';
import { useRoute } from '@react-navigation/native';
import { createCustomerProfile } from '../../services/customer/customerProfileService';
import { useAuth } from '../../contexts/authContext';
import { useDispatch } from 'react-redux';
// import { setCartUser } from '../../store/cartSlice';

let pressLock = false;

const CustomerCreateProfile = () => {
  const [name, setName] = useState('');
  const [dob, setDob] = useState(null);
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [email, setEmail] = useState('');
  const [mobile, setMobile] = useState('');
  const [addressLine1, setAddressLine1] = useState('');
  const [addressLine2, setAddressLine2] = useState('');
  const [landmark, setLandmark] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [pincode, setPincode] = useState('');
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { token } = useAuth();
  const { profile, createProfile } = useProfile();

  const { safePush } = useSafeRouter();
  const route = useRoute();
  const dispatch = useDispatch();

  const params = route.params || {};

  useEffect(() => {
    if (params.toast) {
      try {
        const { type, title, message } = JSON.parse(params.toast);
        showToast(type, title, message);
      } catch (e) {
        console.warn('Failed to parse toast params', e);
      }
    }
    if (params.mobile) setMobile(params.mobile);
  }, [params]);

  const isValidEmail = email => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  const isAlpha = text => /^[A-Za-z\s]{2,}$/.test(text);
  const isValidAddress = text => /^[a-zA-Z0-9\s,\/-]*$/.test(text);
  const isValidPincode = pin => /^\d{6}$/.test(pin);

  const formatDateYYYYMMDD = date => {
    if (!(date instanceof Date) || isNaN(date)) return null;
    const year = date.getFullYear();
    const month = `${date.getMonth() + 1}`.padStart(2, '0');
    const day = `${date.getDate()}`.padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  const handleDobChange = (event, selectedDate) => {
    setShowDatePicker(false);
    if (selectedDate) setDob(selectedDate);
  };

  const handleContinue = async () => {
    if (pressLock) return;
    pressLock = true;
    setIsSubmitting(true);

    const newErrors = {};
    let firstErrorMessage = '';

    if (!name.trim()) {
      newErrors.name = true;
      firstErrorMessage ||= 'Please enter your name.';
    } else if (!isAlpha(name)) {
      newErrors.name = true;
      firstErrorMessage ||= 'Name must contain only letters.';
    }

    if (!email.trim()) {
      newErrors.email = true;
      firstErrorMessage ||= 'Please enter your email.';
    } else if (!isValidEmail(email)) {
      newErrors.email = true;
      firstErrorMessage ||= 'Invalid email format.';
    }

    if (!dob) {
      newErrors.dob = true;
      firstErrorMessage ||= 'Please select your date of birth.';
    }

    if (!addressLine1.trim() || !isValidAddress(addressLine1)) {
      newErrors.addressLine1 = true;
      firstErrorMessage ||= 'Invalid Address Line 1.';
    }

    if (!landmark.trim() || landmark.length < 2) {
      newErrors.landmark = true;
      firstErrorMessage ||= 'Please enter a landmark.';
    }

    if (!city.trim() || !isAlpha(city)) {
      newErrors.city = true;
      firstErrorMessage ||= 'Invalid city name.';
    }

    if (!state.trim() || !isAlpha(state)) {
      newErrors.state = true;
      firstErrorMessage ||= 'Invalid state name.';
    }

    if (!pincode.trim() || !isValidPincode(pincode)) {
      newErrors.pincode = true;
      firstErrorMessage ||= 'Pincode must be 6 digits.';
    }

    setErrors(newErrors);

    if (Object.keys(newErrors).length > 0) {
      if (firstErrorMessage) showToast('error', firstErrorMessage);
      setIsSubmitting(false);
      pressLock = false;
      return;
    }

    const nameParts = name.trim().split(' ');
    const firstName = nameParts[0] || '';
    const lastName = nameParts.slice(1).join(' ') || '';

    const payload = {
      name,
      email,
      mobile,
      dob: formatDateYYYYMMDD(dob),
      addressLine1,
      addressLine2,
      landmark,
      city,
      state,
      pincode,
    };

    const newAddress = {
      id: Date.now().toString(),
      name,
      addressLine1,
      addressLine2,
      landmark,
      city,
      state,
      pincode,
    };

    try {
      await createCustomerProfile(payload, token);

      const newProfile = {
        firstName,
        lastName,
        email,
        mobile,
        dob: formatDateYYYYMMDD(dob),
        role: 'customer',
        image: null,
      };

      await createProfile(newProfile);
      // dispatch(setCartUser(profile.userId));

      showToast('success', 'Registered Successfully');
      Keyboard.dismiss();
      setTimeout(() => {
        pressLock = false;
        safePush('AddStore', { hideBackButton: true });
      }, 1500);
    } catch (err) {
      console.error(err);
      showToast('error', 'Profile creation failed.');
      pressLock = false;
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <View style={{ flex: 1, backgroundColor: Colors.bgClr }}>
      <View style={localStyles.createProfileStyling}>
        <Text style={[localStyles.header, textStyles.subheading]}>
          My Profile
        </Text>
      </View>

      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 60 : 0}
      >
        <ScrollView
          keyboardShouldPersistTaps="handled"
          contentContainerStyle={{ flexGrow: 1, justifyContent: 'center' }}
          removeClippedSubviews={true}
          showsVerticalScrollIndicator={false}
        >
          <View style={styles.pageContainer}>
            <View style={localStyles.centerContainer}>
              <View style={[localStyles.formContainer, { marginTop: 30 }]}>
                <Text style={localStyles.label}>
                  Name <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  placeholder="Enter Your Name"
                  value={name}
                  maxLength={35}
                  onTextChange={text =>
                    setName(text.replace(/[^a-zA-Z\s]/g, ''))
                  }
                  autoCapitalize="words"
                  isError={errors.name}
                />

                <Text style={localStyles.label}>
                  Contact Number <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  value={mobile}
                  editable={false}
                  keyboardType="phone-pad"
                  maxLength={10}
                  style={{ color: Colors.diabledText }}
                  isError={errors.mobile}
                />

                <Text style={localStyles.label}>
                  Email <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  placeholder="Enter Your Email"
                  value={email}
                  keyboardType="email-address"
                  autoCapitalize="none"
                  onTextChange={setEmail}
                  maxLength={38}
                  isError={errors.email}
                />

                <Text style={localStyles.label}>
                  Date of Birth <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <Pressable onPress={() => setShowDatePicker(true)}>
                  <View
                    style={[
                      localStyles.dobInput,
                      errors.dob && { borderColor: Colors.reject },
                    ]}
                  >
                    <Text style={{ color: dob ? Colors.secondary : '#9CA3AF' }}>
                      {dob ? dob.toDateString() : 'Select Date of Birth'}
                    </Text>
                  </View>
                </Pressable>
                {showDatePicker && (
                  <DateTimePicker
                    value={dob || new Date(2000, 0, 1)}
                    mode="date"
                    display="default"
                    maximumDate={new Date()}
                    onChange={handleDobChange}
                  />
                )}

                <Text style={localStyles.label}>
                  Address Line 1 <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  value={addressLine1}
                  placeholder="Enter Your Address"
                  maxLength={38}
                  onTextChange={text =>
                    setAddressLine1(text.replace(/[^a-zA-Z0-9\s,\/-]/g, ''))
                  }
                  isError={errors.addressLine1}
                />

                <Text style={localStyles.label}>Address Line 2</Text>
                <CustomInput
                  value={addressLine2}
                  placeholder="Enter Address Line 2"
                  onTextChange={text =>
                    setAddressLine2(text.replace(/[^a-zA-Z0-9\s,\/-]/g, ''))
                  }
                  maxLength={38}
                />

                <Text style={localStyles.label}>
                  Landmark <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  value={landmark}
                  placeholder="Enter Landmark"
                  onTextChange={setLandmark}
                  maxLength={38}
                  isError={errors.landmark}
                />

                <Text style={localStyles.label}>
                  City <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  placeholder="Enter City"
                  value={city}
                  onTextChange={text =>
                    setCity(text.replace(/[^a-zA-Z\s]/g, ''))
                  }
                  maxLength={38}
                  isError={errors.city}
                />

                <Text style={localStyles.label}>
                  State <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  placeholder="Enter State"
                  value={state}
                  onTextChange={text =>
                    setState(text.replace(/[^a-zA-Z\s]/g, ''))
                  }
                  maxLength={38}
                  isError={errors.state}
                />

                <Text style={localStyles.label}>
                  Pincode <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  value={pincode}
                  placeholder="Enter Pincode"
                  keyboardType="number-pad"
                  maxLength={6}
                  onTextChange={setPincode}
                  isError={errors.pincode}
                />

                <View style={localStyles.buttonWrapper}>
                  <CustomButton
                    title={isSubmitting ? 'Please wait...' : 'Continue'}
                    onPress={handleContinue}
                    disabled={isSubmitting}
                  />
                </View>
              </View>
            </View>
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </View>
  );
};

export default CustomerCreateProfile;

const localStyles = StyleSheet.create({
  createProfileStyling: {
    height: 63,
    backgroundColor: Colors.primary,
    justifyContent: 'center',
    alignItems: 'center',
  },
  header: {
    fontWeight: '600',
    color: Colors.bgClr,
  },
  centerContainer: {
    alignItems: 'center',
    width: '100%',
  },
  formContainer: {
    maxWidth: 500,
    paddingHorizontal: 20,
  },
  label: {
    marginTop: 5,
    marginBottom: 5,
    fontWeight: '500',
    color: Colors.secondary,
  },
  mandatory: {
    color: Colors.reject,
  },
  dobInput: {
    padding: 12,
    borderWidth: 1,
    borderColor: Colors.inputBorder,
    borderRadius: 6,
    backgroundColor: Colors.white,
  },
  buttonWrapper: {
    marginTop: 40,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 30,
  },
});
