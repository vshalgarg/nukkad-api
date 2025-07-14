import React from 'react';
import {
  Alert,
  Linking,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import Ionicons from 'react-native-vector-icons/Ionicons';
import FontAwesome from 'react-native-vector-icons/FontAwesome';

import BackButton from '../../components/BackButton';
import styles from '../../styles/globalStyles';
import Fonts from '../../styles/font';
import Colors from '../../styles/colors';

const HelpSupport = () => {
  const supportEmail = 'support@example.com';
  const supportPhone = '+911234567890';
  const whatsappNumber = '911234567890';

  const openDialer = () => {
    Linking.openURL(`tel:${supportPhone}`).catch(() =>
      Alert.alert('Error', 'Could not open dialer.'),
    );
  };

  const openEmail = () => {
    Linking.openURL(`mailto:${supportEmail}`).catch(() =>
      Alert.alert('Error', 'Could not open mail app.'),
    );
  };

  const openWhatsApp = () => {
    const url = `https://wa.me/${whatsappNumber}`;
    Linking.openURL(url).catch(() =>
      Alert.alert('Error', 'WhatsApp is not installed.'),
    );
  };

  return (
    <View style={styles.pageContainer}>
      <BackButton title="Help and Support" />
      <View style={innerStyles.container}>
        <Text style={innerStyles.title}>We're Here to Help You</Text>
        <Text style={innerStyles.subtitle}>
          Feel free to reach out through any of the methods below:
        </Text>

        <View style={innerStyles.cardContainer}>
          <TouchableOpacity style={innerStyles.card} onPress={openDialer}>
            <View
              style={[innerStyles.iconCircle, { backgroundColor: '#E0F7FA' }]}
            >
              <Ionicons name="call" size={24} color="#00796B" />
            </View>
            <View>
              <Text style={innerStyles.cardTitle}>Call Us</Text>
              <Text style={innerStyles.cardInfo}>{supportPhone}</Text>
            </View>
          </TouchableOpacity>

          <TouchableOpacity style={innerStyles.card} onPress={openEmail}>
            <View
              style={[innerStyles.iconCircle, { backgroundColor: '#FFF3E0' }]}
            >
              <Ionicons name="mail" size={24} color="#EF6C00" />
            </View>
            <View>
              <Text style={innerStyles.cardTitle}>Email</Text>
              <Text style={innerStyles.cardInfo}>{supportEmail}</Text>
            </View>
          </TouchableOpacity>

          <TouchableOpacity style={innerStyles.card} onPress={openWhatsApp}>
            <View
              style={[innerStyles.iconCircle, { backgroundColor: '#E8F5E9' }]}
            >
              <FontAwesome name="whatsapp" size={24} color="#2E7D32" />
            </View>
            <View>
              <Text style={innerStyles.cardTitle}>WhatsApp</Text>
              <Text style={innerStyles.cardInfo}>Chat with us on WhatsApp</Text>
            </View>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default HelpSupport;

const innerStyles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  title: {
    fontSize: Fonts.sizes.xl,
    fontWeight: 'bold',
    marginBottom: 8,
    color: Colors.secondary,
  },
  subtitle: {
    fontSize: Fonts.sizes.base,
    color: Colors.secondaryText,
    marginBottom: 24,
  },
  cardContainer: {
    gap: 20,
  },
  card: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Colors.bgClr,
    padding: 16,
    borderRadius: 10,
  },
  iconCircle: {
    width: 48,
    height: 48,
    borderRadius: 24,
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 14,
  },
  cardTitle: {
    fontSize: Fonts.sizes.base,
    fontWeight: '600',
    color: Colors.secondary,
  },
  cardInfo: {
    fontSize: Fonts.sizes.sm,
    color: Colors.secondaryText,
    marginTop: 2,
  },
});
