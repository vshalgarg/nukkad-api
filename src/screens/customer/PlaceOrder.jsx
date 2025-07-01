import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useSafeRouter } from '../../hooks/useSafeRouter';

import CompleteOrderImage from '../../../assets/images/complete-order.svg';
import Design from '../../../assets/images/design.svg';
import BackButton from '../../components/BackButton';
import styles from '../../styles/globalStyles';
import Fonts from '../../styles/font';

const PlaceOrder = () => {
  const { safePush } = useSafeRouter(); // ✅ Use your hook

  const handleContinueShopping = () => {
    safePush('CustomerDashboard'); // ✅ Replace normal navigation
  };

  return (
    <View style={styles.pageContainer}>
      <BackButton title="CheckOut" />
      <View style={innerStyle.container}>
        <View style={innerStyle.topImage}>
          <Design width="100%" height={100} />
        </View>
        <Text style={innerStyle.heading}>Order Placed</Text>
        <View style={innerStyle.image}>
          <CompleteOrderImage width={150} height={150} />
          <View style={innerStyle.textContainer}>
            <Text style={innerStyle.text}>Thank you for your purchase.</Text>
            <Text style={innerStyle.text}>
              You can view your order in ‘My Orders’
            </Text>
            <Text style={innerStyle.text}>section.</Text>
          </View>
        </View>
        <TouchableOpacity
          style={innerStyle.btn}
          onPress={handleContinueShopping}
        >
          <Text style={innerStyle.btnText}>Continue Shopping</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default PlaceOrder;

const innerStyle = StyleSheet.create({
  container: {
    gap: 30,
    flexDirection: 'column',
    justifyContent: 'space-between',
    padding: 20,
    marginTop: 20,
  },
  topImage: {
    alignItems: 'center',
  },
  image: {
    marginTop: 30,
    alignItems: 'center',
  },
  heading: {
    fontSize: Fonts.sizes.xxl,
    fontWeight: '600',
    textAlign: 'center',
  },
  textContainer: {
    marginTop: 20,
  },
  text: {
    textAlign: 'center',
    fontSize: Fonts.sizes.base,
    marginVertical: 2,
  },
  btn: {
    backgroundColor: '#343434',
    borderRadius: 50,
    alignSelf: 'center',
    paddingVertical: 12,
    paddingHorizontal: 30,
    marginTop: 30,
  },
  btnText: {
    color: 'white',
    textAlign: 'center',
    fontWeight: '600',
    fontSize: Fonts.sizes.lg,
  },
});
