import {
  Linking,
  Modal,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import Colors from '../styles/colors';
import Ionicons from 'react-native-vector-icons/Ionicons';
import FontAwesome from 'react-native-vector-icons/FontAwesome';
import Fonts from '../styles/font';

const ConnectPopup = ({ visible, onClose, phone }) => {
  const handleCall = () => {
    onClose();
    Linking.openURL(`tel:${phone || '9999999999'}`);
  };

  const handleWhatsApp = () => {
    onClose();
    const message = "Hello, I'm contacting you regarding your order.";
    Linking.openURL(
      `whatsapp://send?phone=${phone}&text=${encodeURIComponent(message)}`,
    );
  };

  return (
    <Modal visible={visible} transparent onRequestClose={onClose}>
      <View style={styles.overlay}>
        <View style={styles.popup}>
          <View style={styles.container}>
            <Text style={styles.buttonTextPhone}>
              <Ionicons name="call" size={Fonts.sizes.base} color={Colors.reject} /> Call Now
            </Text>
            <TouchableOpacity onPress={handleCall} style={styles.button}>
              <Text style={[styles.buttonText, { color: Colors.bgClr }]}>Done</Text>
            </TouchableOpacity>
          </View>

          <View style={styles.container}>
            <Text style={styles.buttonTextChat}>
              <FontAwesome name="whatsapp" size={20} color={Colors.primary}/> WhatsApp
            </Text>
            <TouchableOpacity onPress={handleWhatsApp} style={styles.button}>
              <Text style={[styles.buttonText, { color: Colors.bgClr }]}>Done</Text>
            </TouchableOpacity>
          </View>

          <TouchableOpacity
            onPress={onClose}
            style={{ marginTop: 10, width: '100%' }}
          >
            <Text style={{ color: Colors.secondaryText, textAlign: 'center' }}>Cancel</Text>
          </TouchableOpacity>
        </View>
      </View>
    </Modal>
  );
};

export default ConnectPopup;

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: Colors.secondary,
  },
  popup: {
    backgroundColor: Colors.bgClr,
    padding: 20,
    borderRadius: 15,
    width: '80%',
    alignItems: 'center',
  },
  button: {
    backgroundColor: Colors.primary,
    paddingHorizontal: 20,
    paddingVertical: 5,
    borderRadius: 50,
  },
  title: {
    fontSize: Fonts.sizes.lg,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  container: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '100%',
    padding: 12,
    borderRadius: 8,
    alignItems: 'center',
  },
  buttonTextPhone: {
    fontWeight: 'bold',
    fontSize: Fonts.sizes.base,
    alignItems: 'center',
  },
  buttonTextChat: {
    fontWeight: 'bold',
    fontSize: Fonts.sizes.base,
    alignItems: 'center',
  },
  buttonText: {
    fontWeight: 'bold',
  },
});
