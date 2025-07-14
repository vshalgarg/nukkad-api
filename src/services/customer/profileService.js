// services/customer/profile.js
import api from '../api';

// ✅ Get Customer Profile
// export const getCustomerProfile = async (token) => {
//   try {
//     const response = await api.get('/nukkad/api/customer/v1/get/profile', {
//       headers: {
//         Authorization: `Bearer ${token}`,
//       },
//     });

//     console.log('✅ Get Profile API Success:', {
//       status: response.status,
//       data: response.data,
//     });

//     const user = response.data?.data || {};

//     // Parse name -> firstName, lastName
//     const [firstName = '', ...rest] = user.name?.split(' ') || [];
//     const lastName = rest.join(' ');

//     return {
//       ...user,
//       firstName,
//       lastName,
//       DOB: user.dob,
//     };
//   } catch (error) {
//     console.error('❌ Get Profile API Error:', {
//       message: error.message,
//       status: error.response?.status,
//       data: error.response?.data,
//     });

//     throw new Error(error.response?.data?.message || 'Failed to fetch profile');
//   }
// };

// ✅ Update Customer Profile
export const updateCustomerProfile = async (payload, token) => {
  console.log('📤 Updating Customer Profile with payload:', payload);

  try {
    const response = await api.put('/nukkad/api/customer/v1/update', payload, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    console.log('✅ Update Profile API Success:', {
      status: response.status,
      data: response.data,
    });

    const updatedUser = response.data?.data || {};

    const [firstName = '', ...rest] = updatedUser.name?.split(' ') || [];
    const lastName = rest.join(' ');

    return {
      ...updatedUser,
      firstName,
      lastName,
      DOB: updatedUser.dob,
    };
  } catch (error) {
    console.error('❌ Update Profile API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
    });

    throw new Error(
      error.response?.data?.message || 'Failed to update profile',
    );
  }
};
