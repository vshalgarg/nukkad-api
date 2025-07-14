import api from '../api';

/**
 * Add item to cart via API
 */
export const addToCartAPI = async (itemId, quantity, unit, token) => {
  try {
    console.log('🛒 [addToCartAPI] Request:', { itemId, quantity, unit });

    const response = await api.post(
      '/nukkad/api/cartItem/v1/add',
      {
        items: [{ itemId, quantity, unit }],
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      },
    );

    console.log('✅ [addToCartAPI] Response:', response.data);
    return response.data;
  } catch (error) {
    const errorMessage =
      error?.response?.data?.message || error?.message || 'Unknown error';
    console.error('❌ [addToCartAPI] Error:', errorMessage);
    throw new Error(errorMessage);
  }
};

/**
 * Update item quantity in cart via API
 */
export const updateCartAPI = async (itemId, quantity, unit, token) => {
  try {
    const payload = { itemId };
    if (quantity !== undefined) payload.quantity = quantity;
    if (unit !== undefined) payload.unit = unit;

    console.log('🔄 [updateCartAPI] Request:', payload);

    const response = await api.put(
      '/nukkad/api/cartItem/v1/item/update',
      payload,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      },
    );

    console.log('✅ [updateCartAPI] Response:', response.data);
    return response.data;
  } catch (error) {
    const errorMessage =
      error?.response?.data?.message || error?.message || 'Unknown error';
    console.error('❌ [updateCartAPI] Error:', errorMessage);
    throw new Error(errorMessage);
  }
};


/**
 * Delete item from cart using itemId
 */
export const deleteCartItemAPI = async (itemId, token) => {
  try {
    console.log('🗑️ [deleteCartItemAPI] Deleting itemId:', itemId);

    const response = await api.delete(
      `/nukkad/api/cartItem/v1/deleteItem/${itemId}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      },
    );

    console.log('✅ [deleteCartItemAPI] Response:', response.data);
    return response.data;
  } catch (error) {
    const errorMessage =
      error?.response?.data?.message || error?.message || 'Unknown error';
    console.error('❌ [deleteCartItemAPI] Error:', errorMessage);
    throw new Error(errorMessage);
  }
};

/**
 * Fetch all items from the cart
 */
export const getCartItemsAPI = async (token) => {
  if (!token) {
    console.log('❌ No token provided');
    throw new Error('Authentication token missing');
  }
  
  try {
    console.log('📥 [getCartItemsAPI] Fetching cart items...');

    const response = await api.get('/nukkad/api/cartItem/v1/get', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    console.log('✅ [getCartItemsAPI] Response:', response?.data);
    return response.data;
  } catch (error) {
    const errorMessage =
      error?.response?.data?.message || error?.message || 'Unknown error';
    console.error('❌ [getCartItemsAPI] Error:', errorMessage);
    throw new Error(errorMessage);
  }
};
