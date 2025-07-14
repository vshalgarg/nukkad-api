import { useMemo, useState } from 'react';
import { FlatList, Pressable, StyleSheet, Text, View } from 'react-native';
import { useDispatch, useSelector } from 'react-redux';

import { addToCart } from '../store/cartSlice';
import styles from '../styles/globalStyles';
import ProductCard from './ProductCard';
import Fonts from '../styles/font.js';
import Colors from '../styles/colors.js';

const AllProduct = ({ products = [], loading = false }) => {
  const dispatch = useDispatch();
  const [isSorted, setIsSorted] = useState(false);
  const [dropdownOpenId, setDropdownOpenId] = useState(null);
  const cartItems = useSelector(state => state.cart.items);

  const sortedList = isSorted
    ? [...products].sort((a, b) =>
        (a.name || a.title || '').localeCompare(b.name || b.title || ''),
      )
    : products;


  // Pair products into subarrays of [left, right]
  const pairedList = useMemo(() => {
    const result = [];
    for (let i = 0; i < sortedList.length; i += 2) {
      const pair = [sortedList[i]];
      if (i + 1 < sortedList.length) {
        pair.push(sortedList[i + 1]);
      }
      result.push(pair);
    }
    return result;
  }, [sortedList]);

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
            onPress={() => setIsSorted(!isSorted)}
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
        </View>
      </View>

      {loading ? (
        <Text style={{ textAlign: 'center', marginTop: 20 }}>Loading...</Text>
      ) : sortedList.length === 0 ? (
        <Text style={{ textAlign: 'center', marginTop: 20 }}>
          No products found.
        </Text>
      ) : (
        <FlatList
          data={pairedList}
          keyExtractor={(_, index) => index.toString()}
          renderItem={({ item: pair }) => (
            <View
              style={{
                flexDirection: 'row',
                justifyContent:
                  pair.length === 1 ? 'flex-start' : 'space-around',
                paddingHorizontal: 10,
                marginBottom: 10,
              }}
            >
              {pair.map(product => (
                <ProductCard
                  key={product.id}
                  product={product}
                  onAddToCart={handleAddToCart}
                  isDropdownOpen={dropdownOpenId === product.id}
                  setDropdownOpen={open =>
                    setDropdownOpenId(open ? product.id : null)
                  }
                  cartItems={cartItems}
                />
              ))}
            </View>
          )}
          showsVerticalScrollIndicator={false}
          contentContainerStyle={innerStyle.container}
        />
      )}
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
    borderColor: Colors.primary,
  },
  sortButtonActive: {
    backgroundColor: Colors.primary,
    borderColor: Colors.primary,
  },
  sortText: {
    fontSize: Fonts.sizes.sm,
    fontWeight: '500',
  },
  sortTextInactive: {
    color: Colors.primary,
  },
  sortTextActive: {
    color: Colors.bgClr,
  },
  container: {
    paddingBottom: 80,
  },
});

export default AllProduct;
