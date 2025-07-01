import { StyleSheet, Text, TextInput, View } from 'react-native';
import Flag from '../../assets/images/flag.svg';
import Colors from '../styles/colors.js';
import Fonts from '../styles/font.js';

export default function CustomInput({
  isCountryCode,
  value,
  onTextChange,
  maxLength,
  keyboardType,
  autoCapitalize,
  style,
  autoCorrect,
  ...props
}) {
  return (
    <View style={styles.wrapper}>
      {isCountryCode && (
        <View style={styles.countryCodeContainer}>
          <Flag />
          <Text style={styles.countryCodeText}>+91</Text>
        </View>
      )}

      <TextInput
        {...props}
        value={value}
        placeholderTextColor={Colors.secondaryText}
        onChangeText={onTextChange}
        maxLength={maxLength}
        keyboardType={keyboardType}
        autoCapitalize={autoCapitalize}
        autoCorrect={autoCorrect}
        style={[
          styles.input,
          isCountryCode && styles.inputWithCountryCode,
          style,
        ]}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  wrapper: {
    position: 'relative',
    marginBottom: 12, // Optional spacing
  },
  countryCodeContainer: {
    position: 'absolute',
    left: 12,
    top: 12,
    flexDirection: 'row',
    alignItems: 'center',
    zIndex: 1,
  },
  countryCodeText: {
    marginLeft: 8,
    fontSize: Fonts.sizes.sm,
    color: '#000',
  },
  input: {
    width: 300,
    height: 42,
    borderWidth: 1,
    borderColor: 'black',
    borderRadius: 21,
    paddingLeft: 15, 
    fontSize: Fonts.sizes.base,
    color: '#000',
    alignItems:"center"
  },
  inputWithCountryCode: {
    paddingLeft: 80, // more space to make room for country code
  },
});
