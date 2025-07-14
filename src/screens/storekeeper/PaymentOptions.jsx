import React, { useEffect, useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Image,
  TouchableOpacity,
  ScrollView,
  TextInput,
} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import Ionicons from 'react-native-vector-icons/Ionicons';
import Feather from 'react-native-vector-icons/Feather';
import { launchImageLibrary } from 'react-native-image-picker';

import BackButton from '../../components/BackButton';
import styles from '../../styles/globalStyles';
import Fonts from '../../styles/font';
import Colors from '../../styles/colors';

const PaymentOptions = () => {
  const [qrCodes, setQrCodes] = useState([]);
  const [defaultQRIndex, setDefaultQRIndex] = useState(null);
  const [editingIndex, setEditingIndex] = useState(null);

  useEffect(() => {
    const loadData = async () => {
      const storedQR = await AsyncStorage.getItem('qrCodes');
      const storedIndex = await AsyncStorage.getItem('defaultQRIndex');
      if (storedQR) setQrCodes(JSON.parse(storedQR));
      if (storedIndex !== null) setDefaultQRIndex(parseInt(storedIndex));
    };
    loadData();
  }, []);

  const saveQRs = async data => {
    setQrCodes(data);
    await AsyncStorage.setItem('qrCodes', JSON.stringify(data));
  };

  const saveDefaultQR = async index => {
    setDefaultQRIndex(index);
    await AsyncStorage.setItem('defaultQRIndex', index.toString());
  };

  const pickImage = async index => {
    const result = await launchImageLibrary({
      mediaType: 'photo',
      quality: 1,
    });

    if (result.assets && result.assets.length > 0) {
      const updated = [...qrCodes];
      if (updated[index]) {
        updated[index].uri = result.assets[0].uri;
      } else {
        updated[index] = {
          uri: result.assets[0].uri,
          name: `QR Code ${index + 1}`,
        };
      }
      saveQRs(updated);

      if (defaultQRIndex === null) {
        saveDefaultQR(index);
      }
    }
  };
  

  const handleNameChange = (index, text) => {
    const updated = [...qrCodes];
    if (updated[index]) {
      updated[index].name = text;
      saveQRs(updated);
    }
  };

  const displaySlots = [...qrCodes];
  if (displaySlots.length < 3) {
    displaySlots.push(null);
  }

  return (
    <View style={styles.pageContainer}>
      <BackButton title="Payment Options" />

      <ScrollView contentContainerStyle={innerStyle.container}>
        {displaySlots.map((qr, index) => {
          const isDefault = index === defaultQRIndex;
          const isEditing = editingIndex === index;

          return (
            <TouchableOpacity
              key={index}
              style={[
                innerStyle.qrCard,
                isDefault && innerStyle.qrCardSelected,
              ]}
              onPress={() => {
                if (qr?.uri) saveDefaultQR(index);
              }}
              activeOpacity={0.9}
            >
              <View style={innerStyle.qrHeader}>
                {isEditing ? (
                  <>
                    <TextInput
                      value={qr?.name ?? `QR Code ${index + 1}`}
                      onChangeText={text => handleNameChange(index, text)}
                      style={innerStyle.nameInput}
                      placeholder="Enter name"
                    />
                    <TouchableOpacity onPress={() => setEditingIndex(null)}>
                      <Ionicons name="checkmark" size={20} color={Colors.primary}/>
                    </TouchableOpacity>
                  </>
                ) : (
                  <>
                    <Text style={innerStyle.qrLabel}>
                      {qr?.name ?? `QR Code ${index + 1}`}
                    </Text>
                    {qr?.uri && (
                      <TouchableOpacity onPress={() => setEditingIndex(index)}>
                        <Feather name="edit" size={18} color={Colors.secondary} />
                      </TouchableOpacity>
                    )}
                  </>
                )}
              </View>

              {qr?.uri ? (
                <Image
                  source={{ uri: qr.uri }}
                  style={innerStyle.qrImage}
                  resizeMode="contain"
                />
              ) : (
                <View style={innerStyle.qrPlaceholder}>
                  <Ionicons name="qr-code" size={48} color={Colors.secondary} />
                  <Text style={innerStyle.placeholderText}>No QR Selected</Text>
                </View>
              )}

              <TouchableOpacity
                style={[innerStyle.uploadBtn, { marginTop: 12 }]}
                onPress={() => pickImage(index)}
              >
                <Text style={innerStyle.uploadBtnText}>
                  {qr?.uri ? 'Change QR' : 'Upload QR'}
                </Text>
              </TouchableOpacity>
            </TouchableOpacity>
          );
        })}

        {/* <TouchableOpacity
          style={innerStyle.clearBtn}
          onPress={async () => {
            await AsyncStorage.removeItem("qrCodes");
            await AsyncStorage.removeItem("defaultQRIndex");
            setQrCodes([]);
            setDefaultQRIndex(null);
          }}
        >
          <Text style={innerStyle.clearBtnText}>Clear All QR Data</Text>
        </TouchableOpacity> */}
      </ScrollView>
    </View>
  );
};

export default PaymentOptions;

const innerStyle = StyleSheet.create({
  container: {
    padding: 20,
  },
  qrCard: {
    marginBottom: 24,
    backgroundColor:Colors.bgClr,
    borderRadius: 16,
    padding: 16,
    borderWidth: 1,
    borderColor: Colors.borderColor,
    shadowColor: Colors.bgClr,
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.08,
    shadowRadius: 6,
    elevation: 3,
    transform: [{ scale: 1 }],
  },

  qrCardSelected: {
    borderColor: Colors.primary,
    backgroundColor: Colors.primary,
    shadowColor: Colors.borderColor,
    shadowOpacity: 0.2,
    elevation: 6,
    transform: [{ scale: 1.01 }],
  },

  qrHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  qrLabel: {
    fontSize: Fonts.sizes.base,
    fontWeight: '600',
    color: Colors.secondary,
  },
  nameInput: {
    flex: 1,
    fontSize: Fonts.sizes.base,
    padding: 4,
    borderBottomWidth: 1,
    borderColor: Colors.borderColor,
    marginRight: 10,
  },
  qrPlaceholder: {
    height: 200,
    width: 200,
    justifyContent: 'center',
    alignItems: 'center',
    alignSelf: 'center',
    borderWidth: 1,
    borderColor: Colors.borderColor,
    borderStyle: 'dashed',
    borderRadius: 12,
    marginBottom: 12,
    backgroundColor: Colors.bgClr,
  },
  placeholderText: {
    marginTop: 8,
    color: Colors.borderColor,
    fontSize: Fonts.sizes.sm,
  },
  qrImage: {
    width: 200,
    alignSelf: 'center',
    height: 200,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: Colors.borderColor,
    marginBottom: 12,
    backgroundColor: Colors.bgClr,
  },

  uploadBtn: {
    backgroundColor: Colors.primary,
    paddingVertical: 10,
    borderRadius: 8,
    alignItems: 'center',
  },
  uploadBtnText: {
    color: Colors.bgClr,
    fontSize: Fonts.sizes.base,
    fontWeight: '500',
  },
  clearBtn: {
    marginTop: 10,
    alignSelf: 'center',
    backgroundColor: Colors.reject,
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 8,
  },
  clearBtnText: {
    color: Colors.bgClr,
    fontSize: Fonts.sizes.sm,
    fontWeight: '600',
  },
});
