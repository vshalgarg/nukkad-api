import React, { useCallback, useRef, useState } from 'react';
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
import textStyles from '../../styles/textStyles';
import Fonts from '../../styles/font';
import { useAuth } from '../../contexts/authContext';
import { createStorekeeperProfile } from '../../services/storekeeper/storekeeperProfileService';

let pressLock = false; // ✅ Global lock to prevent rapid repeat taps

const StorekeeperCreateProfile = () => {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const isSubmittingRef = useRef(false);

  const [name, setName] = useState('');
  const [storeName, setStoreName] = useState('');
  const [mobile, setMobile] = useState('');
  const [gstIn, setGstIn] = useState('');
  const [addressLine1, setAddressLine1] = useState('');
  const [addressLine2, setAddressLine2] = useState('');
  const [landmark, setLandmark] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [pincode, setPincode] = useState('');
  const [images, setImages] = useState([]);
  const [errors, setErrors] = useState({});

  const { token } = useAuth();
 
  const { profile, createProfile } = useProfile();
  const { saveStorekeeperAddress } = useStorekeeperAddress();
  const { safePush } = useSafeRouter();

  const sanitizeText = (text, regex, setter) => {
    setter(text.replace(regex, ''));
  };

  const isAlpha = text => /^[A-Za-z\s]{2,}$/.test(text);
  const isValidAddress = text => /^[a-zA-Z0-9\s,\/-]*$/.test(text);
  const isValidPincode = pin => /^\d{6}$/.test(pin);

  const handleContinue = useCallback(async () => {
    if (pressLock) return;
    pressLock = true;

    isSubmittingRef.current = true;
    setIsSubmitting(true);

    const newErrors = {};
    let firstErrorMessage = '';

    if (!name.trim()) {
      newErrors.name = true;
      firstErrorMessage ||= 'Enter your name.';
    } else if (!isAlpha(name)) {
      newErrors.name = true;
      firstErrorMessage ||= 'Invalid name.';
    }

    if (!storeName.trim()) {
      newErrors.storeName = true;
      firstErrorMessage ||= 'Enter store name.';
    }

    if (!mobile.trim()) {
      newErrors.mobile = true;
      firstErrorMessage ||= 'Enter contact number.';
    }

    if (!gstIn.trim()) {
      newErrors.gstIn = true;
      firstErrorMessage ||= 'Enter valid GSTIN number.';
    }

    if (!addressLine1.trim() || !isValidAddress(addressLine1)) {
      newErrors.addressLine1 = true;
      firstErrorMessage ||= 'Invalid address.';
    }

    if (!landmark.trim() || landmark.length < 2) {
      newErrors.landmark = true;
      firstErrorMessage ||= 'Enter landmark.';
    }

    if (!city.trim() || !isAlpha(city)) {
      newErrors.city = true;
      firstErrorMessage ||= 'Invalid city.';
    }

    if (!state.trim() || !isAlpha(state)) {
      newErrors.state = true;
      firstErrorMessage ||= 'Invalid state.';
    }

    if (!pincode.trim() || !isValidPincode(pincode)) {
      newErrors.pincode = true;
      firstErrorMessage ||= 'Invalid pincode.';
    }

    setErrors(newErrors);

    if (Object.keys(newErrors).length > 0) {
      if (firstErrorMessage) showToast('error', firstErrorMessage);
      setIsSubmitting(false);
      isSubmittingRef.current = false;
      pressLock = false;
      return;
    }

    const nameParts = name.trim().split(' ');
    const updatedProfile = {
      ...profile,
      firstName: nameParts[0],
      lastName: nameParts.slice(1).join(' '),
      mobile,
      storeName,
      role: 'storekeeper',
    };

    const newAddress = {
      storeName,
      addressLine1,
      addressLine2,
      landmark,
      mobile,
      city,
      state,
      pincode,
    };

    const payload = {
      name,
      storeName,
      gstIn,
      addressLine1,
      addressLine2,
      landmark,
      city,
      state,
      pincode,
      images,
    };

    try {
      
      await createStorekeeperProfile(payload, token);
      await createProfile(updatedProfile);
      await saveStorekeeperAddress(newAddress);
      showToast('success', 'Registered Successfully');
      setTimeout(() => (pressLock = false), 1500); 
      safePush('StorekeeperDashboard');
    } catch (err) {
      console.error('❌ Storekeeper profile error:', err.message);
      showToast('error', 'Profile update failed.');
      pressLock = false;
    } finally {
      setIsSubmitting(false);
      isSubmittingRef.current = false;
    }
  }, [
    name,
    storeName,
    gstIn,
    addressLine1,
    addressLine2,
    landmark,
    city,
    state,
    pincode,
    profile,
    saveStorekeeperAddress,
    mobile,
    safePush,
    token,
  ]);

  return (
    <View style={{ flex: 1, backgroundColor: Colors.bgClr }}>
      <View style={innerStyles.createProfileStyling}>
        <Text style={[innerStyles.header, textStyles.subheading]}>
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
          contentContainerStyle={{
            flexGrow: 1,
            alignItems: 'center',
            justifyContent: 'center',
            paddingBottom: 40,
          }}
          removeClippedSubviews
          showsVerticalScrollIndicator={false}
        >
          <View style={innerStyles.centerContainer}>
            <View style={[innerStyles.formContainer, { marginTop: 30 }]}>
              <LabelledInput
                label="Storekeeper Name"
                value={name}
                required
                placeholder="Enter Your Name"
                onChange={text => sanitizeText(text, /[^a-zA-Z\s]/g, setName)}
                maxLength={30}
                isError={errors.name}
              />
              <LabelledInput
                label="Store Name"
                value={storeName}
                required
                placeholder="Enter Store Name"
                onChange={setStoreName}
                maxLength={30}
                isError={errors.storeName}
              />
              <LabelledInput
                label="Contact Number"
                value={mobile}
                placeholder="Enter Contact Number"
                onChange={setMobile}
                keyboardType="phone-pad"
                maxLength={10}
                isError={errors.mobile}
                required
              />
              <LabelledInput
                label="GSTIN"
                value={gstIn}
                required
                placeholder="Enter GSTIN Number"
                autoCapitalize="characters"
                onChange={text => setGstIn(text.toUpperCase())}
                maxLength={15}
                isError={errors.gstIn}
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
                isError={errors.addressLine1}
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
                isError={errors.landmark}
              />
              <LabelledInput
                label="City"
                value={city}
                required
                placeholder="Enter City"
                onChange={text => sanitizeText(text, /[^a-zA-Z\s]/g, setCity)}
                maxLength={40}
                isError={errors.city}
              />
              <LabelledInput
                label="State"
                value={state}
                required
                placeholder="Enter State"
                onChange={text => sanitizeText(text, /[^a-zA-Z\s]/g, setState)}
                maxLength={40}
                isError={errors.state}
              />
              <LabelledInput
                label="Pincode"
                value={pincode}
                required
                placeholder="Enter Pincode"
                keyboardType="number-pad"
                onChange={setPincode}
                maxLength={6}
                isError={errors.pincode}
              />

              <Text style={innerStyles.label}>Upload Store Picture</Text>
              <StoreImageUploader images={images} setImages={setImages} />

              <View style={innerStyles.buttonWrapper}>
                <CustomButton
                  title={'Continue'}
                  onPress={handleContinue}
                  disabled={isSubmitting}
                />
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
  isError = false,
  ...props
}) => (
  <View>
    <Text style={innerStyles.label}>
      {label} {required && <Text style={innerStyles.mandatory}>*</Text>}
    </Text>
    <CustomInput
      value={value}
      onTextChange={onChange}
      isError={isError}
      {...props}
    />
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
    color: Colors.bgClr,
  },
  centerContainer: {
    flex: 1,
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
    color: Colors.secondary,
  },
  mandatory: {
    color: Colors.reject,
  },
  buttonWrapper: {
    marginTop: 30,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 40,
  },
});

export default StorekeeperCreateProfile;
