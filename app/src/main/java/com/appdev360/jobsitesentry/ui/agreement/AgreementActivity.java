package com.appdev360.jobsitesentry.ui.agreement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.util.AgreementDialog;
import com.appdev360.jobsitesentry.util.UtilSnackbar;
import com.appdev360.jobsitesentry.widget.AgreementDialogListener;

import java.util.Calendar;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.appdev360.jobsitesentry.constant.GeneralConstants.AGREEMENT;
import static com.appdev360.jobsitesentry.constant.GeneralConstants.AGREEMENT_MSG;
import static com.appdev360.jobsitesentry.constant.GeneralConstants.PRIVACY;
import static com.appdev360.jobsitesentry.constant.GeneralConstants.TERMS_OF_USE;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.AGREEMENT_URL;

/**
 * Updated By Hussain Saad on 27/01/22
 */

public class AgreementActivity extends BaseActivity implements AgreementMvpView {

    @BindView(R.id.webview)
    WebView mWebView;
    @BindView(R.id.agreementPB)
    ProgressBar progressBar;
    @BindView(R.id.parentlyt)
    ConstraintLayout parentlyt;
    @BindView(R.id.cancelAgrBtn)
    AppCompatButton cancelAgrBtn;
    @BindView(R.id.acceptAgrBtn)
    AppCompatButton acceptAgrBtn;
    @Inject
    DataManager dataManager;
    @Inject
    AgreementPresenter agreementPresenter;
    String agreement, terms_of_use, privacy;
    StringBuilder stringBuilder;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        activityComponent().inject(this);
        agreementPresenter.attachView(this);

        agreement = getIntent().getStringExtra(AGREEMENT);
        terms_of_use = getIntent().getStringExtra(TERMS_OF_USE);
        privacy = getIntent().getStringExtra(PRIVACY);

//        Log.d("agreement"," = " + agreement);
//        Log.d("terms_of_use"," = " + terms_of_use);
//        Log.d("privacy"," = " + privacy);

        stringBuilder = new StringBuilder();
        stringBuilder.append(agreement);
        stringBuilder.append(terms_of_use);
        stringBuilder.append(privacy);

        UtilSnackbar.showSnakbarError(parentlyt, getResources().getString(R.string.agreement_sign));

        TimeZone tz = TimeZone.getDefault();
        String timeZone = tz.getID();
        Calendar c = Calendar.getInstance(tz);
        Log.d("Time zone"," = " + timeZone);

        mWebView.loadDataWithBaseURL(null, stringBuilder.toString(), "text/html", "utf-8", null);

        acceptAgrBtn.setOnClickListener(v -> {

            new AgreementDialog(AgreementActivity.this, new AgreementDialogListener() {
                @Override
                public void dialogAccept() {
                    progressBar.setVisibility(View.VISIBLE);
                    agreementPresenter.acceptAgreement(timeZone);
                }
                @Override
                public void dialogDeny() {

                }
            }, AgreementActivity.this.getResources().getString(R.string.titleAgre),
                    AgreementActivity.this.getResources().getString(R.string.bodyAgree)).show();

        });

        cancelAgrBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            agreementPresenter.logout();
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        agreementPresenter.detachView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onAgreementSuccess() {
        progressBar.setVisibility(View.GONE);
        UtilSnackbar.showSnakbarSuccess(parentlyt, getResources().getString(R.string.signed_successfully));
        startActivity(new Intent(AgreementActivity.this, SentryLocationActivity.class));
        finish();
    }

    @Override
    public void onAgreementFailure(Error error) {
        progressBar.setVisibility(View.GONE);
        UtilSnackbar.showSnakbarError(parentlyt, getResources().getString(R.string.some_thing_wrong));
    }

    @Override
    public void onTokenExpire(Error error) {
        progressBar.setVisibility(View.GONE);
// MyToast.showMessage(getActivity(),error.getMessage());
        dataManager.getPreferencesHelper().storeToken(null);
        dataManager.getPreferencesHelper().storeUserId(-1);
        dataManager.getPreferencesHelper().setImagePath(null);
        dataManager.getPreferencesHelper().storeUserName(null);
        dataManager.getPreferencesHelper().storeUserRole(null);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onChangePasswrod(Error error) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLogout(String message) {

//        MyToast.showMessage(AgreementActivity.this,message);
        dataManager.getPreferencesHelper().storeToken(null);
        dataManager.getPreferencesHelper().storeUserId(-1);
        dataManager.getPreferencesHelper().setImagePath(null);
        dataManager.getPreferencesHelper().storeUserName(null);
        dataManager.getPreferencesHelper().storeUserRole(null);
        AgreementActivity.this.startActivity(new Intent(AgreementActivity.this, MainActivity.class));
        AgreementActivity.this.finish();

    }

    @Override
    public void onLogoutError(Error error) {
        UtilSnackbar.showSnakbarError(parentlyt, error.getMessage());
    }
}
