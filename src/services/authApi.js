import api from "./api";
// Send OTP
export const sendOtp = async (mobileNumber, role = null) => {
  const payload = { mobileNumber };
  if (role) payload.role = role;

  console.log('📤 Sending OTP request with payload:', payload);

  try {
    const response = await api.post('/nukkad/api/otp/v1/send', payload);

    console.log('✅ Send OTP API Success:', {
      status: response.status,
      data: response.data,
    });

    return response.data;
  } catch (error) {
    console.error('❌ Send OTP API Error:', {
      message: error.message, 
      status: error.response?.status,
      data: error.response?.data,
      request: payload,
    });

    throw new Error(error.response?.data?.message || 'Failed to send OTP');
  }
};

// Verify OTP
export const verifyOtp = async (mobileNumber, otp) => {
  const payload = { mobileNumber, otp };

  console.log('📤 Verifying OTP with payload:', payload);

  try {
    const response = await api.post('/nukkad/api/otp/v1/verify', payload);

    console.log('✅ Verify OTP API Success:', {
      status: response.status,
      data: response.data,
    });

    return response.data;
  } catch (error) {
    console.error('❌ Verify OTP API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
      request: payload,
    });

    throw new Error(error.response?.data?.message || 'OTP verification failed');
  }
};
