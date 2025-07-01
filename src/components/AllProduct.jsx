import AntDesign from 'react-native-vector-icons/AntDesign';
import { useState } from 'react';
import { FlatList, Pressable, StyleSheet, Text, View } from 'react-native';
import { useDispatch, useSelector } from 'react-redux';

import productData from '../HardcodeData/productData.js';
import { addToCart } from '../store/cartSlice';
import styles from '../styles/globalStyles';
import ProductCard from './ProductCard';
import Fonts from '../styles/font.js';

const AllProduct = () => {
  const dispatch = useDispatch();
  const [sortedProducts, setSortedProducts] = useState(productData);
  const [isSorted, setIsSorted] = useState(false);
  const [dropdownOpenId, setDropdownOpenId] = useState(null);

  const cartItems = useSelector(state => state.cart.items);

  const handleSort = () => {
    if (!isSorted) {
      const sorted = [...productData].sort((a, b) =>
        a.title.localeCompare(b.title),
      );
      setSortedProducts(sorted);
    } else {
      setSortedProducts(productData);
    }
    setIsSorted(!isSorted);
  };

  const handleAddToCart = (productWithDetails, cartQuantity) => {
    dispatch(
      addToCart({
        product: {
          ...productWithDetails,
          selectedUnit: productWithDetails.selectedUnit,
          amount: productWithDetails.amount,
        },
        cartQuantity,
      }),
    );
  };

  return (
    <View style={styles.pageContainer}>
      <View style={innerStyle.header}>
        <Text style={innerStyle.title}>All Products</Text>
        <View style={innerStyle.filterContainer}>
          <Pressable
            onPress={handleSort}
            style={[
              innerStyle.sortButton,
              isSorted
                ? innerStyle.sortButtonActive
                : innerStyle.sortButtonInactive,
            ]}
          >
            <Text
              style={[
                innerStyle.sortText,
                isSorted
                  ? innerStyle.sortTextActive
                  : innerStyle.sortTextInactive,
              ]}
            >
              Sort A-Z
            </Text>
          </Pressable>
          <AntDesign name="filter" size={22} color="black" />
        </View>
      </View>

      <FlatList
        data={sortedProducts}
        keyExtractor={item => item.id}
        renderItem={({ item }) => (
          <ProductCard
            product={item}
            onAddToCart={handleAddToCart}
            isDropdownOpen={dropdownOpenId === item.id}
            setDropdownOpen={open => setDropdownOpenId(open ? item.id : null)}
            cartItems={cartItems}
          />
        )}
        numColumns={2}
        columnWrapperStyle={innerStyle.row}
        contentContainerStyle={innerStyle.container}
        showsVerticalScrollIndicator={false}
      />
    </View>
  );
};

const innerStyle = StyleSheet.create({
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: 10,
    marginBottom: 20,
    paddingHorizontal: 20,
  },
  title: {
    width: '40%',
    fontSize: Fonts.sizes.base,
    fontWeight: '600',
  },
  filterContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  sortButton: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 6,
    borderWidth: 1,
  },
  sortButtonInactive: {
    backgroundColor: 'transparent',
    borderColor: 'green',
  },
  sortButtonActive: {
    backgroundColor: 'green',
    borderColor: 'green',
  },
  sortText: {
    fontSize: Fonts.sizes.sm,
    fontWeight: '500',
  },
  sortTextInactive: {
    color: 'green',
  },
  sortTextActive: {
    color: 'white',
  },
  container: {
    paddingBottom: 80,
  },
  row: {
    justifyContent: 'space-around',
  },
});

export default AllProduct;
