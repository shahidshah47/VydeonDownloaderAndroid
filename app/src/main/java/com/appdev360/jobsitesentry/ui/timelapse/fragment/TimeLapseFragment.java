package com.appdev360.jobsitesentry.ui.timelapse.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.timelapse.Data;
import com.appdev360.jobsitesentry.data.model.timelapse.TimeLapse;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.changepassword.ChangePassword;
import com.appdev360.jobsitesentry.ui.timelapse.TimeLapseSectionAdapter;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.ui.timelapse.TimeLapseAdapter;
import com.appdev360.jobsitesentry.ui.timelapse.presenter.TimeLapsePresenter;
import com.appdev360.jobsitesentry.ui.timelapse.view.TimeLapseMvpView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TimeLapseFragment extends BaseFragment implements TimeLapseMvpView {

    @Inject
    TimeLapseAdapter timeLapseAdapter;
    @Inject
    TimeLapsePresenter presenter;

    Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    //    private ArrayList<Camera> mCameraList;
    private LinearLayoutManager lytManager;
    private ArrayList<TimeLapse> timeLapseList;
    private ArrayList<Integer> integerList;
    private String oldLabel = null;
    private SentryLocationActivity activity;
    private int unitId = 0;

    private int cameraId = 0;
    boolean isLoading = false;


    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {
        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity) getActivity();
        ((BaseActivity) getActivity()).activityComponent().inject(this);
    }

    private void initAdapter() {
        List<TimeLapseSectionAdapter.Section> sections = new ArrayList<TimeLapseSectionAdapter.Section>();
        lytManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        initScrollListener();
        timeLapseAdapter.addItems(timeLapseList);
        timeLapseAdapter.setClickListener(new TimeLapseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TimeLapse object) {
                PlayerFragment fragment = new PlayerFragment();
                fragment.setUrl(object.getFilename());
                activity.addFragment(fragment, "");
            }
        });
        recyclerView.setAdapter(timeLapseAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_time_lapse;
    }


    @Override
    public void updateFragmentReference() {
        presenter.attachView(this);
        isLoading = true;
       // unitId = 27;
        presenter.setUnitId(unitId);
        presenter.setCameraId(cameraId);
        presenter.getTimeLapVideos(unitId, cameraId, 1);
    }

    @Override
    public void updateActionState(boolean action, View v) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }


    @Override
    public void showFirstPage(Data unitData) {
        isLoading = false;
        timeLapseList = unitData.getTimeLapseData();
        initAdapter();
    }

    @Override
    public void appendNextItems(Data data) {
        isLoading = false;
        if (timeLapseList != null && timeLapseList.size() > 0) {
            Log.i("Test321", "appendNextItems - list size before " + timeLapseList.size());
            timeLapseList.remove(timeLapseList.size() - 1);
            timeLapseList.addAll(data.getTimeLapseData());
            timeLapseAdapter.setmDataSet(timeLapseList);
            timeLapseAdapter.notifyDataSetChanged();
            Log.i("Test321", "appendNextItems - list size after " + timeLapseList.size());
        }
    }


    @Override
    public void onError(Error error) {

    }

    @Override
    public void onTokenExpire(Error error) {
        onTokenExpire(error, dataManager);
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
                if (!isLoading && timeLapseList != null) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == timeLapseList.size() - 1) {
                        isLoading = true;
                        presenter.loadNextPage();
                    }
                }
            }
        });
    }

    @Override
    public void addLoadingRow() {
        if (timeLapseList != null) {
            timeLapseList.add(null);
            timeLapseAdapter.setmDataSet(timeLapseList);
            timeLapseAdapter.notifyItemChanged(timeLapseList.size() - 1);
        }
    }

}
