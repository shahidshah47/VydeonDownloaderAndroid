package com.appdev360.jobsitesentry.ui.sentryLocation;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.appdev360.jobsitesentry.ui.sentryunit.SentryUnitFragment;
import com.appdev360.jobsitesentry.util.MyToast;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Abubaker on 07,May,2018
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class SentryLocationFragment extends BaseFragment implements  SentryLocationMvpView {

    @BindView(R.id.shimmer_recycler_view)
    ShimmerRecyclerView recyclerView;
    @Inject
    SentryLocationAdapter mAdapter;
    @Inject
    SentryLocationPresenter presenter;
    @Inject
    DataManager dataManager;
    Unbinder unbinder;
    private LinearLayoutManager lytManager;
    private List<Site> siteList;
    private SentryLocationActivity activity;


    @Override
    public void initViews(View parentView, Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity) getActivity();
    }

    @Override
    public int getLayoutId() {
        return R.layout.sentry_location_fragment;
    }

    @Override
    public void updateFragmentReference() {
        presenter.attachView(this);
        presenter.getLocationList();
        recyclerView.setHasFixedSize(true);
        lytManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter.setClickListener(new SentryLocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SentryUnitFragment fragment = new SentryUnitFragment();
                fragment.setUnitId(mAdapter.getDataSet().get(position).getSiteId().intValue());
                activity.addFragment(fragment,getResources().getString(R.string.my_sentry_unit));

            }
        });
        recyclerView.setAdapter(mAdapter);

        recyclerView.showShimmerAdapter();
    }

    @Override
    public void updateActionState(boolean action, View v) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
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
      //  MyToast.showMessage(getActivity(),error.getMessage());
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
