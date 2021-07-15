package com.reactnativeadmobappopen;


import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

/**
 * Prefetches App Open Ads.
 */
public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
  private Activity currentActivity;

  private static final String LOG_TAG = "AppOpenManager";
  private String AD_UNIT_ID;
  private AppOpenAd appOpenAd = null;

  private AppOpenAd.AppOpenAdLoadCallback loadCallback;

  private ReactApplicationContext myApplication;

  public void setAdUnitId(String adUnitId) {
    this.AD_UNIT_ID = adUnitId;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      this.myApplication.getCurrentActivity().registerActivityLifecycleCallbacks(this);
    }
    ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    this.fetchAd();
  }

  /**
   * LifecycleObserver methods
   */
  @OnLifecycleEvent(ON_START)
  public void onStart() {
    showAdIfAvailable();
    Log.d(LOG_TAG, "onStart");
  }

  /**
   * ActivityLifecycleCallback methods
   */
  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
  }

  @Override
  public void onActivityStarted(Activity activity) {
    currentActivity = activity;
  }

  @Override
  public void onActivityResumed(Activity activity) {
    currentActivity = activity;
  }

  @Override
  public void onActivityStopped(Activity activity) {
  }

  @Override
  public void onActivityPaused(Activity activity) {
  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
  }

  @Override
  public void onActivityDestroyed(Activity activity) {
    currentActivity = null;
  }

  /**
   * Constructor
   */
  public AppOpenManager(ReactApplicationContext myApplication) {
    this.myApplication = myApplication;
  }

  /**
   * Request an ad
   */
  public void fetchAd() {
    // Have unused ad, no need to fetch another.
    if (isAdAvailable()) {
      return;
    }

    loadCallback =
      new AppOpenAd.AppOpenAdLoadCallback() {
        /**
         * Called when an app open ad has loaded.
         *
         * @param ad the loaded app open ad.
         */
        @Override
        public void onAdLoaded(AppOpenAd ad) {
          AppOpenManager.this.appOpenAd = ad;
        }

        /**
         * Called when an app open ad has failed to load.
         *
         * @param loadAdError the error.
         */
        @Override
        public void onAdFailedToLoad(LoadAdError loadAdError) {
          // Handle the error.
        }

      };
    AdRequest request = getAdRequest();
    AppOpenAd.load(
      myApplication, AD_UNIT_ID, request,
      AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
  }

  private static boolean isShowingAd = false;

  /**
   * Shows the ad if one isn't already showing.
   */
  public void showAdIfAvailable() {
    // Only show ad if there is not already an app open ad currently showing
    // and an ad is available.
    if (!isShowingAd && isAdAvailable()) {
      Log.d(LOG_TAG, "Will show ad.");

      FullScreenContentCallback fullScreenContentCallback =
        new FullScreenContentCallback() {
          @Override
          public void onAdDismissedFullScreenContent() {
            // Set the reference to null so isAdAvailable() returns false.
            AppOpenManager.this.appOpenAd = null;
            isShowingAd = false;
            fetchAd();
          }

          @Override
          public void onAdFailedToShowFullScreenContent(AdError adError) {
          }

          @Override
          public void onAdShowedFullScreenContent() {
            isShowingAd = true;
          }
        };

      appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
      appOpenAd.show(currentActivity);

    } else {
      Log.d(LOG_TAG, "Can not show ad.");
      fetchAd();
    }
  }

  /**
   * Creates and returns ad request.
   */
  private AdRequest getAdRequest() {
    return new AdRequest.Builder().build();
  }

  /**
   * Utility method that checks if ad exists and can be shown.
   */
  public boolean isAdAvailable() {
    return appOpenAd != null;
  }
}
