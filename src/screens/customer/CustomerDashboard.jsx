// screens/customer/CustomerDashboard.jsx
import { SafeAreaView, View, ActivityIndicator } from 'react-native';
import React, { useEffect, useState } from 'react';

import CategoryGridLayout from '../../components/category/CategoriesGridLayout.jsx';
import ProductSlider from '../../components/ProductSlider.jsx';
import SearchContainer from '../../components/SearchContainer.jsx';
import UserToolbar from '../../components/UserToolbar.jsx';

import styles from '../../styles/globalStyles.js';
import { getAllCategories } from '../../services/customer/categoriesService.js';
import { useSafeRouter } from '../../hooks/useSafeRouter.js';

const CustomerDashboard = () => {
  const { safePush } = useSafeRouter();
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchCategories = async () => {
    try {
      const response = await getAllCategories();
      setCategories(response);
    } catch (error) {
      console.error('❌ Failed to load categories:', error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleCategoryPress = category => {
    console.log('Clicked:', category.name);
    safePush('ProductPage', {
      categoryId: category.id,
      categoryName: category.name,
    });
  };

  const handleSearchSubmit = query => {
    if (query.trim()) {
      safePush('ProductPage', { search: query });
    }
  };

  return (
    <View style={styles.pageContainer}>
      <UserToolbar />
      <SearchContainer onSearchSubmit={handleSearchSubmit} />
      <ProductSlider />

      <SafeAreaView>
        {loading ? (
          <ActivityIndicator size="large" color="#000" />
        ) : (
          <CategoryGridLayout
            categories={categories}
            onPressCategory={handleCategoryPress}
          />
        )}
      </SafeAreaView>
    </View>
  );
};

export default CustomerDashboard;
