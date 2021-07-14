import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import useAppOpenAds from 'react-native-admob-appopen';

export default function App() {
  useAppOpenAds('ca-app-pub-3940256099942544/3419835294');

  return (
    <View style={styles.container}>
      <Text>:)</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
