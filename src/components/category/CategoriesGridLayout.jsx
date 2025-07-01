import { Dimensions, FlatList, StyleSheet, View } from "react-native";
import CategoryCard from "./CategoryCard";

const numColumns = 4;
const screenWidth = Dimensions.get("window").width;
const itemSize = screenWidth / numColumns ;

const CategoryGridLayout = ({ categories, onPressCategory }) => {
  return (
    <FlatList
      data={categories}
      keyExtractor={(item) => item.id.toString()}
      numColumns={numColumns}
      scrollIndicatorInsets={{ bottom: 0 }}
      removeClippedSubviews={true}
      contentContainerStyle={{
        paddingInlineEnd: 0,
        paddingTop: 0,
        paddingBottom: 0,
        marginBottom: 0,
      }}
      renderItem={({ item }) => (
        <View style={[styles.grid,{ width: itemSize }]}>
          <CategoryCard category={item} onPress={onPressCategory} />
        </View>
      )}
    />
  );
};

const styles = StyleSheet.create({
  grid: {
    paddingInlineStart: 10,
    alignItems: "center",
  },
});

export default CategoryGridLayout;
