//package com.appdev360.jobsitesentry.ui.timelapse.component;
//
//import android.app.Activity;
//import android.content.Context;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.VideoView;
//
//import com.appdev360.jobsitesentry.widget.VideoControllerView;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class VideoPlaybackManager implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl{
//
//   /* private final String TESTING_VIDEO_URL = "http://storage.googleapis.com/jobsitesentry/70_1/videos/18-04-04_12:30:11_5ac4c5532adc2.mp4";*/
//        private final String TESTING_VIDEO_URL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
//
//
//    public interface FullscreenBtnClickListener{
//
//        void onFullScreenClicked();
//    }
//
//    public interface PlayerListener{
//
//        void onPlayerReady();
//
//        void onVideoCompleted();
//
//        void onPlayPressed();
//
//        void onPausePressed();
//    }
//
//    private PlayerListener playerListener;
//
//    private FullscreenBtnClickListener fullscreenBtnClickListener;
//
//    private Context context;
//
//    private int seekToTime = -1;
//
//    private FrameLayout videoSurfaceContainer;
//    private SurfaceView videoSurface;
//    private SurfaceHolder videoHolder;
//    private MediaPlayer player;
//    private VideoView videoView;
//
//    private VideoControllerView controller;
//    private ProgressBar progressBarVideo;
//    private ImageView playIcon;
//    private String url = null;
//
//
//    public VideoPlaybackManager(Context context, FrameLayout videoSurfaceContainer, SurfaceView videoSurface,
//                                ProgressBar progressBarVideo,String url){
//
//        this.context = context;
//
//        this.progressBarVideo = progressBarVideo;
//
//        this.url = url;
//
//        player = new MediaPlayer();
//        initPlayer();
//
//        this.videoSurfaceContainer = videoSurfaceContainer;
//        videoSurfaceContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(controller!=null){
//
//                    if(controller.isShowing())
//                        controller.hide();
//                    else controller.show();
//
//                }
//            }
//        });
//
//
//        this.videoSurface = videoSurface;
//        videoHolder = videoSurface.getHolder();
//        videoHolder.addCallback(this);
//
//
//        controller = new VideoControllerView(context);
//
//    }
//
//    public void setFullscreenBtnClickListener(FullscreenBtnClickListener fullscreenBtnClickListener){
//        this.fullscreenBtnClickListener = fullscreenBtnClickListener;
//    }
//
//
//    public void setPlayerListener(PlayerListener playerListener){
//        this.playerListener = playerListener;
//    }
//
//
//    public void startPlayer(){
//        player.start();
//    }
//
//    public void startPlayer(int atPosition){
//
//        player.seekTo(atPosition);
//        player.start();
//    }
//
//
//
//    //--- helpers ---
//
//    private void initPlayer(){
//
//        try {
//
//            Map<String, String> headers = new HashMap<>();
//            headers.put("Content-Type", "video/mp4"); // change content type if necessary
//            headers.put("Accept-Ranges", "bytes");
//            headers.put("Status", "206");
//            headers.put("Cache-control", "no-cache");
//            Uri uri = Uri.parse(url);
//
//            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            player.reset();
//            player.setDataSource(context, uri,headers);
//            player.setOnPreparedListener(this);
//        }
//        catch (IllegalArgumentException e) { e.printStackTrace(); }
//        catch (SecurityException e) { e.printStackTrace(); }
//        catch (IllegalStateException e) { e.printStackTrace(); }
//        catch (IOException e) { e.printStackTrace(); }
//    }
//
//
//    // SurfaceHolder.Callback impl ---
//
//   private boolean isPrepared;
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//
//        if(!isPrepared) {
//            player.prepareAsync();
//            if (progressBarVideo != null){
//                progressBarVideo.setVisibility(View.VISIBLE);
//            }
//
//        }
//
//        player.setDisplay(holder);
//        isPrepared = true;
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        player.pause();
//
//    }
//
//
//    // --- MediaPlayer.OnPreparedListener impl ---
//
//    @Override
//    public void onPrepared(MediaPlayer mp) {
//
//        // so it fits on the screen
//        int videoWidth = player.getVideoWidth();
//        int videoHeight = player.getVideoHeight();
//        float videoProportion = (float) videoWidth / (float) videoHeight;
//        int screenWidth =((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
//        int screenHeight = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
//        float screenProportion = (float) screenWidth / (float) screenHeight;
//        android.view.ViewGroup.LayoutParams lp = videoSurface.getLayoutParams();
//
//        if (videoProportion > screenProportion) {
//            lp.width = screenWidth;
//            lp.height = (int) ((float) screenWidth / videoProportion);
//        } else {
//            lp.width = (int) (videoProportion * (float) screenHeight);
//            lp.height = screenHeight;
//        }
//
//        if (videoSurface != null){
//            videoSurface.setLayoutParams(lp);
//        }
//
//
//
//        controller.setMediaPlayer(this);
//        controller.setAnchorView(videoSurfaceContainer);
//        if (progressBarVideo != null){
//            progressBarVideo.setVisibility(View.GONE);
//        }
//
//
//
//        if(playerListener != null) {
//            playerListener.onPlayerReady();
//
//            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    playerListener.onVideoCompleted();
//                }
//            });
//        }
//    }
//
//
//    // --- VideoControllerView.MediaPlayerControl impl ---
//
//    @Override
//    public void start() {
//        player.start();
//
//        if(playerListener != null) {
//            playerListener.onPlayPressed();
//        }
//    }
//
//    @Override
//    public void pause() {
//        player.pause();
//        if(playerListener != null) {
//            playerListener.onPausePressed();
//        }
//    }
//
//    @Override
//    public int getDuration() {
//        return player.getDuration();
//    }
//
//    @Override
//    public int getCurrentPosition() {
//        return player.getCurrentPosition();
//    }
//
//    @Override
//    public void seekTo(int pos) {
//        player.seekTo(pos);
//    }
//
//    @Override
//    public boolean isPlaying() {
//        return player.isPlaying();
//    }
//
//    @Override
//    public int getBufferPercentage() {
//        return 0;
//    }
//
//    @Override
//    public boolean canPause() {
//        return true;
//    }
//
//    @Override
//    public boolean canSeekBackward() {
//        return true;
//    }
//
//    @Override
//    public boolean canSeekForward() {
//        return true;
//    }
//
//    @Override
//    public boolean isFullScreen() {
//        return false;
//    }
//
//    @Override
//    public void toggleFullScreen() {
//
//        fullscreenBtnClickListener.onFullScreenClicked();
//    }
//
//
//}
