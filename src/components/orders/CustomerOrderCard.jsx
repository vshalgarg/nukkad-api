import { View, Text, StyleSheet, TouchableOpacity, Alert } from "react-native";
import { useStore } from "../../contexts/storeContext";
import { useDispatch } from "react-redux";
import { addToCart, clearCart } from "../../store/cartSlice";
import { useSafeRouter } from "../../hooks/useSafeRouter";
import Fonts from "../../styles/font";
import Colors from "../../styles/colors";

const CustomerOrderCard = ({ order, onPress, isExpanded, expandedView }) => {
  const {safePush}=useSafeRouter();
  const dispatch = useDispatch();
  const storeData = useStore();
  const shopName = storeData.storeData?.shopName;
  const formattedDate = new Date(order.date).toLocaleDateString();

  const handleRepeatOrder = () => {
    if (!order.items || order.items.length === 0) {
      Alert.alert("No items to reorder");
      return;
    }
    dispatch(clearCart());

    order.items.forEach((item) => {
      dispatch(
        addToCart({
          product: {
            ...item.product,
            selectedUnit: item.selectedUnit,
            amount: item.product.amount,
          },
          cartQuantity: item.quantity,
        })
      );
    });

    safePush("ShoppingCart");
  };

  const totalQuantity = order.items?.reduce((sum, item) => {
    if (item.selectedUnit === "pkt") {
      return sum + Number(item.product.amount || 0);
    } else {
      return sum + 1;
    }
  }, 0);

  return (
    <TouchableOpacity onPress={onPress} activeOpacity={0.9} style={styles.card}>
      <View style={styles.rowBetween}>
        <Text style={styles.orderId}>Order ID: {order.id}</Text>
        <Text style={styles.date}>{formattedDate}</Text>
      </View>

      <Text style={styles.name}>Store: {shopName}</Text>

      <View style={styles.rowBetween}>
        <Text style={styles.quantity}>Quantity: {totalQuantity}</Text>
        <TouchableOpacity onPress={handleRepeatOrder}>
          <Text style={styles.repeat}>Repeat Order</Text>
        </TouchableOpacity>
      </View>

      {/* Expanded Content */}
      {isExpanded && <View style={styles.expanded}>{expandedView}</View>}
    </TouchableOpacity>
  );
};

export default CustomerOrderCard;

const styles = StyleSheet.create({
  card: {
    backgroundColor: Colors.bgClr,
    borderRadius: 12,
    padding: 16,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: Colors.borderColor,
    shadowColor: Colors.bgClr,
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
    color: Colors.primary,
  },
  name: {
    marginBottom: 6,
    fontWeight: "500",
  },
  quantity: {
    fontSize: Fonts.sizes.base,
  },
  repeat: {
    color: Colors.primary,
    fontWeight: "600",
  },
  expanded: {
    marginTop: 10,
    borderTopWidth: 1,
    borderTopColor: Colors.borderColor,
    paddingTop: 10,
  },
});
