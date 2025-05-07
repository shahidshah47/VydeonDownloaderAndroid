package com.appdev360.jobsitesentry.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;



public class UtilSnackbar {

    /************************************Snackbar with success case*****************************/
    public static void showSnakbarSuccess(View rootView, String mMessage) {
        if (rootView != null) {
            Snackbar snackbar = Snackbar.make(rootView, mMessage, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.parseColor("#006400"));
            snackbar.show();
        }
    }

    /************************************Snackbar with Error case*****************************/
    public static void showSnakbarError(View rootView, String mMessage) {
        if (rootView != null) {
            Snackbar snackbar = Snackbar.make(rootView, mMessage, Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            View sbView = snackbar.getView();
           /* FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)sbView.getLayoutParams();
            params.gravity = Gravity.TOP;*/
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }

    /************************************ ShowSnackbar with eventType, KeepItDisplayedOnScreen for few seconds*****************************/
    public static void showSnakbarTypeOne(View rootView, String mMessage) {
        Snackbar.make(rootView, mMessage, Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .show();
    }


    /************************************ ShowSnackbar with eventType, KeepItDisplayedOnScreen for few seconds*****************************/
    public static void showSnakbarWithListener(View rootView, String mMessage, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(rootView, mMessage, Snackbar.LENGTH_LONG)
                .setAction("Switch On", listener)
                .setActionTextColor(Color.GREEN);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.RED);
        snackbar.show();
    }


    /************************************ ShowSnackbar with eventType, KeepItDisplayedOnScreen*****************************/
    public static void showSnakbarTypeTwo(View rootView, String mMessage) {

        Snackbar.make(rootView, mMessage, Snackbar.LENGTH_LONG)
                .make(rootView, mMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null)
                .show();

    }

    /************************************ ShowSnackbar without eventType, KeepItDisplayedOnScreen, OnClickOfOk restrat the activity*****************************/
    public static void showSnakbarTypeThree(View rootView, final Activity activity) {

        Snackbar
                .make(rootView, "NoInternetConnectivity", Snackbar.LENGTH_INDEFINITE)
                .setAction("TryAgain", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = activity.getIntent();
                        activity.finish();
                        activity.startActivity(intent);
                    }
                })
                .setActionTextColor(Color.CYAN)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                    }
                })
                .show();

    }

    /************************************ ShowSnackbar with eventType, KeepItDisplayedOnScreen, OnClickOfOk restrat the activity*****************************/
    public static void showSnakbarTypeFour(View rootView, final Activity activity, String mMessage) {

        Snackbar
                .make(rootView, mMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction("TryAgain", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = activity.getIntent();
                        activity.finish();
                        activity.startActivity(intent);
                    }
                })
                .setActionTextColor(Color.CYAN)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                    }
                })
                .show();

    }
}