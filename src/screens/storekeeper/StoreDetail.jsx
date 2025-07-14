import React, { useEffect, useState } from 'react';
import {
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import { useNavigation } from '@react-navigation/native';
import BackButton from '../../components/BackButton';
import CustomButton from '../../components/CustomButton';
import CustomInput from '../../components/CustomInput';
import { useProfile } from '../../contexts/profileContext';
import { useStorekeeperAddress } from '../../contexts/storekeeperAddressContext';
import styles from '../../styles/globalStyles';
import { showToast } from '../../utils/toastUtils';
import Fonts from '../../styles/font';
import Colors from '../../styles/colors';

const StoreDetail = () => {
  const { profile, updateProfile } = useProfile();
  const { storekeeperAddress, saveStorekeeperAddress } =
    useStorekeeperAddress();

  const [storeName, setStoreName] = useState('');
  const [storekeeperName, setStorekeeperName] = useState('');
  const [addressLine1, setAddressLine1] = useState('');
  const [addressLine2, setAddressLine2] = useState('');
  const [landmark, setLandmark] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [pincode, setPincode] = useState('');
  const [editing, setEditing] = useState(false);

  useEffect(() => {
    if (profile) {
      setStoreName(profile.storeName || '');
      setStorekeeperName(
        `${profile.firstName || ''} ${profile.lastName || ''}`.trim(),
      );
    }
    if (storekeeperAddress) {
      setAddressLine1(storekeeperAddress.addressLine1 || '');
      setAddressLine2(storekeeperAddress.addressLine2 || '');
      setLandmark(storekeeperAddress.landmark || '');
      setCity(storekeeperAddress.city || '');
      setState(storekeeperAddress.state || '');
      setPincode(storekeeperAddress.pincode || '');
    }
  }, [profile, storekeeperAddress]);

  const handleSave = async () => {
    if (
      !storeName ||
      !storekeeperName ||
      !addressLine1 ||
      !landmark ||
      !city ||
      !state ||
      !pincode
    ) {
      showToast('error', 'Please fill in all required fields.');
      return;
    }

    const alphanumericRegex = /^[a-zA-Z0-9\s,'-]*$/;
    const cityStateRegex = /^[a-zA-Z\s]{2,25}$/;
    const pincodeRegex = /^[1-9][0-9]{5}$/;

    if (!alphanumericRegex.test(addressLine1)) {
      showToast('error', 'Address Line 1 must contain only valid characters.');
      return;
    }

    if (addressLine2 && !alphanumericRegex.test(addressLine2)) {
      showToast('error', 'Address Line 2 must contain only valid characters.');
      return;
    }

    if (!alphanumericRegex.test(landmark)) {
      showToast('error', 'Landmark must contain only valid characters.');
      return;
    }

    if (!cityStateRegex.test(city)) {
      showToast(
        'error',
        'City must contain only letters and spaces (2–25 characters).',
      );
      return;
    }

    if (!cityStateRegex.test(state)) {
      showToast(
        'error',
        'State must contain only letters and spaces (2–25 characters).',
      );
      return;
    }

    if (!pincodeRegex.test(pincode)) {
      showToast(
        'error',
        'Pincode must be a 6-digit number and not start with 0.',
      );
      return;
    }

    const nameParts = storekeeperName.trim().split(' ');
    const updatedProfile = {
      ...profile,
      storeName,
      firstName: nameParts[0],
      lastName: nameParts.slice(1).join(' ') || '',
    };

    const updatedAddress = {
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
      await saveStorekeeperAddress(updatedAddress);
      showToast('success', 'Store details updated successfully.');
      setEditing(false);
    } catch (err) {
      showToast('error', 'Update failed.');
    }
  };

  const renderAddress = () => {
    let address = addressLine1;
    if (addressLine2) address += `, ${addressLine2}`;
    if (landmark) address += `, ${landmark}`;
    address += `\n${city}, ${state} - ${pincode}`;
    return address;
  };

  return (
    <View style={styles.pageContainer}>
      <BackButton title="My Store" />
      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 80 : 0}
        style={{ flex: 1 }}
      >
        <ScrollView
          contentContainerStyle={{
            paddingTop: 20,
            paddingHorizontal: 20,
            paddingBottom: 40,
          }}
          keyboardShouldPersistTaps="handled"
          showsVerticalScrollIndicator={false}
        >
          <Text style={innerStyle.subTitle}>Store Details</Text>

          {!editing ? (
            <>
              <View style={innerStyle.infoBlock}>
                <View style={innerStyle.row}>
                  <Text style={innerStyle.label}>Name:</Text>
                  <Text style={innerStyle.value}>{storekeeperName || '—'}</Text>
                </View>
                <View style={innerStyle.row}>
                  <Text style={innerStyle.label}>Store Name:</Text>
                  <Text style={innerStyle.value}>{storeName || '—'}</Text>
                </View>
              </View>

              <Text style={innerStyle.subTitle}>Address</Text>
              <Text style={innerStyle.addressText}>{renderAddress()}</Text>

              <TouchableOpacity
                onPress={() => setEditing(true)}
                style={innerStyle.editButton}
              >
                <Text style={innerStyle.editButtonText}>Edit</Text>
              </TouchableOpacity>
            </>
          ) : (
            <>
              <View style={innerStyle.section}>
                <Text style={innerStyle.subTitle}>Store Info</Text>
                <View style={innerStyle.inputBlock}>
                  <Text style={innerStyle.inputLabel}>Store Name</Text>
                  <CustomInput value={storeName} editable={false} />
                </View>
                <View style={innerStyle.inputBlock}>
                  <Text style={innerStyle.inputLabel}>Storekeeper Name</Text>
                  <CustomInput value={storekeeperName} editable={false} />
                </View>
              </View>

              <View style={innerStyle.section}>
                <Text style={innerStyle.subTitle}>Change Address</Text>
                {[
                  {
                    label: 'Address Line 1',
                    value: addressLine1,
                    onChange: setAddressLine1,
                    required: true,
                    maxLength: 40,
                  },
                  {
                    label: 'Address Line 2',
                    value: addressLine2,
                    onChange: setAddressLine2,
                    maxLength: 40,
                  },
                  {
                    label: 'Landmark',
                    value: landmark,
                    onChange: setLandmark,
                    required: true,
                    maxLength: 25,
                  },
                  {
                    label: 'City',
                    value: city,
                    onChange: setCity,
                    required: true,
                    maxLength: 25,
                  },
                  {
                    label: 'State',
                    value: state,
                    onChange: setState,
                    required: true,
                    maxLength: 25,
                  },
                  {
                    label: 'Pincode',
                    value: pincode,
                    onChange: setPincode,
                    required: true,
                    keyboardType: 'number-pad',
                    maxLength: 6,
                  },
                ].map(
                  (
                    {
                      label,
                      value,
                      onChange,
                      required,
                      keyboardType,
                      maxLength,
                    },
                    idx,
                  ) => (
                    <View style={innerStyle.inputBlock} key={idx}>
                      <Text style={innerStyle.inputLabel}>
                        {label}{' '}
                        {required && (
                          <Text style={{ color: Colors.reject }}>*</Text>
                        )}
                      </Text>
                      <CustomInput
                        value={value}
                        onChangeText={onChange}
                        keyboardType={keyboardType}
                        maxLength={maxLength}
                      />
                    </View>
                  ),
                )}
              </View>

              <View style={innerStyle.buttonRow}>
                <CustomButton
                  title="Cancel"
                  onPress={() => setEditing(false)}
                />
                <CustomButton title="Save" onPress={handleSave} />
              </View>
            </>
          )}
        </ScrollView>
      </KeyboardAvoidingView>
    </View>
  );
};

export default StoreDetail;

const innerStyle = StyleSheet.create({
  subTitle: {
    fontSize: Fonts.sizes.xl,
    fontWeight: '700',
    color: Colors.primary,
    marginBottom: 16,
  },
  infoBlock: {
    marginBottom: 24,
    borderBottomWidth: 1,
    borderColor: '#E5E7EB',
    paddingBottom: 16,
  },
  row: {
    flexDirection: 'row',
    marginBottom: 10,
  },
  label: {
    fontWeight: '600',
    color: Colors.secondary,
    fontSize: 15,
    width: '40%',
  },
  value: {
    color: Colors.secondary,
    fontSize: 15,
    width: '60%',
    textAlign: 'right',
  },
  addressText: {
    color: Colors.secondary,
    marginBottom: 20,
    fontSize: 15,
    lineHeight: 22,
  },
  editButton: {
    backgroundColor: Colors.primary,
    paddingVertical: 12,
    paddingHorizontal: 32,
    borderRadius: 30,
    alignSelf: 'flex-start',
    marginTop: 12,
  },
  editButtonText: {
    color: '#fff',
    fontWeight: '600',
    fontSize: 16,
  },
  section: {
    marginBottom: 20,
  },
  inputBlock: {
    marginBottom: 16,
    alignSelf:"center"
  },
  inputLabel: {
    color: Colors.secondary,
    marginBottom: 6,
    fontSize: 15,
    fontWeight: '500',
  },
  buttonRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: 12,
    marginTop: 24,
  },
});
