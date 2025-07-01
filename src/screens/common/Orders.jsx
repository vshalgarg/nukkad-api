import React, { useState } from 'react';
import {
  FlatList,
  Modal,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  Alert,
} from 'react-native';
import { useSelector } from 'react-redux';
import DateTimePicker from '@react-native-community/datetimepicker';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import BackButton from '../../components/BackButton';
import CustomerOrderCard from '../../components/orders/CustomerOrderCard';
import StorekeeperOrderCard from '../../components/orders/StorekeeperOrderCard';
import styles from '../../styles/globalStyles';
import Colors from '../../styles/colors';
import Fonts from '../../styles/font';

const Orders = () => {
  const userRole = useSelector(state => state.user.userType);
  const isCustomer = userRole === 'I AM CUSTOMER';

  const customerOrders = useSelector(state => state.cart.orders);
  const storekeeperOrders = useSelector(
    state => state.storekeeperOrders.orders,
  );

  const [expandedOrderId, setExpandedOrderId] = useState(null);
  const [fromDate, setFromDate] = useState(null);
  const [toDate, setToDate] = useState(null);
  const [filterModalVisible, setFilterModalVisible] = useState(false);
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [activePicker, setActivePicker] = useState(null);
  const [tempFrom, setTempFrom] = useState(null);
  const [tempTo, setTempTo] = useState(null);

  const toggleExpand = id => {
    setExpandedOrderId(prevId => (prevId === id ? null : id));
  };

  const clearFilter = () => {
    setFromDate(null);
    setToDate(null);
    setTempFrom(null);
    setTempTo(null);
  };

  const getTotalQuantity = items => {
    return items.reduce((sum, item) => {
      const selectedUnit =
        item.product?.selectedUnit || item.selectedUnit || '';
      const unit = selectedUnit?.split(' ')[1]?.toLowerCase();
      const qty = unit === 'pkt' ? parseInt(item.amount) || 0 : 1;
      return sum + qty;
    }, 0);
  };

  const rawOrders = isCustomer ? customerOrders : storekeeperOrders;

  const filteredOrders = rawOrders.filter(order => {
    const orderDate = new Date(
      order.createdAt || order.timestamp || order.date,
    );

    const afterFrom = !fromDate || orderDate >= fromDate;

    const inclusiveToDate = toDate
      ? new Date(
          toDate.getFullYear(),
          toDate.getMonth(),
          toDate.getDate(),
          23,
          59,
          59,
        )
      : null;

    const beforeTo = !toDate || orderDate <= inclusiveToDate;

    return afterFrom && beforeTo;
  });

  const orders = filteredOrders?.slice()?.sort((a, b) => {
    const dateA = new Date(a.createdAt || a.timestamp || a.date || 0);
    const dateB = new Date(b.createdAt || b.timestamp || b.date || 0);
    return dateB - dateA;
  });

  const applyFilter = () => {
    if (tempFrom && tempTo && tempTo < tempFrom) {
      Alert.alert('Invalid Date', "'To' date must be after 'From' date.");
      return;
    }
    setFromDate(tempFrom);
    setToDate(tempTo);
    setFilterModalVisible(false);
  };

  const formatDate = date => {
    return date?.toLocaleDateString('en-GB', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
    });
  };

  return (
    <View style={styles.pageContainer}>
      <BackButton title={isCustomer ? 'My Orders' : 'Order History'} />

      <View style={innerStyle.filterContainer}>
        {fromDate || toDate ? (
          <View style={innerStyle.clearFilterContainer}>
            <TouchableOpacity
              onPress={clearFilter}
              style={innerStyle.clearFilterBtn}
            >
              <Text style={innerStyle.clearFilterText}>Clear Filter</Text>
            </TouchableOpacity>
          </View>
        ) : (
          <View></View>
        )}
        <TouchableOpacity
          onPress={() => {
            setTempFrom(fromDate);
            setTempTo(toDate);
            setFilterModalVisible(true);
          }}
          style={innerStyle.filterButton}
        >
          <MaterialIcons name="date-range" size={24} color="black" />
        </TouchableOpacity>
      </View>

      <Modal
        transparent
        animationType="fade"
        visible={filterModalVisible}
        onRequestClose={() => setFilterModalVisible(false)}
      >
        <View style={innerStyle.modalOverlay}>
          <View style={innerStyle.modalContent}>
            <View style={innerStyle.modalHeader}>
              <Text style={innerStyle.modalTitle}>Select Date Range</Text>
              <TouchableOpacity onPress={() => setFilterModalVisible(false)}>
                <MaterialIcons name="close" size={24} color="#333" />
              </TouchableOpacity>
            </View>

            <TouchableOpacity
              onPress={() => {
                setActivePicker('from');
                setShowDatePicker(true);
              }}
              style={innerStyle.dateSelect}
            >
              <Text style={innerStyle.dateLabel}>
                <Text style={innerStyle.datePrefix}>From:</Text>{' '}
                {formatDate(tempFrom) || 'Select'}
              </Text>
            </TouchableOpacity>

            <TouchableOpacity
              onPress={() => {
                setActivePicker('to');
                setShowDatePicker(true);
              }}
              style={innerStyle.dateSelect}
            >
              <Text style={innerStyle.dateLabel}>
                <Text style={innerStyle.datePrefix}>To:</Text>{' '}
                {formatDate(tempTo) || 'Select'}
              </Text>
            </TouchableOpacity>

            <View style={innerStyle.modalButtons}>
              <TouchableOpacity
                onPress={() => setFilterModalVisible(false)}
                style={innerStyle.cancelBtn}
              >
                <Text style={innerStyle.buttonText}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity
                onPress={applyFilter}
                style={innerStyle.modalBtn}
              >
                <Text style={innerStyle.buttonText}>Apply</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>

      {showDatePicker && (
        <DateTimePicker
          value={
            activePicker === 'from'
              ? tempFrom || new Date()
              : tempTo || new Date()
          }
          mode="date"
          display="default"
          minimumDate={
            activePicker === 'to' && tempFrom
              ? tempFrom
              : new Date(new Date().setFullYear(new Date().getFullYear() - 1))
          }
          maximumDate={new Date()}
          onChange={(event, selectedDate) => {
            setShowDatePicker(false);
            if (event.type === 'set' && selectedDate) {
              if (activePicker === 'from') {
                setTempFrom(selectedDate);
                if (tempTo && selectedDate > tempTo) {
                  setTempTo(null);
                }
              }
              if (activePicker === 'to') {
                if (tempFrom && selectedDate < tempFrom) {
                  Alert.alert(
                    'Invalid Date',
                    "'To' date cannot be before 'From' date.",
                  );
                  return;
                }
                setTempTo(selectedDate);
              }
            }
          }}
        />
      )}

      <View style={innerStyle.container}>
        <FlatList
          showsVerticalScrollIndicator={false}
          data={orders}
          keyExtractor={(item, index) =>
            item?.id?.toString() ||
            item?.orderId?.toString() ||
            index.toString()
          }
          renderItem={({ item, index }) => {
            const uniqueId = item.id || `${item.timestamp}_${index}`;
            const isExpanded = expandedOrderId === uniqueId;
            const quantity = getTotalQuantity(item.items);

            const commonProps = {
              order: item,
              isExpanded,
              onPress: () => toggleExpand(uniqueId),
              expandedView: (
                <View style={innerStyle.expandedContainer}>
                  <Text style={innerStyle.itemsTitle}>Items:</Text>
                  {item.items.map((itm, idx) => (
                    <View key={idx} style={innerStyle.itemRow}>
                      <Text style={innerStyle.itemName}>
                        {itm.title || itm.product?.title}
                      </Text>
                      <Text style={innerStyle.itemText}>
                        {itm.product?.amount} {itm.selectedUnit}
                      </Text>
                      <Text style={innerStyle.itemText}>{itm.weight}</Text>
                    </View>
                  ))}
                </View>
              ),
            };

            return isCustomer ? (
              <CustomerOrderCard {...commonProps} quantity={quantity} />
            ) : (
              <StorekeeperOrderCard {...commonProps} />
            );
          }}
          ListEmptyComponent={
            <Text style={{ textAlign: 'center', marginTop: 50 }}>
              No orders found.
            </Text>
          }
        />
      </View>
    </View>
  );
};

