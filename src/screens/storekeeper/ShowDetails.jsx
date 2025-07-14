import React, { useState, memo } from 'react';
import {
  Alert,
  FlatList,
  Image,
  Keyboard,
  KeyboardAvoidingView,
  Platform,
  StyleSheet,
  Text,
  TextInput,
  TouchableWithoutFeedback,
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
  updateOrderNote,
} from '../../store/storekeeperOrdersSlice';
import Fonts from '../../styles/font';

const OrderItem = memo(({ item, price, isEditable, onPriceChange }) => {
  const itemId = item.id || item.productId;
  const itemTitle = item.title || item.productName;
  const itemWeight = item.weight || '0';

  return (
    <View style={innerStyle.card}>
      <Image source={{ uri: item.image }} style={innerStyle.image} />
      <View style={{ flex: 1, marginLeft: 10 }}>
        <Text style={innerStyle.title}>{itemTitle}</Text>
        <Text style={innerStyle.text}>Weight: {itemWeight}</Text>
      </View>

      {isEditable ? (
        <TextInput
          placeholder="Set Price"
          placeholderTextColor={Colors.secondaryText} 
          style={innerStyle.input}
          keyboardType="numeric"
          value={price?.toString()}
          maxLength={4}
          onChangeText={value => onPriceChange(itemId, value)}
        />
      ) : (
        <TextInput
          style={[innerStyle.input, { color: Colors.secondary }]}
          value={`₹ ${price || '0'}`}
          editable={false}
        />
      )}
    </View>
  );
});

const ShowDetails = () => {
  const [note, setNote] = useState('');
  const [showPopup, setShowPopup] = useState(false);

  const route = useRoute();
  const navigation = useNavigation();
  const dispatch = useDispatch();

  const { orderId, items } = route.params;
  const parsedItems = JSON.parse(items);

  const order = useSelector(state =>
    state.storekeeperOrders.orders.find(order => order.orderId === orderId),
  );

  const isInProgress = order?.status === 'In Progress';
  const isDispatched = order?.status === 'Dispatched';
  const isDelivered = order?.status === 'Delivered';
  const isRejected = order?.status === 'Rejected';
  const isPending = !isDelivered && !isRejected;

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
    setPrices(prev => ({ ...prev, [id]: numericValue }));
  };

  const handleReject = () => {
    Alert.alert('Reject Order', 'Are you sure you want to reject order?', [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Reject Order',
        onPress: () => {
          if (order) {
            dispatch(updateOrderStatus({ orderId, newStatus: 'Rejected' }));
            navigation.navigate('StorekeeperDashboard', { tab: 'Rejected' });
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
                return { ...item, price: parseInt(prices[itemId]) || 0 };
              });

              dispatch(updateOrderPrices({ orderId, items: pricedItems }));
              dispatch(updateOrderStatus({ orderId, newStatus: 'Dispatched' }));
              if (note.trim()) {
                dispatch(updateOrderNote({ orderId, note: note.trim() }));
              }
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
              dispatch(updateOrderStatus({ orderId, newStatus: 'Delivered' }));
              navigation.navigate('StorekeeperDashboard', { tab: 'Delivered' });
            } else {
              Alert.alert('Error', 'Order not found.');
            }
          },
        },
      ],
    );
  };

  return (
    <View style={{ flex: 1 }}>
      <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
        <View style={[styles.pageContainer, { flex: 1 }]}>
          <BackButton title="Order Details" />

          <KeyboardAvoidingView
            style={{ flex: 1 }}
            behavior={Platform.OS === 'ios' ? 'padding' : undefined}
            keyboardVerticalOffset={Platform.OS === 'ios' ? 60 : 0}
          >
            <FlatList
              data={parsedItems}
              keyExtractor={(item, index) =>
                item.id || item.productId || index.toString()
              }
              contentContainerStyle={{
                padding: 20,
              }}
              showsVerticalScrollIndicator={false}
              initialNumToRender={5}
              maxToRenderPerBatch={8}
              windowSize={10}
              getItemLayout={(data, index) => ({
                length: 115,
                offset: 115 * index,
                index,
              })}
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
                        <Mobile
                          onPress={() => setShowPopup(true)}
                          height={40}
                        />
                      )}
                    </View>
                    <Text style={innerStyle.addressCardDetails}>
                      {order.address}
                    </Text>
                  </View>
                  <Text style={innerStyle.heading}>Order ID: #{orderId}</Text>
                </View>
              }
              renderItem={({ item }) => (
                <OrderItem
                  item={item}
                  price={prices[item.id || item.productId]}
                  isEditable={!isDelivered && !isDispatched && !isRejected}
                  onPriceChange={handlePriceChange}
                />
              )}
              ListFooterComponent={
                isPending ? (
                  <View style={{ marginTop: 10, marginHorizontal: 5 }}>
                    <Text style={{ marginBottom: 5, fontWeight: 'bold' }}>
                      Note for Customer
                    </Text>
                    <TextInput
                      style={{
                        height: 100,
                        borderWidth: 1,
                        borderColor: Colors.borderColor,
                        borderRadius: 10,
                        padding: 10,
                        textAlignVertical: 'top',
                        backgroundColor: Colors.bgClr,
                      }}
                      multiline
                      placeholder="Write a note to the customer about this order"
                      value={note}
                      onChangeText={setNote}
                    />
                  </View>
                ) : null
              }
            />
          </KeyboardAvoidingView>

          {(isInProgress || isDispatched) && (
            <View style={innerStyle.fixedButtonWrapper}>
              <View style={innerStyle.buttonContainer}>
                {isInProgress && (
                  <>
                    <CustomButton
                      title="Reject Order"
                      onPress={handleReject}
                      style={{ backgroundColor: Colors.reject, borderWidth: 0 }}
                    />
                    <CustomButton
                      title="Dispatch Order"
                      onPress={handleDispatch}
                      style={{fontSize:Fonts.sizes.sm}}
                    />
                  </>
                )}
                {isDispatched && (
                  <CustomButton
                    title="Deliver Order"
                    onPress={handleDeliver}
                    style={{ backgroundColor: Colors.primary, borderWidth: 0 }}
                  />
                )}
              </View>
            </View>
          )}

          <ConnectPopup
            visible={showPopup}
            onClose={() => setShowPopup(false)}
            phone={order?.mobileNumber || '9999999999'}
          />
        </View>
      </TouchableWithoutFeedback>
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
    backgroundColor: Colors.bgClr,
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
    color: Colors.secondary,
  },
  input: {
    width: 80,
    height: 40,
    borderWidth: 1,
    borderColor: Colors.borderColor,
    borderRadius: 50,
    paddingHorizontal: 10,
    textAlign: 'left',
    lineHeight: 20,
    includeFontPadding: false,
    textAlignVertical: 'center',
  },
  rejectedText: {
    color: Colors.reject,
    fontWeight: 'bold',
    fontSize: Fonts.sizes.base,
    width: 80,
    textAlign: 'center',
  },
  // fixedButtonWrapper: {
  //   position: 'absolute',
  //   bottom: 0,
  //   left: 0,
  //   right: 0,
  //   zIndex: 100,
  //   backgroundColor: '#fff',
  //   borderTopWidth: 1,
  //   borderColor: '#ddd',
  //   paddingBottom: Platform.OS === 'ios' ? 20 : 10,
  // },
  buttonContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-around',
    paddingHorizontal: 20,
    paddingVertical: 10,
    gap: 10,
  },
});
