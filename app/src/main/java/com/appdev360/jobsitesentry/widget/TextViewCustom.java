package com.appdev360.jobsitesentry.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.appdev360.jobsitesentry.R;


public class TextViewCustom extends androidx.appcompat.widget.AppCompatTextView {


    public TextViewCustom(Context context) {
        super(context);
    }

    public TextViewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    public TextViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs){

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TextViewCustom,
                0, 0);

        try {


           String ttfName = a.getString(R.styleable.TextViewCustom_ttfName);
            if(ttfName!=null) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), "Fonts/" + ttfName + ".otf");
                setTypeface(font);
            }


        } finally {
            a.recycle();
        }

    }

    private boolean exists(Context context, String ttfName){
        boolean result = false;

        return result;
    }
}
