import DateTimePicker from '@react-native-community/datetimepicker';
import React, { useEffect, useState, useCallback } from 'react';
import {
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import debounce from 'lodash.debounce';
import { useRoute } from '@react-navigation/native';

import CustomButton from '../../components/CustomButton';
import CustomInput from '../../components/CustomInput';
import { useAddress } from '../../contexts/addressContext';
import { useProfile } from '../../contexts/profileContext';
import styles from '../../styles/globalStyles';
import Colors from '../../styles/colors';
import { showToast } from '../../utils/toastUtils';
import { useSafeRouter } from '../../hooks/useSafeRouter';
import textStyles from '../../styles/textStyles';

const CustomerCreateProfile = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [mobile, setMobile] = useState('');
  const [addressLine1, setAddressLine1] = useState('');
  const [addressLine2, setAddressLine2] = useState('');
  const [landmark, setLandmark] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [pincode, setPincode] = useState('');
  const [DOB, setDOB] = useState('');
  const [dobDate, setDobDate] = useState(new Date());
  const [showPicker, setShowPicker] = useState(false);

  const { profile, updateProfile } = useProfile();
  const { addAddress } = useAddress();
  const { safePush } = useSafeRouter();
  const route = useRoute();
  const params = route.params || {};

  // Debounced sanitizers
  const sanitizeText = useCallback(
    debounce((text, regex, setter) => {
      setter(text.replace(regex, ''));
    }, 100),
    [],
  );

  useEffect(() => {
    if (params.toast) {
      try {
        const { type, title, message } = JSON.parse(params.toast);
        showToast(type, title, message);
      } catch (e) {
        console.warn('Failed to parse toast params', e);
      }
    }
  }, [params]);

  useEffect(() => {
    if (profile?.mobile && profile.mobile !== mobile) {
      setMobile(profile.mobile);
    }
  }, [profile]);

  const handleDateChange = (_, selectedDate) => {
    if (!selectedDate) {
      setShowPicker(false);
      return;
    }
    setShowPicker(false);
    setDOB(selectedDate.toISOString().split('T')[0]);
    setDobDate(selectedDate);
  };

  const isValidEmail = email => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  const isAlpha = text => /^[A-Za-z\s]{2,}$/.test(text);
  const isValidAddress = text => /^[a-zA-Z0-9\s,\/-]*$/.test(text);
  const isValidPincode = pin => /^\d{6}$/.test(pin);

  const handleContinue = async () => {
    // if (!name.trim()) return showToast('error', 'Enter your name.');
    // if (!isAlpha(name)) return showToast('error', 'Invalid name.');
    // if (!email.trim() || !isValidEmail(email))
    //   return showToast('error', 'Invalid email.');
    // if (!addressLine1.trim() || !isValidAddress(addressLine1))
    //   return showToast('error', 'Invalid address.');
    // if (!landmark.trim() || landmark.length < 2)
    //   return showToast('error', 'Invalid landmark.');
    // if (!city.trim() || !isAlpha(city))
    //   return showToast('error', 'Invalid city.');
    // if (!state.trim() || !isAlpha(state))
    //   return showToast('error', 'Invalid state.');
    // if (!pincode.trim() || !isValidPincode(pincode))
    //   return showToast('error', 'Invalid pincode.');

    const nameParts = name.trim().split(' ');
    const updatedProfile = {
      ...profile,
      firstName: nameParts[0] || '',
      lastName: nameParts.slice(1).join(' ') || '',
      email,
      mobile,
      role: 'customer',
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
      // await addAddress(newAddress);
      // await updateProfile(updatedProfile);
      showToast('success', 'Registered Successfully');
      safePush('AddStore');
    } catch (err) {
      console.error(err);
      showToast('error', 'Profile update failed.');
    }
  };

  return (
    <View style={{ flex: 1, backgroundColor: 'white' }}>
      <View style={localStyles.createProfileStyling}>
        <Text style={[localStyles.header,textStyles.subheading]}>My Profile</Text>
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
          <View style={[styles.pageContainer]}>
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
                    sanitizeText(text, /[^a-zA-Z\s]/g, setName)
                  }
                  autoCapitalize="words"
                />

                <Text style={localStyles.label}>
                  Contact Number <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  value={mobile}
                  editable={false}
                  keyboardType="phone-pad"
                  maxLength={10}
                  style={{ color: '#555' }}
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
                />

                <Text style={localStyles.label}>
                  Address Line 1 <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  value={addressLine1}
                  placeholder="Enter Your Address"
                  maxLength={38}
                  onTextChange={text =>
                    sanitizeText(text, /[^a-zA-Z0-9\s,\/-]/g, setAddressLine1)
                  }
                />

                <Text style={localStyles.label}>Address Line 2</Text>
                <CustomInput
                  value={addressLine2}
                  placeholder="Enter Address Line 2"
                  onTextChange={text =>
                    sanitizeText(text, /[^a-zA-Z0-9\s,\/-]/g, setAddressLine2)
                  }
                />

                <Text style={localStyles.label}>
                  Landmark <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  value={landmark}
                  placeholder="Enter Landmark"
                  onTextChange={setLandmark}
                />

                <Text style={localStyles.label}>
                  City <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  placeholder="Enter City"
                  value={city}
                  onTextChange={text =>
                    sanitizeText(text, /[^a-zA-Z\s]/g, setCity)
                  }
                />

                <Text style={localStyles.label}>
                  State <Text style={localStyles.mandatory}>*</Text>
                </Text>
                <CustomInput
                  placeholder="Enter State"
                  value={state}
                  onTextChange={text =>
                    sanitizeText(text, /[^a-zA-Z\s]/g, setState)
                  }
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
                />

                <Text style={localStyles.label}>DOB (Date Of Birth)</Text>
                <Pressable onPress={() => setShowPicker(true)}>
                  <CustomInput
                    placeholder="Enter Your DOB"
                    value={DOB}
                    editable={false}
                    pointerEvents="none"
                  />
                </Pressable>
                {showPicker && (
                  <DateTimePicker
                    value={dobDate}
                    mode="date"
                    display={Platform.OS === 'ios' ? 'spinner' : 'default'}
                    onChange={handleDateChange}
                    maximumDate={new Date()}
                  />
                )}

                <View style={localStyles.buttonWrapper}>
                  <CustomButton title="Continue" onPress={handleContinue} />
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
    color: 'white',
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
    color: '#222',
  },
  mandatory: {
    color: 'red',
  },
  buttonWrapper: {
    marginTop: 40,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 30,
  },
});
