// services/customer/rateStore.js
import api from '../api';

// ✅ Submit Rating & Review (customerId from token)
export const rateStore = async ({ storeKeeperId, review, rating }, token) => {
  const payload = {
    storeKeeperId,
    review,
    rating,
  };

  console.log(
    '📤 Submitting Store Rating with payload (token-based customerId):',
    payload,
  );

  try {
    const response = await api.post('/nukkad/api/rating/v1/create', payload, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    console.log('✅ Store Rating API Success:', {
      status: response.status,
      data: response.data,
    });

    return response.data;
  } catch (error) {
    console.error('❌ Store Rating API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
    });

    throw new Error(error.response?.data?.message || 'Failed to submit rating');
  }
};
