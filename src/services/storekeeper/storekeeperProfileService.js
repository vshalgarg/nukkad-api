import api from '../api';

export const createStorekeeperProfile = async (profileData, token) => {
  try {
    const formData = new FormData();

    // ✅ 1. Create a file-like object for JSON (with name/type)
    const jsonBlob = {
      name: 'data.json',
      type: 'application/json',
      string: JSON.stringify({
        name: profileData.name,
        storeName: profileData.storeName,
        gstNum: profileData.gstIn,
        addressLine1: profileData.addressLine1,
        addressLine2: profileData.addressLine2,
        landmark: profileData.landmark,
        city: profileData.city,
        state: profileData.state,
        pincode: profileData.pincode,
      }),
    };

    formData.append('data', jsonBlob); // ✅ send as file, not string

    // ✅ 2. Append image files
    profileData.images?.forEach((img, index) => {
      if (img?.uri) {
        formData.append('images', {
          uri: img.uri,
          name: img.fileName || `image_${index}.jpg`,
          type: img.type || 'image/jpeg',
        });
      }
    });

    // ✅ 3. Axios post
    const response = await api.post(
      '/nukkad/api/storekeeper/v1/profile/save',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`,
        },
      },
    );

    console.log('✅ Storekeeper API success:', response.data);
    return response.data;
  } catch (error) {
    console.error('❌ Storekeeper Create Profile API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
      request: profileData,
    });

    throw new Error(
      error.response?.data?.message || 'Failed to create storekeeper profile',
    );
  }
};
