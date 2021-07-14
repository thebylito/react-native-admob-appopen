import { NativeModules } from 'react-native';

type AdmobAppopenType = {
  multiply(a: number, b: number): Promise<number>;
};

const { AdmobAppopen } = NativeModules;

export default AdmobAppopen as AdmobAppopenType;
