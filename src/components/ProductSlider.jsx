import React, { useEffect, useRef } from 'react';
import {
  Animated,
  Dimensions,
  FlatList,
  Text,
  View,
  StyleSheet,
} from 'react-native';
import FruitBasket from '../../assets/images/fruit-basket.svg';
import Fonts from '../styles/font';

const { width: screenWidth } = Dimensions.get('window');

const peekPercent = 0.05;
const gapPercent = 0.025;
const itemWidth = screenWidth * 0.85;
const sidePeek = screenWidth * peekPercent;
const sideGap = screenWidth * gapPercent;
const fullItemSpace = itemWidth + sideGap * 2;

const originalSlides = [
  {
    id: '1',
    title: 'Enjoy the special offer upto 30%',
    subtitle: 'From 14th June, 2022',
    ImageComponent: FruitBasket,
    backgroundColor: '#D6A937',
  },
  {
    id: '2',
    title: 'New Arrivals',
    subtitle: 'Trendy Collection',
    ImageComponent: FruitBasket,
    backgroundColor: '#F69F8B',
  },
  {
    id: '3',
    title: 'Festive Offers',
    subtitle: 'Buy 1 Get 1',
    ImageComponent: FruitBasket,
    backgroundColor: '#005942',
  },
];

const slides = [
  originalSlides[originalSlides.length - 1],
  ...originalSlides,
  originalSlides[0],
];

export default function AutoSlider() {
  const flatListRef = useRef(null);
  const scrollX = useRef(new Animated.Value(0)).current;
  const indexRef = useRef(1);
  const autoSlideTimer = useRef(null);

  const scrollToIndex = (index, animated = true) => {
    if (flatListRef.current) {
      flatListRef.current.scrollToOffset({
        offset: index * fullItemSpace,
        animated,
      });
    }
  };

  const startAutoSlide = () => {
    stopAutoSlide(); // clear previous timer
    autoSlideTimer.current = setInterval(() => {
      indexRef.current += 1;
      scrollToIndex(indexRef.current);
    }, 3000);
  };

  const stopAutoSlide = () => {
    if (autoSlideTimer.current) {
      clearInterval(autoSlideTimer.current);
    }
  };

  useEffect(() => {
    startAutoSlide();
    return () => stopAutoSlide();
  }, []);

  const onMomentumScrollEnd = e => {
    const offsetX = e.nativeEvent.contentOffset.x;
    let index = Math.round(offsetX / fullItemSpace);

    if (index === 0) {
      indexRef.current = originalSlides.length;
      scrollToIndex(indexRef.current, false);
    } else if (index === slides.length - 1) {
      indexRef.current = 1;
      scrollToIndex(indexRef.current, false);
    } else {
      indexRef.current = index;
    }
  };

  return (
    <View style={{ marginTop: 20 }}>
      <Animated.FlatList
        ref={flatListRef}
        data={slides}
        horizontal
        keyExtractor={(_, index) => index.toString()}
        renderItem={({ item }) => (
          <View
            style={[
              styles.slideContainer,
              {
                width: itemWidth,
                marginHorizontal: sideGap,
                backgroundColor: item.backgroundColor,
              },
            ]}
          >
            <View style={styles.textContainer}>
              <Text style={styles.title}>{item.title}</Text>
              <Text style={styles.subtitle}>{item.subtitle}</Text>
            </View>
            <FruitBasket width={100} height={100} />
          </View>
        )}
        showsHorizontalScrollIndicator={false}
        snapToInterval={fullItemSpace}
        decelerationRate="fast"
        contentContainerStyle={{ paddingHorizontal: sidePeek }}
        onScroll={Animated.event(
          [{ nativeEvent: { contentOffset: { x: scrollX } } }],
          { useNativeDriver: false },
        )}
        onMomentumScrollEnd={onMomentumScrollEnd}
        initialScrollIndex={1}
        getItemLayout={(_, index) => ({
          length: fullItemSpace,
          offset: fullItemSpace * index,
          index,
        })}
        initialNumToRender={3}
        windowSize={5}
        removeClippedSubviews
      />

      <View style={styles.indicatorContainer}>
        {originalSlides.map((_, i) => {
          const inputRange = [
            (i + 1 - 1) * fullItemSpace,
            (i + 1) * fullItemSpace,
            (i + 1 + 1) * fullItemSpace,
          ];

          const dotWidth = scrollX.interpolate({
            inputRange,
            outputRange: [16, 32, 16],
            extrapolate: 'clamp',
          });

          const dotColor = scrollX.interpolate({
            inputRange,
            outputRange: ['#D1D5DB', '#3B82F6', '#D1D5DB'],
            extrapolate: 'clamp',
          });

          return (
            <Animated.View
              key={i}
              style={[
                styles.indicatorDot,
                {
                  width: dotWidth,
                  backgroundColor: dotColor,
                },
              ]}
            />
          );
        })}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  slideContainer: {
    borderRadius: 16,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    height: 200,
  },
  textContainer: {
    flex: 1,
  },
  title: {
    color: 'white',
    fontSize: Fonts.sizes.lg,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  subtitle: {
    color: 'white',
    fontSize: Fonts.sizes.sm,
  },
  indicatorContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginTop: 8,
    zIndex: 10,
  },
  indicatorDot: {
    height: 4,
    borderRadius: 4,
    marginHorizontal: 4,
  },
  activeDot: {
    backgroundColor: '#3B82F6',
    width: 32,
  },
  inactiveDot: {
    backgroundColor: '#D1D5DB',
    width: 16,
  },
});