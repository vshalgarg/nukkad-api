import axios from 'axios';

import { API_URL, CLIENT_NAME, CLIENT_SECRET } from '@env';
import AsyncStorage from '@react-native-async-storage/async-storage';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
    clientName: CLIENT_NAME,
    clientSecret: CLIENT_SECRET,
    Accept: 'application/json',
  },
});

api.interceptors.request.use(async(config)=>{
  const token=await AsyncStorage.getItem('authToken');
  if(token){
    config.headers.Authorization=`Bearer ${token}`;
  }
  return config;
})

api.interceptors.response.use(
  response => {
    const { responseCode, message } = response.data;
    if (responseCode && responseCode !== 200) {
      return Promise.reject({
        code: responseCode,
        message: message,
        data: response.data,
      });
    }
    return response;
  },
  error => {
    return Promise.reject(error);
  },
);
export default api;