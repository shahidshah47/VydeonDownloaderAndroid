package com.appdev360.jobsitesentry.ui.sentryLocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.appdev360.jobsitesentry.constant.NetworkConstants;
import com.appdev360.jobsitesentry.ui.changepassword.ChangePassword;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdev360.jobsitesentry.application.JobSiteSentry;
import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.constant.GeneralConstants;
import com.appdev360.jobsitesentry.ui.alarm.notification.AlarmNotificationFragment;
import com.appdev360.jobsitesentry.ui.arrest.ArrestFragment;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.camerastream.CameraStreamFragment;
import com.appdev360.jobsitesentry.ui.dashboard.DashBoardFragment;
import com.appdev360.jobsitesentry.ui.drone.DroneFragment;
import com.appdev360.jobsitesentry.ui.interfaces.MenuClickListener;
import com.appdev360.jobsitesentry.ui.safety.SafetyFragment;
import com.appdev360.jobsitesentry.ui.setting.SettingFragment;
import com.appdev360.jobsitesentry.ui.sidemenu.SideMenuController;
import com.appdev360.jobsitesentry.ui.timelapse.fragment.PlayerFragment;
import com.appdev360.jobsitesentry.util.EventBusMessage;
import com.appdev360.jobsitesentry.util.GeneralUtils;
import com.appdev360.jobsitesentry.util.statusbarsupport.SystemBarTintManager;
import com.appdev360.jobsitesentry.widget.TextViewCustom;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.crypto.SecretKey;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abubaker on 3/12/18.
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class SentryLocationActivity extends BaseActivity implements MenuClickListener, View.OnClickListener {

    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.toolbar_title)
    TextViewCustom toolbarTitle;
    @BindView(R.id.nav_icon)
    ImageView navIcon;
    @Inject
    DataManager dataManager;
    private SystemBarTintManager systemBarTintManager;
    private boolean isMaterialStatusBar;
    private static final int REGULAR_SCREEN_TOOLBAR_HEIGHT_DP = 64;
    private boolean isLandScape = false;
    private SideMenuController controller;
    private ShowAgreementBroadcastReceiver showAgreementBroadcastReceiver;

    private DrawerLayout drawerLayout;


    @Override
    public void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        activityComponent().inject(this);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        controller = new SideMenuController(SentryLocationActivity.this);
        setUserProfile();

        toolbarTitle.setText(getResources().getString(R.string.jobsitesentry));
        startScreen();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getSerializable("password").equals("password")) {
                addFragment(new ChangePassword(), getResources().getString(R.string.change_password));
            } else {
                addFragment(new AlarmNotificationFragment(), getResources().getString(R.string.notification));
            }
        }


        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {


                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

                if (fragment != null) {

                    toolbarTitle.setText(fragment.getTag());
                    if (fragment instanceof PlayerFragment) {
                        myToolbar.setVisibility(View.GONE);
                    } else if (fragment instanceof CameraStreamFragment) {
                        myToolbar.setVisibility(View.GONE);
                    } else {
                        myToolbar.setVisibility(View.VISIBLE);
                    }
                    if (getSupportFragmentManager().getFragments().size() > 1) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        navIcon.setVisibility(View.GONE);
                    } else {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        navIcon.setVisibility(View.VISIBLE);
                    }
                    //                    if (getSupportFragmentManager().getBackStackEntryCount() >= 2) {
//                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                        navIcon.setVisibility(View.GONE);
//
//                    } else {
//                        navIcon.setVisibility(View.VISIBLE);
//                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                    }
                }
            }
        });

