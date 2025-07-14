// services/customer/stores.js
import api from "../api";

export const getMyStores = async token => {
  try {
    const response = await api.get('/nukkad/api/customer/v1/myStores', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    console.log('✅ getMyStores API response:', response.data); 
    return response.data;
  } catch (err) {
    console.error(
      '❌ getMyStores API error:',
      err.response?.data || err.message,
    );
    throw err;
  }
};

export const deleteStore = async (storeId, token) => {
  try {
    const response = await api.delete(`/customer/stores/${storeId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    console.log(`🗑️ deleteStore response for ID ${storeId}:`, response.data); // 👈 log here
    return response.data;
  } catch (err) {
    console.error(
      '❌ deleteStore API error:',
      err.response?.data || err.message,
    );
    throw err;
  }
};
