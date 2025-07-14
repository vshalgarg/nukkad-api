// Save New Address
import api from "../api";
export const addNewAddress = async (addressData) => {
  console.log('📤 Saving New Address with payload:', addressData);

  try {
    const response = await api.post(
      '/nukkad/api/addresses/v1/create',
      addressData,
    );

    console.log('✅ Save Address API Success:', {
      status: response.status,
      data: response.data,
    });

    return response.data;
  } catch (error) {
    console.error('❌ Save Address API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
      request: addressData,
    });

    throw new Error(error.response?.data?.message || 'Failed to save address');
  }
};

// Get All Addresses
export const getAllAddresses = async () => {
    console.log('📤 Fetching all addresses...');
  
    try {
      const response = await api.get('/nukkad/api/addresses/v1/get');
  
      console.log('✅ Get Addresses API Success:', {
        status: response.status,
        data: response.data,
      });
  
      return response.data;
    } catch (error) {
      console.error('❌ Get Addresses API Error:', {
        message: error.message,
        status: error.response?.status,
        data: error.response?.data,
      });
  
      throw new Error(
        error.response?.data?.message || 'Failed to fetch addresses'
      );
    }
  };

// Update Address
export const updateExistingAddress = async (addressId, updatedData) => {
    console.log(`📤 Updating Address ID ${addressId} with payload:`, updatedData);
  
    try {
      const response = await api.put(
        `/nukkad/api/addresses/v1/address/${addressId}`,
        updatedData
      );
  
      console.log('✅ Update Address API Success:', {
        status: response.status,
        data: response.data,
      });
  
      return response.data;
    } catch (error) {
      console.error('❌ Update Address API Error:', {
        message: error.message,
        status: error.response?.status,
        data: error.response?.data,
        request: updatedData,
      });
  
      throw new Error(
        error.response?.data?.message || 'Failed to update address'
      );
    }
  };
    
  // Mark Address as Default
  export const markAddressAsDefault = async (addressId) => {
    console.log(`📤 Marking Address ID ${addressId} as default...`);

    try {
      const response = await api.put(
        `/nukkad/api/addresses/v1/address/${addressId}/mark-default`,
      );

      console.log('✅ Mark Default Address API Success:', {
        status: response.status,
        data: response.data,
      });

      return response.data;
    } catch (error) {
      console.error('❌ Mark Default Address API Error:', {
        message: error.message,
        status: error.response?.status,
        data: error.response?.data,
        url: `/nukkad/api/addresses/v1/address/${addressId}/mark-default`,
      });

      throw new Error(
        error.response?.data?.message || 'Failed to mark address as default',
      );
    }
  };
  
  // Delete Address
export const deleteAddressFromServer = async addressId => {
  console.log(`🗑️ Deleting Address ID ${addressId}...`);

  try {
    const response = await api.delete(
      `/nukkad/api/addresses/v1/address/${addressId}/delete`
    );

    console.log('✅ Delete Address API Success:', {
      status: response.status,
      data: response.data,
    });

    return response.data;
  } catch (error) {
    console.error('❌ Delete Address API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
      url: `/nukkad/api/addresses/v1/address/${addressId}/delete`,
    });

    throw new Error(
      error.response?.data?.message || 'Failed to delete address'
    );
  }
};
