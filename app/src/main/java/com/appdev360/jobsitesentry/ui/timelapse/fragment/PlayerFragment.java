package com.appdev360.jobsitesentry.ui.timelapse.fragment;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.potyvideo.library.AndExoPlayerView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlayerFragment extends BaseFragment {

    private final String TAG = "TimeLapsePlayerFragment";

    private final String STATE_IS_FULLSCREEN = "fullscreenstate";
    private final String STATE_CURRENT_PLACE = "currentplaceinvideo";

    Unbinder unbinder;
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
    private SentryLocationActivity activity;
    private String url = null;

    private boolean isInstanceStateNull;

    private boolean isShowFullScreen = false;
    private int seekToTime = -1;
//    private VideoPlaybackManager videoPlaybackManager;

    @BindView(R.id.andExoPlayerView)
    AndExoPlayerView andExoPlayerView;

    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {

        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity) getActivity();
        isInstanceStateNull = (saveInstanceState == null);
        initVideoPlayer();
    }

    @Override
    public int getLayoutId() {
        return R.layout.time_lapse_player_fragment_layout;
    }

    @Override
    public void updateFragmentReference() {

    }

    @Override
    public void updateActionState(boolean action, View v) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(STATE_IS_FULLSCREEN, isShowFullScreen);
        outState.putInt(STATE_CURRENT_PLACE, seekToTime);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void initVideoPlayer() {

        HashMap<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("foo", "bar");
        andExoPlayerView.setSource(url, extraHeaders);

//        videoPlaybackManager = new VideoPlaybackManager(getActivity(), (FrameLayout) videoSurfaceContainer,
//                (SurfaceView) videoSurface, (ProgressBar) loadVideoProgress, url);

//        videoPlaybackManager.setFullscreenBtnClickListener(new VideoPlaybackManager.FullscreenBtnClickListener() {
//            @Override
//            public void onFullScreenClicked() {
//                showToast("onFullScreenClicked");
//
//                isShowFullScreen = !isShowFullScreen;
//                seekToTime = videoPlaybackManager != null ? videoPlaybackManager.getCurrentPosition() : -1;
//                int activityOrientation = (isShowFullScreen) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//                activity.setRequestedOrientation(activityOrientation);
//            }
//        });

//        videoPlaybackManager.setPlayerListener(new VideoPlaybackManager.PlayerListener() {
//
//            @Override
//            public void onPlayerReady() {
//                showToast("onPlayerReady");
//
//                if (seekToTime != -1)
//                    videoPlaybackManager.startPlayer(seekToTime);
//                else
//                    videoPlaybackManager.startPlayer();
//                videoPlaybackManager.pause();
//                if (isInstanceStateNull)
//                    if (videoStartPlayingContainer != null) {
//                        videoStartPlayingContainer.setVisibility(View.VISIBLE);
//                    }
//                if (videoSurface != null) {
//                    videoSurface.setBackgroundResource(0);
//                }
//            }
//
//            @Override
//            public void onVideoCompleted() {
//                //todo inspect why getActivity() returns null at times.
//                showToast("onVideoCompleted");
//                if (getActivity() != null) {
//                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_in);
//                }
//            }
//
//            @Override
//            public void onPlayPressed() {
//                showToast("onPlayPressed");
//                if (videoStartPlayingContainer != null) {
//                    videoStartPlayingContainer.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onPausePressed() {
//                showToast("onPausePressed");
//                if (videoStartPlayingContainer != null) {
//                    videoStartPlayingContainer.setVisibility(View.VISIBLE);
//                }
//            }
//        });

//        startVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showToast("onClick");
//                if (videoStartPlayingContainer != null) {
//                    videoStartPlayingContainer.setVisibility(View.GONE);
//                }
//                if (videoPlaybackManager != null) {
//                    videoPlaybackManager.startPlayer();
//                }
//            }
//        });
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

    public void setUrl(String url) {
        this.url = url;
    }
}
