package com.appdev360.jobsitesentry.ui.camerastream;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.constant.GeneralConstants;
import com.appdev360.jobsitesentry.data.model.Camera;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.unit.UnitData;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.util.EventBusMessage;
import com.appdev360.jobsitesentry.util.GeneralUtils;
import com.appdev360.jobsitesentry.util.MyToast;
import com.appdev360.jobsitesentry.util.PermissionsHelper;
import com.appdev360.jobsitesentry.util.ScreenshotUtils;
import com.appdev360.jobsitesentry.util.UtilSnackbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.videolan.libvlc.MediaPlayer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abubaker on 3/12/18.
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class CameraStreamFragment extends BaseFragment implements CameraSteamMvpView {


    private static final int TIMEOUT = 5;
    private static final List<String> galleryPermissions = Arrays.asList(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);
    private final String TAG = CameraStreamFragment.this.getClass().getName();
    private final String STATE_IS_FULLSCREEN = "fullscreenstate";
    @BindView(R.id.guideline)
    Guideline guideline;
    Unbinder unbinder;
    @Inject
    CameraStreamPresenter presenter;

    @BindView(R.id.click_blocker)
    View clickBlocker;
    @BindView(R.id.icon_play)
    ImageView iconPlay;
    @BindView(R.id.icon_ptz)
    ImageView iconPtz;
    @BindView(R.id.icon_camera)
    ImageView iconCamera;
    @BindView(R.id.icon_full_screen)
    ImageView iconFullScreen;
    @BindView(R.id.back_arrow)
    ImageView backArrow;
    @BindView(R.id.controller_layout)
    ConstraintLayout controllerLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.camera_list_layout)
    ConstraintLayout cameraListLayout;
    //@BindView(R.id.videoView)
    TextureVlc mjpegView;
    @BindView(R.id.camera_name)
    TextView cameraName;
    @BindView(R.id.root_view)
    FrameLayout mRootLayout;

    @BindView(R.id.layout_bottom_sheet)
    ConstraintLayout layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;


    @Inject
    CameraAdapter mAdapter;
    ProgressDialog mProgressDialog;
    private boolean isShowFullScreen = false;
    private SentryLocationActivity activity;
    private boolean isVideoPlaying = false;
    private ArrayList<Camera> cameraList = null;
    private LinearLayoutManager lytManager;
    private boolean isFirstTime = true;
    private String lastPlayedLink = null;
    private Bitmap frameBitmap;
    private boolean isScreenVisible;
    private boolean isBackPressed = false; //To Avoid call


    private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);
    private UnitData unitData;
    private int channel = 0;

    @Override
    public void initViews(View parentView, Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity) getActivity();
        activity.setFullScreen(true);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);


        if (savedInstanceState != null) {
            isShowFullScreen = savedInstanceState.getBoolean(STATE_IS_FULLSCREEN);
            setViewsVisibilityForFullScreen(isShowFullScreen);
            activity.setFullScreen(isShowFullScreen);
        }
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        if (!this.unitData.getPtzEnabled()) {
            iconPtz.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.icon_full_screen, R.id.icon_play, R.id.back_arrow, R.id.icon_camera, R.id.icon_ptz,
            R.id.up, R.id.bottom, R.id.right, R.id.left,
            R.id.upLeft, R.id.upRight, R.id.bottomLeft, R.id.bottomRight,
            R.id.zoomin,
            R.id.zoomOut,
            R.id.btnReset,
            R.id.click_blocker
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icon_full_screen:
                if (isVideoPlaying) {
                    // activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                    activity.setRequestedOrientation(Build.VERSION.SDK_INT < 9 ?
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    // activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    MyToast.showMessage(getActivity(), "Streaming is in paused state");
                }


                break;
            case R.id.click_blocker:
                //do nothing
                break;
            case R.id.icon_ptz:
                toggleBottomSheet();
                break;
            case R.id.icon_play:
                if (isVideoPlaying) {
                    isVideoPlaying = false;

                    if (iconPlay != null)
                        iconPlay.setImageResource(R.drawable.video_play_icon);

                } else {

                    if (GeneralUtils.isNetworkConnected(getActivity())) {
                        if (iconPlay != null)
                            iconPlay.setImageResource(R.drawable.video_pause_icon);

                        isFirstTime = true;
                        loadIpCam(lastPlayedLink);
                        isVideoPlaying = true;
                    } else {
                        UtilSnackbar.showSnakbarError(parent, getResources().getString(R.string.internet_connection));
                    }
                }

                break;
            case R.id.back_arrow:
                if (!isShowFullScreen) {
                    activity.onBackPressed();
                    return;
                }
                changeOrientation();
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                break;
            case R.id.icon_camera:

                if (GeneralUtils.isNetworkConnected(getActivity())) {
                    if (PermissionsHelper.isExplicitPermissionsRequired()) {
                        if (PermissionsHelper.arePermissionGranted(getActivity(), galleryPermissions)) {

                            //If bitmap is not null
                            storeBitmap();

                        } else {

                            isScreenVisible = true;
                            PermissionsHelper.checkForMultiplePermission(getActivity(), GeneralConstants.SAVE_BITMAP,
                                    galleryPermissions);
                        }

                    } else {

                        //If bitmap is not null
                        storeBitmap();

                    }

                } else {
                    UtilSnackbar.showSnakbarError(parent, getResources().getString(R.string.screenshot_take_failed));
                }
                break;
            case R.id.up:
                ptzClick("Up", 1);
                break;
            case R.id.bottom:
                ptzClick("Down", 5);
                break;
            case R.id.right:
                ptzClick("Right", 3);
                break;
            case R.id.left:
                ptzClick("Left", 7);
                break;
            case R.id.upLeft:
                ptzClick("LeftUp", 8);
                break;
            case R.id.upRight:
                ptzClick("RightUp", 2);
                break;
            case R.id.bottomLeft:
                ptzClick("LeftDown", 6);
                break;
            case R.id.bottomRight:
                ptzClick("RightDown", 4);
                break;
            case R.id.zoomin:
                ptzClick("ZoomTele", -2);

                break;
            case R.id.zoomOut:
                ptzClick("ZoomWide", -3);

                break;
            case R.id.btnReset:
                ptzClick("Reset", -4);
                break;


        }
    }


    private void ptzClick(String code, int intCode) {

        //        URL url = new URL("http://166.167.220.158/cgi-bin/ptz.cgi?action=start&channel=1&code=ZoomWide&arg1=0&arg2=5&arg3=0");


        clickBlocker.setVisibility(View.VISIBLE);
        presenter.doPtz(unitData, String.valueOf(channel), code, intCode);


    }


    private void onBackPressed() {
        mjpegView.releasePlayer();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_camera_stream_layout;
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
        presenter.attachView(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mjpegView != null)
            mjpegView.releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(STATE_IS_FULLSCREEN, isShowFullScreen);
        super.onSaveInstanceState(outState);
    }

    /**
     * Toggles the views other than the video player visible
     * or gone depending on the full screen state.
     *
     * @param isFullScreen
     */
    private void setViewsVisibilityForFullScreen(boolean isFullScreen) {

        int viewVisibility = (isFullScreen) ? View.GONE : View.VISIBLE;

        if (isFullScreen) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
            params.guidePercent = 1.0f;
            guideline.setLayoutParams(params);
            backArrow.setVisibility(View.VISIBLE);
            mjpegView.setLayoutParams(mjpegView.getLayoutParams());
        } else {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
            params.guidePercent = 0.40f;
            guideline.setLayoutParams(params);
        }
    }


    public void setCamera(ArrayList<Camera> list) {
        cameraList = list;
    }

    public void setUnitData(UnitData unitData) {
        this.unitData = unitData;
        this.cameraList = unitData.getCameras();

    }

    @Override
    public void onPause() {
        if (!isScreenVisible) {
            if (mjpegView != null) {
                if (!isBackPressed) {
                    mjpegView.releasePlayer();
                }
            }
            activity.setFullScreen(false);
        }
        dismissDialog();
        super.onPause();
    }

    private void intiAdapter() {
        recyclerView.setHasFixedSize(true);
        lytManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter.addItems(cameraList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(new CameraAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(int position) {
                channel = position + 1;
                if (mjpegView != null) {

                    Single<Boolean> single = Single.create(new Single.OnSubscribe<Boolean>() {
                        @Override
                        public void call(SingleSubscriber<? super Boolean> singleSubscriber) {
                            singleSubscriber.onSuccess(true);
                        }
                    });

                    single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<Boolean>() {
                        @Override
                        public void onSuccess(Boolean value) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (iconPlay != null)
                                        iconPlay.setImageResource(R.drawable.video_play_icon);
                                    isFirstTime = true;
                                    loadIpCam(mAdapter.getDataSet().get(position).getCameraLink());
                                    cameraName.setText(mAdapter.getDataSet().get(position).getCameraNameFull());
                                }
                            }, 200);

                        }

                        @Override
                        public void onError(Throwable error) {

                        }
                    });


                } else {
                    isFirstTime = true;
                    loadIpCam(mAdapter.getDataSet().get(position).getCameraLink());
                    if (cameraName != null) {
                        cameraName.setText(mAdapter.getDataSet().get(position).getCameraNameFull());
                    }

                }

            }
        });
    }


    private void loadIpCam(String link) {
        showDialog("Loading...");
// testing ptz camera
//        link = "rtsp://admin:Sentry_2020!@166.167.220.158:554/cam/realmonitor?channel=1&subtype=1";
        lastPlayedLink = link;
        if (mjpegView != null) {
            mjpegView.releasePlayer();
        }
        mRootLayout.removeAllViews();
        mjpegView = new TextureVlc(getContext());
        mjpegView.setAudio(true);
        mjpegView.setURL(lastPlayedLink);
        mjpegView.setEventCallback(mPlayerListener);
        FrameLayout.LayoutParams textureView_params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mjpegView.setLayoutParams(textureView_params);
        mRootLayout.addView(mjpegView);

    }


    @Override
    public void onResume() {

        activity.setFullScreen(true);
        if (GeneralUtils.isNetworkConnected(getActivity())) {
            isFirstTime = true;
            loadFirstVideo();
        } else {
            UtilSnackbar.showSnakbarError(parent, getResources().getString(R.string.internet_connection));
        }
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            setViewsVisibilityForFullScreen(true);
            activity.setLandScape(true);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setViewsVisibilityForFullScreen(false);
            activity.setLandScape(false);

        }
    }

    private void changeOrientation() {
        isShowFullScreen = !isShowFullScreen;
        int activityOrientation = (isShowFullScreen) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setViewsVisibilityForFullScreen(isShowFullScreen);
        activity.setRequestedOrientation(activityOrientation);
    }

    private void storeBitmap() {
        frameBitmap = mjpegView.getBitmap();
        if (frameBitmap != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    ContentResolver resolver = requireActivity().getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    // Create an image file name
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    String imageName = "JPEG_" + timeStamp + "_";

                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, imageName + ".jpg");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
//                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/" + "JobSiteSentry");
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    OutputStream fos;

                    fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                    frameBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    Objects.requireNonNull(fos).close();

                    UtilSnackbar.showSnakbarSuccess(parent, "Screenshot saved");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ScreenshotUtils.store(getActivity(), frameBitmap);
                UtilSnackbar.showSnakbarSuccess(parent, "Screenshot saved");
            }
        } else {
            UtilSnackbar.showSnakbarError(parent, getResources().getString(R.string.screenshot_take_failed));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case GeneralConstants.SAVE_BITMAP: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for Camera Permission
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    storeBitmap();
                    isScreenVisible = false;

                } else {
                    // Permission Denied
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) storeBitmap();
                    else
                        MyToast.showMessage(getActivity(), getResources().getString(R.string.some_permission_denied));
                }

            }

            break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void showDialog(String message) {
        // TODO Auto-gensherated method stub
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(getActivity(), "", message);

            return;
        }
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog = ProgressDialog.show(getActivity(), "", message);
        }
    }

    public void dismissDialog() {
        // TODO Auto-generated method stub
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        Log.i("CameraStream", "Event Bus Message received");
        if (event.eventType.equals(EventBusMessage.EVENT_ON_BACK_PRESSED)) {
            isBackPressed = true;
            onBackPressed();
            activity.setFullScreen(false);
        }
    }


    private void loadFirstVideo() {
        Single<Boolean> single = Single.create(new Single.OnSubscribe<Boolean>() {
            @Override
            public void call(SingleSubscriber<? super Boolean> singleSubscriber) {

                singleSubscriber.onSuccess(true);

            }
        });
        single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<Boolean>() {
            @Override
            public void onSuccess(Boolean value) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (iconPlay != null)
                            iconPlay.setImageResource(R.drawable.video_play_icon);
                        isFirstTime = true;
                        loadIpCam(mAdapter.getDataSet().get(0).getCameraLink());
                        cameraName.setText(mAdapter.getDataSet().get(0).getCameraNameFull());
                    }
                }, 200);

            }

            @Override
            public void onError(Throwable error) {

            }
        });

    }

    @Override
    public void onPtzCallCompleted() {
        clickBlocker.setVisibility(View.GONE);

    }


    @Override
    public void onError(Error error) {
        MyToast.showMessage(getActivity(), error.getMessage());
        clickBlocker.setVisibility(View.GONE);

    }

    @Override
    public void onTokenExpire(Error error) {

    }

    @Override
    public void onChangePasswrod(Error error) {

    }

    private class MyPlayerListener implements MediaPlayer.EventListener {
        private WeakReference<CameraStreamFragment> mOwner;

        public MyPlayerListener(CameraStreamFragment owner) {
            mOwner = new WeakReference<CameraStreamFragment>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
            CameraStreamFragment player = mOwner.get();
            switch (event.type) {
                case MediaPlayer.Event.EndReached:
                    mjpegView.releasePlayer();
                    break;
                case MediaPlayer.Event.Playing:
                    isVideoPlaying = true;
                    break;
                case MediaPlayer.Event.TimeChanged:
                    dismissDialog();
                    break;
                case MediaPlayer.Event.Paused:
                case MediaPlayer.Event.Stopped:
                    isVideoPlaying = false;

                    break;
                case MediaPlayer.Event.EncounteredError:
                    dismissDialog();
                    mjpegView.releasePlayer();
                    break;
                default:
                    break;
            }
        }
    }

    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


}
