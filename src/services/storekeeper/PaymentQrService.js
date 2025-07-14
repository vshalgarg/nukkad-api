import api from '../api';

export const uploadQRImage = async (file, name = '',token) => {
  console.log('📤 Uploading QR image:', file?.fileName);

  try {
    const formData = new FormData();

    formData.append('qrCode', {
      uri: file.uri,
      name: file.fileName || 'qr.jpg',
      type: file.type || 'image/jpeg',
    });

    if (name) {
      formData.append('name', name);
    }

    const response = await api.post('/api/qr/upload', formData, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'multipart/form-data',
      },
    });

    console.log('✅ QR Upload API Success:', {
      status: response.status,
      data: response.data,
    });

    return response.data;
  } catch (error) {
    console.error('❌ QR Upload API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
    });

    throw new Error(
      error.response?.data?.message || 'Failed to upload QR image',
    );
  }
};
