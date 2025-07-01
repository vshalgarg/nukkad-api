// utils/toastConfig.js
import Toast from "react-native-toast-message";

export const showToast = (type, title, message = "") => {
  Toast.show({
    type,
    text1: title,
    text2: message,
    position: "top",
    visibilityTime:2000
  });
};
