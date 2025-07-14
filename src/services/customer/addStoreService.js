import api from '../api';

// Add Store to Customer
export const addCustomerStore = async storekeeperId => {
  console.log('📤 Adding Store to Customer with storeQrId:', storekeeperId);

  try {
    const response = await api.post(
      `/nukkad/api/customer/v1/add/store?storeQrId=${storekeeperId}`,
    );

    console.log('✅ Add Store API Success:', {
      status: response.status,
      data: response.data,
    });
    return response.data;
  } catch (error) {
    console.error('❌ Add Store API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
      storekeeperId,
    });

    throw new Error(
      error.response?.data?.message || 'Failed to add store to customer',
    );
  }
};
