import React, { useEffect, useRef, useState } from 'react';
import {
  Alert,
  Animated,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
  Platform,
  Pressable,
} from 'react-native';
import { launchImageLibrary } from 'react-native-image-picker';
import Toast from 'react-native-toast-message';
import DateTimePicker from '@react-native-community/datetimepicker';

import CameraIcon from '../../../assets/images/Camera.svg';
import ProfileImage from '../../../assets/images/ProfileImage.svg';
import BackButton from '../../components/BackButton';
import CustomButton from '../../components/CustomButton';
import { useProfile } from '../../contexts/profileContext';
import { useSafeRouter } from '../../hooks/useSafeRouter';
import styles from '../../styles/globalStyles';
import { showToast } from '../../utils/toastUtils';
import Colors from '../../styles/colors';
import Fonts from '../../styles/font';

const formatDate = date => {
  if (!date) return '';
  const d = new Date(date);
  return `${String(d.getDate()).padStart(2, '0')}-${String(
    d.getMonth() + 1,
  ).padStart(2, '0')}-${d.getFullYear()}`;
};

const ProfileSetting = () => {
  const { safePush } = useSafeRouter();
  const { profile: profileData, updateProfile } = useProfile();

  const [profile, setProfile] = useState({
    firstName: '',
    lastName: '',
    email: '',
    image: null,
    role: '',
  });

  const [DOB, setDOB] = useState('');
  const [dobDate, setDobDate] = useState(new Date());
  const [showPicker, setShowPicker] = useState(false);
  const [loading, setLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const fadeAnim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    if (profileData) {
      setProfile({
        firstName: profileData.firstName || '',
        lastName: profileData.lastName || '',
        email: profileData.email || '',
        image: profileData.image || null,
        role: profileData.role || '',
      });
      if (profileData.dob) {
        setDOB(formatDate(profileData.dob));
        setDobDate(new Date(profileData.dob));
      }
      setLoading(false);
    }
  }, [profileData]);

  const pickImage = () => {
    const options = { mediaType: 'photo', quality: 1 };

    launchImageLibrary(options, response => {
      if (response.didCancel || response.errorCode) return;
      if (response.assets && response.assets[0]?.uri) {
        setProfile(prev => ({ ...prev, image: response.assets[0].uri }));
        fadeAnim.setValue(0);
      }
    });
  };

  const handleImageLoad = () => {
    Animated.timing(fadeAnim, {
      toValue: 1,
      duration: 300,
      useNativeDriver: true,
    }).start();
  };

  const handleChange = (key, value) => {
    setProfile(prev => ({ ...prev, [key]: value }));
  };

  const handleDateChange = (_, selectedDate) => {
    setShowPicker(false);
    if (selectedDate) {
      setDobDate(selectedDate);
      setDOB(formatDate(selectedDate));
    }
  };

  const validateProfile = () => {
    if (!profile.firstName.trim())
      return showToast('error', 'Enter Valid First Name') || false;
    if (!profile.lastName.trim())
      return showToast('error', 'Enter Valid Last Name') || false;
    if (!profile.email.trim() || !/\S+@\S+\.\S+/.test(profile.email))
      return showToast('error', 'Enter Valid Email') || false;
    if (!DOB)
      return showToast('error', 'Please select your Date of Birth') || false;
    return true;
  };

  const saveProfile = () => {
    if (!validateProfile()) return;

    const formattedDOB = dobDate.toISOString().split('T')[0]; 

    const updatedProfile = {
      ...profileData,
      ...profile,
      dob: formattedDOB, 
    };

    updateProfile(updatedProfile);
    setIsEditing(false);
    showToast('success', 'Profile updated successfully');
    safePush('CustomerDashboard');
  };
  

  const handlePress = () => {
    if (profileData?.role === 'storekeeper') safePush('StorekeeperDashboard');
    else safePush('CustomerDashboard');
  };

  const handleDeleteAccount = () => safePush('DeleteAccount');

  return (
    <View style={styles.pageContainer}>
      <BackButton title="Profile Setting" onPress={handlePress} />

      <View style={innerStyle.container}>
        <View style={innerStyle.profileImageSection}>
          <TouchableOpacity onPress={isEditing ? pickImage : null}>
            {!loading && profile.image ? (
              <Animated.Image
                source={{ uri: profile.image }}
                style={[innerStyle.image, { opacity: fadeAnim }]}
                onLoad={handleImageLoad}
              />
            ) : (
              <View style={[innerStyle.image, { opacity: fadeAnim }]}>
                <ProfileImage height={130} width={130} />
              </View>
            )}
          </TouchableOpacity>
          {isEditing && (
            <TouchableOpacity
              onPress={pickImage}
              style={innerStyle.cameraIconContainer}
            >
              <CameraIcon style={innerStyle.cameraIcon} />
            </TouchableOpacity>
          )}
        </View>
      </View>

      <View style={innerStyle.profileDetails}>
        <View style={innerStyle.row}>
          <Text style={innerStyle.halfLabel}>First Name</Text>
          <Text style={innerStyle.halfLabel}>Last Name</Text>
        </View>
        <View style={innerStyle.row}>
          {isEditing ? (
            <>
              <TextInput
                style={innerStyle.halfInput}
                value={profile.firstName}
                onChangeText={val => handleChange('firstName', val)}
              />
              <TextInput
                style={innerStyle.halfInput}
                value={profile.lastName}
                onChangeText={val => handleChange('lastName', val)}
              />
            </>
          ) : (
            <>
              <Text style={innerStyle.halfInput}>{profile.firstName}</Text>
              <Text style={innerStyle.halfInput}>{profile.lastName}</Text>
            </>
          )}
        </View>

        <View style={innerStyle.email}>
          <Text style={innerStyle.fullLabel}>Email Address</Text>
          {isEditing ? (
            <TextInput
              style={innerStyle.fullInput}
              value={profile.email}
              onChangeText={val => handleChange('email', val)}
            />
          ) : (
            <Text style={innerStyle.fullInput}>{profile.email}</Text>
          )}
        </View>

        <View>
          <Text style={innerStyle.fullLabel}>Date of Birth</Text>
          {isEditing ? (
            <>
              <Pressable onPress={() => setShowPicker(true)}>
                <TextInput
                  style={innerStyle.mobileInput}
                  value={DOB}
                  editable={false}
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
            </>
          ) : (
            <Text style={innerStyle.mobileInput}>{DOB}</Text>
          )}
        </View>
      </View>

      <View
        style={{
          flexDirection: 'row',
          justifyContent: 'flex-end',
          marginRight: 20,
        }}
      >
        {!isEditing ? (
          <Pressable onPress={() => setIsEditing(true)}>
            <Text style={{ color: Colors.primary }}>Edit</Text>
          </Pressable>
        ) : (
          <Pressable
            onPress={() => {
              setIsEditing(false);
              setProfile({
                firstName: profileData.firstName || '',
                lastName: profileData.lastName || '',
                email: profileData.email || '',
                image: profileData.image || null,
                role: profileData.role || '',
              });
              setDOB(formatDate(profileData.dob));
              setDobDate(new Date(profileData.dob));
            }}
          >
            <Text style={{ color: Colors.reject }}>Cancel</Text>
          </Pressable>
        )}
      </View>

      <View style={innerStyle.buttonContainer}>
        {isEditing ? (
          <CustomButton title="Save Changes" onPress={saveProfile} />
        ) : profile.role === 'customer' ? (
          <CustomButton
            title="Delete Account"
            onPress={handleDeleteAccount}
            style={{
              backgroundColor: Colors.reject,
              borderColor: Colors.reject,
            }}
          />
        ) : null}
      </View>

      <Toast />
    </View>
  );
};

