import React from 'react';
import { Pressable, StyleSheet, View, Text } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import Entypo from 'react-native-vector-icons/Entypo';
import textStyles from '../styles/textStyles';
import Fonts from '../styles/font';
import Colors from '../styles/colors';

const BackButton = ({ title, backgroundColor =Colors.backbuttonColor, onPress }) => {
  const navigation = useNavigation();

  const handlePress = () => {
    if (onPress) {
      onPress();
      } else if (navigation.canGoBack()) {
      navigation.goBack();
    } else {
      // Prevent crash: fallback
      navigation.navigate('CustomerDashboard'); // or 'Home'
    }
  };

  return (
    <View style={innerStyle.container}>
      <Pressable
        style={[innerStyle.backButton, { backgroundColor }]}
        onPress={handlePress}
      >
        <Entypo name="chevron-left" size={20} color={Colors.secondary} />
      </Pressable>

      <Text style={[innerStyle.title,textStyles.subheading]}>{title}</Text>
    </View>
  );
};

export default BackButton;

const innerStyle = StyleSheet.create({
  container: {
    marginVertical:5,
    position: 'relative',
    height: 50,
    justifyContent: 'center',
    zIndex:100
  },
  backButton: {
    position: 'absolute',
    left: 20,
    width: 35,
    height: 35,
    borderRadius: 50,
    borderColor:Colors.borderColor,
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    alignSelf: 'center',
  },
});
