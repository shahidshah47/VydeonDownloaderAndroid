package com.intellicoder.vydeondownloader.extraFeatures.videolivewallpaper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.intellicoder.vydeondownloader.R;
import com.intellicoder.vydeondownloader.databinding.ActivityMainLivevideoBinding;
import com.intellicoder.vydeondownloader.utils.Constants;
import com.intellicoder.vydeondownloader.utils.LocaleHelper;
import com.intellicoder.vydeondownloader.utils.iUtils;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class MainActivityLivewallpaper extends AppCompatActivity {
    public CinimaWallService cinimaService;
    public InterstitialAd mInterstitialAd;

    private String url = null;
    BillingProcessor bp;
    private ActivityMainLivevideoBinding binding;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityMainLivevideoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        bp = new BillingProcessor(this, getString(R.string.playlisencekey), new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails details) {

            }

            @Override
            public void onPurchaseHistoryRestored() {

            }

            @Override
            public void onBillingError(int errorCode, Throwable error) {

            }

            @Override
            public void onBillingInitialized() {

            }
        });
        bp.initialize();


        binding.spinKit.setVisibility(View.GONE);
        cinimaService = new CinimaWallService();
        binding.checkboxSound.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                cinimaService.setEnableVideoAudio(MainActivityLivewallpaper.this, binding.checkboxSound.isChecked());
            }
        });
        binding.checkboxPlayBegin.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                cinimaService.setPlayB(MainActivityLivewallpaper.this, binding.checkboxPlayBegin.isChecked());
            }
        });
        binding.checkboxBattery.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                cinimaService.setPlayBatterySaver(MainActivityLivewallpaper.this, binding.checkboxBattery.isChecked());
            }
        });

        if (Constants.show_admobads) {
            if (!bp.isPurchased(getString(R.string.productidcode))) {

                iUtils.admobBannerCall((AdView) findViewById(R.id.adView));
            }
        }

        if (Constants.show_admobads) {
            if (!bp.isPurchased(getString(R.string.productidcode))) {
                mInterstitialAd = new InterstitialAd(MainActivityLivewallpaper.this);
                mInterstitialAd.setAdUnitId(getResources().getString(R.string.AdmobInterstitial));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdClosed() {
                        super.onAdClosed();
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                });
            }

        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 53213 && i2 == -1) {
            ArrayList stringArrayListExtra = intent.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            for (int i3 = 0; i3 < stringArrayListExtra.size(); i3++) {
                Log.e("Path", (String) stringArrayListExtra.get(i3));
                this.url = (String) stringArrayListExtra.get(i3);
                binding.spinKit.setVisibility(View.VISIBLE);
                binding.videoSelectButton.setVisibility(View.GONE);
                Glide.with(this).asBitmap().addListener(new RequestListener<Bitmap>() {
                    public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
                        binding.spinKit.setVisibility(View.GONE);
                        binding.videoSelectButton.setVisibility(View.VISIBLE);
                        return false;
                    }

                    public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
                        binding.spinKit.setVisibility(View.GONE);
                        binding.videoSelectButton.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).load(Uri.fromFile(new File((String) stringArrayListExtra.get(i3)))).into(binding.imgThumb);
            }
        }
    }


    public void set_up_video_clicked(View view) {
        if (this.url == null) {
            Toasty.error(this, getString(R.string.please_select_video)).show();
            return;
        }
        cinimaService.setEnableVideoAudio(this, binding.checkboxSound.isChecked());
        cinimaService.setPlayB(this, binding.checkboxPlayBegin.isChecked());
        cinimaService.setPlayBatterySaver(this, binding.checkboxBattery.isChecked());
        cinimaService.setVidSource(this, this.url);
        if (cinimaService.getVideoSource(this) == null) {
            Toasty.info(this, getString(R.string.error_emty_video)).show();
            return;
        }
        try {
            clearWallpaper();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
        intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", new ComponentName(this, CinimaWallService.class));
        startActivity(intent);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void video_on_clicked(View view) {
        new VideoPicker.Builder(this).mode(VideoPicker.Mode.GALLERY).directory(VideoPicker.Directory.DEFAULT).extension(VideoPicker.Extension.MP4).enableDebuggingMode(false).build();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }
}
