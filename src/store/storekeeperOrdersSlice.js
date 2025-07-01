// store/storekeeperOrdersSlice.js
import { createSlice } from '@reduxjs/toolkit';
import ordersData from '../HardcodeData/orderData.js';

const initialState = {
  orders: [...ordersData],
};

const storekeeperOrdersSlice = createSlice({
  name: 'storekeeperOrders',
  initialState,
  reducers: {
    updateOrderStatus: (state, action) => {
      const { orderId, newStatus } = action.payload;
      const order = state.orders.find(order => order.orderId === orderId);
      if (order) {
        order.status = newStatus;
      }
    },
    updateOrderPrices: (state, action) => {
      const { orderId, items } = action.payload;
      const order = state.orders.find(order => order.orderId === orderId);
      if (order) {
        order.items = items;
      }
    },
    resetOrdersFromFile: state => {
      state.orders = [...ordersData];
    },
  },
});

export const { updateOrderStatus, updateOrderPrices, resetOrdersFromFile } =
  storekeeperOrdersSlice.actions;

export default storekeeperOrdersSlice.reducer;
