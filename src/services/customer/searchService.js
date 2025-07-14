// services/customer/search.js
import api from '../api';

export const searchProducts = async keyword => {
  try {
    const response = await api.get('/nukkad/api/search/v1/get', {
      params: { keyword },
    });

    console.log('✅ Search API Result:', response.data);
    return response.data;
  } catch (error) {
    console.error('❌ Search API Error:', error.message);
    throw new Error('Failed to fetch search results');
  }
};
