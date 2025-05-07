package com.appdev360.jobsitesentry.application;

import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.appdev360.jobsitesentry.data.services.TokenValidityService;
import com.appdev360.jobsitesentry.injection.component.ApplicationComponent;
import com.appdev360.jobsitesentry.injection.component.DaggerApplicationComponent;
import com.appdev360.jobsitesentry.injection.module.ApplicationModule;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by abubaker on 3/12/18.
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class JobSiteSentry extends Application {


    ApplicationComponent mApplicationComponent;
    private static boolean appRunning = false;
    private CountDownTimer countDownTimer;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        startTimer();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public static boolean isAppRunning() {
        return appRunning;
    }

    public static void setAppRunning(boolean appRunning) {
        JobSiteSentry.appRunning = appRunning;
    }

    public static JobSiteSentry get(Context context) {
        return (JobSiteSentry) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    /**
     * Timer runs every 1 minute to hit a web request to check token validity
     */
    public void startTimer() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 60000) {
            public void onTick(long millisUntilFinished) {
                Log.i("Test321", "Timer Called after 1 minute");
                if (isAppRunning()) {
                    TokenValidityService.startTokenValidity(getApplicationContext());
                }
            }

            public void onFinish() {
                Log.i("Test321", "CountDown timer cancelled");
                start();
            }
        }.start();
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
