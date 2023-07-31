package com.intellicoder.vydeondownloader;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.intellicoder.vydeondownloader.databinding.ActivityVideoPlay2Binding;


public class VideoPlayActivity2 extends AppCompatActivity {

    String urls;
    private ActivityVideoPlay2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlay2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        try {
//            urls = getIntent().getStringExtra("videourl");
//            binding.videoView.setMediaController(binding.mediaController);
//            binding.videoView.setVideoURI(Uri.parse(urls));
//            binding.videoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
//
//                @Override
//                public void onScaleChange(boolean isFullscreen) {
//
//                }
//
//                @Override
//                public void onPause(MediaPlayer mediaPlayer) { // Video pause
//                }
//
//                @Override
//                public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
//                }
//
//                @Override
//                public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
//                }
//
//                @Override
//                public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
//                }
//
//            });
//
//            binding.storyVideo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    binding.storyVideo.setVisibility(View.GONE);
//                    binding.videoView.start();
//                }
//            });
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
