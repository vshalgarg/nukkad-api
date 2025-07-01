import React, { useEffect, useState } from 'react';
import {
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { useNavigation } from '@react-navigation/native';
import BackButton from '../../components/BackButton';
import CustomButton from '../../components/CustomButton';
import { useProfile } from '../../contexts/profileContext';
import { useStorekeeperAddress } from '../../contexts/storekeeperAddressContext';
import styles from '../../styles/globalStyles';
import { showToast } from '../../utils/toastUtils';
import Fonts from '../../styles/font';

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
      showToast(
        'error',
        'Address Line 1 must contain only letters, numbers, commas, or hyphens.',
      );
      return;
    }

    if (addressLine2 && !alphanumericRegex.test(addressLine2)) {
      showToast(
        'error',
        'Address Line 2 must contain only letters, numbers, commas, or hyphens.',
      );
      return;
    }

    if (!alphanumericRegex.test(landmark)) {
      showToast(
        'error',
        'Landmark must contain only letters, numbers, commas, or hyphens.',
      );
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
            paddingBottom: 30,
            flexGrow: 1,
            justifyContent: 'center',
            alignItems: 'center',
          }}
          keyboardShouldPersistTaps="handled"
          showsVerticalScrollIndicator={false}
        >
          <View style={innerStyle.card}>
            {!editing ? (
              <>
                <View style={innerStyle.infoBlock}>
                  <View style={innerStyle.row}>
                    <Text style={innerStyle.label}>Name:</Text>
                    <Text style={innerStyle.value}>
                      {storekeeperName || '—'}
                    </Text>
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
                    <TextInput
                      value={storeName}
                      editable={false}
                      style={innerStyle.textInput}
                    />
                  </View>
                  <View style={innerStyle.inputBlock}>
                    <Text style={innerStyle.inputLabel}>Storekeeper Name</Text>
                    <TextInput
                      value={storekeeperName}
                      editable={false}
                      style={innerStyle.textInput}
                    />
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
                          {required && <Text style={{ color: 'red' }}>*</Text>}
                        </Text>
                        <TextInput
                          value={value}
                          onChangeText={onChange}
                          style={innerStyle.textInput}
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
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </View>
  );
};

export default StoreDetail;

const innerStyle = StyleSheet.create({
  card: {
    marginHorizontal: 20,
    backgroundColor: 'white',
    padding: 20,
    borderRadius: 16,
    shadowColor: '#000',
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 4,
  },
  subTitle: {
    fontSize: Fonts.sizes.lg,
    fontWeight: '600',
    color: '#374151',
    marginBottom: 10,
    textAlign: 'center',
  },
  infoBlock: {
    marginBottom: 16,
  },
  row: {
    flexDirection: 'row',
    marginBottom: 8,
  },
  label: {
    width: '40%',
    fontWeight: '600',
    color: '#4b5563',
  },
  value: {
    width: '60%',
    color: '#1f2937',
  },
  addressText: {
    color: '#1f2937',
    marginBottom: 20,
  },
  editButton: {
    backgroundColor: '#2563eb',
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 999,
    alignSelf: 'center',
  },
  editButtonText: {
    color: 'white',
    fontWeight: '600',
  },
  section: {
    backgroundColor: '#f3f4f6',
    padding: 16,
    borderRadius: 12,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOpacity: 0.05,
    shadowRadius: 4,
    elevation: 2,
  },
  inputBlock: {
    marginBottom: 12,
  },
  inputLabel: {
    color: '#4b5563',
    marginBottom: 4,
  },
  textInput: {
    backgroundColor: 'white',
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 25,
    borderWidth: 1,
    borderColor: '#d1d5db',
    color: '#1f2937',
  },
  buttonRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: 12,
    marginTop: 20,
  },
});
