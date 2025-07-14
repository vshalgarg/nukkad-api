// src/styles/styles.js
import { StyleSheet } from "react-native";
import Colors from "./colors";
import Fonts from "./font";

const styles = StyleSheet.create({
  pageContainer: {
    flex: 1,
    width: '100%',
    height: '100%',
    margin: 0,
    padding: 0,
    backgroundColor: 'white',
  },
  pageHeading: {
    // fontWeight: '600',
    textAlign: 'center',
    fontSize:Fonts.sizes.lg,
    color:Colors.secondary
  },
  // Customer or Storekeeper
  userType: {
    borderRadius: 24,
    borderWidth: 2,
    borderColor: Colors.secondary,
    padding: 12,
    width: 291,
    height: 48,
  },

  // Button Styles
  allBtn: {
    paddingBlock: 8,
    width: 154,
    height: 42,
  },
});

export default styles;
