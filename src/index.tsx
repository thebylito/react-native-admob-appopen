import { NativeModules } from 'react-native';

type AdmobAppopenType = {
  prepareAppOpenAd(adUnitId: string): Promise<void>;
};

const { AdmobAppOpen } = NativeModules;

export default AdmobAppOpen as AdmobAppopenType;
