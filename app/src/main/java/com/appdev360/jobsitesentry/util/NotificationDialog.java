//package com.appdev360.jobsitesentry.util;
//
//import android.app.Dialog;
//import android.content.pm.ActivityInfo;
//import android.os.Bundle;
//import androidx.fragment.app.DialogFragment;
//import android.view.LayoutInflater;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//
//import com.appdev360.jobsitesentry.R;
//import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
//import com.appdev360.jobsitesentry.ui.timelapse.component.VideoPlaybackManager;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
//
///**
// * Created by Abubaker on 02,May,2018
// */
//public class NotificationDialog extends DialogFragment {
//
//    public static final String TAG = "NotificationDialog";
//
//    private final String STATE_IS_FULLSCREEN = "fullscreenstate";
//    private final String STATE_CURRENT_PLACE = "currentplaceinvideo";
//
//    Unbinder unbinder;
//    @BindView(R.id.videoSurface)
//    SurfaceView videoSurface;
//    @BindView(R.id.load_video_progress)
//    ProgressBar loadVideoProgress;
//    @BindView(R.id.start_video)
//    ImageButton startVideo;
//    @BindView(R.id.video_start_playing_container)
//    RelativeLayout videoStartPlayingContainer;
//    @BindView(R.id.videoSurfaceContainer)
//    FrameLayout videoSurfaceContainer;
//
//    private SentryLocationActivity activity;
//   // private String url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
//    private String url = null;
//
//    private boolean isInstanceStateNull;
//
//    private boolean isShowFullScreen = false;
//    private int seekToTime = -1;
//    private VideoPlaybackManager videoPlaybackManager;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            dialog.getWindow().setLayout(width, height);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
//        super.onCreateView(inflater, parent, state);
//
//        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_layout, parent, false);
//        unbinder = ButterKnife.bind(this, view);
//        activity = (SentryLocationActivity) getActivity();
//
//
//
//        initVideoPlayer();
//     //   unbinder = ButterKnife.bind(this, view);
//
//        return view;
//    }
//
//
//
//
//    private void initVideoPlayer() {
//
//        videoPlaybackManager = new VideoPlaybackManager(getActivity(), (FrameLayout) videoSurfaceContainer,
//                (SurfaceView) videoSurface, (ProgressBar) loadVideoProgress, url);
//
//        videoPlaybackManager.setFullscreenBtnClickListener(new VideoPlaybackManager.FullscreenBtnClickListener() {
//            @Override
//            public void onFullScreenClicked() {
//
//                isShowFullScreen = !isShowFullScreen;
//                seekToTime = videoPlaybackManager != null ? videoPlaybackManager.getCurrentPosition() : -1;
//
//                int activityOrientation = (isShowFullScreen) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//
//                activity.setRequestedOrientation(activityOrientation);
//            }
//        });
//
//
//        videoPlaybackManager.setPlayerListener(new VideoPlaybackManager.PlayerListener() {
//
//            @Override
//            public void onPlayerReady() {
//
//
//                if (seekToTime != -1)
//                    videoPlaybackManager.startPlayer(seekToTime);
//                else
//                    videoPlaybackManager.startPlayer();
//
//                videoPlaybackManager.pause();
//
//                if (isInstanceStateNull)
//                    videoStartPlayingContainer.setVisibility(View.VISIBLE);
//
//
//
//            }
//
//            @Override
//            public void onVideoCompleted() {
//
//
//                //todo inspect why getActivity() returns null at times.
//                if (getActivity() != null) {
//                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_in);
//
//                }
//            }
//
//
//            @Override
//            public void onPlayPressed() {
//                videoStartPlayingContainer.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onPausePressed() {
//
//                if(videoStartPlayingContainer != null){
//                    videoStartPlayingContainer.setVisibility(View.VISIBLE);
//                }
//
//
//
//            }
//        });
//
//        startVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                videoSurface.setBackgroundResource(0);
//                videoStartPlayingContainer.setVisibility(View.GONE);
//                videoPlaybackManager.startPlayer();
//
//            }
//        });
//
//
//
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    public void setUrl(String url) {
//
//        this.url = url;
//    }
//
//}
