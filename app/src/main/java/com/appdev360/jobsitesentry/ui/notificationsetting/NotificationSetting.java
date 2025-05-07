package com.appdev360.jobsitesentry.ui.notificationsetting;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.Success;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.changepassword.ChangePassword;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.util.GeneralUtils;
import com.appdev360.jobsitesentry.util.MyToast;
import com.appdev360.jobsitesentry.util.UtilSnackbar;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Abubaker on 02,May,2018
 */
public class NotificationSetting extends BaseFragment implements NotificationMvpView{


    @BindView(R.id.img_start_time)
    ImageView imgStartTime;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.img_end_time)
    ImageView imgEndTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    Unbinder unbinder;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @Inject
    NotificationPresenter presenter;

    private SentryLocationActivity activity;
    private TimePickerDialog mTimePicker;
    private String startTime = null;
    private String endTime = null;


    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {

        ((BaseActivity) getActivity()).activityComponent().inject(this);
        activity = (SentryLocationActivity) getActivity();

    }

    @Override
    public int getLayoutId() {
        return R.layout.notification_setting;
    }

    @Override
    public void updateFragmentReference() {
        presenter.attachView(this);
    }

    @Override
    public void updateActionState(boolean action, View v) {
        btnSave.setEnabled(action);
        if (v != null) {
            v.setEnabled(!action);
        }

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

    @OnClick({R.id.img_start_time, R.id.img_end_time,R.id.btn_save})
    public void onViewClicked(View view) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
        switch (view.getId()) {

            case R.id.img_start_time:
                imgStartTime.startAnimation(animation);
                selectTimeRange("Select Start Time", true);
                break;

            case R.id.img_end_time:
                imgEndTime.startAnimation(animation);
                selectTimeRange("Select End Time", false);
                break;

            case R.id.btn_save:

                if (startTime == null){
                    MyToast.showMessage(getActivity(),getResources().getString(R.string.start_time_required));
                    return;
                }

                if (endTime == null){
                    MyToast.showMessage(getActivity(),getResources().getString(R.string.end_time_required));
                    return;
                }
                updateActionState(false, null);
                progressBar.setVisibility(View.VISIBLE);
                presenter.updateNotificationSetting(true,startTime,endTime);
                break;

        }
    }

    private void selectTimeRange(String title, final boolean isStartTime) {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(getActivity(), R.style.TimePickerTheme,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                if (isStartTime) {
                    startTime = selectedHour+":"+selectedMinute+":"+"00";
                    String strtTime = GeneralUtils.convert24hrTo12hr("" + selectedHour + ":" + selectedMinute);
                    tvStartTime.setText(strtTime);

                } else {
                    endTime = selectedHour+":"+selectedMinute+":"+"00";
                    String strtTime = GeneralUtils.convert24hrTo12hr("" + selectedHour + ":" + selectedMinute);
                    tvEndTime.setText(strtTime);

                }

            }
        }, hour, minute, false);//Yes 24 hour time

        mTimePicker.setTitle(title);
        mTimePicker.show();

    }

    @Override
    public void onSuccess(String message) {
        progressBar.setVisibility(View.GONE);
        UtilSnackbar.showSnakbarSuccess(parent, message);
        getActivity().onBackPressed();
        EventBus.getDefault().post(new Success());
    }

    @Override
    public void onNotificationError(Error error) {

        updateActionState(true, null);
        progressBar.setVisibility(View.GONE);
        UtilSnackbar.showSnakbarError(parent, error.getMessage());
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
}
