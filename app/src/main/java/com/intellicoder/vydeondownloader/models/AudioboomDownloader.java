package com.intellicoder.vydeondownloader.models;

import android.content.Context;
import android.os.AsyncTask;

import com.intellicoder.vydeondownloader.R;
import com.intellicoder.vydeondownloader.tasks.downloadFile;
import com.intellicoder.vydeondownloader.utils.iUtils;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.intellicoder.vydeondownloader.tasks.downloadVideo.Mcontext;
import static com.intellicoder.vydeondownloader.tasks.downloadVideo.fromService;
import static com.intellicoder.vydeondownloader.tasks.downloadVideo.pd;

public class AudioboomDownloader {

    private Context context;
    private String FinalURL;
    private String VideoURL;

    public AudioboomDownloader(Context context, String vid) {
        this.context = context;
        VideoURL = vid;
    }

    public void DownloadVideo() {
        new CallAudioboomData().execute(VideoURL);
    }


    public static class CallAudioboomData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {
            boolean isSecon = false;

            try {

                if (!fromService) {

                    pd.dismiss();
                }
                System.out.println("myresponseis111 exp166 " + document);


                String data = "";

                Elements elements = document.select("div");
                for (Element element : elements) {
                    if (element.attr("data-auto-play-primary-item").equals("1")) {
                        //Save As you want to

                        String list_of_qualities = element.getElementsByAttribute("data-new-clip-store").attr("data-new-clip-store").replace("&quot;", "\"");


                        JSONObject obj = new JSONObject(list_of_qualities);


                        String replaceString = obj.getJSONArray("clips").getJSONObject(0).getString("clipURLPriorToLoading");

                        System.out.println("myresponseis111 list_of_qualities" + replaceString);

                        new downloadFile().Downloading(Mcontext, replaceString, "Audioboom_" + System.currentTimeMillis(), ".mp3");


                    }
                }


            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }


}
