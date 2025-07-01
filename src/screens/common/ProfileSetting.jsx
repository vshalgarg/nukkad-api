import React, { useEffect, useRef, useState } from 'react';
import {
  Alert,
  Animated,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { launchImageLibrary } from 'react-native-image-picker';
import Toast from 'react-native-toast-message';
import EvilIcons from 'react-native-vector-icons/EvilIcons';

import CameraIcon from '../../../assets/images/Camera.svg';
import ProfileImage from '../../../assets/images/ProfileImage.svg';
import BackButton from '../../components/BackButton';
import CustomButton from '../../components/CustomButton';
import { useProfile } from '../../contexts/profileContext';
import { useSafeRouter } from '../../hooks/useSafeRouter';
import styles from '../../styles/globalStyles';
import { showToast } from '../../utils/toastUtils';
import EditMobileNo from '../../components/EditMobileNo';
import Colors from '../../styles/colors';
import Fonts from '../../styles/font';

const ProfileSetting = () => {
  const [profile, setProfile] = useState({
    firstName: '',
    lastName: '',
    email: '',
    mobile: '',
    image: null,
  });

  const { safePush } = useSafeRouter();
  const { profile: profileData, updateProfile } = useProfile();
  const [loading, setLoading] = useState(true);
  const fadeAnim = useRef(new Animated.Value(0)).current;
  const [isModalVisible, setModalVisible] = useState(false);

  useEffect(() => {
    if (profileData) {
      setProfile({
        firstName: profileData.firstName || '',
        lastName: profileData.lastName || '',
        email: profileData.email || '',
        mobile: profileData.mobile || '',
        image: profileData.image || null,
      });
      setLoading(false);
    }
  }, [profileData]);

  const pickImage = async () => {
    const options = {
      mediaType: 'photo',
      quality: 1,
    };

    launchImageLibrary(options, response => {
      if (response.didCancel) return;

      if (response.errorCode) {
        Alert.alert('Error', 'Failed to pick image: ' + response.errorMessage);
        return;
      }

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

  const validateProfile = () => {
    if (!profile.firstName.trim())
      return showToast('error', 'Enter Valid First Name') || false;
    if (!profile.lastName.trim())
      return showToast('error', 'Enter Valid Last Name') || false;
    if (!profile.email.trim() || !/\S+@\S+\.\S+/.test(profile.email))
      return showToast('error', 'Enter Valid Email') || false;
    if (!profile.mobile.trim() || !/^\d{10}$/.test(profile.mobile))
      return showToast('error', 'Mobile must be 10 digits') || false;
    return true;
  };

  const saveProfile = () => {
    if (!validateProfile()) return;
    const updatedProfile = {
      ...profileData,
      ...profile,
      email: profileData.email,
      storeName: profileData.storeName,
    };
    updateProfile(updatedProfile);
    Toast.show({ type: 'success', text1: 'Profile saved successfully!' });
    setTimeout(() => handlePress(), 1000);
  };

  const handlePress = () => {
    if (profileData?.role === 'storekeeper') safePush('StorekeeperDashboard');
    else safePush('CustomerDashboard');
  };

  return (
    <View style={styles.pageContainer}>
      <BackButton title="Profile Setting" onPress={handlePress} />
      <View style={innerStyle.container}>
        <View style={innerStyle.profileImageSection}>
          <View style={innerStyle.imageWrapper}>
            <TouchableOpacity onPress={pickImage} activeOpacity={0.8}>
              {!loading && profile.image ? (
                <Animated.Image
                  source={{ uri: profile.image }}
                  style={[innerStyle.image, { opacity: fadeAnim }]}
                  resizeMode="cover"
                  onLoad={handleImageLoad}
                />
              ) : (
                <View style={{ overflow: 'hidden', borderRadius: 75 }}>
                  <ProfileImage height={130} width={130} />
                </View>
              )}
            </TouchableOpacity>
          </View>
          <TouchableOpacity
            onPress={pickImage}
            style={innerStyle.cameraIconContainer}
          >
            <CameraIcon style={innerStyle.cameraIcon} />
          </TouchableOpacity>
        </View>
      </View>

      <View style={innerStyle.profileDetails}>
        <View style={innerStyle.row}>
          <Text style={innerStyle.halfLabel}>First Name</Text>
          <Text style={innerStyle.halfLabel}>Last Name</Text>
        </View>
        <View style={innerStyle.row}>
          <TextInput
            style={innerStyle.halfInput}
            value={profile.firstName}
            maxLength={20}
            autoCapitalize="words"
            onChangeText={val => handleChange('firstName', val)}
          />
          <TextInput
            style={innerStyle.halfInput}
            value={profile.lastName}
            maxLength={20}
            autoCapitalize="words"
            onChangeText={val => handleChange('lastName', val)}
          />
        </View>

        <View style={innerStyle.email}>
          <Text style={innerStyle.fullLabel}>Email Address</Text>
          <TextInput
            style={innerStyle.fullInput}
            value={profile.email}
            keyboardType="email-address"
            onChangeText={val => handleChange('email', val)}
          />
        </View>

        <View
          style={[
            innerStyle.fullLabel,
            {
              flexDirection: 'row',
              justifyContent: 'space-between',
              alignItems: 'center',
            },
          ]}
        >
          <Text>Mobile Number</Text>
          <TouchableOpacity
            style={{
              paddingInline: 15,
              borderRadius: 20,
              flexDirection: 'row',
              alignItems: 'center',
            }}
            onPress={() => setModalVisible(true)}
          >
            <EvilIcons name="pencil" size={24} color="black" />
          </TouchableOpacity>
        </View>

        <TextInput
          style={innerStyle.mobileInput}
          value={profile.mobile}
          editable={false}
          keyboardType="number-pad"
          maxLength={10}
        />
      </View>

      <View style={innerStyle.buttonContainer}>
        <CustomButton title="Save Changes" onPress={saveProfile} />
      </View>

      <EditMobileNo
        visible={isModalVisible}
        onClose={() => setModalVisible(false)}
        onVerified={newmobile =>
          setProfile(prev => ({ ...prev, mobile: newmobile }))
        }
      />

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
    marginBottom: 50,
  },
  profileImageSection: {
    position: 'relative',
  },
  imageWrapper: {
    width: 130,
    height: 130,
    borderRadius: 75,
    backgroundColor: '#f0f0f0',
    justifyContent: 'center',
    alignItems: 'center',
    elevation: 5,
    overflow: 'hidden',
  },
  image: {
    width: 130,
    height: 130,
    borderRadius: 75,
    resizeMode: 'cover',
  },
  cameraIconContainer: {
    position: 'absolute',
    bottom: 0,
    right: 0,
  },
  cameraIcon: {
    height: 42,
    width: 42,
  },
  profileDetails: {
    width: '100%',
    marginTop: 10,
  },
  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    gap: 10,
    paddingHorizontal: 20,
    marginBottom: 10,
  },
  halfInput: {
    flex: 1,
    borderBottomWidth: 1,
    borderColor: '#aaa',
    borderRadius: 12,
    padding: 14,
    fontSize: Fonts.sizes.base,
  },
  fullInput: {
    marginBottom: 20,
    marginHorizontal: 20,
    borderBottomWidth: 1,
    borderColor: '#aaa',
    borderRadius: 12,
    padding: 14,
    fontSize: Fonts.sizes.base,
  },
  halfLabel: {
    flex: 1,
    margin: 10,
    fontSize: Fonts.sizes.sm,
    color: '#333',
  },
  mobileInput: {
    color: '#999',
    marginBottom: 20,
    marginHorizontal: 20,
    borderBottomWidth: 1,
    borderColor: '#ccc',
    borderRadius: 12,
    padding: 14,
    fontSize: Fonts.sizes.base,
  },
  fullLabel: {
    marginBlock: 10,
    marginInline: 30,
    fontSize: Fonts.sizes.sm,
    color: '#333',
  },
  buttonContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
