package com.appdev360.jobsitesentry.util;

/**
 * Created by abubaker on 3/21/18.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.appdev360.jobsitesentry.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by sonu on 23/03/17.
 */

public class ScreenshotUtils {


    /*  Store taken screenshot into above created path  */
    public static File store(Context context, Bitmap bm) {
        File file = null;
        try {
            file = createImageFile(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            galleryAddPic(context, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static File createImageFile(Context context) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();
        File appFolder = new File(storageDir, "JobSiteSentry");
        if (!appFolder.exists()) {
            appFolder.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                appFolder      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //  mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private static void galleryAddPic(Context context, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);

    }
}
