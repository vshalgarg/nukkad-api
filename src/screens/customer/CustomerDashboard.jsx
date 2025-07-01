import { SafeAreaView, View } from 'react-native';
import CategoryGridLayout from '../../components/category/CategoriesGridLayout.jsx';
import ProductSlider from '../../components/ProductSlider.jsx';
import SearchContainer from '../../components/SearchContainer.jsx';
import UserToolbar from '../../components/UserToolbar.jsx';
import styles from '../../styles/globalStyles.js';
import categories from '../../HardcodeData/categories.js';
import { useSafeRouter } from '../../hooks/useSafeRouter.js';

const CustomerDashboard = () => {
  const { safePush } = useSafeRouter();
  const handleCategoryPress = category => {
    console.log('Clicked:', category.name);
    safePush('ProductPage');
  };

  const handleSearchSubmit = query => {
    if (query.trim()) {
      safePush({
        pathname: 'ProductPage',
        params: { search: query },
      });
    }
  };

  return (
    <View style={styles.pageContainer}>
      <UserToolbar />
      <SearchContainer onSearchSubmit={handleSearchSubmit} />
      <ProductSlider />
      <SafeAreaView>
        <CategoryGridLayout
          categories={categories}
          onPressCategory={handleCategoryPress}
        />
      </SafeAreaView>
    </View>
  );
};

export default CustomerDashboard;
