package com.intellicoder.vydeondownloader;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.intellicoder.vydeondownloader.utils.AppOpenManager;
import com.intellicoder.vydeondownloader.utils.LocaleHelper;
import com.onesignal.OneSignal;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.util.Locale;

public class Appcontroller extends MultiDexApplication {

    AppOpenManager appOpenManager;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }


    @Override
    public void onCreate() {
        super.onCreate();

//        MobileAds.initialize(
//                this,
//                new OnInitializationCompleteListener() {
//                    @Override
//                    public void onInitializationComplete(InitializationStatus initializationStatus) {
//                    }
//                });


      //  appOpenManager = new AppOpenManager(this);


      //  StartAppSDK.init(this, getString(R.string.startapp_app_id), false);
      //  StartAppAd.disableSplash();

     //   OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
       // OneSignal.initWithContext(this);
      //  OneSignal.setAppId(getString(R.string.onsignalappid));

       // AudienceNetworkAds.initialize(this);

        SharedPreferences prefs = getSharedPreferences("lang_pref", MODE_PRIVATE);
        // System.out.println("qqqqqqqqqqqqqqqqq = "+Locale.getDefault().getLanguage());

        String lang = prefs.getString("lang", Locale.getDefault().getLanguage());//"No name defined" is the default value.

//
//        List<Locale> locales = new ArrayList<>();
//        locales.add(Locale.ENGLISH);
//        locales.add(new Locale("ar", "ARABIC"));
//        locales.add(new Locale("ur", "URDU"));
//        locales.add(new Locale("tr", "Turkish"));
//        locales.add(new Locale("hi", "Hindi"));
        LocaleHelper.setLocale(getApplicationContext(), lang);

    }
}
