import AntDesign from 'react-native-vector-icons/AntDesign';
import Ionicons from 'react-native-vector-icons/Ionicons';
import { Dimensions, Pressable, StyleSheet, Text, View } from 'react-native';
import { useSafeRouter } from '../hooks/useSafeRouter.js';
import Colors from '../styles/colors.js';
import Fonts from '../styles/font.js';

const { width } = Dimensions.get('window');

const AddressCard = ({
  item,
  onEdit,
  onDelete,
  onSelect,
  onMarkDefault,
  isSelected,
  hideDelete = false,
  source = 'sidebar', // "cart" | "sidebar"
}) => {
  const { safePush } = useSafeRouter();

  return (
    <Pressable
      onPress={source === 'cart' ? onSelect : undefined}
      style={[styles.card, isSelected && styles.selectedCard]}
    >
      

      <View style={styles.contentContainer}>
        <View style={styles.infoContainer}>
          {item.name && (
            <Text style={styles.nameText} numberOfLines={1}>
              {item.name}
            </Text>
          )}

          <View style={styles.addressLines}>
            <Text style={styles.secondaryText}>
              {[item.address1, item.address2, item.landmark]
                .filter(Boolean)
                .join(', ')}
            </Text>
          </View>

          <Text style={styles.cityLine} numberOfLines={1}>
            {item.city}, {item.state} - {item.pincode}
          </Text>
        </View>

        <View style={styles.actionContainer}>
          {/* === If from Cart: show "Change Address" button === */}
          {source === 'cart' ? (
            <Pressable style={styles.changeAddressBtn} onPress={onSelect}>
              <Text style={styles.changeAddressText}>Change Address</Text>
            </Pressable>
          ) : (
            <View style={styles.btnContainer}>
              <View
                style={{
                  flexDirection: 'row',
                  justifyContent: 'space-between',
                  width:"70"
                }}
              >
                <Pressable
                  style={[styles.editButton, styles.primaryButton]}
                  onPress={() => onEdit(item)}
                >
                  <Ionicons
                    name="create-outline"
                    size={24}
                    color={Colors.primary}
                  />
                </Pressable>

                {!hideDelete && !item.isDefault && (
                  <Pressable
                    style={[styles.editButton, styles.deleteButton]}
                    onPress={() => onDelete(item)}
                  >
                    <AntDesign name="delete" size={24} color={Colors.reject} />
                  </Pressable>
                )}
              </View>

              {!item.isDefault && (
                <Pressable
                  onPress={() => onMarkDefault?.(item.id)}
                  style={styles.defaultBtn}
                >
                  <Text style={styles.defaultBtnText}>Mark as Default</Text>
                </Pressable>
              )}
            </View>
          )}
        </View>
      </View>
    </Pressable>
  );
};

const styles = StyleSheet.create({
  card: {
    marginTop: 16,
    borderRadius: 12,
    backgroundColor: Colors.bgClr,
    borderColor: Colors.borderColor,
    borderWidth: 1,
    elevation: 2,
    padding: 14,
    position: 'relative',
    height:'110'
  },
  selectedCard: {
    borderColor: Colors.primary,
    borderWidth: 2,
  },
  contentContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  infoContainer: {
    flex: 1,
    paddingRight: 10,
  },
  nameText: {
    fontSize: Fonts.sizes.base,
    fontWeight: '700',
    color: Colors.secondary,
    marginBottom: 4,
  },
  addressLines: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 4,
    marginBottom: 4,
  },
  secondaryText: {
    fontSize: Fonts.sizes.sm,
    color: Colors.secondaryText,
  },
  cityLine: {
    fontSize: Fonts.sizes.sm,
    color: Colors.secondaryText,
    marginTop: 2,
  },
  actionContainer: {
    justifyContent: 'center',
    alignItems: 'flex-end',
  },
  btnContainer: {
    justifyContent: 'space-between',
    alignItems:"center",
    gap: 5,
  },
  editButton: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  primaryButton: {
    paddingVertical: 4,
  },
  deleteButton: {
    paddingVertical: 4,
  },
  editText: {
    fontSize: Fonts.sizes.sm,
    fontWeight: '600',
    marginLeft: 6,
    color: Colors.primary,
  },
  defaultBtn: {
    borderWidth: 1.5,
    borderColor: Colors.primary,
    borderRadius: 20,
    paddingHorizontal: 10,
    paddingVertical: 4,
    marginTop: 10,
  },
  defaultBtnText: {
    color: Colors.primary,
    fontSize: Fonts.sizes.sm,
    fontWeight: '600',
  },
  defaultLabel: {
    position: 'absolute',
    top: -12,
    right: 5,
    backgroundColor: Colors.primary,
    paddingHorizontal: 10,
    paddingVertical: 2,
    borderRadius: 12,
    zIndex: 2,
  },
  defaultLabelText: {
    color: Colors.bgClr,
    fontSize: Fonts.sizes.xs,
    fontWeight: '600',
  },
  changeAddressBtn: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 20,
    backgroundColor: Colors.primary,
    marginTop: 4,
  },
  changeAddressText: {
    color: Colors.bgClr,
    fontWeight: '600',
    fontSize: Fonts.sizes.sm,
  },
});

export default AddressCard;
