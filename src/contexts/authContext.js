import React, { createContext, useContext, useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [authData, setAuthData] = useState({
    token: null,
    role: null,
    userId: null,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadAuthData = async () => {
      try {
        const token = await AsyncStorage.getItem('authToken');
        const role = await AsyncStorage.getItem('role');
        const userId = await AsyncStorage.getItem('userId');

        setAuthData({
          token,
          role,
          userId: userId ? parseInt(userId) : null,
        });
      } catch (error) {
        console.error('Error loading auth data:', error);
      } finally {
        setLoading(false);
      }
    };

    loadAuthData();
  }, []);

  const login = async ({ token, role, userId }) => {
    try {
      if (!token || !role || userId === undefined || userId === null) {
        throw new Error('Missing login fields: token, role, or userId');
      }

      await AsyncStorage.multiSet([
        ['authToken', token],
        ['role', role],
        ['userId', String(userId)],
      ]);

      setAuthData({ token, role, userId });
    } catch (error) {
      console.error('Error saving login data:', error);
    }
  };
  

  const logout = async () => {
    try {
      await AsyncStorage.multiRemove(['authToken', 'role', 'userId']);
      setAuthData({ token: null, role: null, userId: null });
    } catch (error) {
      console.error('Error during logout:', error);
    }
  };

  return (
    <AuthContext.Provider value={{ ...authData, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
