import React, { useEffect, useState, useCallback } from 'react';
import {
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';

import CustomButton from '../../components/CustomButton';
import CustomInput from '../../components/CustomInput';
import StoreImageUploader from '../../components/StoreImageUploader';
import { useStorekeeperAddress } from '../../contexts/storekeeperAddressContext';
import { useSafeRouter } from '../../hooks/useSafeRouter';
import { useProfile } from '../../contexts/profileContext';
import { showToast } from '../../utils/toastUtils';
import Colors from '../../styles/colors';
import styles from '../../styles/globalStyles';
import textStyles from '../../styles/textStyles';
import Fonts from '../../styles/font';

const StorekeeperCreateProfile = () => {
  const [storekeeperName, setStorekeeperName] = useState('');
  const [storeName, setStoreName] = useState('');
  const [mobile, setMobile] = useState('');
  const [email, setEmail] = useState('');
  const [gst, setGst] = useState('');
  const [addressLine1, setAddressLine1] = useState('');
  const [addressLine2, setAddressLine2] = useState('');
  const [landmark, setLandmark] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [pincode, setPincode] = useState('');
  const [images, setImages] = useState([]);

  const { profile, updateProfile } = useProfile();
  const { saveStorekeeperAddress } = useStorekeeperAddress();
  const { safePush } = useSafeRouter();

  useEffect(() => {
    if (profile?.mobile) setMobile(profile.mobile);
  }, [profile]);

  const sanitizeText = (text, regex, setter) => {
    setter(text.replace(regex, ''));
  };

  const isValidEmail = email => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  const isAlpha = text => /^[A-Za-z\s]{2,}$/.test(text);
  const isValidAddress = text => /^[a-zA-Z0-9\s,\/-]*$/.test(text);
  const isValidPincode = pin => /^\d{6}$/.test(pin);

  const handleContinue = useCallback(async () => {
    // if (!storekeeperName.trim()) return showToast('error', 'Enter your name.');
    // if (!isAlpha(storekeeperName)) return showToast('error', 'Invalid name.');
    // if (!storeName.trim()) return showToast('error', 'Enter store name.');
    // if (!email.trim() || !isValidEmail(email))
    //   return showToast('error', 'Invalid email.');
    // if (!gst.trim()) return showToast('error', 'Enter valid GST number.');
    // if (!addressLine1.trim() || !isValidAddress(addressLine1))
    //   return showToast('error', 'Invalid address.');
    // if (!landmark.trim() || landmark.length < 2)
    //   return showToast('error', 'Enter landmark.');
    // if (!city.trim() || !isAlpha(city))
    //   return showToast('error', 'Invalid city.');
    // if (!state.trim() || !isAlpha(state))
    //   return showToast('error', 'Invalid state.');
    // if (!pincode.trim() || !isValidPincode(pincode))
    //   return showToast('error', 'Invalid pincode.');

    const nameParts = storekeeperName.trim().split(' ');
    const updatedProfile = {
      ...profile,
      firstName: nameParts[0],
      lastName: nameParts.slice(1).join(' '),
      mobile,
      storeName,
      email,
      role: 'storekeeper',
    };

    const newAddress = {
      storeName,
      addressLine1,
      addressLine2,
      landmark,
      city,
      state,
      pincode,
    };

    try {
      await updateProfile(updatedProfile);
      await saveStorekeeperAddress(newAddress);
      showToast('success', 'Registered Successfully');
      safePush('StorekeeperDashboard');
    } catch {
      showToast('error', 'Profile update failed.');
    }
  }, [
    storekeeperName,
    storeName,
    email,
    gst,
    addressLine1,
    addressLine2,
    landmark,
    city,
    state,
    pincode,
    profile,
    updateProfile,
    saveStorekeeperAddress,
    mobile,
    safePush,
  ]);

  return (
    <View style={{ flex: 1, backgroundColor: 'white' }}>
      <View style={innerStyles.createProfileStyling}>
        <Text style={[innerStyles.header, textStyles.subheading]}>
          My Profile
        </Text>
      </View>
      <KeyboardAvoidingView
        style={{ flex: 1, backgroundColor: 'white' }}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 60 : 0}
      >
        <ScrollView
          keyboardShouldPersistTaps="handled"
          contentContainerStyle={{
            flexGrow: 1,
            alignItems: 'center',
            justifyContent: 'center',
            paddingBottom: 40,
          }}
          removeClippedSubviews={true}
          showsVerticalScrollIndicator={false}
        >
          <View style={innerStyles.centerContainer}>
            <View style={[innerStyles.formContainer, { marginTop: 30 }]}>
              <LabelledInput
                label="Storekeeper Name"
                value={storekeeperName}
                required
                placeholder="Enter Your Name"
                onChange={text =>
                  sanitizeText(text, /[^a-zA-Z\s]/g, setStorekeeperName)
                }
                maxLength={30}
              />
              <LabelledInput
                label="Store Name"
                value={storeName}
                required
                placeholder="Enter Store Name"
                onChange={setStoreName}
                maxLength={30}
              />
              <LabelledInput
                label="Mobile"
                value={mobile}
                placeholder="Enter Mobile Number"
                onChange={setMobile}
                keyboardType="phone-pad"
                maxLength={10}
              />
              <LabelledInput
                label="Email"
                value={email}
                required
                placeholder="Enter Email"
                keyboardType="email-address"
                onChange={setEmail}
                maxLength={30}
              />
              <LabelledInput
                label="GST IN"
                value={gst}
                required
                placeholder="Enter GST Number"
                onChange={text => setGst(text.toUpperCase())}
                maxLength={15}
              />
              <LabelledInput
                label="Address Line 1"
                value={addressLine1}
                required
                placeholder="Enter Address"
                onChange={text =>
                  sanitizeText(text, /[^a-zA-Z0-9\s,\/-]/g, setAddressLine1)
                }
                maxLength={40}
              />
              <LabelledInput
                label="Address Line 2"
                value={addressLine2}
                placeholder="Enter Address Line 2"
                onChange={text =>
                  sanitizeText(text, /[^a-zA-Z0-9\s,\/-]/g, setAddressLine2)
                }
                maxLength={40}
              />
              <LabelledInput
                label="Landmark"
                value={landmark}
                required
                placeholder="Enter Landmark"
                onChange={setLandmark}
                maxLength={40}
              />
              <LabelledInput
                label="City"
                value={city}
                required
                placeholder="Enter City"
                onChange={text => sanitizeText(text, /[^a-zA-Z\s]/g, setCity)}
                maxLength={40}
              />
              <LabelledInput
                label="State"
                value={state}
                required
                placeholder="Enter State"
                onChange={text => sanitizeText(text, /[^a-zA-Z\s]/g, setState)}
                maxLength={40}
              />
              <LabelledInput
                label="Pincode"
                value={pincode}
                required
                placeholder="Enter Pincode"
                keyboardType="number-pad"
                onChange={setPincode}
                maxLength={6}
              />

              <Text style={innerStyles.label}>Upload Store Picture</Text>
              <StoreImageUploader images={images} setImages={setImages} />

              <View style={innerStyles.buttonWrapper}>
                <CustomButton title="Continue" onPress={handleContinue} />
              </View>
            </View>
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </View>
  );
};

const LabelledInput = ({
  label,
  value,
  onChange,
  required = false,
  ...props
}) => (
  <View>
    <Text style={innerStyles.label}>
      {label} {required && <Text style={innerStyles.mandatory}>*</Text>}
    </Text>
    <CustomInput value={value} onTextChange={onChange} {...props} />
  </View>
);

const innerStyles = StyleSheet.create({
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
  },
  label: {
    marginTop: 5,
    marginBottom: 5,
    fontSize: Fonts.sizes.base,
    fontWeight: '500',
    color: '#222',
  },
  mandatory: {
    color: 'red',
  },
  buttonWrapper: {
    marginTop: 30,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 40,
  },
});

export default StorekeeperCreateProfile;
