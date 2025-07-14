import { StyleSheet, Text, TextInput, View } from 'react-native';
import Flag from '../../assets/images/flag.svg';
import Colors from '../styles/colors.js';
import Fonts from '../styles/font.js';

export default function CustomInput({
  isCountryCode,
  value = '',
  fixedPrefix = '',
  onTextChange,
  maxLength,
  keyboardType,
  autoCapitalize,
  style,
  autoCorrect,
  placeholder,
  isError = false,
  ...props
}) {
  // Get text part after prefix (only for display)
  const inputOnly = value.startsWith(fixedPrefix)
    ? value.slice(fixedPrefix.length)
    : '';

  const handleChangeText = text => {
    const newText = fixedPrefix + text;
    onTextChange(newText);
  };

  return (
    <View style={styles.wrapper}>
      {isCountryCode && (
        <View style={styles.countryCodeContainer}>
          <Flag />
          <Text style={styles.countryCodeText}>+91</Text>
        </View>
      )}

      <View
        style={[
          styles.inputContainer,
          isCountryCode && styles.inputWithCountryCode,
          style,
          isError && { borderColor: Colors.reject, borderWidth: 1.5 },
        ]}
      >
        <Text style={styles.fixedPrefix}>{fixedPrefix}</Text>

        <TextInput
          {...props}
          value={inputOnly}
          onChangeText={handleChangeText}
          placeholder={placeholder}
          placeholderTextColor={Colors.secondaryText}
          maxLength={maxLength ? maxLength - fixedPrefix.length : undefined}
          keyboardType={keyboardType}
          autoCapitalize={autoCapitalize}
          autoCorrect={autoCorrect}
          style={styles.input}
          autoFocus={props.autoFocus || false}
          underlineColorAndroid="transparent"
        />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  wrapper: {
    marginBottom: 12,
  },
  countryCodeContainer: {
    position: 'absolute',
    left: 12,
    top: '52%',
    transform: [{ translateY: -12 }],
    flexDirection: 'row',
    alignItems: 'center',
    zIndex: 1,
  },
  countryCodeText: {
    marginLeft: 8,
    fontSize: Fonts.sizes.base,
    color: Colors.secondary,
    textAlignVertical:"center"
  },
  inputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: Colors.secondary,
    borderRadius: 24,
    height: 48,
    paddingHorizontal: 15,
    backgroundColor: Colors.bgClr,
    width: 300,
  },
  fixedPrefix: {
    fontSize: Fonts.sizes.base,
    fontWeight:500,
    color: Colors.secondary,
    marginRight: 4, // spacing between prefix and text input
  },
  input: {
    flex: 1,
    fontSize: Fonts.sizes.base,
    color: Colors.secondary,
    paddingVertical: 0,
    paddingHorizontal: 0,
  },
  inputWithCountryCode: {
    paddingLeft: 70,
    fontSize: Fonts.sizes.base,
    color: Colors.secondary,
    textAlignVertical:"center"
  },
});
