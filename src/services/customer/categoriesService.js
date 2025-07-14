// services/category.js
import api from '../api';

// ✅ Get All Categories
export const getAllCategories = async () => {
  console.log('📤 Fetching All Categories');

  try {
    const response = await api.get('/nukkad/api/category/v1/get');


    console.log('✅ Get All Categories API Success:', {
      status: response.status,
      data: response.data,
    });

    return response.data;
  } catch (error) {
    console.error('❌ Get All Categories API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
    });

    throw new Error(
      error.response?.data?.message || 'Failed to fetch categories',
    );
  }
};

// services/category.js (same file)

export const getCategoryById = async (id) => {
    console.log(`📤 Fetching Category by ID: ${id}`);
  
    try {
      const response = await api.get(`/nukkad/api/category/v1/get/${id}`);
  
      console.log('✅ Get Category by ID API Success:', {
        status: response.status,
        data: response.data,
      });
  
      return response.data;
    } catch (error) {
      console.error('❌ Get Category by ID API Error:', {
        message: error.message,
        status: error.response?.status,
        data: error.response?.data,
      });
  
      throw new Error(
        error.response?.data?.message || `Failed to fetch category with ID ${id}`
      );
    }
  };
  