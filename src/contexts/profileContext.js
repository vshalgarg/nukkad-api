import AsyncStorage from '@react-native-async-storage/async-storage';
import { createContext, useContext, useEffect, useState } from 'react';
import { updateCustomerProfile } from '../services/customer/profileService';

const ProfileContext = createContext();

export const ProfileProvider = ({ children }) => {
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    (async () => {
      try {
        const savedProfile = await AsyncStorage.getItem('userProfile');
        if (savedProfile) {
          const parsed = JSON.parse(savedProfile);

          if (parsed.name && (!parsed.firstName || !parsed.lastName)) {
            const [firstName = '', ...rest] = parsed.name.split(' ');
            parsed.firstName = firstName;
            parsed.lastName = rest.join(' ');
          }

          setProfile(parsed);
        }
      } catch (err) {
        console.log('❌ Failed to load profile from storage', err);
      }
    })();
  }, []);

  const resetProfile = async () => {
    setProfile(null);
    await AsyncStorage.removeItem('userProfile');
  };

  const createProfile = async (fields) => {
    try {
      const newProfile = {
        ...fields,
        name: `${fields.firstName} ${fields.lastName}`.trim(),
      };
      setProfile(newProfile);
      await AsyncStorage.setItem('userProfile', JSON.stringify(newProfile));
    } catch (err) {
      console.error('❌ Failed to create profile locally:', err);
    }
  };

  const updateProfile = async (fields, token) => {
    if (!profile) throw new Error('Profile does not exist yet.');

    const payload = {
      ...fields,
      name: `${fields.firstName || profile.firstName} ${
        fields.lastName || profile.lastName
      }`.trim(),
    };

    try {
      await updateCustomerProfile(payload, token);
      const updated = { ...profile, ...fields };
      setProfile(updated);
      await AsyncStorage.setItem('userProfile', JSON.stringify(updated));
    } catch (err) {
      console.error('❌ Failed to update profile:', err);
      throw err;
    }
  };

  return (
    <ProfileContext.Provider
      value={{ profile, updateProfile, createProfile, resetProfile }}
    >
      {children}
    </ProfileContext.Provider>
  );
};

export const useProfile = () => useContext(ProfileContext);
