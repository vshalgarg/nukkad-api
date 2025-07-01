import AsyncStorage from "@react-native-async-storage/async-storage";
import { createContext, useContext, useEffect, useState } from "react";

const ProfileContext = createContext();

export const ProfileProvider = ({ children }) => {
  const [profile, setProfile] = useState({
    firstName: "",
    lastName: "",
    email: "",
    storeName: "",
    mobile: "",
    role: "",
    image: null,
  });

  useEffect(() => {
    (async () => {
      try {
        const savedProfile = await AsyncStorage.getItem("userProfile");
        if (savedProfile) setProfile(JSON.parse(savedProfile));
      } catch (err) {
        console.log("Failed to load profile", err);
      }
    })();
  }, []);

  const resetProfile = () => setProfile(null);

  const updateProfile = async (updatedFields) => {
    const newProfile = { ...profile, ...updatedFields };
    setProfile(newProfile);
    try {
      await AsyncStorage.setItem("userProfile", JSON.stringify(newProfile));
    } catch (err) {
      console.log("Failed to save profile", err);
    }
  };
  

  return (
    <ProfileContext.Provider value={{ profile, updateProfile, resetProfile }}>
      {children}
    </ProfileContext.Provider>
  );
};

export const useProfile = () => useContext(ProfileContext);
