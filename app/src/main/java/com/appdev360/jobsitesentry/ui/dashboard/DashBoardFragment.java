package com.appdev360.jobsitesentry.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.constant.GeneralConstants;
import com.appdev360.jobsitesentry.data.local.PreferencesDataHelper;
import com.appdev360.jobsitesentry.data.model.DashboardMenu;
import com.appdev360.jobsitesentry.ui.arrest.ArrestFragment;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.drone.DroneFragment;
import com.appdev360.jobsitesentry.ui.safety.SafetyFragment;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by abubaker on 4/4/18.
 */

public class DashBoardFragment extends BaseFragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private SentryLocationActivity activity;
    private ArrayList<DashboardMenu> menuList = null;
    private LinearLayoutManager lytManager;
    //    private String menuName[] = {"Live Videos", "Time Lapse Videos", "Drone Videos", "Arrests Videos", "Alarm Videos",
//            "Safety"};
    private String menuName[] = {"Live Videos", "Time Lapse Videos", "Drone Videos", "Arrests Videos", "Text 24 Hour \n Central Monitoring", "Call 24 Hour \n Central Monitoring", ""};
    private int iconName[] = {R.drawable.live_video, R.drawable.time_lapse, R.drawable.drone_video, R.drawable
            .arrest_video, R.drawable
            .ic_menu_sms, R.drawable
            .ic_menu_call, R.drawable.ic_logo_menu};

    @Inject
    DashBoardAdapter mAdapter;

    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity) getActivity();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dashboard_fragment;
    }

    @Override
    public void updateFragmentReference() {
        intiAdapter();
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


    private void intiAdapter() {

        recyclerView.setHasFixedSize(true);
        //lytManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        //Set Span size
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == (iconName.length - 1) ? 2 : 1;
            }
        });


        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter.addItems(populateDashboardList());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(new DashBoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (mAdapter.getDataSet().get(position).getName().contains("Live Videos")) {

                    GeneralConstants.IS_TIME_LAPSE = false;
                    GeneralConstants.IS_ALARM_VIDEO = false;

                    activity.addFragment(new SentryLocationFragment(), getActivity().getResources().getString(R.string.sentry_location_label));

                } else if (mAdapter.getDataSet().get(position).getName().contains("Time Lapse Videos")) {

                    GeneralConstants.IS_TIME_LAPSE = true;

                    activity.addFragment(new SentryLocationFragment(), getActivity().getResources().getString(R.string.sentry_location_label));

                } else if (mAdapter.getDataSet().get(position).getName().contains("Drone Videos")) {

                    activity.addFragment(new DroneFragment(), getActivity().getResources().getString(R.string
                            .sentry_location_label));

                } else if (mAdapter.getDataSet().get(position).getName().contains("Arrests Videos")) {

                    activity.addFragment(new ArrestFragment(), getActivity().getResources().getString(R.string
                            .video));

                } else if (mAdapter.getDataSet().get(position).getName().contains("Alarm Videos")) {
                    GeneralConstants.IS_ALARM_VIDEO = true;
                    GeneralConstants.IS_TIME_LAPSE = false;
                    activity.addFragment(new SentryLocationFragment(), getActivity().getResources().getString(R.string.sentry_location_label));
                } else if (mAdapter.getDataSet().get(position).getName().contains("Safety")) {
                    activity.addFragment(new SafetyFragment(), getActivity().getResources().getString(R.string
                            .safety));

                } else if (mAdapter.getDataSet().get(position).getName().contains("Text 24 Hour")) {
                    sendSMS();
                } else if (mAdapter.getDataSet().get(position).getName().contains("Call 24 Hour")) {
                    makeCall();
                }
            }
        });
    }

    private void sendSMS() {
        Uri sms_uri = Uri.parse("smsto:(623)-335-2568");
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        sms_intent.putExtra("sms_body", "");
        if (sms_intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(sms_intent);
        } else {
            Toast.makeText(getActivity(), "SMS app not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:(623)-335-2568"));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "No Calling app found", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<DashboardMenu> populateDashboardList() {

        String userRole = PreferencesDataHelper.retrieve(getActivity(), PreferencesDataHelper.PersistenceKey.USER_ROLE);
        menuList = new ArrayList<>();

        for (int i = 0; i < menuName.length; i++) {
            DashboardMenu object = new DashboardMenu();
            if (userRole != null && (userRole.equals("user")) && menuName[i].equals("Alarm Videos")) {
                continue;
            }
            object.setName(menuName[i]);
            object.setIconName(iconName[i]);
            menuList.add(object);
        }
        return menuList;

    }
}
