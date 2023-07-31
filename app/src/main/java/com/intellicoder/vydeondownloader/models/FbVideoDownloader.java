package com.intellicoder.vydeondownloader.models;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Keep;

import com.intellicoder.vydeondownloader.Interfaces.VideoDownloader;
import com.intellicoder.vydeondownloader.tasks.downloadFile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.intellicoder.vydeondownloader.tasks.downloadVideo.fromService;
import static com.intellicoder.vydeondownloader.tasks.downloadVideo.pd;

@Keep
public class FbVideoDownloader implements VideoDownloader {

    private Context context;
    private String VideoURL;
    private long DownLoadID;
    private String VideoTitle;

    String downlink;
    String matag;

    public FbVideoDownloader(Context context, String videoURL) {
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
            subFolder = new File(folder.getPath() + File.separator + "Facebook Videos");
            if (!subFolder.exists()) {
                success1 = subFolder.mkdirs();
            }
        }
        assert subFolder != null;
        return subFolder.getPath();
    }

    @Override
    public String getVideoId(String link) {
        return link;
    }

    @Override
    public void DownloadVideo() {
        downlink = VideoURL;
        new Fbwatch().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class Data extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection;
            BufferedReader reader;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String buffer = "No URL";
                String Line;
                while ((Line = reader.readLine()) != null) {
                    if (Line.contains("og:video:url")) {
                        Line = Line.substring(Line.indexOf("og:video:url"));
                        if (Line.contains("og:title")) {
                            VideoTitle = Line.substring(Line.indexOf("og:title"));
                            VideoTitle = VideoTitle.substring(ordinalIndexOf(VideoTitle, "\"", 1) + 1, ordinalIndexOf(VideoTitle, "\"", 2));
                        }
                        Line = Line.substring(ordinalIndexOf(Line, "\"", 1) + 1, ordinalIndexOf(Line, "\"", 2));
                        if (Line.contains("amp;")) {
                            Line = Line.replace("amp;", "");
                        }
                        if (!Line.contains("https")) {
                            Line = Line.replace("http", "https");
                        }
                        buffer = Line;
                        break;
                    } else {
                        buffer = "No URL";
                    }
                }
                return buffer;
            } catch (IOException e) {
                if (!fromService) {
                    pd.dismiss();
                }
                return "No URL";

            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.contains("No URL")) {
                VideoTitle = "fbVideo" + System.currentTimeMillis() + ".mp4";
                try {
                    if(s != null)
                    new downloadFile().Downloading(context, s, VideoTitle, ".mp4");
                    if (!fromService) {
                        pd.dismiss();
                    }
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(context, "Video Can't be downloaded! Try Again", Toast.LENGTH_SHORT).show();
                    Looper.loop();

                    if (!fromService) {
                        pd.dismiss();
                    }
                }

            } else {
                Looper.prepare();
                Toast.makeText(context, "Wrong Video URL or Check Internet Connection", Toast.LENGTH_SHORT).show();
                Looper.loop();

                if (!fromService) {
                    pd.dismiss();
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Looper.prepare();
            Toast.makeText(context, "Video Can't be downloaded! Try Again", Toast.LENGTH_SHORT).show();
            Looper.loop();

            if (!fromService) {
                pd.dismiss();
            }
        }
    }


    private class Fbwatch extends AsyncTask<Void, Void, Void> {

        String title;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://fbdown.net/download.php")
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .timeout(100 * 1000)
                        .post();

                Element link = doc.body().getElementById("chrome-webstore-item");
                String atag = link.select("a").first().attr("href");
               // atag = URLDecoder.decode(atag, "UTF-8");
                Log.e("TextLink", atag);

                matag = atag;
//                }
                Log.e("Main12346", matag);
            } catch (Exception e) {
                if (!fromService) {
                    pd.dismiss();
                }
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (!fromService) {
                    pd.dismiss();
                }
                if(matag !=null )
                new downloadFile().Downloading(context, matag, "Facebook_" + System.currentTimeMillis(), ".mp4");

            } catch (Exception e) {
                if (!fromService) {
                    pd.dismiss();
                }
            }
        }
    }


}
