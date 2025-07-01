// components/Edit.jsx
import React, { useState, useEffect } from "react";
import {
  Modal,
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
} from "react-native";
import { showToast } from "../utils/toastUtils";
import Colors from "../styles/colors";
import Fonts from "../styles/font";

const Edit = ({ visible, onClose, currentmobile, onVerified }) => {
  const [mobile, setMobile] = useState("");
  const [otp, setOtp] = useState("");
  const [otpSent, setOtpSent] = useState(false);

  useEffect(() => {
    if (visible) {
      setMobile(currentmobile);
      setOtp("");
      setOtpSent(false);
    }
  }, [visible]);

  const sendOtp = () => {
    if (!/^\d{10}$/.test(mobile)) {
      showToast("error", "Enter valid 10-digit number");
      return;
    }
    
    setOtpSent(true);
    showToast("success", "OTP sent to " + mobile);
  };

  const verifyOtp = () => {
    if (otp === "123456") {
      onVerified(mobile);
      showToast("success", "mobile number updated successfully");
      onClose();
    } else {
      showToast("error", "Invalid OTP");
    }
  };

  return (
    <Modal visible={visible} transparent animationType="slide">
      <View style={styles.overlay}>
        <View style={styles.modalContainer}>
          <Text style={styles.title}>Edit Mobile Number</Text>

          <TextInput
            style={styles.input}
            value={mobile}
            keyboardType="phone-pad"
            maxLength={10}
            onChangeText={setMobile}
            placeholder="Enter Mobile Number"
          />

          {otpSent && (
            <TextInput
              style={styles.input}
              value={otp}
              keyboardType="phone-pad"
              maxLength={6}
              onChangeText={setOtp}
              placeholder="Enter OTP"
            />
          )}

          <View style={styles.buttonRow}>
            {!otpSent ? (
              <TouchableOpacity style={styles.button} onPress={sendOtp}>
                <Text style={styles.buttonText}>Send OTP</Text>
              </TouchableOpacity>
            ) : (
              <TouchableOpacity style={styles.button} onPress={verifyOtp}>
                <Text style={styles.buttonText}>Verify OTP</Text>
              </TouchableOpacity>
            )}
            <TouchableOpacity
              style={[styles.button, styles.cancel]}
              onPress={onClose}
            >
              <Text style={[styles.buttonText, { color: "#333" }]}>Cancel</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
};

export default Edit;

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: "rgba(0,0,0,0.5)",
    justifyContent: "center",
    alignItems: "center",
  },
  modalContainer: {
    backgroundColor: "white",
    borderRadius: 12,
    padding: 20,
    width: "85%",
    elevation: 5,
  },
  title: {
    fontSize: Fonts.sizes.lg,
    fontWeight: "bold",
    marginBottom: 15,
    textAlign: "center",
  },
  input: {
    borderColor: "#ccc",
    borderWidth: 1,
    borderRadius:25,
    paddingHorizontal: 12,
    marginBottom: 15,
  },
  buttonRow: {
    flexDirection: "row",
    justifyContent: "space-between",
  },
  button: {
    backgroundColor: Colors.primary,
    padding: 10,
    borderRadius: 20,
    flex: 1,
    marginHorizontal: 5,
    alignItems: "center",
  },
  cancel: {
    backgroundColor: "#eee",
  },
  buttonText: {
    color: "white",
    fontWeight: "bold",
  },
});
