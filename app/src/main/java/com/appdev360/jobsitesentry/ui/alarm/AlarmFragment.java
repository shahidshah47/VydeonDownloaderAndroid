package com.appdev360.jobsitesentry.ui.alarm;

import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Alarm;
import com.appdev360.jobsitesentry.data.model.Camera;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.alarm.adapter.AlarmAdapter;
import com.appdev360.jobsitesentry.ui.alarm.adapter.AlarmSectionAdapter;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.changepassword.ChangePassword;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.ui.timelapse.fragment.PlayerFragment;
import com.appdev360.jobsitesentry.util.MyToast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Abubaker on 19,April,2018
 */
public class AlarmFragment extends BaseFragment implements AlarmVideoMvpView{

    @Inject
    AlarmAdapter timeLapseAdapter;
    @Inject
    AlarmSectionAdapter alarmSectionAdapter;
    @Inject
    AlarmPresenter presenter;
    Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    private ArrayList<Camera> mCameraList;
    private LinearLayoutManager lytManager;
    private ArrayList<Alarm> alarmList;
    private ArrayList<Integer> integerList;
    private String oldLabel = null;
    private SentryLocationActivity activity;
    private int unitId;
    private int counter;
    List<AlarmSectionAdapter.Section> sections = new ArrayList<AlarmSectionAdapter.Section>();


    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {
        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity)getActivity();
        ((BaseActivity) getActivity()).activityComponent().inject(this);

        sections = new ArrayList<AlarmSectionAdapter.Section>();
        alarmList = new ArrayList<>();
        integerList = new ArrayList<>();

       // mCameraList = cameras;

        setData();

        recyclerView.setHasFixedSize(true);
        lytManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        timeLapseAdapter.addItems(alarmList);


        //Add your adapter to the sectionAdapter
        AlarmSectionAdapter.Section[] dummy = new AlarmSectionAdapter.Section[sections.size()];
        alarmSectionAdapter.setAdapterValues(timeLapseAdapter, recyclerView);
        alarmSectionAdapter.setSections(sections.toArray(dummy));
        recyclerView.setAdapter(alarmSectionAdapter);



        timeLapseAdapter.setClickListener(new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Alarm object) {

                if (object.getFilename()!= null && !object.getFilename().equals("")){
                    PlayerFragment fragment = new PlayerFragment();
                    fragment.setUrl(object.getFilename());
                    activity.addFragment(fragment,"");
                }else {
                    MyToast.showMessage(getActivity(),"Please try again");
                }


            }
        });


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//                if (presenter != null){
//                    counter = 0;
//                    presenter.getAlarmVideos(unitId);
//                }

            }
        });



        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,R.color.success_color);



    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_alarm_video;
    }

    @Override
    public void updateFragmentReference() {
       // presenter.attachView(this);
       // presenter.getAlarmVideos(unitId);
    }

    @Override
    public void updateActionState(boolean action, View v) {

    }

    public void setCamera(ArrayList<Camera> list) {
        mCameraList = list;
    }

    public void setUnitId(int unitId){
        this.unitId = unitId;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showVideos(ArrayList<Camera> cameras) {
        swipeContainer.setRefreshing(false);
        timeLapseAdapter.getDataSet().clear();
        alarmSectionAdapter.clearSection();
        mCameraList = cameras;
        sections = null;
        sections = new ArrayList<AlarmSectionAdapter.Section>();
        alarmList = new ArrayList<>();
        oldLabel = null;
        // Manipulate Data
        setData();

        // Setting Adapter
       timeLapseAdapter.addItems(alarmList);

        //Add your adapter to the sectionAdapter
        AlarmSectionAdapter.Section[] dummy = new AlarmSectionAdapter.Section[sections.size()];
        alarmSectionAdapter.setAdapterValues(timeLapseAdapter, recyclerView);
        alarmSectionAdapter.setSections(sections.toArray(dummy));
        recyclerView.setAdapter(alarmSectionAdapter);

    }

    @Override
    public void onError(Error error) {

    }


    private void setData(){


//        for (int i = 0;i<mCameraList.size();i++){
//
//            if (mCameraList.get(i).getAlarmVideo().size() > 0){
//                alarmList.addAll(mCameraList.get(i).getAlarmVideo());
//            }
//            //getting all Time lapse videos list
//
//            //Setting header on specific positions
//            if (mCameraList.get(i).getAlarmVideo().size() > 0){
//
//                if (oldLabel == null){
//                    counter = 0;
//                    if (sections.size() == 0){
//                        sections.add(new AlarmSectionAdapter.Section(0, mCameraList.get(i).getCameraNameFull()));
//                    }
//                    oldLabel = mCameraList.get(i).getCameraNameFull();
//                    if (i == 0){
//                        counter += mCameraList.get(0).getAlarmVideo().size();
//                    }else {
//                        counter += mCameraList.get(i).getAlarmVideo().size();
//                    }
//
//                    //continue;
//                }
//
//                if (!oldLabel.equals(mCameraList.get(i).getCameraNameFull())){
//
//                    sections.add(new AlarmSectionAdapter.Section(counter, mCameraList.get(i).getCameraNameFull()));
//                    counter += mCameraList.get(i).getAlarmVideo().size();
//                    oldLabel = mCameraList.get(i).getCameraNameFull();
//
//                }
//            }
//
//        }
    }

    @Override
    public void onTokenExpire(Error error) {

//        MyToast.showMessage(getActivity(),error.getMessage());
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
