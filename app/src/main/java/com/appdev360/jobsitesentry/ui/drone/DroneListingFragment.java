package com.appdev360.jobsitesentry.ui.drone;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Video;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.ui.timelapse.fragment.PlayerFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Abubaker on 09,April,2018
 */
public class DroneListingFragment extends BaseFragment{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private SentryLocationActivity activity;
    private ArrayList<Video> videoLsit = null;
    private LinearLayoutManager lytManager;
    @Inject
    VideoAdapter droneAdapter;


    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.drone_listing_fragment;
    }

    @Override
    public void updateFragmentReference() {
        intiAdapter();
    }

    @Override
    public void updateActionState(boolean action, View v) {

    }

    public void setVideo(ArrayList<Video> list) {
        videoLsit = list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void intiAdapter() {
        recyclerView.setHasFixedSize(true);
        lytManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        droneAdapter.addItems(videoLsit);
        recyclerView.setAdapter(droneAdapter);
        droneAdapter.setClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video model) {

                PlayerFragment fragment = new PlayerFragment();
                fragment.setUrl(model.getFilename());
                activity.addFragment(fragment,"");


            }

        });
    }
}
