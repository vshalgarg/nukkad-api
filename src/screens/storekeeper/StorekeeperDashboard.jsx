import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import Entypo from 'react-native-vector-icons/Entypo';
import FontAwesome5 from 'react-native-vector-icons/FontAwesome5';
import React, { useEffect, useState } from 'react';
import {
  Alert,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  Linking,
} from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigation, useRoute } from '@react-navigation/native';

import SideBar from '../../components/sidebar/SideBar';
import { updateOrderStatus } from '../../store/storekeeperOrdersSlice';
import Colors from '../../styles/colors';
import styles from '../../styles/globalStyles';
import Fonts from '../../styles/font';
import textStyles from '../../styles/textStyles';

const StorekeeperDashboard = () => {
  const [formState, setFormState] = useState(0);
  const [isSideBarOpen, setIsSideBarOpen] = useState(false);
  const [popupOrderId, setPopupOrderId] = useState(null);

  const dispatch = useDispatch();
  const navigation = useNavigation();
  const route = useRoute();
  const tab = route?.params?.tab;

  const statusMap = ['Pending', 'In Progress', 'Delivered'];
  const orders = useSelector(state => state.storekeeperOrders.orders);

  useEffect(() => {
    const tabIndex = statusMap.findIndex(
      status => status.toLowerCase() === tab?.toLowerCase(),
    );
    if (tabIndex !== -1) setFormState(tabIndex);
  }, [tab]);

  const filteredOrders = orders.filter(order => {
    if (statusMap[formState] === 'In Progress') {
      return order.status === 'In Progress' || order.status === 'Dispatched';
    }
    return order.status === statusMap[formState];
  });

  const safePush = routeObj => {
    try {
      navigation.navigate(routeObj?.pathname, routeObj?.params || {});
    } catch (err) {
      console.error('Navigation Error:', err);
    }
  };

  const handleDetails = order => {
    if (order.status === 'Pending') {
      dispatch(
        updateOrderStatus({ orderId: order.orderId, newStatus: 'In Progress' }),
      );
    }
    safePush({
      pathname: 'ShowDetails',
      params: {
        orderId: order.orderId,
        items: JSON.stringify(order.items),
      },
    });
  };

  const handleReject = orderId => {
    Alert.alert(
      'Reject Order',
      'Are you sure you want to reject this order?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Reject',
          style: 'destructive',
          onPress: () =>
            dispatch(updateOrderStatus({ orderId, newStatus: 'Rejected' })),
        },
      ],
      { cancelable: true },
    );
  };

  const handleDeliver = orderId => {
    Alert.alert(
      'Deliver Order',
      'Are you sure you want to deliver this order?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Yes, Deliver',
          onPress: () =>
            dispatch(updateOrderStatus({ orderId, newStatus: 'Delivered' })),
        },
      ],
      { cancelable: true },
    );
  };

  return (
    <View style={styles.pageContainer}>
      <View style={innerStyle.topBar}>
        <TouchableOpacity onPress={() => setIsSideBarOpen(true)}>
          <MaterialIcons name="menu" size={26} color={Colors.secondary} />
        </TouchableOpacity>
        <Text style={[innerStyle.heading,textStyles.subheading]}>My Orders</Text>
        <TouchableOpacity
          onPress={() => safePush({ pathname: 'Notification' })}
        >
          <FontAwesome5 name="bell" size={24} color={Colors.secondary} />
        </TouchableOpacity>
      </View>

      <SideBar
        isVisible={isSideBarOpen}
        onClose={() => setIsSideBarOpen(false)}
      />

      <View style={innerStyle.status}>
        {statusMap.map((status, index) => (
          <Pressable
            key={index}
            style={[
              innerStyle.statusButton,
              formState === index && { backgroundColor: Colors.secondaryText },
            ]}
            onPress={() => setFormState(index)}
          >
            <Text style={formState === index && { color: Colors.bgClr }}>
              {status}
            </Text>
          </Pressable>
        ))}
      </View>

      {filteredOrders.length === 0 ? (
        <View style={innerStyle.emptyStateContainer}>
          <Text style={innerStyle.emptyStateText}>
            No {statusMap[formState]} orders found.
          </Text>
        </View>
      ) : (
        <ScrollView
          style={{ paddingHorizontal: 20, marginTop: 20 }}
          showsVerticalScrollIndicator={false}
          contentContainerStyle={{ paddingBottom: 30 }}
        >
          {filteredOrders.map(order => (
            <Pressable
              key={order.orderId}
              style={innerStyle.orderCard}
              onPress={() => handleDetails(order)}
            >
              <View>
                <Text style={innerStyle.orderText}>
                  Order <Text>#{order.orderId}</Text>
                </Text>
                <Text style={innerStyle.orderDetailsHeading}>
                  Customer Name:
                  <Text style={innerStyle.orderDetails}>
                    {' ' + order.customerName}
                  </Text>
                </Text>
                <Text style={innerStyle.orderDetailsHeading}>
                  LandMark:
                  <Text style={innerStyle.orderDetails}>
                    {' ' + order.landmark}
                  </Text>
                </Text>
                {order.status !== 'Pending' && order.status !== 'Rejected' && (
                  <Text style={innerStyle.orderDetailsHeading}>
                    Tracking
                    <Text style={innerStyle.orderDetails}>
                      {' #' + order.trackingNumber}
                    </Text>
                  </Text>
                )}
                <Text style={innerStyle.orderDetailsHeading}>
                  Quantity:
                  <Text style={innerStyle.orderDetails}>
                    {' ' + order.quantity}
                  </Text>
                </Text>
                <Text
                  style={[
                    innerStyle.updatedStatus,
                    {
                      color:
                        order.status === 'Pending'
                          ? 'red'
                          : order.status === 'In Progress'
                          ? 'orange'
                          : order.status === 'Delivered'
                          ? 'green'
                          : 'red',
                    },
                  ]}
                >
                  {order.status.toUpperCase()}
                </Text>
              </View>

              <View
                style={{
                  alignItems: 'center',
                  justifyContent: 'space-between',
                }}
              >
                <View
                  style={{ flexDirection: 'row', alignItems: 'center', gap: 5 }}
                >
                  <Text style={innerStyle.orderDetails}>{order.date}</Text>
                  {order.status !== 'Delivered' &&
                    order.status !== 'Rejected' && (
                      <Pressable
                        onPress={() =>
                          setPopupOrderId(prev =>
                            prev === order.orderId ? null : order.orderId,
                          )
                        }
                      >
                        <Entypo
                          name="dots-three-vertical"
                          size={18}
                          color={Colors.secondary}
                        />
                      </Pressable>
                    )}
                </View>

                {popupOrderId === order.orderId && (
                  <View style={innerStyle.popupMenu}>
                    <Pressable
                      style={innerStyle.popupItem}
                      onPress={() => {
                        setPopupOrderId(null);
                        const url = `tel:${
                          order?.mobileNumber || '9999999999'
                        }`;
                        Linking.openURL(url).catch(err =>
                          console.error('Call error:', err),
                        );
                      }}
                    >
                      <View
                        style={{
                          flexDirection: 'row',
                          justifyContent: 'space-between',
                          alignItems: 'center',
                        }}
                      >
                        <Text style={innerStyle.popupText}>Call</Text>
                        <FontAwesome5 name="phone" size={15} color={Colors.secondary}/>
                      </View>
                    </Pressable>
                    <Pressable
                      style={innerStyle.popupItem}
                      onPress={() => {
                        setPopupOrderId(null);
                        const url = `https://wa.me/${
                          order?.mobileNumber || '9999999999'
                        }`;
                        Linking.openURL(url).catch(err =>
                          console.error('WhatsApp error:', err),
                        );
                      }}
                    >
                      <View
                        style={{
                          flexDirection: 'row',
                          justifyContent: 'space-between',
                          alignItems: 'center',
                        }}
                      >
                        <Text style={innerStyle.popupText}>WhatsApp</Text>
                        <FontAwesome5
                          name="whatsapp"
                          size={18}
                          color="#25D366"
                        />
                      </View>
                    </Pressable>
                  </View>
                )}

                {order.status !== 'Delivered' &&
                  order.status !== 'Rejected' &&
                  order.status !== 'Dispatched' && (
                    <Pressable
                      style={innerStyle.showDetailsBtn}
                      onPress={() => handleReject(order.orderId)}
                    >
                      <Text style={{ color: Colors.bgClr, fontWeight: '800' }}>
                        Reject
                      </Text>
                    </Pressable>
                  )}

                {order.status === 'Dispatched' && (
                  <Pressable
                    style={[
                      innerStyle.showDetailsBtn,
                      { backgroundColor: Colors.primary },
                    ]}
                    onPress={() => handleDeliver(order.orderId)}
                  >
                    <Text style={{ color: Colors.bgClr, fontWeight: '800' }}>
                      Deliver
                    </Text>
                  </Pressable>
                )}
              </View>
            </Pressable>
          ))}
        </ScrollView>
      )}
    </View>
  );
};

