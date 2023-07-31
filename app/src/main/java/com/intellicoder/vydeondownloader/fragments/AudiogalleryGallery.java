package com.intellicoder.vydeondownloader.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intellicoder.vydeondownloader.R;
import com.intellicoder.vydeondownloader.adapters.SongAdapter;
import com.intellicoder.vydeondownloader.models.SongInfo;

import java.io.File;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.intellicoder.vydeondownloader.utils.Constants.MY_ANDROID_10_IDENTIFIER_OF_FILE;

public class AudiogalleryGallery extends Fragment implements Runnable {


    private ArrayList<SongInfo> _songs;

    RecyclerView recyclerView;
    SeekBar seekBar;
    SongAdapter songAdapter;
    MediaPlayer mediaPlayer;
    private final Handler myHandler = new Handler();
    TextView seekBarHint;

    ProgressDialog mDialog;
    private TextView noresultfound;
    private AudiogalleryGallery.FetchRecordingsAsyncTask fetchRecordingsAsyncTask;
    private Runnable runnable;
    private TextView pausePlayTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audiogallery, container, false);


        seekBarHint = (TextView) view.findViewById(R.id.textView);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        noresultfound = (TextView) view.findViewById(R.id.noresultfound);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);

        _songs = new ArrayList<SongInfo>();

        songAdapter = new SongAdapter(getActivity(), _songs);
        recyclerView.setAdapter(songAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                //   seekBarHint.setVisibility(View.VISIBLE);
                int x = (int) Math.ceil(progress / 1000f);

                if (x < 10)
                    seekBarHint.setText("0:0" + x);
                else
                    seekBarHint.setText("0:" + x);

                double percent = progress / (double) seekBar.getMax();
                int offset = seekBar.getThumbOffset();
                int seekWidth = seekBar.getWidth();
                int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                int labelWidth = seekBarHint.getWidth();
                seekBarHint.setX(offset + seekBar.getX() + val
                        - Math.round(percent * offset)
                        - Math.round(percent * labelWidth / 2));

                if (progress > 0 && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    clearMediaPlayer();
                    seekBar.setProgress(0);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //  seekBarHint.setVisibility(View.VISIBLE);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }


        });
        songAdapter.setOnItemClickListener((b, view1, obj, position) -> {
            try {
                pausePlayTV = b;
                if (b.getText().equals("Stop")) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    b.setText("Play");
                } else {


                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.pause();
                        mediaPlayer.release();
                        mediaPlayer = null;

                    }

                    runnable = () -> {
                        try {
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(obj.getSongUrl());
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                    seekBar.setProgress(0);
                                    seekBar.setMax(mediaPlayer.getDuration());
                                    Log.d("Prog", "run: " + mediaPlayer.getDuration());
                                }
                            });
                            b.setText("Stop");


                        } catch (Exception e) {
                        }
                    };
                    myHandler.postDelayed(runnable, 100);

                }
            } catch (Exception e) {
                b.setText("Play");
            }
        });


        fetchRecordingsAsyncTask = new AudiogalleryGallery.FetchRecordingsAsyncTask(getActivity());
        fetchRecordingsAsyncTask.execute();
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(getString(R.string.loadingfiles));
        mDialog.show();

        Thread t = new runThread();
        t.start();


        return view;
    }

    @Override
    public void run() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();


        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }

            seekBar.setProgress(currentPosition);

        }

        Log.e("", "");
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public class runThread extends Thread {


        @Override
        public void run() {
            while (true) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("Runwa", "run: " + 1);
                if (mediaPlayer != null) {
                    seekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                            } catch (Exception e) {

                            }
                        }
                    });

                    Log.d("Runwa", "run: " + mediaPlayer.getCurrentPosition());
                } else {
                    if(getActivity()!=null)
                    getActivity().runOnUiThread(() -> {
                        if (pausePlayTV != null)
                            pausePlayTV.setText("Play");
                    });
                }
            }
        }
    }

    private void getAllFiles() {
        String location = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) + "";

        File[] files = new File(location).listFiles();
        if (files != null) {
            for (File file : files) {

                if (file.getName().contains(MY_ANDROID_10_IDENTIFIER_OF_FILE) && file.getName().endsWith(".mp3") || file.getName().endsWith(".m4a") || file.getName().endsWith(".wav")) {
                    Uri vv = Uri.parse(file.getAbsolutePath());
                    SongInfo s = new SongInfo(file.getName(), file.getName(), vv.toString());
                    _songs.add(s);
                    songAdapter.notifyDataSetChanged();
                }
            }
            if (mDialog != null) {
                mDialog.dismiss();
            }
        } else {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    noresultfound.setVisibility(View.VISIBLE);

                }
            });
        }
    }


    private class FetchRecordingsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog;

        public FetchRecordingsAsyncTask(Context activity) {
            dialog = new ProgressDialog(activity, R.style.AppTheme_Dark_Dialog);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getString(R.string.loadingdata));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {
            try {
                getAllFiles();

            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (dialog.isShowing()) {
                dialog.dismiss();
                if (fetchRecordingsAsyncTask != null) {
                    fetchRecordingsAsyncTask.cancel(true);
                }
            }
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            myHandler.removeCallbacks(runnable);

            if (mDialog != null) {
                mDialog.dismiss();
            }

            fetchRecordingsAsyncTask.cancel(true);
        } catch (Exception e) {

        }


    }
}