// store/cartSlice.js
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  items: [],
  userId: null,
};

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    setCartUser: (state, action) => {
      const newUserId = action.payload;

      if (state.userId && state.userId !== newUserId) {
        state.items = [];
      }

      state.userId = newUserId;
    },

    clearCart: state => {
      state.items = [];
      state.userId = null;
    },

    setCartItems: (state, action) => {
      state.items = action.payload;
    },

    addToCart: (state, action) => {
      const { product, quantity, selectedUnit, itemId } = action.payload;

      const index = state.items.findIndex(
        item => item.product.id === product.id,
      );

      if (index >= 0) return; // prevent duplicates

      state.items.push({
        itemId,
        product: {
          ...product,
          amount: product.amount,
        },
        quantity,
        selectedUnit,
      });
    },

    updateCartItemQuantity: (state, action) => {
      const { itemId, amount, selectedUnit, itemCount } = action.payload;

      const item = state.items.find(item => item.itemId === itemId);
      if (item) {
        if (typeof amount === 'number' || typeof amount === 'string') {
          item.product.amount = amount;
        }
        if (selectedUnit) {
          item.selectedUnit = selectedUnit;
        }
        if (itemCount) {
          item.quantity = itemCount;
        }
      }
    },

    removeFromCart: (state, action) => {
      const { itemId } = action.payload;
      state.items = state.items.filter(item => item.product.id !== itemId);
    },
  },
});

export const {
  setCartUser,
  clearCart,
  setCartItems,
  addToCart,
  updateCartItemQuantity,
  removeFromCart,
} = cartSlice.actions;

export default cartSlice.reducer;
