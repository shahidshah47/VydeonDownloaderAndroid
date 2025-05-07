package com.appdev360.jobsitesentry.ui.alarm.notification;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.constant.GeneralConstants;
import com.appdev360.jobsitesentry.data.model.AlarmNotification;
import com.appdev360.jobsitesentry.data.model.Data;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.NotificationFlag;
import com.appdev360.jobsitesentry.ui.alarm.adapter.AlarmNotificationAdapter;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.changepassword.ChangePassword;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.ui.timelapse.fragment.PlayerFragment;
import com.appdev360.jobsitesentry.util.UtilSnackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Abubaker on 07,May,2018
 */
public class AlarmNotificationFragment extends BaseFragment implements AlarmNotificationMvpView {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @Inject
    AlarmNotificationPresenter presenter;
    @Inject
    AlarmNotificationAdapter mAdapter;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private SentryLocationActivity activity;
    private ArrayList<AlarmNotification> mAlarmNotificationList = new ArrayList<>();
    private LinearLayoutManager lytManager;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private int currentPage;
    private int totalPage;
    private int nextPage;


    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {
        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity) getActivity();
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        //  activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public int getLayoutId() {
        return R.layout.alarm_notification_fragment;
    }

    @Override
    public void updateFragmentReference() {
        presenter.attachView(this);
        refreshAlarmList();
        recyclerView.setHasFixedSize(true);
        lytManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lytManager);
        mAdapter.setClickListener(new AlarmNotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


                PlayerFragment fragment = new PlayerFragment();
                fragment.setUrl(mAlarmNotificationList.get(position).getFilename());
                activity.addFragment(fragment, "");

            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(scrollListener);

    }

    @Override
    public void updateActionState(boolean action, View v) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onNotificationReceived(Data data) {
        if (progressBar != null) {

            progressBar.setVisibility(View.GONE);
        }

        if (recyclerView == null) {
            return;
        }

        if (GeneralConstants.IS_NOTIFICATION_TAG) {
            mAdapter.getDataSet().clear();
            mAdapter.setAlarmItems(data.getAlarmNotificationList());
            GeneralConstants.IS_NOTIFICATION_TAG = false;

            loading = true;
            getValue(data);
            return;
        }

        if (mAlarmNotificationList == null || mAlarmNotificationList.size() == 0) {
            mAlarmNotificationList = data.getAlarmNotificationList();
            mAdapter.setAlarmItems(data.getAlarmNotificationList());
        } else {

            mAdapter.addItems(data.getAlarmNotificationList());
        }
        loading = true;
        getValue(data);
    }

    @Override
    public void onError(Error error) {
        if (progressBar != null) {

            progressBar.setVisibility(View.GONE);
        }
        UtilSnackbar.showSnakbarError(parent, error.getMessage());
    }


    @Override
    public void onResume() {
        super.onResume();
        GeneralConstants.IS_ALARM_SCREEN_VISIBLE = true;
        EventBus.getDefault().register(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        GeneralConstants.IS_ALARM_SCREEN_VISIBLE = false;
        EventBus.getDefault().unregister(this);
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


            if (dy > 0) {

                visibleItemCount = lytManager.getChildCount();
                totalItemCount = lytManager.getItemCount();
                pastVisiblesItems = lytManager.findFirstVisibleItemPosition();

                if (loading) {

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;

                        if (currentPage <= totalPage) {

                            if (presenter != null) {
                                presenter.getAlarmNotifications(nextPage + "");
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.VISIBLE);
                                }

                            }
                        }

                    }
                }
            }
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotificationFlag flag) {

        refreshAlarmList();

    }

    private void refreshAlarmList() {

        if (presenter != null) {

            presenter.getAlarmNotifications(1 + "");

            if (progressBar != null) {

                progressBar.setVisibility(View.VISIBLE);
            }


        }

    }

    public void getValue(Data data) {
        currentPage = data.getCurrentPage();
        nextPage = data.getNextPage();
        totalPage = data.getTotalPages();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTokenExpire(Error error) {
        //MyToast.showMessage(getActivity(),error.getMessage());
        dataManager.getPreferencesHelper().storeToken(null);
        dataManager.getPreferencesHelper().storeUserId(-1);
        dataManager.getPreferencesHelper().setImagePath(null);
        dataManager.getPreferencesHelper().storeUserName(null);
        dataManager.getPreferencesHelper().storeUserRole(null);
        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void onChangePasswrod(Error error) {
        activity.replaceFragment(new ChangePassword(), "Change Password");

    }
}
