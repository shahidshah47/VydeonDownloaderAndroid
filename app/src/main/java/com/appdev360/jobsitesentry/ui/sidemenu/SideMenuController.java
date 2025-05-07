package com.appdev360.jobsitesentry.ui.sidemenu;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.ui.interfaces.MenuClickListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Abubaker on 07,May,2018
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class SideMenuController implements View.OnClickListener {

    private static final long ANIMATION_DURATION = 800;
    private Activity mActivity;
//    private SlidingMenu mMenu;
    private ScrollView mMenuOptionsContainer;
    private boolean menuOpened;
    private MenuClickListener menuClickListener;

    private TextView live;
    private TextView timeLapse;
    private TextView drone;
    private TextView arrestVideo;
    private TextView alarm;
    private TextView setting;
    private TextView safety;
    private TextView home;
    private TextView userName;
    private TextView userEmail;
    private ImageView profilePic;
    private String imagePath;
    private String name;
    private String email;
    private View alarmView;


    public SideMenuController(Activity mActivity) {
        this.mActivity = mActivity;
//        SlidingMenu menu = new SlidingMenu(mActivity);
//        menu.setMode(SlidingMenu.LEFT);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        menu.setFadeDegree(0.35f);
//        menu.attachToActivity(mActivity, SlidingMenu.SLIDING_CONTENT);
//        menu.setMenu(getSideMenuLayoutId());
//        menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
//            public void onOpened() {
//                menuOpened = true;
//            }
//        });
//        menu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
//            public void onClosed() {
//                menuOpened = false;
//            }
//        });
//
//        mMenu = menu;

        mMenuOptionsContainer = mActivity.findViewById(R.id.menu_options_container);

        initSideMenuItems();

        if (menuClickListener != null){
            menuClickListener.onImageLoad(profilePic);
        }
    }

    public int getSideMenuLayoutId() {
        return R.layout.side_menu;
    }

    public void initSideMenuItems() {

        live = getActivity().findViewById(R.id.nav_live_item);
        timeLapse = getActivity().findViewById(R.id.nav_time_lapse_item);
        drone = getActivity().findViewById(R.id.nav_drone_item);
        arrestVideo = getActivity().findViewById(R.id.nav_arrest_video_item);
        alarm = getActivity().findViewById(R.id.nav_alarm_item);
        profilePic = getActivity().findViewById(R.id.profile_pic);
        setting = getActivity().findViewById(R.id.nav_setting_item);
        safety = getActivity().findViewById(R.id.nav_safety_item);
        home = getActivity().findViewById(R.id.nav_home_item);
        userName = getActivity().findViewById(R.id.user_name);
        userEmail = getActivity().findViewById(R.id.user_email);
        alarmView = getActivity().findViewById(R.id.alarm_view);

        live.setOnClickListener(this);
        timeLapse.setOnClickListener(this);
        drone.setOnClickListener(this);
        arrestVideo.setOnClickListener(this);
        alarm.setOnClickListener(this);
        setting.setOnClickListener(this);
        home.setOnClickListener(this);
        safety.setOnClickListener(this);
    }

    public void reloadSide() {
        initSideMenuItems();
    }

    public Activity getActivity(){
        return mActivity;
    }

//    public SlidingMenu getSlidingMenu() {
//        return this.mMenu;
//    }

    public boolean isMenuOpened() {
        return menuOpened;
    }

    public void setMenuOpened(boolean menuOpened) {
        this.menuOpened = menuOpened;
    }

    public void toggle() {
//        mMenu.toggle();
    }

    public void setMenuClickListener(MenuClickListener menuClickListener){
        this.menuClickListener = menuClickListener;
    }

    @Override
    public void onClick(View v) {
//        toggle();
        menuClickListener.onMenuClicked(v);
    }

    public void loadImage(DataManager dataManager){
        if (menuClickListener != null){
            menuClickListener.onImageLoad(profilePic);
        }
        if (dataManager.getPreferencesHelper().getUserThumb() != null) {
            Picasso.get().load(dataManager.getPreferencesHelper().getUserThumb()).resize(100, 100).centerCrop()
                    .into(profilePic);
        }
    }

    public void setUserName(String name){
        this.name = name;
        userName.setText(name);
    }

    public void setUserEmail(String email){
        this.email = email;
        userEmail.setText(email);
    }

    public void hideAlarm(){
//        alarmView.setVisibility(View.GONE);
        alarm.setVisibility(View.GONE);
    }
}
