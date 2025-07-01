import Ionicons from 'react-native-vector-icons/Ionicons';
import AntDesign from 'react-native-vector-icons/AntDesign';
import { useState } from 'react';
import {
  FlatList,
  Image,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import AddressCard from '../../components/AddressCard';
import BackButton from '../../components/BackButton';
import CustomButton from '../../components/CustomButton';
import { useAddress } from '../../contexts/addressContext';
import { useSafeRouter } from '../../hooks/useSafeRouter.js';
import {
  addOrder,
  clearCart,
  removeFromCart,
  updateCartItemQuantity,
} from '../../store/cartSlice';
import Colors from '../../styles/colors';
import styles from '../../styles/globalStyles';
import { showToast } from '../../utils/toastUtils.js';
import Fonts from '../../styles/font.js';

const ShoppingCart = () => {
  const { address, selectedAddressId, setMode, setAddressData } = useAddress();
  const dispatch = useDispatch();
  const { safeReplace, safePush } = useSafeRouter();

  const cartItems = useSelector(state => state.cart.items);
  const totalCount = cartItems.reduce((total, item) => {
    const isPacket = item.product.selectedUnit?.toLowerCase() === 'pkt';
    return total + (isPacket ? parseInt(item.product.amount) || 0 : 1);
  }, 0);

  const selectedAddress = address.find(item => item.id === selectedAddressId);
  const [openDropdownId, setOpenDropdownId] = useState(null);

  const handleAddAddress = () => {
    setMode('add');
    setAddressData(null);
    safePush('AddressForm');
  };

  const handleAddItems = () => {
    safePush('ProductPage');
  };

  const handleCompleteOrder = () => {
    if (!selectedAddress) {
      showToast('error', 'Add address before checkout');
      return;
    }
    if (cartItems.length === 0) {
      showToast('error', 'Add items before checkout');
    }

    const hasInvalidAmount = cartItems.some(item => {
      const amt = Number(item.product.amount);
      return isNaN(amt) || amt <= 0;
    });

    if (hasInvalidAmount) {
      alert('Some items have zero or invalid quantity. Please correct them.');
      return;
    }

    const totalValidCount = cartItems.reduce(
      (sum, item) => sum + (item.quantity || 1),
      0,
    );

    const newOrder = {
      id: Date.now().toString(),
      items: cartItems,
      address: selectedAddress,
      date: new Date().toISOString(),
      status: 'pending',
      totalItems: totalValidCount,
    };

    dispatch(addOrder(newOrder));
    dispatch(clearCart());
    safeReplace('PlaceOrder');
  };

  const handleEditAddress = address => {
    setMode('edit');
    setAddressData(address);
    safePush('AddressForm');
  };

  const renderCartItem = ({ item }) => {
    const { product, selectedUnit } = item;
    const amount = product.amount;

    const handleAmountChange = text => {
      const numericValue = parseFloat(text);
      if (!isNaN(numericValue) && numericValue >= 0) {
        dispatch(
          updateCartItemQuantity({
            productId: product.id,
            amount: numericValue,
          }),
        );
      } else if (text === '') {
        dispatch(
          updateCartItemQuantity({
            productId: product.id,
            amount: 0,
          }),
        );
      }
    };

    const handleUnitSelect = unit => {
      dispatch(
        updateCartItemQuantity({
          productId: product.id,
          selectedUnit: unit,
        }),
      );
      setOpenDropdownId(null);
    };

    const handleDelete = () => {
      dispatch(removeFromCart({ productId: product.id }));
    };

    const isDropdownOpen = openDropdownId === product.id;

    return (
      <View style={innerStyle.cartItem}>
        <Image source={{ uri: product.image }} style={innerStyle.image} />
        <View style={innerStyle.itemInfoContainer}>
          <Text style={innerStyle.name}>{product.title}</Text>
          <View style={innerStyle.row}>
            <TextInput
              style={innerStyle.input}
              value={amount !== undefined ? amount.toString() : ''}
              keyboardType="numeric"
              onChangeText={handleAmountChange}
              placeholder="0"
              maxLength={3}
            />
            <View style={{ marginLeft: 10 }}>
              <Pressable
                onPress={() =>
                  setOpenDropdownId(isDropdownOpen ? null : product.id)
                }
                style={innerStyle.unitSelector}
              >
                <Text style={innerStyle.unitText}>
                  {selectedUnit || 'Unit'}
                </Text>
                <AntDesign name={isDropdownOpen ? 'up' : 'down'} size={14} />
              </Pressable>

              {isDropdownOpen && (
                <View style={innerStyle.dropdown}>
                  {product.quantity.map(unit => (
                    <Pressable
                      key={unit}
                      onPress={() => handleUnitSelect(unit)}
                      style={innerStyle.dropdownItem}
                    >
                      <Text style={innerStyle.dropdownItemText}>{unit}</Text>
                    </Pressable>
                  ))}
                </View>
              )}
            </View>
          </View>
        </View>

        <TouchableOpacity onPress={handleDelete}>
          <AntDesign name="delete" size={24} color="black" />
        </TouchableOpacity>
      </View>
    );
  };

  if (cartItems.length === 0) {
    return (
      <View style={styles.pageContainer}>
        <BackButton title="Shopping Cart" />
        <View style={innerStyle.emptyContainer}>
          <Text style={{ fontSize: 30, fontWeight: '800', marginBottom: 20 }}>
            Your cart is empty 🛒
          </Text>
          <TouchableOpacity
            onPress={() => safePush('ProductPage')}
            style={innerStyle.browseBtn}
          >
            <Text style={innerStyle.browseBtnText}>Browse Grocery</Text>
          </TouchableOpacity>
        </View>
      </View>
    );
  }

  return (
    <View style={[{ flex: 1 }, styles.pageContainer]}>
      <BackButton
        style={innerStyle.backButton}
        backgroundColor="#eeeeee55"
        title={`Shopping Cart (${totalCount} ${
          totalCount > 1 ? 'items' : 'item'
        })`}
      />

      <FlatList
        data={cartItems}
        keyExtractor={item => item.product.id}
        renderItem={renderCartItem}
        contentContainerStyle={{ padding: Fonts.sizes.base }}
        ListHeaderComponent={
          <>
            <Text style={innerStyle.heading}>Delivery Address</Text>
            {selectedAddress ? (
              <AddressCard
                item={selectedAddress}
                onEdit={handleEditAddress}
                isSelected={true}
                actionType="change"
                hideDelete={true}
              />
            ) : (
              <Pressable style={innerStyle.button} onPress={handleAddAddress}>
                <Ionicons name="add-circle-outline" size={24} color="black" />
                <Text style={innerStyle.buttonText}>Add Address</Text>
              </Pressable>
            )}

            <Text style={[innerStyle.heading, { marginTop: 20 }]}>
              Selected Items ({cartItems.length})
            </Text>
          </>
        }
        ListFooterComponent={
          <>
            <Pressable onPress={handleAddItems}>
              <Text style={innerStyle.addItems}>+ Add more Items</Text>
            </Pressable>
            <View style={{ marginTop: 30, alignItems: 'center' }}>
              <CustomButton
                title={`Proceed`}
                onPress={handleCompleteOrder}
                disabled={!selectedAddress}
              />
            </View>
          </>
        }
      />
    </View>
  );
};

export default ShoppingCart;

const innerStyle = StyleSheet.create({
  itemInfoContainer: {
    flex: 1,
    marginLeft: 10,
    justifyContent: 'space-around',
  },
  heading: {
    fontSize: Fonts.sizes.base,
    fontWeight: '800',
    marginBottom: 8,
  },
  button: {
    marginTop: 10,
    flexDirection: 'row',
    alignItems: 'center',
    padding: 12,
    backgroundColor: '#f5f5f5',
    borderRadius: 8,
  },
  buttonText: {
    fontSize: Fonts.sizes.base,
    marginLeft: 6,
  },
  addItems: {
    color: Colors.primary,
    textAlign: 'right',
    marginTop: 12,
    fontWeight: '500',
    fontSize: Fonts.sizes.base,
  },
  cartItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 12,
    borderRadius: 8,
    marginBottom: 10,
    borderWidth: 1,
    borderColor: '#ddd',
  },
  image: {
    width: 80,
    height: 80,
    borderRadius: 8,
    resizeMode: 'cover',
  },
  name: {
    fontSize: Fonts.sizes.base,
    fontWeight: '600',
    marginBottom: 4,
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 8,
  },
  input: {
    width: 50,
    height: 40,
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 6,
    paddingHorizontal: 8,
    textAlign: 'center',
    fontWeight: '800',
  },
  unitSelector: {
    borderWidth: 1,
    borderColor: '#bbb',
    borderRadius: 8,
    paddingHorizontal: 10,
    paddingVertical: 8,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    width: 80,
    height: 42,
  },
  unitText: {
    fontSize: Fonts.sizes.sm,
    fontWeight: '600',
    color: '#444',
    marginRight: 6,
  },
  dropdown: {
    position: 'absolute',
    top: 44,
    width: 80,
    backgroundColor: '#ffffff',
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    zIndex: 100,
    elevation: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 4,
  },
  dropdownItem: {
    paddingVertical: 10,
    paddingHorizontal: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
  },
  dropdownItemText: {
    fontSize: Fonts.sizes.sm,
    color: '#333',
    fontWeight: '500',
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  browseBtn: {
    backgroundColor: 'black',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 30,
  },
  browseBtnText: {
    color: 'white',
    fontSize: Fonts.sizes.base,
    fontWeight: '600',
  },
});
