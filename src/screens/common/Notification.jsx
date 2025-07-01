import React from 'react';
import { View, Text, FlatList, StyleSheet } from 'react-native';
import BackButton from '../../components/BackButton';
import Fonts from '../../styles/font';

const notifications = [
  {
    id: '1',
    title: 'Welcome!',
    message: 'Thanks for joining our Grocery App. Happy shopping!',
  },
  {
    id: '2',
    title: 'Fresh Deals',
    message: "Check out today's fresh fruit discounts!",
  },
  {
    id: '3',
    title: 'Order Update',
    message: 'Your order #1234 has been shipped and is on its way.',
  },
  {
    id: '4',
    title: 'Weekly Tips',
    message: 'Try our new organic veggies for a healthy meal.',
  },
];

const NotificationCard = ({ title, message }) => (
  <View style={styles.card}>
    <Text style={styles.title}>{title}</Text>
    <Text style={styles.message}>{message}</Text>
  </View>
);

const Notification = () => {
  return (
    <View style={styles.container}>
      <BackButton title="Notifications" />
      <View style={styles.listWrapper}>
        <FlatList
          data={notifications}
          keyExtractor={item => item.id}
          renderItem={({ item }) => (
            <NotificationCard title={item.title} message={item.message} />
          )}
          contentContainerStyle={{ paddingBottom: 20 }}
        />
      </View>
    </View>
  );
};

export default Notification;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  listWrapper: {
    marginTop: 20,
    paddingHorizontal: 20,
  },
  card: {
    backgroundColor: '#ffffff',
    padding: 18,
    borderRadius: 12,
    marginBottom: 16,
    marginHorizontal: 3,
    marginTop: 1,
    elevation: 4,
    shadowColor: '#000',
    shadowOpacity: 0.15,
    shadowOffset: { width: 2, height: 4 },
    shadowRadius: 6,
  },
  title: {
    fontWeight: '700',
    fontSize: Fonts.sizes.lg,
    marginBottom: 6,
    color: '#2a6f2a',
  },
  message: {
    fontSize: Fonts.sizes.sm,
    color: '#3a3a3a',
  },
});
