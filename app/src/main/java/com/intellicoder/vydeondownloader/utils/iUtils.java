package com.intellicoder.vydeondownloader.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.intellicoder.vydeondownloader.utils.Constants.PREF_APPNAME;

public class iUtils {
    //private InterstitialAd interstitialAd;


    public static boolean isSameDomain(String url, String url1) {
        return getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
    }

    private static String getRootDomainUrl(String url) {
        String[] domainKeys = url.split("/")[2].split("\\.");
        int length = domainKeys.length;
        int dummy = domainKeys[0].equals("www") ? 1 : 0;
        if (length - dummy == 2)
            return domainKeys[length - 2] + "." + domainKeys[length - 1];
        else {
            if (domainKeys[length - 1].length() == 2) {
                return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1];
            } else {
                return domainKeys[length - 2] + "." + domainKeys[length - 1];
            }
        }
    }


    public static boolean hasMarsallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    public static void admobBannerCall(final AdView adView) {

        // adView.setAdUnitId(bannerid);
        //adView.setAdSize(AdSize.BANNER);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

            }
        });
    }

    public static void facebookBannerCall(Activity activity, final LinearLayout linerlayout, String bannerid) {

        com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(activity);
        adView.setAdUnitId(bannerid);
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(new AdRequest.Builder().build());
        linerlayout.setVisibility(View.GONE);
        linerlayout.addView(adView);
        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                linerlayout.setVisibility(View.VISIBLE);
            }
        });
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static long getRemoteFileSize(String str) {
        try {
            URLConnection str1 = new URL(str).openConnection();
            str1.connect();
            long contentLength = (long) str1.getContentLength();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("file_size = ");
            stringBuilder.append(contentLength);
            Log.e("sasa", stringBuilder.toString());
            return contentLength;
        } catch (Exception str2) {
            str2.printStackTrace();
            return 0;
        }
    }


    public static void tintMenuIcon(Context context, MenuItem item, int color) {
        Drawable drawable = item.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        }
    }


    public static String decryptSoundUrl(String cipherText) throws Exception {

        String iv_key = "asd!@#!@#@!12312";
        String decryp_key = "g@1n!(f1#r.0$)&%";

        IvParameterSpec iv2 = new IvParameterSpec(iv_key.getBytes("UTF-8"));
        SecretKeySpec key2 = new SecretKeySpec(decryp_key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key2, iv2);
        byte[] plainText = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(cipherText));
        } else {

            return "";
        }
        return new String(plainText);
    }


    private void mergeSongs(File mergedFile, File... mp3Files) {
        FileInputStream fisToFinal = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mergedFile);
            fisToFinal = new FileInputStream(mergedFile);
            for (File mp3File : mp3Files) {
                if (!mp3File.exists())
                    continue;
                FileInputStream fisSong = new FileInputStream(mp3File);
                SequenceInputStream sis = new SequenceInputStream(fisToFinal, fisSong);
                byte[] buf = new byte[1024];
                try {
                    for (int readNum; (readNum = fisSong.read(buf)) != -1; )
                        fos.write(buf, 0, readNum);
                } finally {
                    if (fisSong != null) {
                        fisSong.close();
                    }
                    if (sis != null) {
                        sis.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                if (fisToFinal != null) {
                    fisToFinal.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void bookmarkUrl(Context context, String url) {
        SharedPreferences pref = context.getSharedPreferences(PREF_APPNAME, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        // if url is already bookmarked, unbookmark it
        if (pref.getBoolean(url, false)) {
            editor.remove(url).commit();
        } else {
            editor.putBoolean(url, true);
        }

        editor.commit();
    }

    public static boolean isBookmarked(Context context, String url) {
        SharedPreferences pref = context.getSharedPreferences(PREF_APPNAME, 0);
        return pref.getBoolean(url, false);
    }

    public static void ShowToast(Context context, String str) {
        FancyToast.makeText(context, str, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

    }

    public static boolean checkURL(CharSequence input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean isURL = URL_PATTERN.matcher(input).matches();
        if (!isURL) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    isURL = true;
                } catch (Exception e) {
                }
            }
        }
        return isURL;
    }

    public static String getFilenameFromURL(String str) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(new File(new URL(str).getPath()).getName());
            stringBuilder.append("");
            return stringBuilder.toString();
        } catch (Exception str2) {
            str2.printStackTrace();
            return String.valueOf(System.currentTimeMillis());

        }
    }


    public static String getImageFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }

    public static String getVideoFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }


    public static String getStringSizeLengthFile(long j) {
        try {

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            float f = (float) j;
            if (f < 1048576.0f) {
                return decimalFormat.format((double) (f / 1024.0f)) + " Kb";
            } else if (f < 1.07374182E9f) {
                return decimalFormat.format((double) (f / 1048576.0f)) + " Mb";
            } else if (f >= 1.09951163E12f) {
                return "";
            } else {
                return decimalFormat.format((double) (f / 1.07374182E9f)) + " Gb";
            }
        } catch (Exception e) {
            return "NaN";
        }
    }

    public static String getStringSizeLengthFile_onlylong(long j) {
        try {

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            float f = (float) j;
            if (f < 1048576.0f) {
                return decimalFormat.format((double) (f / 1024.0f));
            } else if (f < 1.07374182E9f) {
                return decimalFormat.format((double) (f / 1048576.0f));
            } else if (f >= 1.09951163E12f) {
                return "";
            } else {
                return decimalFormat.format((double) (f / 1.07374182E9f));
            }
        } catch (Exception e) {
            return "0";
        }
    }


    public static String formatDuration(long j) {
        String str;
        String str2;
        long j2 = (j / 1000) % 60;
        long j3 = (j / DefaultLoadErrorHandlingPolicy.DEFAULT_TRACK_BLACKLIST_MS) % 60;
        long j4 = j / 3600000;
        StringBuilder sb = new StringBuilder();
        if (j4 == 0) {
            str = "";
        } else if (j4 < 10) {
            str = String.valueOf(0 + j4);
        } else {
            str = String.valueOf(j4);
        }
        sb.append(str);
        if (j4 != 0) {
            sb.append("h");
        }
        String str3 = "00";
        if (j3 == 0) {
            str2 = str3;
        } else if (j3 < 10) {
            str2 = String.valueOf(0 + j3);
        } else {
            str2 = String.valueOf(j3);
        }
        sb.append(str2);
        sb.append("min");
        if (j2 != 0) {
            if (j2 < 10) {
                str3 = String.valueOf(0 + j2);
            } else {
                str3 = String.valueOf(j2);
            }
        }
        sb.append(str3);
        sb.append("s");
        return sb.toString();
    }


    public static boolean isPackageInstalled(Context context, String packageName) {

        boolean found = true;

        try {

            context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {

            found = false;
        }

        return found;
    }

}
