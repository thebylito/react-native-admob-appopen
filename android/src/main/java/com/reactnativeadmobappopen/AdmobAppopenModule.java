package com.reactnativeadmobappopen;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

@ReactModule(name = AdmobAppopenModule.NAME)
public class AdmobAppopenModule extends ReactContextBaseJavaModule {
  public static final String NAME = "AdmobAppopen";
  private static AppOpenManager appOpenManager;

  public AdmobAppopenModule(ReactApplicationContext reactContext) {
    super(reactContext);
    MobileAds.initialize(
      reactContext,
      new OnInitializationCompleteListener() {
        @Override
        public void onInitializationComplete(InitializationStatus initializationStatus) {
        }
      });

    appOpenManager = new AppOpenManager(reactContext);

//    if(reactContext != null){
//      if(reactContext.getCurrentActivity() != null){
//        if(reactContext.getCurrentActivity().getApplication() != null){
//          appOpenManager = new AppOpenManager(reactContext.getCurrentActivity().getApplication());
//          Log.d("hue", "1");
//        }
//        Log.d("hue", "2");
//      }
//      Log.d("hue", "3");
//    }
//    Log.d("hue", "4");

  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void showAd(Promise promise) {

    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        appOpenManager.showAdIfAvailable();
      }
    });
    promise.resolve(true);
  }

  @ReactMethod
  public void prepareAppOpenAd(String adUnitId, Promise promise) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        appOpenManager.setAdUnitId(adUnitId);
      }
    });
    promise.resolve(true);
  }

  public static native int nativeMultiply(int a, int b);
}
