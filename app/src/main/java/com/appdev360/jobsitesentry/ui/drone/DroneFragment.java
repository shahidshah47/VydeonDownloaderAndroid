package com.appdev360.jobsitesentry.ui.drone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.Site;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.changepassword.ChangePassword;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationAdapter;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationMvpView;
import com.appdev360.jobsitesentry.util.MyToast;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Abubaker on 09,April,2018
 */
public class DroneFragment extends BaseFragment implements SentryLocationMvpView {

    @BindView(R.id.shimmer_recycler_view)
    ShimmerRecyclerView recyclerView;
    @Inject
    SentryLocationAdapter mAdapter;
    @Inject
    DronePresenter presenter;
    @Inject
    DataManager dataManager;
    Unbinder unbinder;
    private List<Site> siteList;
    private SentryLocationActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (SentryLocationActivity) getActivity();
    }

    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, parent);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.drone_fragment_layout;
    }

    @Override
    public void updateFragmentReference() {
        presenter.attachView(this);
        presenter.getLocationList();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter.setClickListener(new SentryLocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (mAdapter.getDataSet().get(position).getDroneVideos().size() > 0) {
                    DroneListingFragment fragment = new DroneListingFragment();
                    fragment.setVideo(mAdapter.getDataSet().get(position).getDroneVideos());
                    activity.addFragment(fragment, getResources().getString(R.string.drone_videos));
                }else {
                    MyToast.showMessage(getActivity(),"No Video found");
                }

            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.showShimmerAdapter();

    }

    @Override
    public void updateActionState(boolean action, View v) {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    @Override
    public void showItems(ArrayList<Site> items) {

        if (items.size() < 1){
            MyToast.showMessage(getActivity(),getResources().getString(R.string.no_record_found));
            return;
        }

        if (recyclerView == null) {
            return;
        }
        if (siteList == null || siteList.size() == 0) {
            siteList = items;
            mAdapter.setSiteItems(items);
        } else {
            mAdapter.addItems(items);
        }
        mAdapter.notifyDataSetChanged();
        recyclerView.hideShimmerAdapter();

    }

    @Override
    public void onError(Error error) {
        MyToast.showMessage(getActivity(),error.getMessage());
    }


    @Override
    public void onTokenExpire(Error error) {
       // MyToast.showMessage(getActivity(),error.getMessage());
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
}
