module.exports = {
  presets: ['@react-native/babel-preset'],
  plugins: [
    'react-native-reanimated/plugin', // ⚠️ Must be last in the list if used
  ],
};
