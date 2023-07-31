package com.intellicoder.vydeondownloader.models;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.intellicoder.vydeondownloader.Interfaces.VideoDownloader;
import com.intellicoder.vydeondownloader.tasks.downloadFile;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;

import static com.intellicoder.vydeondownloader.tasks.downloadVideo.fromService;
import static com.intellicoder.vydeondownloader.tasks.downloadVideo.pd;

public class TwitterVideoDownloader implements VideoDownloader {

    private Context context;
    private String VideoURL;
    private String VideoTitle;

    public TwitterVideoDownloader(Context context, String videoURL) {
        this.context = context;
        VideoURL = videoURL;
    }

    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }

    @Override
    public String createDirectory() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "My Video Downloader");

        File subFolder = null;
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        } else {
            boolean success1 = true;
            subFolder = new File(folder.getPath() + File.separator + "Twitter Videos");
            if (!subFolder.exists()) {
                success1 = subFolder.mkdirs();
            }
        }
        assert subFolder != null;
        return subFolder.getPath();
    }

    @Override
    public String getVideoId(String link) {
        if (link.contains("?")) {
            link = link.substring(link.indexOf("status"));
            link = link.substring(link.indexOf("/") + 1, link.indexOf("?"));
        } else {
            link = link.substring(link.indexOf("status"));
            link = link.substring(link.indexOf("/") + 1);
        }
        return link;
    }

    @Override
    public void DownloadVideo() {
        AndroidNetworking.post("https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php")
                .addBodyParameter("id", getVideoId(VideoURL))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Hello", response.toString());
                        String URL = response.toString();
                        if (URL.contains("url")) {
                            URL = URL.substring(URL.indexOf("url"));
                            URL = URL.substring(ordinalIndexOf(URL, "\"", 1) + 1, ordinalIndexOf(URL, "\"", 2));
                            if (URL.contains("\\")) {
                                URL = URL.replace("\\", "");
                            }
                            //Log.e("HelloURL",URL);
                            if (URLUtil.isValidUrl(URL)) {

                                if (!fromService) {

                                    pd.dismiss();
                                }
                                String path = createDirectory();
                                if (VideoTitle == null || VideoTitle.equals("")) {
                                    VideoTitle = "TwitterVideo" + new Date().toString() + ".mp4";
                                } else {
                                    VideoTitle = VideoTitle + ".mp4";
                                }
                                File newFile = new File(path, VideoTitle);

                                new downloadFile().Downloading(context, URL, VideoTitle, ".mp4");

                            } else {
                                if (!fromService) {

                                    pd.dismiss();
                                }
                                if (Looper.myLooper() == null)
                                    Looper.prepare();
                                Toast.makeText(context, "No Video Found", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } else {
                            if (!fromService) {

                                pd.dismiss();
                            }
                            if (Looper.myLooper() == null)
                                Looper.prepare();
                            Toast.makeText(context, "No Video Found", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (!fromService) {

                            pd.dismiss();
                        }
                        if (Looper.myLooper() == null)
                            Looper.prepare();
                        Toast.makeText(context, "Invalid Video URL", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });
    }
}
