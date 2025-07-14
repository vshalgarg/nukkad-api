import React from "react";
import { View, Text, Image, Pressable, StyleSheet } from "react-native";
import textStyles from "../../styles/textStyles";

const CategoryCard = ({ category, onPress }) => {
  return (
    <Pressable style={styles.card} onPress={() => onPress(category)}>
      <Image source={{ uri: category.imageUrl }} style={styles.image} />
      <Text style={[styles.name,textStyles.caption]}>{category.name}</Text>
    </Pressable>
  );
};

const styles = StyleSheet.create({
  card: {
    width: "100%",
    margin: 3,
    alignItems: "center",
    borderRadius: 8,
    paddingInlineEnd: 8,
    paddingBlockStart: 20,
  },
  image: {
    borderRadius: 5,
    width: "100%",
    height: 75,
    marginBottom: 8,
  },
  name: {
    textAlign: "center",
  },
});

export default CategoryCard;