export default ProfileSetting;

const innerStyle = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 30,
    marginBottom: 40,
  },
  profileImageSection: { position: 'relative' },
  image: { width: 130, height: 130, borderRadius: 75, resizeMode: 'cover' },
  cameraIconContainer: { position: 'absolute', bottom: 0, right: 0 },
  cameraIcon: { height: 42, width: 42 },
  profileDetails: { width: '100%', marginTop: 10 },
  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
  },
  halfInput: {
    flex: 1,
    marginBottom: 10,
    borderBottomWidth: 1,
    borderRadius: 12,
    padding: 12,
    fontSize: Fonts.sizes.base,
  },
  fullInput: {
    marginBottom: 16,
    marginHorizontal: 20,
    borderBottomWidth: 1,
    borderRadius: 12,
    padding: 12,
    fontSize: Fonts.sizes.base,
  },
  halfLabel: {
    flex: 1,
    marginHorizontal: 10,
    marginTop: 10,
    fontSize: Fonts.sizes.sm,
    color: Colors.secondary,
  },
  fullLabel: {
    marginHorizontal: 30,
    marginTop: 10,
    fontSize: Fonts.sizes.sm,
    color: Colors.secondary,
  },
  mobileInput: {
    marginTop: 8,
    marginBottom: 20,
    marginHorizontal: 20,
    borderBottomWidth: 1,
    borderRadius: 12,
    padding: 12,
    fontSize: Fonts.sizes.base,
  },
  buttonContainer: {
    justifyContent: 'space-evenly',
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 30,
  },
});
