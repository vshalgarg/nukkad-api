import React from 'react';
import { Pressable, StyleSheet, View, Text } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import Entypo from 'react-native-vector-icons/Entypo';
import textStyles from '../styles/textStyles';
import Fonts from '../styles/font';

const BackButton = ({ title, backgroundColor = '#F8F9FB', onPress }) => {
  const navigation = useNavigation();

  const handlePress = () => {
    if (onPress) {
      onPress();
    } else {
      navigation.goBack();
    }
  };

  return (
    <View style={innerStyle.container}>
      <Pressable
        style={[innerStyle.backButton, { backgroundColor }]}
        onPress={handlePress}
      >
        <Entypo name="chevron-left" size={20} color="black" />
      </Pressable>

      <Text style={[innerStyle.title,textStyles.subheading]}>{title}</Text>
    </View>
  );
};

export default BackButton;

const innerStyle = StyleSheet.create({
  container: {
    position: 'relative',
    height: 50,
    justifyContent: 'center',
  },
  backButton: {
    position: 'absolute',
    left: 20,
    width: 35,
    height: 35,
    borderRadius: 50,
    backgroundColor: '#f1f1f1',
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    alignSelf: 'center',
    fontSize: Fonts.sizes.lg,
    fontWeight: '600',
  },
});
