import { useEffect, useState } from 'react';
import {
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  TouchableWithoutFeedback,
  Keyboard,
  View,
  Alert,
  Text,
  StyleSheet,
} from 'react-native';
import BackButton from '../../components/BackButton';
import CustomButton from '../../components/CustomButton';
import CustomInput from '../../components/CustomInput';
import { useAddress } from '../../contexts/addressContext';
import { useNavigation } from '@react-navigation/native';
import Fonts from '../../styles/font';

const AddressForm = () => {
  const [keyboardVisible, setKeyboardVisible] = useState(false);

  useEffect(() => {
    const showSub = Keyboard.addListener('keyboardDidShow', () => {
      setKeyboardVisible(true);
    });
    const hideSub = Keyboard.addListener('keyboardDidHide', () => {
      setKeyboardVisible(false);
    });
    return () => {
      showSub.remove();
      hideSub.remove();
    };
  }, []);

  const {
    mode,
    addressData,
    addAddress,
    updateAddress,
    setAddressData,
    setMode,
  } = useAddress();

  const [name, setName] = useState('');
  const [address1, setAddress1] = useState('');
  const [address2, setAddress2] = useState('');
  const [landmark, setLandmark] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [pincode, setPincode] = useState('');

  useEffect(() => {
    if (mode === 'edit' && addressData) {
      setName(addressData.name || '');
      setAddress1(addressData.addressLine1 || '');
      setAddress2(addressData.addressLine2 || '');
      setLandmark(addressData.landmark || '');
      setCity(addressData.city || '');
      setState(addressData.state || '');
      setPincode(addressData.pincode || '');
    } else {
      setName('');
      setAddress1('');
      setAddress2('');
      setLandmark('');
      setCity('');
      setState('');
      setPincode('');
    }
  }, [mode, addressData]);

  const navigation = useNavigation();

  const handleContinue = () => {
    const trimmedName = name.trim();
    const trimmedAddress1 = address1.trim();
    const trimmedLandmark = landmark.trim();
    const trimmedCity = city.trim();
    const trimmedState = state.trim();
    const trimmedPincode = pincode.trim();

    if (
      !trimmedName ||
      !trimmedAddress1 ||
      !trimmedLandmark ||
      !trimmedCity ||
      !trimmedState ||
      !trimmedPincode
    ) {
      Alert.alert('Error', 'Please fill in all required (*) fields.');
      return;
    }

    if (!/^[a-zA-Z\s]+$/.test(trimmedName)) {
      Alert.alert('Invalid Name', 'Name can only contain letters and spaces.');
      return;
    }

    if (!/^[a-zA-Z\s]+$/.test(trimmedCity)) {
      Alert.alert('Invalid City', 'City can only contain letters and spaces.');
      return;
    }

    if (!/^[a-zA-Z\s]+$/.test(trimmedState)) {
      Alert.alert(
        'Invalid State',
        'State can only contain letters and spaces.',
      );
      return;
    }

    if (!/^\d{6}$/.test(trimmedPincode)) {
      Alert.alert('Invalid Pincode', 'Pincode must be exactly 6 digits.');
      return;
    }

    const addressObject = {
      name: trimmedName,
      addressLine1: trimmedAddress1,
      addressLine2: address2.trim(),
      landmark: trimmedLandmark,
      city: trimmedCity,
      state: trimmedState,
      pincode: trimmedPincode,
    };

    if (mode === 'edit' && addressData?.id) {
      updateAddress({ ...addressObject, id: addressData.id });
    } else {
      addAddress({ ...addressObject, id: Date.now().toString() }); 
    }

    setMode('add');
    setAddressData(null);
    navigation.goBack();
  };

  return (
    <View style={{ flex: 1, backgroundColor: 'white' }}>
      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={keyboardVisible ? 30 : 0}
      >
        <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
          <ScrollView
            keyboardShouldPersistTaps="handled"
            showsVerticalScrollIndicator={false}
            contentContainerStyle={formStyles.scrollContent}
            keyboardDismissMode="interactive"
          >
            <BackButton
              title={
                mode === 'add'
                  ? 'Add Delivery Address'
                  : 'Edit Delivery Address'
              }
            />

            <View style={formStyles.centerContainer}>
              {/* Name */}
              <View>
              <Text style={formStyles.label}>
                Name <Text style={formStyles.mandatory}>*</Text>
              </Text>
              <CustomInput
                placeholder="Enter Your Name"
                value={name}
                maxLength={25}
                autoCapitalize="words"
                onTextChange={setName}
              />
              </View>

              {/* Address Line 1 */}
              <View>
              <Text style={formStyles.label}>
                Address Line 1 <Text style={formStyles.mandatory}>*</Text>
              </Text>
              <CustomInput
                placeholder="Enter Your Address Line 1"
                value={address1}
                maxLength={50}
                autoCapitalize="sentences"
                onTextChange={setAddress1}
              />
              </View>

              {/* Address Line 2 */}
              <View>
              <Text style={formStyles.label}>Address Line 2</Text>
              <CustomInput
                placeholder="Enter Your Address Line 2"
                value={address2}
                maxLength={50}
                autoCapitalize="sentences"
                onTextChange={setAddress2}
              />
              </View>

              {/* Landmark */}
              <View>
              <Text style={formStyles.label}>
                Landmark <Text style={formStyles.mandatory}>*</Text>
              </Text>
              <CustomInput
                placeholder="Enter Your Landmark"
                value={landmark}
                maxLength={25}
                autoCapitalize="sentences"
                onTextChange={setLandmark}
              />
              </View>

              {/* City */}
              <View>
              <Text style={formStyles.label}>
                City <Text style={formStyles.mandatory}>*</Text>
              </Text>
              <CustomInput
                placeholder="Enter Your City"
                value={city}
                maxLength={25}
                autoCapitalize="sentences"
                onTextChange={setCity}
              />
              </View>

              {/* State */}
              <View> 
              <Text style={formStyles.label}>
                State <Text style={formStyles.mandatory}>*</Text>
              </Text>
              <CustomInput
                placeholder="Enter Your State"
                value={state}
                maxLength={25}
                autoCapitalize="sentences"
                onTextChange={setState}
              />
              </View>

              {/* Pincode */}
              <View>
              <Text style={formStyles.label}>
                Pincode <Text style={formStyles.mandatory}>*</Text>
              </Text>
              <CustomInput
                placeholder="Enter Your Pincode"
                value={pincode}
                maxLength={6}
                keyboardType="number-pad"
                onTextChange={setPincode}
              />
              </View>

              <View style={formStyles.buttonContainer}>
                <CustomButton title="Continue" onPress={handleContinue} />
              </View>
            </View>
          </ScrollView>
        </TouchableWithoutFeedback>
      </KeyboardAvoidingView>
    </View>
  );
};

export default AddressForm;

const formStyles = StyleSheet.create({
  scrollContent: {
    flexGrow: 1,
    paddingBottom: 20,
    justifyContent: 'flex-start',
    backgroundColor: 'white',
  },
  centerContainer: {
    flex: 1,
    marginTop: 30,
    paddingHorizontal: 20,
    justifyContent: 'flex-start',
    alignItems: 'center',
  },
  label: {
    alignSelf: 'flex-start',
    marginTop: 10,
    marginBottom: 5,
    fontSize: Fonts.sizes.base,
    fontWeight: '500',
    color: '#222',
  },
  mandatory: {
    color: 'red',
  },
  buttonContainer: {
    marginTop: 40,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 30,
  },
});
