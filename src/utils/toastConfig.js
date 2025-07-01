// utils/toastConfig.js
import { BaseToast, ErrorToast } from "react-native-toast-message";

export const toastConfig = {
  success: (props) => (
    <BaseToast
      {...props}
      style={{ borderLeftColor: "green", marginTop: 0 }}
      contentContainerStyle={{ paddingHorizontal: 30 }}
      text1Style={{
        fontSize: 16,
        fontWeight: "bold",
        color: "green",
      }}
      text2Style={{
        fontSize: 14,
        color: "#006600",
      }}
    />
  ),

  error: (props) => (
    <ErrorToast
      {...props}
      style={{ borderLeftColor: "red", marginTop: 0 }}
      text1Style={{
        fontSize: 16,
        fontWeight: "bold",
        color: "red",
      }}
      text2Style={{
        fontSize: 14,
        color: "#800000",
      }}
    />
  ),

};
