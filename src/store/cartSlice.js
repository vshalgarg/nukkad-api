  import { createSlice } from "@reduxjs/toolkit";

  const cartSlice = createSlice({
    name: "cart",
    initialState: {
      items: [],
      orders: [],
    },
    reducers: {
      addToCart: (state, action) => {
        const { product, cartQuantity } = action.payload;

        const index = state.items.findIndex(
          (item) => item.product.id === product.id
        );

        if (index >= 0) {
          return;
        }

        state.items.push({
          product: {
            ...product,
            amount: product.amount,
          },
          quantity: cartQuantity,
          selectedUnit: product.selectedUnit,
        });
      },

      updateCartItemQuantity: (state, action) => {
        const { productId, amount, selectedUnit } = action.payload;
        const item = state.items.find((item) => item.product.id === productId);
        if (item) {
          if (typeof amount === "number" || typeof amount === "string") {
            item.product.amount = amount;
          }
          if (selectedUnit) {
            item.selectedUnit = selectedUnit;
          }
        }
      },

      removeFromCart: (state, action) => {
        const { productId } = action.payload;
        state.items = state.items.filter(
          (item) => item.product.id !== productId
        );
      },

      addOrder: (state, action) => {
        state.orders.push(action.payload);
      },

      clearCart: (state) => {
        state.items = [];
      },
    },
  });

  export const {
    addToCart,
    updateCartItemQuantity,
    addOrder,
    clearCart,
    removeFromCart,
  } = cartSlice.actions;

  export default cartSlice.reducer;
