import React, { useState } from 'react';
import {
  Alert,
  FlatList,
  Image,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import { useRoute, useNavigation } from '@react-navigation/native';
import { useDispatch, useSelector } from 'react-redux';

import Mobile from '../../../assets/images/contact.svg';
import BackButton from '../../components/BackButton';
import ConnectPopup from '../../components/ConnectPopUp';
import CustomButton from '../../components/CustomButton';
import Colors from '../../styles/colors';
import styles from '../../styles/globalStyles';
import {
  updateOrderPrices,
  updateOrderStatus,
} from '../../store/storekeeperOrdersSlice';
import Fonts from '../../styles/font';

const ShowDetails = () => {
  const route = useRoute();
  const navigation = useNavigation();

  const { orderId, items } = route.params;
  const parsedItems = JSON.parse(items);

  const dispatch = useDispatch();
  const order = useSelector(state =>
    state.storekeeperOrders.orders.find(order => order.orderId === orderId),
  );

  const isInProgress = order?.status === 'In Progress';
  const isDispatched = order?.status === 'Dispatched';
  const isCompleted = order?.status === 'Completed';
  const isCancelled = order?.status === 'Cancelled';
  const isPending = !isCompleted && !isCancelled;

  const [showPopup, setShowPopup] = useState(false);

  const [prices, setPrices] = useState(
    parsedItems.reduce((acc, item) => {
      const itemId = item.id || item.productId;
      const matchedItem = order?.items.find(
        ordItem => (ordItem.id || ordItem.productId) === itemId,
      );
      acc[itemId] = matchedItem?.price || '';
      return acc;
    }, {}),
  );

  const handlePriceChange = (id, value) => {
    const numericValue = value.replace(/[^0-9]/g, '');
    setPrices(prev => ({
      ...prev,
      [id]: numericValue,
    }));
  };

  const handleReject = () => {
    Alert.alert('Reject Order', 'Are you sure you want to reject order?', [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Reject Order',
        onPress: () => {
          if (order) {
            dispatch(updateOrderStatus({ orderId, newStatus: 'Cancelled' }));
            navigation.navigate('StorekeeperDashboard', { tab: 'Cancelled' });
          } else {
            Alert.alert('Error', 'Order not found.');
          }
        },
      },
    ]);
  };

  const handleDispatch = () => {
    const allPricesEntered = parsedItems.every(item => {
      const itemId = item.id || item.productId;
      return prices[itemId] && prices[itemId].trim() !== '';
    });

    if (!allPricesEntered) {
      Alert.alert(
        'Missing Prices',
        'Please enter prices for all items before dispatching.',
      );
      return;
    }

    Alert.alert(
      'Dispatch Order',
      'Are you sure you want to dispatch this order?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Dispatch',
          onPress: () => {
            if (order) {
              const pricedItems = parsedItems.map(item => {
                const itemId = item.id || item.productId;
                return {
                  ...item,
                  price: parseInt(prices[itemId]) || 0,
                };
              });

              dispatch(updateOrderPrices({ orderId, items: pricedItems }));
              dispatch(updateOrderStatus({ orderId, newStatus: 'Dispatched' }));
            } else {
              Alert.alert('Error', 'Order not found.');
            }
          },
        },
      ],
    );
  };

  const handleDeliver = () => {
    Alert.alert(
      'Deliver Order',
      'Are you sure you want to deliver this order?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Yes, Deliver',
          onPress: () => {
            if (order) {
              dispatch(updateOrderStatus({ orderId, newStatus: 'Completed' }));
              navigation.navigate('StorekeeperDashboard', { tab: 'Completed' });
            } else {
              Alert.alert('Error', 'Order not found.');
            }
          },
        },
      ],
    );
  };

  return (
    <View style={[styles.pageContainer, { flex: 1 }]}>
      <BackButton title="Order Details" />
      <FlatList
        data={parsedItems}
        keyExtractor={(item, index) =>
          item.id || item.productId || index.toString()
        }
        contentContainerStyle={{
          padding: 20,
          paddingBottom: isPending ? 220 : 80,
        }}
        ListHeaderComponent={
          <View>
            <Text style={innerStyle.heading}>Delivery Address</Text>
            <View style={innerStyle.AddressCard}>
              <Text style={innerStyle.addressCardDetails}>
                {order.customerName}
              </Text>
              <View style={innerStyle.rowBetween}>
                <Text style={innerStyle.addressCardDetails}>
                  {order.mobileNumber}
                </Text>
                {(isPending || isDispatched) && (
                  <Mobile onPress={() => setShowPopup(true)} height={40} />
                )}
              </View>
              <Text style={innerStyle.addressCardDetails}>{order.address}</Text>
            </View>
            <Text style={innerStyle.heading}>Order ID: #{orderId}</Text>
          </View>
        }
        renderItem={({ item }) => {
          const itemId = item.id || item.productId;
          const itemTitle = item.title || item.productName;
          const itemWeight = item.weight || 'N/A';

          return (
            <View style={innerStyle.card}>
              <Image source={{ uri: item.image }} style={innerStyle.image} />
              <View style={{ flex: 1, marginLeft: 10 }}>
                <Text style={innerStyle.title}>{itemTitle}</Text>
                <Text style={innerStyle.text}>Weight: {itemWeight}</Text>
              </View>

              {isCancelled ? (
                <Text style={innerStyle.rejectedText}>Rejected</Text>
              ) : isCompleted || isDispatched ? (
                <TextInput
                  style={[innerStyle.input, { color: '#333' }]}
                  value={`₹ ${prices[itemId] || 'N/A'}`}
                  editable={false}
                />
              ) : (
                <TextInput
                  placeholder="Set Price"
                  style={innerStyle.input}
                  keyboardType="numeric"
                  value={prices[itemId]?.toString()}
                  maxLength={4}
                  onChangeText={value => handlePriceChange(itemId, value)}
                />
              )}
            </View>
          );
        }}
      />

      {isInProgress && (
        <View style={innerStyle.buttonContainer}>
          <CustomButton
            title="Reject Order"
            onPress={handleReject}
            style={{ backgroundColor: 'red', borderWidth: 0 }}
          />
          <CustomButton title="Dispatch Order" onPress={handleDispatch} />
        </View>
      )}

      {isDispatched && (
        <View style={innerStyle.buttonContainer}>
          <CustomButton
            title="Deliver Order"
            onPress={handleDeliver}
            style={{ backgroundColor: Colors.primary, borderWidth: 0 }}
          />
        </View>
      )}

      <ConnectPopup
        visible={showPopup}
        onClose={() => setShowPopup(false)}
        phone={order?.mobileNumber || '9999999999'}
      />
    </View>
  );
};

