import React, { useEffect, useState } from 'react';
import {
  Keyboard,
  KeyboardAvoidingView,
  FlatList,
  Platform,
  StyleSheet,
  Text,
  TouchableOpacity,
  TouchableWithoutFeedback,
  View,
} from 'react-native';
import { useSelector } from 'react-redux';
import { useNavigation, useRoute } from '@react-navigation/native';

import AllProduct from '../../components/AllProduct.jsx';
import SearchContainer from '../../components/SearchContainer.jsx';
import UserToolbar from '../../components/UserToolbar.jsx';
import CategoryListLayout from '../../components/category/CategoriesListLayout.jsx';

import Colors from '../../styles/colors.js';
import styles from '../../styles/globalStyles.js';
import { useSafeRouter } from '../../hooks/useSafeRouter.js';
import Fonts from '../../styles/font.js';

const ProductPage = () => {
  const navigation = useNavigation();
  const route = useRoute();
  const { safePush } = useSafeRouter();

  const search = route?.params?.search || '';
  const [searchQuery, setSearchQuery] = useState(search);
  const [keyboardVisible, setKeyboardVisible] = useState(false);

  const cartItems = useSelector(state => state.cart.items);
  const totalItems = cartItems.reduce((total, item) => {
    const isPacket = item.product.selectedUnit?.toLowerCase() === 'pkt';
    return total + (isPacket ? parseInt(item.product.amount) || 0 : 1);
  }, 0);

  useEffect(() => {
    const showSub = Keyboard.addListener('keyboardDidShow', () =>
      setKeyboardVisible(true),
    );
    const hideSub = Keyboard.addListener('keyboardDidHide', () =>
      setKeyboardVisible(false),
    );
    return () => {
      showSub.remove();
      hideSub.remove();
    };
  }, []);

  return (
    <View style={[styles.pageContainer, { flex: 1, backgroundColor: 'white' }]}>
      {/* Fixed Bottom Banner */}
      {totalItems > 0 && (
        <View style={innerStyle.fixedBottomBanner}>
          <Text style={innerStyle.popupText}>
            {totalItems} item{totalItems > 1 ? 's' : ''} in cart
          </Text>
          <TouchableOpacity
            style={innerStyle.goToCartButton}
            onPress={() => safePush('ShoppingCart')}
          >
            <Text style={innerStyle.goToCartText}>Go to Cart</Text>
          </TouchableOpacity>
        </View>
      )}

      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={keyboardVisible ? 50 : 0}
      >
        <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
          <FlatList
            data={[]}
            keyExtractor={(item, index) => index.toString()}
            ListHeaderComponent={
              <View>
                <UserToolbar hideNotification={true} hideMenu={true} />
                <SearchContainer
                  query={searchQuery}
                  onSearchSubmit={newQuery => setSearchQuery(newQuery)}
                />
                <CategoryListLayout />
                <AllProduct searchQuery={searchQuery} />
              </View>
            }
            showsVerticalScrollIndicator={false}
            keyboardShouldPersistTaps="handled"
            ListFooterComponent={<View style={{ height: 100 }} />}
          />
        </TouchableWithoutFeedback>
      </KeyboardAvoidingView>
    </View>
  );
};

const innerStyle = StyleSheet.create({
  fixedBottomBanner: {
    position: 'absolute',
    bottom: 15,
    left: 15,
    right: 15,
    backgroundColor: '#f9f9f9',
    borderColor: Colors.primary,
    borderWidth: 1.5,
    borderRadius: 40,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    paddingVertical: 12,
    elevation: 6,
    shadowColor: '#000',
    shadowOpacity: 0.15,
    shadowOffset: { width: 0, height: 3 },
    shadowRadius: 5,
    zIndex: 999,
  },
  popupText: {
    fontSize: Fonts.sizes.base,
    fontWeight: '500',
    color: '#333',
  },
  goToCartButton: {
    backgroundColor: Colors.primary,
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 25,
  },
  goToCartText: {
    color: '#fff',
    fontSize: Fonts.sizes.sm,
    fontWeight: '600',
  },
});

export default ProductPage;
