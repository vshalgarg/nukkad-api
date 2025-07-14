import { useEffect, useState } from 'react';
import {
  Dimensions,
  Image,
  Platform,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';
import { useDispatch, useSelector } from 'react-redux';
import { updateCartItemQuantity, addToCart } from '../store/cartSlice';
import Fonts from '../styles/font';
import Colors from '../styles/colors';
import { useAuth } from '../contexts/authContext';
import { addToCartAPI, updateCartAPI } from '../services/customer/cartService';
import { showToast } from '../utils/toastUtils';

const { width } = Dimensions.get('window');

const ProductCard = ({ product, isDropdownOpen, setDropdownOpen }) => {
  const dispatch = useDispatch();
  const cartItems = useSelector(state => state.cart.items);
  const cartItem = cartItems.find(item => item.product.id === product.id);
  const { token } = useAuth();

  const [selectedUnit, setSelectedUnit] = useState(null);
  const [amount, setAmount] = useState('');

  const units = product.quantity || product.unit || [];
  const unitOptions = units.map(q => ({ label: q, value: q }));

  useEffect(() => {
    if (!selectedUnit && units.length > 0) {
      setSelectedUnit(units[0]);
    }
  }, [units]);



  const isValidAmount =
    amount && !isNaN(parseFloat(amount)) && parseFloat(amount) > 0;

  const hasChanged =
    (cartItem &&
      (cartItem.product.amount?.toString() !== amount ||
        cartItem.selectedUnit !== selectedUnit)) ||
    (!cartItem && selectedUnit && amount);

  const canSubmit = isValidAmount && selectedUnit && hasChanged;

  const isRecentlyAdded =
    cartItem &&
    cartItem.selectedUnit === selectedUnit &&
    cartItem.product.amount?.toString() === amount;

    const handleAddToCart = async () => {
      const cartQuantity = parseFloat(amount);
      const validAmount = cartQuantity.toString();

      if (isNaN(cartQuantity) || cartQuantity <= 0) return;

      let response = null;

      try {
        const itemId = product.id;
        const isPkt = selectedUnit?.toLowerCase() === 'pkt';
        const itemCount = isPkt ? Math.round(cartQuantity) : 1;

        if (cartItem && cartItem.itemId) {
          await updateCartAPI(itemId, cartQuantity, selectedUnit, token);

          dispatch(
            updateCartItemQuantity({
              itemId,
              amount: validAmount,
              selectedUnit,
              itemCount,
            }),
          );
        } else {
          // ➕ Add new item
          response = await addToCartAPI(
            itemId,
            cartQuantity,
            selectedUnit,
            token,
          );

          const newItemId = response?.itemIds?.[0] || response?.id || itemId; // fallback

          const newItem = {
            itemId: newItemId,
            product: { ...product, selectedUnit, amount: validAmount },
            selectedUnit,
            quantity: itemCount,
          };

          dispatch(addToCart(newItem));
        }

        showToast('success', 'Added to cart');
      } catch (err) {
        console.error('Add to cart failed:', err.message || err);
        showToast('error', 'Failed to add item to cart');
      }
    };
    

  return (
    <View style={[styles.card, isDropdownOpen && { zIndex: 2000 }]}>
      <View style={styles.imageContainer}>
        <Image
          style={styles.image}
          source={{ uri: product.image || product.imageUrls?.[0] }}
        />
      </View>
      <Text style={styles.title} numberOfLines={1}>
        {product.title || product.name}
      </Text>

      <View style={styles.row}>
        <TextInput
          value={amount !== undefined && amount !== null ? String(amount) : ''}
          onChangeText={setAmount}
          placeholder="Amt"
          keyboardType="numeric"
          maxLength={4}
          style={styles.textInput}
        />
        <DropDownPicker
          open={isDropdownOpen}
          value={selectedUnit}
          items={unitOptions}
          setOpen={setDropdownOpen}
          ArrowUpIconComponent={() => null}
          ArrowDownIconComponent={() => null}
          setValue={setSelectedUnit}
          style={styles.dropdown}
          placeholder={null}
          containerStyle={styles.dropdownContainer}
          dropDownContainerStyle={styles.dropdownBox}
          textStyle={styles.text}
          placeholderStyle={styles.placeholder}
          listMode="SCROLLVIEW"
          TickIconComponent={() => null}
        />
      </View>

      <TouchableOpacity
        style={[
          styles.button,
          !canSubmit && isRecentlyAdded && styles.buttonDisabled,
          isRecentlyAdded && styles.buttonAdded,
        ]}
        onPress={handleAddToCart}
        disabled={!canSubmit}
      >
        <Text
          style={[
            styles.buttonText,
            isRecentlyAdded && { color: Colors.primary },
          ]}
        >
          {isRecentlyAdded ? 'Added' : 'Add to Cart'}
        </Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  card: {
    padding: width < 360 ? 8 : 10,
    backgroundColor: Colors.bgClr,
    borderRadius: 10,
    elevation: 3,
    alignItems: 'center',
    width: width > 768 ? '30%' : '45%',
    height: width < 360 ? 210 : 230,
    marginBottom: 20,
    justifyContent: 'space-around',
  },
  imageContainer: {
    width: width < 360 ? 90 : 130,
    height: width < 360 ? 70 : 90,
    borderRadius: 8,
    overflow: 'hidden',
    justifyContent: 'center',
    alignItems: 'center',
  },
  image: {
    width: '100%',
    height: '100%',
    resizeMode: 'contain',
  },
  title: {
    fontSize: width < 360 ? Fonts.sizes.sm : Fonts.sizes.base,
    fontWeight: '600',
    alignSelf: 'flex-start',
    width: '100%',
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 5,
    gap: 5,
    width: '100%',
  },
  dropdownContainer: {
    width: '50%',
  },
  dropdown: {
    borderColor: Colors.borderColor,
    borderRadius: 10,
    minHeight: 35,
  },
  dropdownBox: {
    borderColor: Colors.borderColor,
  },
  text: {
    fontSize: Fonts.sizes.sm,
    textAlign: 'center',
  },
  placeholder: {
    fontSize: Fonts.sizes.sm,
    textAlign: 'center',
  },
  textInput: {
    borderWidth: 1,
    borderColor: Colors.borderColor,
    borderRadius: 10,
    paddingHorizontal: 10,
    paddingVertical: Platform.OS === 'android' ? 4 : 6,
    width: '50%',
    height: 35,
    fontSize: Fonts.sizes.sm,
  },
  button: {
    marginTop: 5,
    width: '100%',
    paddingVertical: 8,
    borderRadius: 50,
    alignItems: 'center',
    borderWidth: 1,
  },
  buttonText: {
    fontWeight: '600',
    fontSize: Fonts.sizes.sm,
  },
  buttonAdded: {
    borderColor: Colors.primary,
  },
});

export default ProductCard;
