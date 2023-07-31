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

public class SolidfilesDownloader {

    private Context context;
    private String FinalURL;
    private String VideoURL;

    public SolidfilesDownloader(Context context, String vid) {
        this.context = context;
        VideoURL = vid;
    }

    public void DownloadVideo() {
        new CallSolidfilesData().execute(VideoURL);
    }


    public static class CallSolidfilesData extends AsyncTask<String, Void, Document> {
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

                Elements elements = document.select("script");
                for (Element element : elements) {
                    if (element.html().contains("viewerOptions")) {
                        //Save As you want to

                        String list_of_qualities = element.html();


                        list_of_qualities = list_of_qualities.substring(list_of_qualities.indexOf("viewerOptions',") + 15, list_of_qualities.indexOf("});")) + "}";

                        JSONObject obj = new JSONObject(list_of_qualities);


                        String replaceString = obj.getString("downloadUrl");

                        String ext = replaceString.substring(replaceString.length() - 4);


                        System.out.println("myresponseis111 list_of_qualities" + replaceString);
                        if (ext.equals(".gif") || ext.equals("png")) {
                            new downloadFile().DownloadingInsta(Mcontext, replaceString, "Solidfiles_" + System.currentTimeMillis(), ext);
                        } else {
                            new downloadFile().Downloading(Mcontext, replaceString, "Solidfiles_" + System.currentTimeMillis(), ext);
                        }


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
