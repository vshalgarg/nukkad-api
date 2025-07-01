import { View, Text, StyleSheet, TouchableOpacity } from "react-native";
import Fonts from "../../styles/font";

const StorekeeperOrderCard = ({ order, onPress, isExpanded, expandedView }) => {
  const getStatusColor = (status) => {
    switch (status.toLowerCase()) {
      case "pending":
        return "#e74c3c";
      case "in progress":
        return "#e67e22";
      case "completed":
        return "#27ae60";
      case "cancelled":
        return "#c0392b";
      default:
        return "#555";
    }
  };

  return (
    <TouchableOpacity onPress={onPress} activeOpacity={0.9}>
      <View style={styles.card}>
        <View style={styles.rowBetween}>
          <Text style={styles.orderId}>Order ID: {order.orderId}</Text>
          <Text style={styles.date}>{order.date}</Text>
        </View>

        <Text style={styles.name}>Customer: {order.customerName}</Text>

        <View style={styles.rowBetween}>
          <Text style={styles.quantity}>Quantity: {order.quantity}</Text>
          <Text
            style={[styles.status, { color: getStatusColor(order.status) }]}
          >
            {order.status}
          </Text>
        </View>

        {/* Expanded view */}
        {isExpanded && <View style={styles.expanded}>{expandedView}</View>}
      </View>
    </TouchableOpacity>
  );
};

export default StorekeeperOrderCard;

const styles = StyleSheet.create({
  card: {
    backgroundColor: "#fff",
    borderRadius: 12,
    padding: 16,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: "#ddd",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  rowBetween: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 6,
  },
  orderId: {
    fontWeight: "600",
  },
  date: {
    color: "#666",
  },
  name: {
    marginBottom: 6,
    fontWeight: "500",
  },
  quantity: {
    fontSize: Fonts.sizes.base,
  },
  status: {
    fontWeight: "600",
  },
  expanded: {
    marginTop: 10,
    borderTopWidth: 1,
    borderTopColor: "#eee",
    paddingTop: 10,
  },
});
