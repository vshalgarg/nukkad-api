import { useEffect, useRef, useState } from 'react';
import {
  Alert,
  Keyboard,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TouchableWithoutFeedback,
  View,
} from 'react-native';
  
import { useProfile } from '../../contexts/profileContext';
import { useSafeRouter } from '../../hooks/useSafeRouter';
import { useSelector } from 'react-redux';

import Grocery from '../../../assets/images/grocery-logo.svg';
import CustomInput from '../../components/CustomInput';
import CustomButton from '../../components/CustomButton';
import { sendOtp, verifyOtp } from '../../services/authApi';
import { deleteUserAccount } from '../../services/userApi'; // ✅ create this
import { showToast } from '../../utils/toastUtils';
import styles from '../../styles/globalStyles';
import Colors from '../../styles/colors';
import Fonts from '../../styles/font';
import BackButton from '../../components/BackButton';

const DeleteAccount = () => {
  const { profile, clearProfile } = useProfile();
  const { safePush } = useSafeRouter();

  const [mobile, setMobile] = useState('');
  const [otp, setOtp] = useState('');
  const [sendOtpClicked, setSendOtpClicked] = useState(false);
  const [canResend, setCanResend] = useState(false);
  const [timer, setTimer] = useState(0);
  const timerRef = useRef(null);

  const userType = useSelector(state => state.user.userType);

  useEffect(() => {
    setSendOtpClicked(false);
    setCanResend(false);
    setTimer(0);
    clearTimeout(timerRef.current);
  }, [mobile]);

  useEffect(() => {
    if (sendOtpClicked && !canResend && timer > 0) {
      timerRef.current = setTimeout(() => {
        setTimer(prev => prev - 1);
      }, 1000);
    } else if (timer === 0 && sendOtpClicked) {
      setCanResend(true);
      clearTimeout(timerRef.current);
    }
    return () => clearTimeout(timerRef.current);
  }, [timer, sendOtpClicked]);

  const sendOtpRequest = async (isResend = false) => {
    try {
      const res = await sendOtp(mobile, null);
      showToast(
        'success',
        isResend ? 'OTP Resent' : 'OTP Sent',
        res.message ||
          `OTP has been ${isResend ? 'resent' : 'sent'} successfully`,
      );
      setSendOtpClicked(true);
      setTimer(30);
      setCanResend(false);
    } catch (error) {
      console.error(`${isResend ? 'Resend' : 'Send'} OTP Error:`, error);
      showToast('error', `Failed to ${isResend ? 'resend' : 'send'} OTP`);
    }
  };

  const handleSendOtp = () => {
    if (!mobile || mobile.length !== 10) {
      return showToast('error', 'Enter a valid 10-digit mobile number');
    }
    if (mobile !== profile.mobile) {
      return showToast('error', 'Enter your registered mobile number');
    }
    sendOtpRequest(false);
  };

  const handleResendOtp = () => {
    if (!canResend) return;
    sendOtpRequest(true);
  };

  const handleVerifyAndDelete = async () => {
    if (!mobile || mobile.length !== 10) {
      return showToast('error', 'Enter a valid 10-digit mobile number');
    }
    if (!otp || otp.length !== 4) {
      return showToast('error', 'Enter a valid 4-digit OTP');
    }

    try {
      await verifyOtp(mobile, otp);

      Alert.alert(
        'Confirm Account Deletion',
        'Are you sure you want to delete your account? This action cannot be undone.',
        [
          { text: 'Cancel', style: 'cancel' },
          {
            text: 'Delete',
            style: 'destructive',
            onPress: async () => {
              try {
                // await deleteUserAccount(mobile);
                showToast('success', 'Account deleted successfully');
                clearProfile();
                safePush('Landing');
              } catch (deleteErr) {
                console.error('Account deletion failed:', deleteErr);
                showToast('error', 'Failed to delete account');
              }
            },
          },
        ],
      );
    } catch (error) {
      console.error('OTP verification failed:', error);
      showToast('error', 'OTP verification failed');
    }
  };

  return (
    <View style={styles.pageContainer}>
      {/* BackButton stays fixed at the top */}
      <BackButton title="Delete Account" />

      {/* Keyboard-safe, scrollable form section */}
      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 100 : 0}
      >
        <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
          <ScrollView
            contentContainerStyle={localStyles.scrollContainer}
            keyboardShouldPersistTaps="handled"
          >
            <View style={localStyles.centerContent}>
              <Grocery style={localStyles.logo} />

              <CustomInput
                isCountryCode={true}
                placeholder="Enter Mobile Number"
                keyboardType="numeric"
                maxLength={10}
                value={mobile}
                onTextChange={text => setMobile(text.replace(/[^0-9]/g, ''))}
              />

              <Text
                style={[
                  localStyles.sendOtpText,
                  sendOtpClicked
                    ? localStyles.sendOtpDisabled
                    : localStyles.sendOtpEnabled,
                ]}
                onPress={!sendOtpClicked ? handleSendOtp : null}
              >
                Send OTP
              </Text>

              <Text style={localStyles.otpPrompt}>Enter 4 Digit OTP</Text>

              <CustomInput
                placeholder="Enter OTP"
                keyboardType="numeric"
                maxLength={4}
                value={otp}
                onTextChange={text => setOtp(text.replace(/[^0-9]/g, ''))}
              />

              <View style={localStyles.resendContainer}>
                <Text>Didn’t get OTP? </Text>
                <Pressable
                  onPress={handleResendOtp}
                  disabled={!canResend || !sendOtpClicked}
                >
                  <Text
                    style={[
                      localStyles.resendText,
                      canResend && sendOtpClicked
                        ? localStyles.resendEnabled
                        : localStyles.resendDisabled,
                    ]}
                  >
                    {!sendOtpClicked
                      ? 'Resend OTP'
                      : canResend
                      ? 'Resend OTP'
                      : `Resend in ${timer}s`}
                  </Text>
                </Pressable>
              </View>

              <View style={localStyles.loginBtn}>
                <CustomButton
                  style={{
                    backgroundColor: Colors.reject,
                    borderColor: Colors.reject,
                  }}
                  onPress={handleVerifyAndDelete}
                  title="Delete Account"
                />
              </View>
            </View>
          </ScrollView>
        </TouchableWithoutFeedback>
      </KeyboardAvoidingView>
    </View>
  );
};

