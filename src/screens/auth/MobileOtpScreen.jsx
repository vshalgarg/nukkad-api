import { useEffect, useRef, useState } from 'react';
import { Linking, Pressable, StyleSheet, Text, View } from 'react-native';
import { useSelector } from 'react-redux';
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

const MobileOtpScreen = () => {
  const { safePush } = useSafeRouter();
  const { updateProfile } = useProfile();

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

  const handleSendOtp = () => {
    if (!mobile || mobile.length < 10) {
      showToast('error', 'Invalid number', 'Enter a 10-digit number');
      return;
    }

    showToast(
      'success',
      'OTP Sent',
      'OTP has been sent to your mobile number.',
    );
    setSendOtpClicked(true);
    setTimer(30);
    setCanResend(false);
  };

  const handleResendOtp = () => {
    if (!canResend) return;

    showToast('success', 'OTP Resent', 'OTP has been sent successfully');
    setTimer(30);
    setCanResend(false);
  };

  const handleLogin = () => {
    // if (mobile.length !== 10) {
    //   showToast(
    //     'error',
    //     'Invalid Mobile Number',
    //     'Please enter a valid mobile number',
    //   );
    //   return;
    // }
    // if (!otp || otp.length !== 6) {
    //   showToast('error', 'Invalid OTP', 'Please enter a valid 6-digit OTP');
    //   return;
    // }

    // const MOCK_OTP = '123456';
    // if (otp !== MOCK_OTP) {
    //   showToast('error', 'Incorrect OTP', 'Please try again.');
    //   return;
    // }

    const toastPayload = {
      type: 'success',
      title: 'OTP Verified',
      message: 'Update your profile to complete login.',
    };

    updateProfile({ mobile });

    if (userType === 'I AM CUSTOMER') {
      safePush('CustomerCreateProfile', {
        toast: JSON.stringify(toastPayload),
      });
    } else if (userType === 'I AM STOREKEEPER') {
      safePush('StorekeeperCreateProfile', {
        toast: JSON.stringify(toastPayload),
      });
    }
  };

  return (
    <View style={[styles.pageContainer, localStyles.centerContent]}>
      <Grocery style={localStyles.logo} />
      <Text style={[styles.pageHeading,textStyles.heading]}>Login</Text>
      <View>
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
      </View>

      <Text style={localStyles.otpPrompt}>Enter 6 Digit Code Here</Text>

      <CustomInput
        placeholder="Enter OTP"
        keyboardType="numeric"
        maxLength={6}
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
              Terms and conditions & Privacy Policy
            </Text>
          </Text>
        </Text>
      </View>

      <View style={localStyles.loginBtn}>
        <CustomButton onPress={handleLogin} title="Login" />
      </View>
    </View>
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
    color: '#6B7280', 
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
    color: '#374151',
    textAlign: 'center',
    lineHeight: 22,
  },
  underline: {
    textDecorationLine: 'underline',
    color: '#2563EB',
  },
  loginBtn: {
    marginTop: 40,
    width: '100%',
    alignItems: 'center',
  },
});