export default ShowDetails;

const innerStyle = StyleSheet.create({
  AddressCard: {
    padding: 8,
    borderWidth: 2,
    borderRadius: 15,
    borderColor: Colors.primary,
    marginBottom: 15,
  },
  addressCardDetails: {
    lineHeight: 30,
    fontSize: Fonts.sizes.base,
  },
  heading: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  card: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#fff',
    padding: 15,
    borderRadius: 10,
    marginBottom: 15,
    elevation: 2,
    height: 100,
  },
  rowBetween: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  image: {
    width: 80,
    height: '100%',
    borderRadius: 10,
    resizeMode: 'cover',
  },
  title: {
    fontSize: Fonts.sizes.base,
    fontWeight: 'bold',
  },
  text: {
    marginTop: 5,
    color: '#555',
  },
  input: {
    width: 80,
    height: 40,
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 50,
    paddingHorizontal: 10,
    textAlign: 'center',
    lineHeight: 20,
  },
  rejectedText: {
    color: 'red',
    fontWeight: 'bold',
    fontSize: Fonts.sizes.base,
    width: 80,
    textAlign: 'center',
  },
  buttonContainer: {
    justifyContent: 'space-around',
    flexDirection: 'row',
    position: 'absolute',
    bottom: 20,
    left: 20,
    right: 20,
    zIndex: 10,
  },
});