export default DeleteAccount;

const localStyles = StyleSheet.create({
  centerContent: {
    justifyContent: 'center',
    alignItems: 'center',
    flex: 1,
    paddingHorizontal: 20,
    paddingTop: 40,
  },
  scrollContainer: {
    flexGrow: 1,
    paddingBottom: 40,
  },
  logo: {
    marginBottom: 32,
  },
  sendOtpText: {
    fontSize: Fonts.sizes.sm,
    fontWeight: '600',
    alignSelf: 'flex-end',
    marginTop: 5,
    marginBottom: 20,
    marginRight: 10,
  },
  sendOtpEnabled: {
    color: Colors.primary,
  },
  sendOtpDisabled: {
    color: '#9CA3AF',
    opacity: 0.5,
  },
  otpPrompt: {
    fontSize: Fonts.sizes.base,
    fontWeight: '500',
    color: Colors.secondaryText,
    textAlign: 'center',
    marginBottom: 12,
  },
  resendContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 16,
    marginBottom: 8,
  },
  resendText: {
    fontSize: Fonts.sizes.sm,
    fontWeight: '600',
    marginLeft: 4,
  },
  resendEnabled: {
    color: Colors.primary,
  },
  resendDisabled: {
    color: '#9CA3AF',
  },
  loginBtn: {
    marginTop: 40,
    width: '100%',
    alignItems: 'center',
  },
});
