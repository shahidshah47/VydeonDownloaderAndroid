package com.appdev360.jobsitesentry.ui.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.appdev360.jobsitesentry.application.JobSiteSentry;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.injection.component.ActivityComponent;
import com.appdev360.jobsitesentry.injection.component.ConfigPersistentComponent;
import com.appdev360.jobsitesentry.injection.component.DaggerConfigPersistentComponent;
import com.appdev360.jobsitesentry.injection.module.ActivityModule;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.util.GeneralUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import butterknife.ButterKnife;

/**
 * Created by abubaker on 1/23/18.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private ActivityComponent mActivityComponent;
    private TryAgainClickListener tryAgainClickListener;
    private long mActivityId;
    private int menuResId = -1;
    //main content
    private FrameLayout mainContentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(JobSiteSentry.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        if (activityComponent() == null) {
            GeneralUtils.restartApp(this);
            return;
        }
        initViews(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            sComponentsMap.remove(mActivityId);
        }
        super.onDestroy();
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    public abstract void initViews(Bundle savedInstanceState);

    public abstract int getLayoutId();


    /**
     * Callback for listening to try again
     */
    public interface TryAgainClickListener {
        void onTryAgainClick(View view);
    }


    protected void onTokenExpire(DataManager dataManager) {
        // MyToast.showMessage(getActivity(), error.getMessage());
        dataManager.getPreferencesHelper().storeToken(null);
        dataManager.getPreferencesHelper().storeUserId(-1);
        dataManager.getPreferencesHelper().setImagePath(null);
        dataManager.getPreferencesHelper().storeUserName(null);
        dataManager.getPreferencesHelper().storeUserRole(null);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}
