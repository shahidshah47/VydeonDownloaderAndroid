package com.appdev360.jobsitesentry.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import android.util.DisplayMetrics;
import android.view.Display;

import com.appdev360.jobsitesentry.BuildConfig;
import com.appdev360.jobsitesentry.constant.GeneralConstants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abubaker on 1/23/18.
 * <p>
 * Updated By Saad on 27/01/22
 */

public class GeneralUtils {

    static String mCurrentPhotoPath, mCurrentVideoPath;

    public static void restartApp(Context context) {
        Intent i = context.getPackageManager().
                getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        ((Activity) context).finish();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnectedOrConnecting();
        }
    }

    public static int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int pxToDp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.heightPixels;
    }

    public static void dispatchTakePictureIntent(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
         /*   File photoFile = null;
            try {
                photoFile = createImageFile(context);
            } catch (IOException ex) {
                // Error occurred while creating the File

            }*/
            // Continue only if the File was successfully created
            try {
                Uri photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile(context));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            ((Activity) context).startActivityForResult(takePictureIntent, GeneralConstants.TAKE_PHOTO_CODE);
        }
    }

    public static void readFileFromGallery(Context context) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Choose File"), GeneralConstants.SELECT_FILE);
    }

    private static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();
        File appFolder = new File(storageDir, "");
        if (!appFolder.exists()) {
            appFolder.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                appFolder      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public static String convert24hrTo12hr(String time) {
        String convertedTime = "";
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(time);
            System.out.println(_24HourDt);

            convertedTime = _12HourSDF.format(_24HourDt);
            System.out.println(convertedTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedTime;
    }
}
