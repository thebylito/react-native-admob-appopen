import React from 'react';
import { AppState, AppStateStatus, NativeModules } from 'react-native';

type AdmobAppopenType = {
  prepareAppOpenAd(adUnitId: string): Promise<void>;
  showAd(): Promise<void>;
};

const AdmobAppOpenModule: AdmobAppopenType = NativeModules.AdmobAppopen;

function useAppOpenAds(adUnitId: string) {
  const appState = React.useRef(AppState.currentState);
  React.useEffect(() => {
    AdmobAppOpenModule.prepareAppOpenAd(adUnitId);
    AppState.addEventListener('change', _handleAppStateChange);
    return () => {
      AppState.removeEventListener('change', _handleAppStateChange);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const _handleAppStateChange = (nextAppState: AppStateStatus) => {
    if (
      appState.current.match(/inactive|background/) &&
      nextAppState === 'active'
    ) {
      AdmobAppOpenModule.showAd();
    }
    appState.current = nextAppState;
  };
}

export default useAppOpenAds;
