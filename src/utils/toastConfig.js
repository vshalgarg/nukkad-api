import { BaseToast, ErrorToast } from 'react-native-toast-message';

export const toastConfig = {
  success: props => (
    <BaseToast
      {...props}
      style={{
        borderLeftColor: 'green',
        marginTop: 30,
        paddingVertical: 12,
        alignItems: 'center',
      }}
      contentContainerStyle={{
        paddingHorizontal: 20,
        flexDirection: 'column',
        alignItems: 'center',
      }}
      text1Style={{
        fontSize: 15,
        fontWeight: 'bold',
        color: 'green',
        width: '100%',
      }}
      text2Style={{
        fontSize: 13,
        color: '#006600',
        width: '100%',
        marginTop: 4,
      }}
      text2NumberOfLines={0}
    />
  ),

  error: props => (
    <ErrorToast
      {...props}
      style={{
        borderLeftColor: 'red',
        marginTop: 10,
        paddingVertical: 12,
        alignItems: 'center',
      }}
      contentContainerStyle={{
        paddingHorizontal: 20,
        flexDirection: 'column',
        alignItems: 'center',
      }}
      text1Style={{
        fontSize: 15,
        fontWeight: 'bold',
        color: 'red',
        width: '100%',
      }}
      text2Style={{
        fontSize: 13,
        color: '#800000',
        width: '100%',
        marginTop: 4,
      }}
      text2NumberOfLines={0}
    />
  ),
};
