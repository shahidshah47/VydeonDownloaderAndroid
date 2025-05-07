package com.appdev360.jobsitesentry.util;

import android.content.Context;
import android.widget.Toast;

public class MyToast {

    public static void showMessage(Context mContext, String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
}
