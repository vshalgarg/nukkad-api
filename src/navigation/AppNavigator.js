import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import HomeScreen from '../screens/HomeScreen.jsx';
import MobileOtpScreen from '../screens/auth/MobileOtpScreen.jsx';
import CustomerCreateProfile from '../screens/customer/CustomerCreateProfile.jsx';
import AddStore from '../screens/common/AddStore.jsx';
import CustomerDashboard from '../screens/customer/CustomerDashboard.jsx';
import ProductPage from '../screens/customer/ProductPage.jsx';
import ShoppingCart from '../screens/customer/ShoppingCart.jsx';
import AddressForm from '../screens/common/AddressForm.jsx';
import Address from '../screens/common/Address.jsx';
import PlaceOrder from '../screens/customer/PlaceOrder.jsx';
import StorekeeperCreateProfile from '../screens/storekeeper/StorekeeperCreateProfile.jsx';
import StorekeeperDashboard from '../screens/storekeeper/StorekeeperDashboard.jsx';
import ShowDetails from '../screens/storekeeper/ShowDetails.jsx';
import StoreDetail from '../screens/storekeeper/StoreDetail.jsx';
import PaymentOptions from '../screens/storekeeper/PaymentOptions.jsx';
import HelpSupport from '../screens/common/HelpSupport.jsx';
import MyStores from '../screens/common/MyStores.jsx';
import Notification from '../screens/common/Notification.jsx';
import Orders from '../screens/common/Orders.jsx';
import ProfileSetting from '../screens/common/ProfileSetting.jsx';
import RateStore from '../screens/common/RateStore.jsx';
import ReferToCustomer from '../screens/common/ReferToCustomer.jsx';

const Stack = createNativeStackNavigator();

export default function AppNavigator() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Home" component={HomeScreen} />
      <Stack.Screen name="MobileOtpScreen" component={MobileOtpScreen} />
      <Stack.Screen name="CustomerCreateProfile" component={CustomerCreateProfile} />
      <Stack.Screen name="AddStore" component={AddStore} />
      <Stack.Screen name="CustomerDashboard" component={CustomerDashboard} />
      <Stack.Screen name="ProductPage" component={ProductPage} />
      <Stack.Screen name="ShoppingCart" component={ShoppingCart} />
      <Stack.Screen name="AddressForm" component={AddressForm} />
      <Stack.Screen name="Address" component={Address} />
      <Stack.Screen name="PlaceOrder" component={PlaceOrder} />
      <Stack.Screen
        name="StorekeeperCreateProfile"
        component={StorekeeperCreateProfile}
      />
      <Stack.Screen
        name="StorekeeperDashboard"
        component={StorekeeperDashboard}
      />
      <Stack.Screen name="ShowDetails" component={ShowDetails} />
      <Stack.Screen name="ReferToCustomer" component={ReferToCustomer} />
      <Stack.Screen name='StoreDetail' component={StoreDetail} />
      <Stack.Screen name='PaymentOptions' component={PaymentOptions} />
      <Stack.Screen name='HelpSupport' component={HelpSupport}/>
      <Stack.Screen name= 'MyStores' component={MyStores}/>
      <Stack.Screen name='Notification' component={Notification}/>
      <Stack.Screen name='Orders' component={Orders}/>
      <Stack.Screen name='ProfileSetting' component={ProfileSetting}/>
      <Stack.Screen name='RateStore' component={RateStore} />
    </Stack.Navigator>
  );
}
