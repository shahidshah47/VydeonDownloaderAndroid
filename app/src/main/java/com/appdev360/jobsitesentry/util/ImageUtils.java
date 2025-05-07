package com.appdev360.jobsitesentry.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.appdev360.jobsitesentry.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImageUtils {
    static Target target;

    public static void loadImage(Context context, String url, ImageView imageView) {
        if (url == null || url.length() == 0 || url.equals("null")) {
            return;
        }
        Picasso.get()
                .load(url)
                .fit()
                .centerCrop().into(imageView);
    }

    public static void loadImage(Context context, String url,int width,int height, ImageView imageView,Callback callback) {
        if (url == null || url.length() == 0 || url.equals("null")) {
            return;
        }
        Picasso.get()
                .load(url)
                .resize(width,height)
                .centerCrop().into(imageView,callback);
    }

    public static void loadImageLocally(Context context, String url ,ImageView imageView) {
        Picasso.get().load("file://" + url).fit().centerCrop().into(imageView);
    }

    public static void loadImageLocally(Context context, String url,int width, int height ,ImageView imageView) {
        Picasso.get().load("file://" + url).resize(width,height).centerCrop().into(imageView);
    }

    public static void loadResizedImage(Context context, String url, ImageView imageView, int reqWidth, int reqHeight) {
        if (url == null || url.length() == 0 || url.equals("null")) {
            return;
        }
        Picasso.get().load(url).resize(reqWidth, reqHeight).into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView, int radius, int margin) {
        if (url == null || url.length() == 0 || url.equals("null")) {
            return;
        }
        Picasso.get()
                .load(url).transform(new RoundedTransformation(radius, margin))
                .into(imageView);
    }

    public static class RoundedTransformation implements com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin;  // dp

        // radius is corner radii in dp
        // margin is the board in dp
        public RoundedTransformation(final int radius, final int margin) {
            this.radius = radius;
            this.margin = margin;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);
            if (source != output) {
                source.recycle();
            }
            return output;
        }

        @Override
        public String key() {
            return "rounded";
        }
    }

    public static void loadImageFromServer(Context context, String url, final OnImageLoadListener listener) {
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                listener.onBitmapLoaded(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                listener.onError();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        Picasso.get().load(url).into(target);
    }

    public static void loadResizedImageFromServer(Context context, String url, ImageView view, int reqWidth, int
            reqHeight, final Callback callback) {
        Picasso.get().cancelRequest(view);
        Picasso.get()
                .load(url)
                .resize(reqWidth,reqHeight)
                .placeholder(context.getResources().getDrawable(R.drawable.profile_pic_dummy))
                .centerCrop().
                into(view, callback);
    }


    public static void loadBackgroundImage(Context context, String url,int width , int height ,ImageView view, final
    Callback callback) {
        Picasso.get()
                .load(url)
                .resize(width,height)
                .centerCrop()
                //.fit()
                .into(view, callback);
    }


    public static void loadBackgroundImage(Context context, String url ,ImageView view, final
    Callback callback) {
        Picasso.get()
                .load(url)
                // .resize(width,height)
                // .centerCrop()
                //.centerInside()
               .fit()
                .into(view, callback);
    }

    public interface OnImageLoadListener {
        public void onBitmapLoaded(Bitmap bmp);

        public void onError();
    }
}