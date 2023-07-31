package com.intellicoder.vydeondownloader.facebookstorysaver.fbutils;

import android.text.TextUtils;

public class FBhelper {
    public static boolean valadateCooki(String str) {
        return !TextUtils.isEmpty(str) && str.contains("c_user");
    }
}
