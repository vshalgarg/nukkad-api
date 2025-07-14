import React, { useEffect, useRef } from 'react';
import {
  Animated,
  Dimensions,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import FruitBasket from '../../assets/images/fruit-basket.svg';
import Fonts from '../styles/font';
import Colors from '../styles/colors';

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
    backgroundColor: '#D6A937',
  },
  {
    id: '2',
    title: 'New Arrivals',
    subtitle: 'Trendy Collection',
    backgroundColor: '#F69F8B',
  },
  {
    id: '3',
    title: 'Festive Offers',
    subtitle: 'Buy 1 Get 1',
    backgroundColor: '#005942',
  },
];

// Cloned first/last slides for infinite loop illusion
const slides = [
  originalSlides[originalSlides.length - 1],
  ...originalSlides,
  originalSlides[0],
];

export default function AutoSlider() {
  const scrollViewRef = useRef(null);
  const scrollX = useRef(new Animated.Value(fullItemSpace)).current;
  const indexRef = useRef(1);
  const timerRef = useRef(null);

  const scrollToIndex = (index, animated = true) => {
    const x = index * fullItemSpace;
    if (scrollViewRef.current) {
      scrollViewRef.current.scrollTo({ x, animated });
    }
  };

  const startAutoScroll = () => {
    stopAutoScroll();
    timerRef.current = setInterval(() => {
      indexRef.current += 1;
      scrollToIndex(indexRef.current);
    }, 3500);
  };

  const stopAutoScroll = () => {
    if (timerRef.current) {
      clearInterval(timerRef.current);
    }
  };

  useEffect(() => {
    scrollToIndex(indexRef.current, false);
    startAutoScroll();
    return stopAutoScroll;
  }, []);

  const handleScrollEnd = e => {
    const offsetX = e.nativeEvent.contentOffset.x;
    const index = Math.round(offsetX / fullItemSpace);

    // Loop handling
    if (index === 0) {
      indexRef.current = originalSlides.length;
      setTimeout(() => {
        scrollToIndex(indexRef.current, false);
      }, 20);
    } else if (index === slides.length - 1) {
      indexRef.current = 1;
      setTimeout(() => {
        scrollToIndex(indexRef.current, false);
      }, 20);
    } else {
      indexRef.current = index;
    }
  };

  return (
    <View style={{ marginTop: 20 }}>
      <Animated.ScrollView
        ref={scrollViewRef}
        horizontal
        pagingEnabled={false}
        scrollEventThrottle={16}
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={{ paddingHorizontal: sidePeek }}
        snapToInterval={fullItemSpace}
        decelerationRate="fast"
        onScroll={Animated.event(
          [{ nativeEvent: { contentOffset: { x: scrollX } } }],
          { useNativeDriver: false },
        )}
        onMomentumScrollEnd={handleScrollEnd}
      >
        {slides.map((item, index) => (
          <View
            key={index.toString()}
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
            <FruitBasket width={160} height={160} />
          </View>
        ))}
      </Animated.ScrollView>

      {/* Indicators */}
      <View style={styles.indicatorContainer}>
        {originalSlides.map((_, i) => {
          const inputRange = [
            (i + 1 - 1) * fullItemSpace,
            (i + 1) * fullItemSpace,
            (i + 1 + 1) * fullItemSpace,
          ];

          const width = scrollX.interpolate({
            inputRange,
            outputRange: [8, 24, 8],
            extrapolate: 'clamp',
          });

          const backgroundColor = scrollX.interpolate({
            inputRange,
            outputRange: ['#D1D5DB', Colors.primary, '#D1D5DB'],
            extrapolate: 'clamp',
          });

          return (
            <Animated.View
              key={i}
              style={[
                styles.indicatorDot,
                {
                  width,
                  backgroundColor,
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
    elevation: 3,
  },
  textContainer: {
    flex: 1,
    marginRight: 10,
  },
  title: {
    color: Colors.bgClr,
    fontSize: Fonts.sizes.lg,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  subtitle: {
    color: Colors.bgClr,
    fontSize: Fonts.sizes.sm,
  },
  indicatorContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginTop: 10,
  },
  indicatorDot: {
    height: 4,
    borderRadius: 4,
    marginHorizontal: 4,
  },
});
