// components/CartItem.jsx
import { useEffect, useRef, useState } from 'react';
import {
  View,
  Text,
  TextInput,
  Image,
  Pressable,
  TouchableOpacity,
  StyleSheet,
} from 'react-native';
import AntDesign from 'react-native-vector-icons/AntDesign';
import Colors from '../styles/colors';
import Fonts from '../styles/font';
import { useDispatch } from 'react-redux';
import { removeFromCart, updateCartItemQuantity } from '../store/cartSlice';
import { updateCartAPI } from '../services/customer/cartService';
import { useAuth } from '../contexts/authContext';
import { deleteCartItemAPI } from '../services/customer/cartService';
import AsyncStorage from '@react-native-async-storage/async-storage';
const CartItem = ({
  item,
  openDropdownId,
  setOpenDropdownId,
  onItemRemoved,
}) => {
  const dispatch = useDispatch();
  const { token } = useAuth();
  const { product, selectedUnit } = item;
  const originalAmount = useRef(product.amount?.toString() || '');
  const [amountInput, setAmountInput] = useState(
    product.amount?.toString() || '',
  );

  const isDropdownOpen = openDropdownId === product.id;
  console.log(item?.product.id);
  const handleDelete = async () => {
    try {
      await deleteCartItemAPI(item?.product.id, token);
      dispatch(removeFromCart({ itemId: item?.product.id }));

      if (onItemRemoved) {
        onItemRemoved();
      }
    } catch (err) {
      console.error('❌ Failed to delete item from cart', err);
    }
  };

  const handleUnitSelect = unit => {
    dispatch(
      updateCartItemQuantity({
        cartItemId: item.cartItemId,
        selectedUnit: unit,
      }),
    );
    setOpenDropdownId(null);
  };

  useEffect(() => {
    const timeout = setTimeout(() => {
      const numericValue = parseFloat(amountInput);

      // ✅ Only run API if value changed from original
      if (
        !isNaN(numericValue) &&
        numericValue >= 0 &&
        amountInput !== originalAmount.current
      ) {
        updateCartAPI(item.cartItemId, numericValue, token)
          .then(() => {
            originalAmount.current = amountInput; // ✅ Update the ref
            dispatch(
              updateCartItemQuantity({
                cartItemId: item.cartItemId,
                amount: numericValue,
              }),
            );
          })
          .catch(() => {
            console.log('❌ Failed to update quantity');
          });
      }
    }, 500);

    return () => clearTimeout(timeout);
  }, [amountInput]);

  return (
    <View style={styles.cartItem}>
      <Image
        source={{ uri: product.image || product.imageUrls?.[0] }}
        style={styles.image}
      />

      <View style={styles.itemInfoContainer}>
        <Text style={styles.name}>{product.name}</Text>
        <View style={styles.row}>
          <TextInput
            style={styles.input}
            value={amountInput}
            keyboardType="numeric"
            onChangeText={setAmountInput}
            placeholder=""
            maxLength={3}
          />
          <View style={{ marginLeft: 10 }}>
            <Pressable
              onPress={() =>
                setOpenDropdownId(isDropdownOpen ? null : product.id)
              }
              style={styles.unitSelector}
            >
              <Text style={styles.unitText}>{selectedUnit || 'Unit'}</Text>
              <AntDesign name={isDropdownOpen ? 'up' : 'down'} size={14} />
            </Pressable>

            {isDropdownOpen && (
              <View style={styles.dropdown}>
                {product.quantity?.map(unit => (
                  <Pressable
                    key={unit}
                    onPress={() => handleUnitSelect(unit)}
                    style={styles.dropdownItem}
                  >
                    <Text style={styles.dropdownItemText}>{unit}</Text>
                  </Pressable>
                ))}
              </View>
            )}
          </View>
        </View>
      </View>

      <TouchableOpacity onPress={handleDelete}>
        <AntDesign name="delete" size={24} color={Colors.secondary} />
      </TouchableOpacity>
    </View>
  );
};

export default CartItem;

const styles = StyleSheet.create({
  cartItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 12,
    borderRadius: 8,
    marginBottom: 10,
    borderWidth: 1,
    borderColor: Colors.borderColor,
  },
  image: {
    width: 80,
    height: 80,
    borderRadius: 8,
    resizeMode: 'cover',
  },
  itemInfoContainer: {
    flex: 1,
    marginLeft: 10,
    justifyContent: 'space-around',
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
    borderColor: Colors.borderColor,
    borderRadius: 6,
    paddingHorizontal: 8,
    textAlign: 'center',
    fontWeight: '800',
  },
  unitSelector: {
    borderWidth: 1,
    borderColor: Colors.borderColor,
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
    color: Colors.secondary,
    marginRight: 6,
  },
  dropdown: {
    position: 'absolute',
    top: 44,
    width: 80,
    backgroundColor: Colors.bgClr,
    borderWidth: 1,
    borderColor: Colors.borderColor,
    borderRadius: 8,
    zIndex: 100,
    elevation: 8,
    shadowColor: Colors.secondary,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 4,
  },
  dropdownItem: {
    paddingVertical: 10,
    paddingHorizontal: 12,
    borderBottomWidth: 1,
    borderBottomColor: Colors.borderColor,
  },
  dropdownItemText: {
    fontSize: Fonts.sizes.sm,
    color: Colors.secondary,
    fontWeight: '500',
  },
});