export default Orders;

const innerStyle = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#fff',
  },
  filterContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 16,
    marginBlock: 12,
  },
  clearFilterText: {
    color: '#000',
    fontWeight: '600',
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.4)',
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 20,
  },
  modalContent: {
    width: '100%',
    maxWidth: 350,
    backgroundColor: '#fff',
    borderRadius: 16,
    padding: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 6,
    elevation: 8,
  },
  modalHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
  },
  modalTitle: {
    fontSize: Fonts.sizes.lg,
    fontWeight: '600',
    color: '#222',
  },
  dateSelect: {
    borderWidth: 1,
    borderColor: '#ddd',
    padding: 14,
    borderRadius: 10,
    marginBottom: 12,
    backgroundColor: '#f9f9f9',
  },
  dateLabel: {
    fontSize: Fonts.sizes.base,
    color: '#333',
  },
  datePrefix: {
    fontWeight: '600',
    color: '#444',
  },
  modalButtons: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: 12,
    marginTop: 20,
  },
  modalBtn: {
    flex: 1,
    paddingVertical: 12,
    backgroundColor: Colors.primary,
    borderRadius: 10,
    alignItems: 'center',
  },
  cancelBtn: {
    flex: 1,
    paddingVertical: 12,
    backgroundColor: '#999',
    borderRadius: 10,
    alignItems: 'center',
  },
  buttonText: {
    color: '#fff',
    fontWeight: '600',
  },
  expandedContainer: {
    marginTop: 12,
    borderTopWidth: 1,
    borderTopColor: '#ccc',
    paddingTop: 10,
  },
  itemsTitle: {
    fontSize: Fonts.sizes.sm,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  itemRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 4,
  },
  itemName: {
    flex: 1,
    fontWeight: '600',
  },
  itemText: {
    color: '#444',
  },
});
