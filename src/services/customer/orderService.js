import api from '../api';

export const placeOrder = async (payload, token) => {
  console.log('Order placed by customer request', payload);
  try {
    const response = await api.post(
      'nukkad/api/orders/v1/placeOrder',
      payload,
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      },
    );
    console.log('Order Api response', {
      status: response.status,
      data: response.data,
    });
    return response.data;
  } catch (error) {
    const errorMessage=error?.response?.data?.message || error?.message || "Unknown error";
    console.error('❌ [getCartItemsAPI] Error:', errorMessage);
    throw new Error(errorMessage);
  }
};
