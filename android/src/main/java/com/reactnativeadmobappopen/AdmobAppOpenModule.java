package com.reactnativeadmobappopen;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

@ReactModule(name = AdmobAppOpenModule.NAME)
public class AdmobAppOpenModule extends ReactContextBaseJavaModule {
  public static final String NAME = "AdmobAppOpen";
  private static AppOpenManager appOpenManager;

  public AdmobAppOpenModule(ReactApplicationContext reactContext) {
    super(reactContext);
    MobileAds.initialize(
      reactContext,
      new OnInitializationCompleteListener() {
        @Override
        public void onInitializationComplete(InitializationStatus initializationStatus) {
        }
      });

    appOpenManager = new AppOpenManager(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
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
}
