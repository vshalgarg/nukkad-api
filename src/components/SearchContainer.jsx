import { View, TextInput, StyleSheet } from "react-native";
import React, { useEffect, useState } from "react";
import Entypo from 'react-native-vector-icons/Entypo';
import Colors from "../styles/colors";

const SearchContainer = ({ query, onSearchSubmit }) => {
  const [input, setInput] = useState(query || "");

  useEffect(() => {
    setInput(query || "");
  }, [query]);

  return (
    <View style={styles.container}>
      <Entypo
        name="magnifying-glass"
        size={20}
        color={Colors.secondaryText}
        style={styles.icon}
      />

      <TextInput
        style={styles.input}
        placeholder="Search here for anything you want..."
        placeholderTextColor={Colors.secondaryText}
        value={input}
        onChangeText={setInput}
        onSubmitEditing={() => {
          if (onSearchSubmit) onSearchSubmit(input);
        }}
        returnKeyType="search"
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    marginTop: 20,
    paddingHorizontal: 20,
    position: "relative",
    justifyContent: "center",
  },
  icon: {
    position: "absolute",
    left: 35,
    zIndex: 1,
  },
  input: {
    paddingLeft: 40 + 12, 
    height: 46,
    borderRadius: 999,
    backgroundColor: Colors.bgClr,
    color: Colors.secondary,
  },
});

export default SearchContainer;