//        controller.setMenuClickListener(this);

        showAgreementBroadcastReceiver = new ShowAgreementBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(NetworkConstants.ACTION_LOGOUT);
        LocalBroadcastManager.getInstance(SentryLocationActivity.this).registerReceiver(showAgreementBroadcastReceiver, intentFilter);

        init();
    }

    /**
     * navigation drawer
     */
    public void init() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View view = findViewById(R.id.nav_view);
        view.findViewById(R.id.nav_live_item).setOnClickListener(this);
        view.findViewById(R.id.nav_time_lapse_item).setOnClickListener(this);
        view.findViewById(R.id.nav_drone_item).setOnClickListener(this);
        view.findViewById(R.id.nav_arrest_video_item).setOnClickListener(this);
        view.findViewById(R.id.nav_alarm_item).setOnClickListener(this);
        view.findViewById(R.id.nav_setting_item).setOnClickListener(this);
        view.findViewById(R.id.nav_home_item).setOnClickListener(this);
        view.findViewById(R.id.nav_safety_item).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sentry_location;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_live_item:
                GeneralConstants.IS_TIME_LAPSE = false;
                GeneralConstants.IS_ALARM_VIDEO = false;
                replaceFragment(new SentryLocationFragment(), getResources().getString(R.string.sentry_location_label));
                break;
            case R.id.nav_time_lapse_item:
                GeneralConstants.IS_TIME_LAPSE = true;
                replaceFragment(new SentryLocationFragment(), getResources().getString(R.string.sentry_location_label));
                break;
            case R.id.nav_drone_item:
                replaceFragment(new DroneFragment(), getResources().getString(R.string.sentry_location_label));
                break;
            case R.id.nav_arrest_video_item:
                replaceFragment(new ArrestFragment(), getResources().getString(R.string.video));
                break;
            case R.id.nav_alarm_item:
                GeneralConstants.IS_ALARM_VIDEO = true;
                GeneralConstants.IS_TIME_LAPSE = false;
                replaceFragment(new SentryLocationFragment(), getResources().getString(R.string.sentry_location_label));
                break;
            case R.id.nav_setting_item:
                replaceFragment(new SettingFragment(), getResources().getString(R.string.setting));
                break;
            case R.id.nav_home_item:
                replaceFragment(new DashBoardFragment(), getResources().getString(R.string.jobsitesentry));
                break;
            case R.id.nav_safety_item:
                replaceFragment(new SafetyFragment(), getResources().getString(R.string.safety));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }


    public void replaceFragment(Fragment fragment, String tag) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_down, R
                .anim.slide_out_down);
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right, R.anim.slide_in_from_left, R
                .anim.slide_out_from_left);
        fragmentTransaction.add(R.id.container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void startScreen() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, new DashBoardFragment(), getResources().getString(R.string.my_jobsitesentry_location));
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCMToken", "Fetching FCM registration token failed", task.getException());
                        return;
                    } else {
                        // Get new FCM registration token
                        String firebaseToken = task.getResult();
                        Log.i("FCMToken", "Firebase Token = " + firebaseToken);
                    }
                });
    }

    public void setToolbarTitle(CharSequence charSequence) {
        String title = charSequence.toString();
        ((TextView) findViewById(R.id.toolbar_title)).setText(title);
    }

    public void setToolbarTitleLeftPadding(int leftPaddingInDp) {
        findViewById(R.id.toolbar_title).setPadding(GeneralUtils.dpToPx(this, leftPaddingInDp), 0, 0, 0);
    }

    @OnClick({R.id.nav_icon})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.nav_icon) {
            controller.loadImage(dataManager);
            controller.toggle();
            if (!drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void setFullScreen(boolean fullScreen) {

        int flagToAdd = fullScreen ? WindowManager.LayoutParams.FLAG_FULLSCREEN : WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
        int flagToClear = fullScreen ? WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN : WindowManager.LayoutParams.FLAG_FULLSCREEN;

        getWindow().addFlags(flagToAdd);
        getWindow().clearFlags(flagToClear);

        if (fullScreen) {
            myToolbar.setVisibility(View.GONE);
        } else {
            myToolbar.setVisibility(View.VISIBLE);
        }

        if (isMaterialStatusBar)
            systemBarTintManager.setStatusBarTintEnabled(!fullScreen);

    }

    // public void enable
    public void setMaterialSystemBar(boolean enable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (enable) {
                setStatusBarForKitKat();
                isMaterialStatusBar = true;
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                isMaterialStatusBar = false;

                adjustToolbarPadding();
            }
        }
    }

    /**
     * Makse the system bar translucent
     */
    private void setStatusBarForKitKat() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        systemBarTintManager = new SystemBarTintManager(this);

        // enable status bar tint
        systemBarTintManager.setStatusBarTintEnabled(true);

        // enable navigation bar tint
        systemBarTintManager.setNavigationBarTintEnabled(true);
        systemBarTintManager.setTintColor(getResources().getColor(R.color.semi_transparent));
    }//end initializeStatusBar


    /**
     * returns the system bar's height by using SystemBarTintManager to get precise size
     */
    private int getSystemBarHeight() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

        return config.getStatusBarHeight();
    }

    // --- helpers ---

    /**
     * Expands the size of the toolbar and moves all of its
     * content down by the status bar height if material desing
     * status bar is enabled.
     */
    private void adjustToolbarPadding() {

        if (isMaterialStatusBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            myToolbar.getLayoutParams().height = GeneralUtils.dpToPx(this, REGULAR_SCREEN_TOOLBAR_HEIGHT_DP)
                    + getSystemBarHeight();
            myToolbar.setPadding(0, getSystemBarHeight(), 0, 0);
        } else {

            myToolbar.getLayoutParams().height = GeneralUtils.dpToPx(this, REGULAR_SCREEN_TOOLBAR_HEIGHT_DP);
            myToolbar.setPadding(0, 0, 0, 0);

        }
    }


    public boolean isLandScape() {
        return isLandScape;
    }

    public void setLandScape(boolean landScape) {
        isLandScape = landScape;
    }


    @Override
    public void onBackPressed() {
        if (isLandScape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isLandScape = false;
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            EventBus.getDefault().post(new EventBusMessage(EventBusMessage.EVENT_ON_BACK_PRESSED));
            if (getSupportFragmentManager().getFragments().size() > 1)
                getSupportFragmentManager().popBackStack();
            else
                super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        currentFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onMenuClicked(View view) {

        clearBackStack();

        switch (view.getId()) {
            case R.id.nav_live_item:
                GeneralConstants.IS_TIME_LAPSE = false;
                GeneralConstants.IS_ALARM_VIDEO = false;
                replaceFragment(new SentryLocationFragment(), getResources().getString(R.string.sentry_location_label));
                break;
            case R.id.nav_time_lapse_item:
                GeneralConstants.IS_TIME_LAPSE = true;
                replaceFragment(new SentryLocationFragment(), getResources().getString(R.string.sentry_location_label));
                break;
            case R.id.nav_drone_item:
                replaceFragment(new DroneFragment(), getResources().getString(R.string.sentry_location_label));
                break;
            case R.id.nav_arrest_video_item:
                replaceFragment(new ArrestFragment(), getResources().getString(R.string.video));
                break;
            case R.id.nav_alarm_item:
                GeneralConstants.IS_ALARM_VIDEO = true;
                GeneralConstants.IS_TIME_LAPSE = false;
                replaceFragment(new SentryLocationFragment(), getResources().getString(R.string.sentry_location_label));
                break;
            case R.id.nav_setting_item:
                replaceFragment(new SettingFragment(), getResources().getString(R.string.setting));
                break;
            case R.id.nav_home_item:
                replaceFragment(new DashBoardFragment(), getResources().getString(R.string.jobsitesentry));
                break;
            case R.id.nav_safety_item:
                replaceFragment(new SafetyFragment(), getResources().getString(R.string.safety));
                break;
        }
    }

    @Override
    public void onImageLoad(ImageView view) {
        if (dataManager.getPreferencesHelper().getUserThumb() != null) {
            Picasso.get().load(dataManager.getPreferencesHelper().getUserThumb()).resize(100, 100).centerCrop()
                    .into(view);

        }
    }

    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    private void setUserProfile() {

        if (dataManager.getPreferencesHelper().getUserName() != null && !dataManager.getPreferencesHelper().getUserName().equals("null")) {
            controller.setUserName(dataManager.getPreferencesHelper().getUserName());
        }

        if (dataManager.getPreferencesHelper().getUserRole() != null && dataManager.getPreferencesHelper().getUserRole().equals("user")) {
            controller.hideAlarm();
        }

        // Get key
        Store store = new Store(getApplicationContext());
        SecretKey key = store.getSymmetricKey(getResources().getString(R.string.jobsitesentry), null);
        // Encrypt/Decrypt data
        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);

        if (key != null && !key.equals("")) {
            String decryptEmail = crypto.decrypt(dataManager.getPreferencesHelper().getEncryptedEmail(), key);
            if (decryptEmail != null) {
                controller.setUserEmail(decryptEmail);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().post(new EventBusMessage("Main Application class"));
        JobSiteSentry.setAppRunning(false);
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JobSiteSentry.setAppRunning(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(SentryLocationActivity.this).unregisterReceiver(showAgreementBroadcastReceiver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        //Check if user Logged in - Logout user
        if (event.eventType.equals(EventBusMessage.TOKEN_EXPIRED)) {
            if (dataManager.getPreferencesHelper().getToken() != null && dataManager.getPreferencesHelper().getToken().length() > 0) {
                if (JobSiteSentry.isAppRunning()) {
                    onTokenExpire(dataManager);
                }
            }//else -> User not logged in
        } else if (event.eventType.equals(EventBusMessage.EVENT_FIREBASE_MESSAGE)) {
            Snackbar.make(findViewById(android.R.id.content), event.message, Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .show();
        }
    }


    private class ShowAgreementBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkConstants.ACTION_LOGOUT.equals(intent.getAction())) {
//                CommonUtils.showToast(HomeActivity.this, R.string.token_expired);
//                logout();
            }
        }
    }
}
