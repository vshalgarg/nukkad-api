import { useRouter } from 'expo-router';
import { Pressable, Text, View } from 'react-native';
import Grocery from '../../assets/images/grocery-logo.svg';
import styles from './../styles/globalStyles.js';
import { useSafeRouter } from '../hooks/useSafeRouter';

import { useDispatch, useSelector } from 'react-redux';
import { setUserType } from './../store/userSlice.js';
import textStyles from '../styles/textStyles.js';
import Fonts from '../styles/font.js';

export default function Index() {
  const { safePush } = useSafeRouter();
  const dispatch = useDispatch();
  const userType = useSelector(state => state.user.userType);

  const options = ['I AM CUSTOMER', 'I AM STOREKEEPER'];

  return (
    <View style={[styles.pageContainer,{justifyContent:"center",alignItems:"center"}]} >
      <Grocery style={{ marginBottom: 30 }} />

      <Text style={[styles.pageHeading,textStyles.heading]}>Select User Type</Text>

      <View
        style={{
          width: '100%',
          alignItems: 'center',
          gap: 20,
          marginBottom: 50,
        }}
      >
        {options.map((option, index) => (
          <Pressable
            key={index}
            onPress={() => {
              dispatch(setUserType(option));
              safePush('MobileOtpScreen');
            }}
            style={{
              borderRadius: 24,
              borderWidth: 2,
              borderColor: userType === option ? '#007bff' : '#ccc',
              backgroundColor: userType === option ? '#e0f0ff' : '#fff',
              alignItems: 'center',
              paddingVertical: 12,
              width: 291,
              height: 48,
            }}
          >
            <Text
              style={{
                fontSize: Fonts.sizes.base,
                color: userType === option ? '#007bff' : '#333',
                fontWeight: userType === option ? 'bold' : 'normal',
              }}
            >
              {option}
            </Text>
          </Pressable>
        ))}
      </View>
    </View>
  );
}
