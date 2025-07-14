import { useEffect, useRef, useState } from 'react';
import {
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  Linking,
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { useDispatch, useSelector } from 'react-redux';

import Grocery from '../../../assets/images/grocery-logo.svg';
import CustomButton from '../../components/CustomButton';
import CustomInput from '../../components/CustomInput';

import { useProfile } from '../../contexts/profileContext';
import { useSafeRouter } from '../../hooks/useSafeRouter';
import styles from '../../styles/globalStyles';
import { showToast } from '../../utils/toastUtils';
import Colors from '../../styles/colors';
import textStyles from '../../styles/textStyles';
import Fonts from '../../styles/font';

import { sendOtp, verifyOtp } from '../../services/authApi';
import { useAuth } from '../../contexts/authContext';
import { setCartUser } from '../../store/cartSlice';
import AsyncStorage from '@react-native-async-storage/async-storage';

const MobileOtpScreen = () => {
  const { safePush } = useSafeRouter();
  const { updateProfile } = useProfile();
  const { login } = useAuth();
  const dispatch = useDispatch();

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
      const role = userType === 'I AM CUSTOMER' ? 'CUSTOMER' : 'STOREKEEPER';
      const res = await sendOtp(mobile, isResend ? null : role);

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
      showToast(
        'error',
        `Failed to ${isResend ? 'resend' : 'send'} OTP`,
        error.message || 'Something went wrong',
      );
    }
  };

  const handleSendOtp = () => {
    if (!mobile || mobile.length < 10) {
      showToast('error', 'Invalid number', 'Enter a 10-digit number');
      return;
    }
    sendOtpRequest(false);
  };

  const handleResendOtp = () => {
    if (!canResend) return;
    sendOtpRequest(true);
  };

  const handleLogin = async () => {
    if (mobile.length !== 10) {
      showToast(
        'error',
        'Invalid Mobile Number',
        'Please enter a valid 10-digit mobile number',
      );
      return;
    }
    if (!otp || otp.length !== 4) {
      showToast('error', 'Invalid OTP', 'Please enter a valid 4-digit OTP');
      return;
    }

    try {
      const res = await verifyOtp(mobile, otp);
      const token = res?.token;
      const role = res?.roles?.[0];
      const userId = res?.userId;

      console.log(role, token);

      if (!token) throw new Error('No token received');
      await AsyncStorage.removeItem('selectedAddressId');
      await login({ token, role, userId });
      dispatch(setCartUser(userId));

      const toastPayload = {
        type: 'success',
        title: 'OTP Verified',
        message: 'Update your profile to complete login.',
      };
      console.log(mobile);

      if (role === 'CUSTOMER') {
        safePush('CustomerCreateProfile', {
          toast: JSON.stringify(toastPayload),
          mobile,
          role,
        });
      } else {
        safePush('StorekeeperCreateProfile', {
          toast: JSON.stringify(toastPayload),
        });
      }
    } catch (error) {
      console.error('OTP Verify Error:', error);
      showToast(
        'error',
        'OTP Verification Failed',
        error.message || 'Something went wrong',
      );
    }
  };

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <ScrollView
        contentContainerStyle={localStyles.scrollContent}
        keyboardShouldPersistTaps="handled"
        showsVerticalScrollIndicator={false}
      >
        <View style={localStyles.centerContent}>
          <Grocery style={localStyles.logo} />
          <Text
            style={[
              styles.pageHeading,
              textStyles.heading,
              { marginBottom: 20 },
            ]}
          >
            Login
          </Text>

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

          <Text style={localStyles.otpPrompt}>Enter 4 Digit Code Here</Text>

          <CustomInput
            placeholder="Enter OTP"
            keyboardType="numeric"
            maxLength={4}
            value={otp}
            onTextChange={text => setOtp(text.replace(/[^0-9]/g, ''))}
          />

          <View style={localStyles.resendContainer}>
            <Text>Haven't received OTP? </Text>
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
                  : `Resend available in ${timer}s`}
              </Text>
            </Pressable>
          </View>

          <View style={localStyles.policyContainer}>
            <Text style={localStyles.policyText}>
              I agreed to{' '}
              <Text
                onPress={() =>
                  Linking.openURL('https://policies.google.com/terms?hl=en-US')
                }
              >
                <Text style={localStyles.underline}>
                  Terms and conditions &{'\n'}Privacy Policy
                </Text>
              </Text>
            </Text>
          </View>
        </View>
        <View style={localStyles.loginBtn}>
          <CustomButton onPress={handleLogin} title="Login" />
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
};

export default MobileOtpScreen;

const localStyles = StyleSheet.create({
  centerContent: {
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingTop: 40,
  },
  logo: {
    marginBottom: 32,
  },
  scrollContent: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingTop: 40,
    paddingBottom: 40,
    backgroundColor: Colors.bgClr,
  },
  centerContent: {
    width: '100%',
    alignItems: 'center',
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
  policyContainer: {
    marginTop: 40,
    paddingHorizontal: 10,
  },
  policyText: {
    fontSize: Fonts.sizes.base,
    color: Colors.secondary,
    textAlign: 'center',
    lineHeight: 22,
  },
  underline: {
    textDecorationLine: 'underline',
    color: Colors.secondary,
  },
  loginBtn: {
    marginTop: 40,
    width: '100%',
    alignItems: 'center',
  },
});
