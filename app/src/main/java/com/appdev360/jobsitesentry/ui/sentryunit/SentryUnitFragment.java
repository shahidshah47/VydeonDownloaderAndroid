package com.appdev360.jobsitesentry.ui.sentryunit;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.constant.GeneralConstants;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.unit.Data;
import com.appdev360.jobsitesentry.data.model.unit.UnitData;
import com.appdev360.jobsitesentry.ui.alarm.AlarmPresenter;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.camerastream.CameraStreamFragment;
import com.appdev360.jobsitesentry.ui.changepassword.ChangePassword;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.ui.timelapse.fragment.TimeLapseListFragment;
import com.appdev360.jobsitesentry.util.MyToast;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Abubaker on 07,May,2018
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */


public class SentryUnitFragment extends BaseFragment implements SentryUnitMvpView {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    Unbinder unbinder;
    private SentryLocationActivity activity;

    private ArrayList<UnitData> unitList = null;
    private LinearLayoutManager lytManager;
    @Inject
    UnitAdapter mAdapter;
    @Inject
    AlarmPresenter presenter;

    boolean isLoading = false;


    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    private int unitId;

    @Override
    public void initViews(View parentView, Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity) getActivity();
        // activity.setToolbarTitle(getActivity().getResources().getString(R.string.my_sentry_unit));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sentry_unit_layout;
    }

    @Override
    public void updateFragmentReference() {
        presenter.attachView(this);
      //  unitId = 97;
        isLoading = true;
        presenter.setUnitId(unitId);
        presenter.getUnits(unitId, 1);
    }


    @Override
    public void updateActionState(boolean action, View v) {

    }

    @Override
    public void onPause() {
        super.onPause();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        lytManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        initScrollListener();
        mAdapter.addItems(unitList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(new UnitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (GeneralConstants.IS_TIME_LAPSE) {


//                    for (int i = 0;i<list.size();i++){
//
//                        if (list.get(i).getTimeLapses() != null && list.get(i).getTimeLapses().size() > 0){
//                            timeLapseList.addAll(list.get(i).getTimeLapses());
//                        }
//
//                    }

                    if (unitList.get(position).getCameras() != null && unitList.get(position).getCameras().size() > 0) {
                        TimeLapseListFragment fragment = new TimeLapseListFragment();
                        fragment.setCamera(unitList.get(position).getCameras());
                        fragment.setUnitId(unitId);
                        activity.addFragment(fragment, getResources().getString(R.string.time_lapse));
                    } else {
                        MyToast.showMessage(getActivity(), "No Cameras found");
                    }

                }
//                else if (GeneralConstants.IS_ALARM_VIDEO){  // Alarm videos section removed
//                    progressBar.setVisibility(View.VISIBLE);
//                    unitId = unitList.get(position).getUnitId();
//                    presenter.getAlarmVideos(unitId);
//
//                }
                else {

                    CameraStreamFragment fragment = new CameraStreamFragment();
//                    fragment.setCamera(unitList.get(position).getCameras());
                    fragment.setUnitData(unitList.get(position));
                    activity.addFragment(fragment, getResources().getString(R.string.camera_streaming));
                }
            }
        });
    }


    @Override
    public void showFirstPage(Data unitData) {
        isLoading = false;
        unitList = unitData.getUnitData();
        initAdapter();
    }

    @Override
    public void appendNextItems(Data unitData) {
        isLoading = false;
        if (unitList != null && unitList.size() > 0) {
            unitList.remove(unitList.size()-1);
            unitList.addAll(unitData.getUnitData());
            mAdapter.setmDataSet(unitList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(Error error) {

    }

    @Override
    public void onTokenExpire(Error error) {
      onTokenExpire(error,dataManager);
    }

    @Override
    public void onChangePasswrod(Error error) {
        activity.replaceFragment(new ChangePassword(), "Change Password");
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager linearLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && unitList != null) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == unitList.size() - 1) {
                        isLoading = true;
                        presenter.loadNextPage();
                    }
                }
            }
        });
    }

    @Override
    public void addLoadingRow() {
        if(unitList!=null) {
            unitList.add(null);
            mAdapter.setmDataSet(unitList);
            mAdapter.notifyItemChanged(unitList.size() - 1);
        }
    }
}
