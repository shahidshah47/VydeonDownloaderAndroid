package com.appdev360.jobsitesentry.ui.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.login.LoginFragment;
import com.appdev360.jobsitesentry.util.GeneralUtils;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abubaker on 07,May,2018
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.container)
    FrameLayout container;

    @Override
    public void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        startLoginScreen();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_down, R
                .anim.slide_out_down);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right, R.anim.slide_in_from_left, R
                .anim.slide_out_from_left);
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void startLoginScreen() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, new LoginFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public void setToolbarTitle(CharSequence charSequence) {
        String title = charSequence.toString();
        ((TextView) findViewById(R.id.toolbar_title)).setText(title);
    }

    public void setToolbarTitleLeftPadding(int leftPaddingInDp) {
        findViewById(R.id.toolbar_title).setPadding(GeneralUtils.dpToPx(this, leftPaddingInDp), 0, 0, 0);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("Test321", "BackPressed");
    }
}
