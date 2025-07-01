import { Pressable, Text, StyleSheet } from 'react-native';
import globalStyles from '../styles/globalStyles';
import Colors from '../styles/colors';
import Fonts from '../styles/font';

export default function Button({ title, onPress, style, textStyle }) {
  return (
    <Pressable
      onPress={onPress}
      style={[globalStyles.allBtn, styles.button, style]}
    >
      <Text
        style={[styles.buttonText, textStyle]}
        numberOfLines={1}
        ellipsizeMode="tail"
      >
        {title}
      </Text>
    </Pressable>
  );
}


const styles = StyleSheet.create({
  button: {
    backgroundColor: Colors.primary,
    borderRadius: 9999,
    borderWidth: 1,
    borderColor:Colors.primary,
    paddingVertical: 12,
    paddingHorizontal: 24,
    alignItems: 'center',
  },
  buttonText: {
    color: '#fff',
    fontWeight: '600',
    fontSize:Fonts.sizes.base,
    textAlign: 'center',
  },
});
