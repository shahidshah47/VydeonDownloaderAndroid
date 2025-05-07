package com.appdev360.jobsitesentry.ui.splash;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.data.local.PreferencesDataHelper;
import com.appdev360.jobsitesentry.data.model.BaseModel;
import com.appdev360.jobsitesentry.ui.agreement.AgreementActivity;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.cunoraz.gifview.library.GifView;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.appdev360.jobsitesentry.constant.GeneralConstants.AGREEMENT;
import static com.appdev360.jobsitesentry.constant.GeneralConstants.AGREEMENT_MSG;
import static com.appdev360.jobsitesentry.constant.GeneralConstants.PRIVACY;
import static com.appdev360.jobsitesentry.constant.GeneralConstants.TERMS_OF_USE;
import static com.appdev360.jobsitesentry.ui.base.BasePresenter.SUCCESS_HTTP_AGREEMENT_REQUIRED;
import static com.appdev360.jobsitesentry.ui.base.BasePresenter.SUCCESS_HTTP_INVALID_TOKEN;
import static com.appdev360.jobsitesentry.ui.base.BasePresenter.SUCCESS_HTTP_PASSWORD_EXPIRE;
import static com.appdev360.jobsitesentry.ui.base.BasePresenter.SUCCESS_HTTP_TOKEN_EXPIRE;

/**
 * Created by Abubaker on 07,May,2018
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class SplashActivity extends BaseActivity {

    @Inject
    DataManager dataManager;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.splashPB)
    ProgressBar progressBar;
    //    @BindView(R.id.gifImageView)
//    GifImageView gifImageView;
    private GifDrawable gifDrawable;
    private GifView gifView;
    private Subscription mSubscription;

    @Override
    public void initViews(Bundle savedInstanceState) {
        activityComponent().inject(this);
        ButterKnife.bind(SplashActivity.this);
        // initVideoView();
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video);
        videoView.setVideoURI(video);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (dataManager.getPreferencesHelper().getToken() != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    getAgreemtInfo();
//                        startActivity(new Intent(SplashActivity.this, SentryLocationActivity.class));
//                        SplashActivity.this.finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                //works only from api 23
                PlaybackParams myPlayBackParams = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    myPlayBackParams = new PlaybackParams();
                    myPlayBackParams.setSpeed(2.5f); //you can set speed here
                    mp.setPlaybackParams(myPlayBackParams);
                }

            }
        });
        videoView.start();

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public void getAgreemtInfo() {


        String authToken = PreferencesDataHelper.retrieve(this, PreferencesDataHelper.PersistenceKey.TOKEN);
        mSubscription = dataManager.getNetworkService().sentryLocations("bearer"+authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseModel>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            Gson gson = new Gson();
                            BaseModel resp = gson.fromJson(((HttpException) e).response().errorBody().string(), BaseModel.class);

                            if (resp.getStatusCode() == SUCCESS_HTTP_INVALID_TOKEN) {
                                onTokenExpire();
                            } else if (resp.getStatusCode() == SUCCESS_HTTP_TOKEN_EXPIRE) {
                                onTokenExpire();
                            }else if (resp.getStatusCode() == SUCCESS_HTTP_PASSWORD_EXPIRE) {
                                onChangePasswrod();
                            } else if (resp.getStatusCode() == SUCCESS_HTTP_AGREEMENT_REQUIRED) {
                                progressBar.setVisibility(View.GONE);

//                                new AgreementDialog(SplashActivity.this, new AgreementDialogListener() {
//                                    @Override
//                                    public void dialogAccept() {
//                                        Intent agreementIntent = new Intent(SplashActivity.this, AgreementActivity.class);
//                                        agreementIntent.putExtra(AGREEMENT, resp.getData().getAgreement());
//                                        agreementIntent.putExtra(TERMS_OF_USE, resp.getData().getTermOfUse());
//                                        agreementIntent.putExtra(PRIVACY, resp.getData().getPrivacyPolicy());
//                                        startActivity(agreementIntent);
//                                        finish();
//                                    }
//                                    @Override
//                                    public void dialogDeny() {
//                                        finish();
//                                    }
//                                }, SplashActivity.this.getResources().getString(R.string.titleAgre),
//                                        SplashActivity.this.getResources().getString(R.string.bodyAgre)).show();

                                Intent agreementIntent = new Intent(SplashActivity.this, AgreementActivity.class);
                                agreementIntent.putExtra(AGREEMENT, resp.getData().getAgreement());
                                agreementIntent.putExtra(TERMS_OF_USE, resp.getData().getTermOfUse());
                                agreementIntent.putExtra(PRIVACY, resp.getData().getPrivacyPolicy());
                                startActivity(agreementIntent);
                                finish();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(SplashActivity.this, SentryLocationActivity.class));
                                SplashActivity.this.finish();
                            }

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(BaseModel resp) {
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(SplashActivity.this, SentryLocationActivity.class));
                        SplashActivity.this.finish();
                    }
                });
    }

    public void onTokenExpire() {
        progressBar.setVisibility(View.GONE);
        // MyToast.showMessage(getActivity(),error.getMessage());
        dataManager.getPreferencesHelper().storeToken(null);
        dataManager.getPreferencesHelper().storeUserId(-1);
        dataManager.getPreferencesHelper().setImagePath(null);
        dataManager.getPreferencesHelper().storeUserName(null);
        dataManager.getPreferencesHelper().storeUserRole(null);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }


    public void onChangePasswrod() {
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(SplashActivity.this, SentryLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("password", "password");
        intent.putExtras(bundle);
        startActivity(intent);
        SplashActivity.this.finish();
    }
}
