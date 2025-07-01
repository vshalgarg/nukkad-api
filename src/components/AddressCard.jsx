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
  isSelected,
  actionType = 'edit',
  hideDelete = false,
}) => {
  const { safePush } = useSafeRouter();
  const handleChangeAddress = () => {
    safePush({
      name: 'Address',
      params: { fromCart: 'true' },
    });
    
  };

  return (
    <Pressable
      onPress={onSelect}
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
            {[item.address1, item.address2, item.landmark]
              .filter(Boolean)
              .map((line, index) => (
                <Text
                  key={index}
                  style={styles.secondaryText}
                  numberOfLines={1}
                >
                  {line},
                </Text>
              ))}
          </View>

          <Text style={styles.cityLine} numberOfLines={1}>
            {item.city}, {item.state} - {item.pincode}
          </Text>
        </View>

        <View style={styles.actionContainer}>
          {actionType === 'edit' ? (
            <View style={styles.btnContainer}>
              {!hideDelete && (
                <Pressable
                  style={[styles.editButton, styles.deleteButton]}
                  onPress={() => onDelete(item)}
                >
                  <AntDesign name="delete" size={18} color="red" />
                  <Text style={[styles.editText, { color: 'red' }]}>
                    Delete
                  </Text>
                </Pressable>
              )}
              <Pressable
                style={[styles.editButton, styles.primaryButton]}
                onPress={() => onEdit(item)}
              >
                <Ionicons
                  name="create-outline"
                  size={18}
                  color={Colors.primary}
                />
                <Text style={[styles.editText, { color: Colors.primary }]}>
                  Edit
                </Text>
              </Pressable>
            </View>
          ) : (
            <Pressable
              style={[styles.editButton, styles.primaryButton]}
              onPress={handleChangeAddress}
            >
              <Text style={styles.editText}>Change Address</Text>
            </Pressable>
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
    backgroundColor: '#fff',
    borderColor: '#ddd',
    borderWidth: 1,
    elevation: 2,
    padding: 14,
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
    color: '#222',
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
    color: '#555',
  },
  cityLine: {
    fontSize: Fonts.sizes.sm,
    color: '#444',
    marginTop: 2,
  },
  actionContainer: {
    justifyContent: 'center',
    alignItems: 'flex-end',
  },
  btnContainer: {
    justifyContent: 'space-between',
    gap: 10,
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
});

export default AddressCard;
