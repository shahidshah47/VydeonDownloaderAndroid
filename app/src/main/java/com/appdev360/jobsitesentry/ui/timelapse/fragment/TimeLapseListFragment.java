package com.appdev360.jobsitesentry.ui.timelapse.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Camera;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.ui.timelapse.adapter.TimeLapseListAdapter;
import java.util.ArrayList;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class TimeLapseListFragment extends BaseFragment implements TimeLapseListAdapter.OnItemClickListener {


    @Inject
    TimeLapseListAdapter timeLapseAdapter;

    Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ArrayList<Camera> mCameraList;
    private SentryLocationActivity activity;

    private int unitId = 0;

    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {
        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity)getActivity();
        ((BaseActivity) getActivity()).activityComponent().inject(this);
    }

    private void initAdapter()
    {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        timeLapseAdapter.addItems(mCameraList);
        timeLapseAdapter.setClickListener(this::onItemClick);
        recyclerView.setAdapter(timeLapseAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_time_lapse_list;
    }

    @Override
    public void updateFragmentReference() {
        initAdapter();
    }

    @Override
    public void updateActionState(boolean action, View v) {
        
    }

    public void setCamera(ArrayList<Camera> list) {
        mCameraList = list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(Camera cam) {
        TimeLapseFragment fragment = new TimeLapseFragment();
        fragment.setCameraId(cam.getCameraId().intValue());
        fragment.setUnitId(unitId);
        activity.addFragment(fragment,getResources().getString(R.string.my_sentry_unit));
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
}