export default StorekeeperDashboard;

const innerStyle = StyleSheet.create({
  topBar: {
    padding: 10,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  heading: {
    fontSize: Fonts.sizes.lg,
    fontWeight: '700',
  },
  status: {
    paddingHorizontal: 5,
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginTop:20
  },
  statusButton: {
    paddingHorizontal: 20,
    paddingVertical: 8,
    borderRadius: 50,
  },
  emptyStateContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 20,
  },
  emptyStateText: {
    fontSize: Fonts.sizes.base,
    fontWeight: '600',
    color: Colors.secondaryText,
    textAlign: 'center',
  },
  orderCard: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    elevation: 3,
    backgroundColor: Colors.bgClr,
    padding: 15,
    borderRadius: 20,
    marginBottom: 10,
  },
  showDetailsBtn: {
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 50,
    backgroundColor: Colors.reject,
  },
  orderText: {
    fontWeight: 'bold',
    fontSize: Fonts.sizes.base,
    lineHeight: 30,
  },
  orderDetails: {
    color: Colors.secondary,
    fontWeight: '500',
  },
  orderDetailsHeading: {
    lineHeight: 30,
    color: Colors.secondaryText,
  },
  updatedStatus: {
    lineHeight: 30,
    fontWeight: '600',
  },
  popupMenu: {
    position: 'absolute',
    top: 20, // adjust to be just below the dot icon
    right: 0,
    backgroundColor: Colors.bgClr,
    borderRadius: 8,
    borderColor: Colors.borderColor,
    borderWidth: 1,
    elevation: 5,
    zIndex: 1000,
    width: '150%',
  },
  popupItem: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: Colors.borderColor,
  },
  popupText: {
    fontSize: Fonts.sizes.sm,
    color: Colors.secondary,
  },
});
