package com.intellicoder.vydeondownloader.extraFeatures;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.intellicoder.vydeondownloader.InstagramBulkDownloader;
import com.intellicoder.vydeondownloader.R;
import com.intellicoder.vydeondownloader.extraFeatures.videolivewallpaper.MainActivityLivewallpaper;
import com.intellicoder.vydeondownloader.utils.Constants;
import com.startapp.sdk.adsbase.StartAppAd;


public class ExtraFeaturesFragment extends Fragment {
    BillingProcessor bp;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_extra_features, container, false);

        bp = new BillingProcessor(getActivity(), getString(R.string.playlisencekey), new BillingProcessor.IBillingHandler() {
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

        loadAdmobAds();

        view.findViewById(R.id.btn_one)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(), MainActivityLivewallpaper.class));
                        // showstarappasds();
                        //TODO to show admobb ads here instead of startapp ads
                        showAdmobAds();

                    }
                });
        view.findViewById(R.id.bankcardtiktokId)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(), TikTokWebview.class));
                        showstarappasds();
                        //TODO to show admobb ads here instead of startapp ads
                        // showAdmobAds();

                    }
                });


        if (Constants.show_earning_card_in_extrafragment) {
            view.findViewById(R.id.card_extra).setVisibility(View.INVISIBLE);

        } else {
            view.findViewById(R.id.card_extra).setVisibility(View.INVISIBLE);

        }

        view.findViewById(R.id.earnmoneycard)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(), EarningAppWebviewActivity.class));
                        showstarappasds();
                        //TODO to show admobb ads here instead of startapp ads
                        // showAdmobAds();

                    }
                });

        view.findViewById(R.id.instabulkcard)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(), InstagramBulkDownloader.class));
                        showstarappasds();
                        //TODO to show admobb ads here instead of startapp ads
                        // showAdmobAds();

                    }
                });

        return view;
    }

    private void showstarappasds() {
        if (Constants.show_startappads) {
            if (!bp.isPurchased(getString(R.string.productidcode))) {
                StartAppAd.showAd(getActivity());
            }
        }
    }

    private void showAdmobAds() {
        if (Constants.show_admobads) {
            if (!bp.isPurchased(getString(R.string.productidcode))) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(getActivity());
                }
            }
        }
    }

    private void loadAdmobAds() {
        if (Constants.show_admobads) {
            if (!bp.isPurchased(getString(R.string.productidcode))) {


                AdRequest adRequest = new AdRequest.Builder().build();

                InterstitialAd.load(getActivity(), getResources().getString(R.string.AdmobInterstitial), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });


//                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                    @Override
//                    public void onAdDismissedFullScreenContent() {
//                        // Called when fullscreen content is dismissed.
//                    }
//
//                    @Override
//                    public void onAdFailedToShowFullScreenContent(AdError adError) {
//                        // Called when fullscreen content failed to show.
//                    }
//
//                    @Override
//                    public void onAdShowedFullScreenContent() {
//
//                        mInterstitialAd = null;
//                    }
//                });


            }
        }
    }


}
