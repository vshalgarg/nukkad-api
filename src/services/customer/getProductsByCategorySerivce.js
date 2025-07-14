// services/products.js
import api from "../api";
export const getProductsByCategory = async categoryId => {
  try {
    const response = await api.get(
      `/nukkad/api/item/v1/get_by_category/${categoryId}`,
    );

    console.log(
      '✅ API raw response:',
      JSON.stringify(response?.data, null, 2),
    );

    return response?.data?.items || []; // use correct path here
  } catch (error) {
    console.error('❌ Get Products by Category API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
    });
    throw new Error(
      error.response?.data?.message ||
        `Failed to fetch products for category ID ${categoryId}`,
    );
  }
};
