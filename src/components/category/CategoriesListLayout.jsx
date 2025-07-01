import { useRef } from 'react';
import {
  Dimensions,
  FlatList,
  Image,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import categories from '../../../src/HardcodeData/categories.js';
import textStyles from '../../styles/textStyles.js';

const { width } = Dimensions.get('window');
const ITEM_WIDTH = width / 4 -5; 

export default function CategorySlider() {
  const flatListRef = useRef(null);

  const renderItem = ({ item }) => (
    <View style={[styles.category, { width: ITEM_WIDTH }]}>
      <Image source={{ uri: item.image }} style={styles.image} />
      <Text style={[styles.name,textStyles.caption]}>{item.name}</Text>
    </View>
  );

  return (
    <View style={styles.wrapper}>
      <FlatList
        ref={flatListRef}
        data={categories}
        horizontal
        keyExtractor={item => item.id.toString()}
        renderItem={renderItem}
        showsHorizontalScrollIndicator={false}
        scrollEnabled={true} 
        contentContainerStyle={styles.flatList}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  wrapper: {
    flexDirection: 'row',
    marginTop: 20,
  },
 
  category: {
    marginHorizontal: 0,
    paddingVertical: 10,
    alignItems: 'center',
    justifyContent: 'flex-start',
    borderRadius: 10,
    height: 120,
  },
  image: {
    width: 80,
    height: 60,
    resizeMode: 'contain',
  },
  name: {
    fontWeight: '500',
    textAlign: 'center',
    marginTop: 8,
    height: 36,
  },
});
