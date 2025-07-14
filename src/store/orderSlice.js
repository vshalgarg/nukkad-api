// store/orderSlice.js
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  orders: [],
};

const orderSlice = createSlice({
  name: 'order',
  initialState,
  reducers: {
    addOrder: (state, action) => {
      state.orders.push(action.payload);
    },

    setOrders: (state, action) => {
      state.orders = action.payload;
    },

    clearOrders: state => {
      state.orders = [];
    },
  },
});

export const { addOrder, setOrders, clearOrders } = orderSlice.actions;

export default orderSlice.reducer;
