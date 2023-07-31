package com.intellicoder.vydeondownloader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.intellicoder.vydeondownloader.databinding.ActivitySplashScreenBinding;
import com.intellicoder.vydeondownloader.utils.LocaleHelper;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        //

        Thread splashThread = new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(2000);

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }catch (InterruptedException e) {

                    e.printStackTrace();


                }

            }
        };
        splashThread.start();

    }
}
