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
import { updateCartItemQuantity } from '../store/cartSlice';
import Fonts from '../styles/font';

const { width } = Dimensions.get('window');

const ProductCard = ({
  product,
  onAddToCart,
  isDropdownOpen,
  setDropdownOpen,
}) => {
  const dispatch = useDispatch();
  const cartItems = useSelector(state => state.cart.items);
  const cartItem = cartItems.find(item => item.product.id === product.id);

  const [selectedUnit, setSelectedUnit] = useState(null);
  const [amount, setAmount] = useState('');

  useEffect(() => {
    if (cartItem) {
      const newUnit = cartItem.selectedUnit;
      const newAmount = cartItem.product.amount?.toString() || '';
      if (newUnit !== selectedUnit) setSelectedUnit(newUnit);
      if (newAmount !== amount) setAmount(newAmount);
    }
  }, [cartItem]);

  const unitOptions = product.quantity.map(q => ({ label: q, value: q }));

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

  const handleAddToCart = () => {
    const isPacket = selectedUnit?.toLowerCase() === 'pkt';
    const cartQuantity = isPacket ? parseInt(amount, 10) : 1;
    const validAmount = isPacket ? amount : parseFloat(amount).toString();

    if (
      (isPacket && (isNaN(cartQuantity) || cartQuantity <= 0)) ||
      !validAmount
    )
      return;

    if (cartItem) {
      dispatch(
        updateCartItemQuantity({
          productId: product.id,
          amount: validAmount,
          selectedUnit,
        }),
      );
    } else {
      onAddToCart(
        {
          ...product,
          selectedUnit,
          amount: validAmount,
        },
        cartQuantity,
      );
    }
  };

  return (
    <View style={[styles.card, isDropdownOpen && { zIndex: 2000 }]}>
      <Image source={{ uri: product.image }} style={styles.image} />
      <Text style={styles.title} numberOfLines={1}>
        {product.title}
      </Text>

      <View style={styles.row}>
        <DropDownPicker
          open={isDropdownOpen}
          value={selectedUnit}
          items={unitOptions}
          setOpen={setDropdownOpen}
          setValue={setSelectedUnit}
          placeholder="Unit"
          style={styles.dropdown}
          containerStyle={styles.dropdownContainer}
          dropDownContainerStyle={styles.dropdownBox}
          textStyle={styles.text}
          placeholderStyle={styles.placeholder}
          listMode="SCROLLVIEW"
        />

        <TextInput
          value={amount !== undefined && amount !== null ? String(amount) : ''}
          onChangeText={setAmount}
          placeholder="Amt"
          keyboardType="numeric"
          maxLength={4}
          style={styles.textInput}
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
          style={[styles.buttonText, isRecentlyAdded && { color: 'green' }]}
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
    backgroundColor: '#fff',
    borderRadius: 10,
    elevation: 3,
    alignItems: 'center',
    width: width > 768 ? '30%' : '45%',
    height: width < 360 ? 210 : 230,
    marginBottom: 20,
    justifyContent: 'space-around',
  },
  image: {
    width: width < 360 ? 90 : 110,
    height: width < 360 ? 70 : 80,
    borderRadius: 8,
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
    gap: 10,
    width: '100%',
  },
  dropdownContainer: {
    width: '50%',
  },
  dropdown: {
    borderColor: '#ccc',
    borderRadius: 10,
    minHeight: 35,
  },
  dropdownBox: {
    borderColor: '#ddd',
  },
  text: {
    fontSize: Fonts.sizes.xs,
    textAlign: 'center',
  },
  placeholder: {
    fontSize: Fonts.sizes.xs,
    textAlign: 'center',
  },
  textInput: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 10,
    paddingHorizontal: 10,
    paddingVertical: Platform.OS === 'android' ? 4 : 6,
    width: '50%',
    height: 35,
    fontSize: Fonts.sizes.xs,
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
  buttonDisabled: {
    opacity: 0.5,
  },
  buttonAdded: {
    borderColor: 'green',
  },
});

export default ProductCard;
