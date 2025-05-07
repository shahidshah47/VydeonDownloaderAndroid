package com.appdev360.jobsitesentry.ui.arrest;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.Video;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.changepassword.ChangePassword;
import com.appdev360.jobsitesentry.ui.drone.VideoAdapter;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.ui.timelapse.fragment.PlayerFragment;
import com.appdev360.jobsitesentry.util.MyToast;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Abubaker on 10,April,2018
 */
public class ArrestFragment extends BaseFragment implements ArrestMvpView{

    @BindView(R.id.shimmer_recycler_view)
    ShimmerRecyclerView recyclerView;
    Unbinder unbinder;
    private SentryLocationActivity activity;
    private ArrayList<Video> videoLsit = null;
    private LinearLayoutManager lytManager;
    @Inject
    VideoAdapter droneAdapter;
    @Inject
    ArrestPresenter presenter;

    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity) getActivity();
    }

    @Override
    public int getLayoutId() {
        return R.layout.arrest_video_fragment_layout;
    }

    @Override
    public void updateFragmentReference() {

        presenter.attachView(this);
        presenter.getArrestVideos();
        recyclerView.setHasFixedSize(true);
        lytManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        droneAdapter.setClickListener(new VideoAdapter.OnItemClickListener() {
         @Override
         public void onItemClick(Video model) {

             PlayerFragment fragment = new PlayerFragment();
             fragment.setUrl(model.getFilename());
             activity.addFragment(fragment,"");

         }
     });
        recyclerView.setAdapter(droneAdapter);
        recyclerView.showShimmerAdapter();

    }

    @Override
    public void updateActionState(boolean action, View v) {

    }

    @Override
    public void showItems(ArrayList<Video> items) {

        if (items.size() < 1){
            MyToast.showMessage(getActivity(),getResources().getString(R.string.no_record_found));
            return;
        }

        if (recyclerView == null) {
            return;
        }
        if (videoLsit == null || videoLsit.size() == 0) {
            videoLsit = items;
            droneAdapter.setVideoItems(items);
        } else {
            droneAdapter.addItems(items);
        }
        droneAdapter.notifyDataSetChanged();
        recyclerView.hideShimmerAdapter();
    }

    @Override
    public void onError(Error error) {
        MyToast.showMessage(getActivity(),error.getMessage());
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
    public void onTokenExpire(Error error) {
      onTokenExpire(error,dataManager);
    }

    @Override
    public void onChangePasswrod(Error error) {
        activity.replaceFragment(new ChangePassword(), "Change Password");

    }
}
