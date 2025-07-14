import api from '../api';

// Create Customer Profile
export const createCustomerProfile = async profileData => {
  console.log('📤 Creating Customer Profile with payload:', profileData);

  try {
    const response = await api.post(
      '/nukkad/api/customer/v1/create',
      profileData,
    );

    console.log('✅ Create Profile API Success:', {
      status: response.status,
      data: response.data,
    });

    return response.data;
  } catch (error) {
    console.error('❌ Create Profile API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
      request: profileData,
    });

    throw new Error(
      error.response?.data?.message || 'Failed to create profile',
    );
  }
};
