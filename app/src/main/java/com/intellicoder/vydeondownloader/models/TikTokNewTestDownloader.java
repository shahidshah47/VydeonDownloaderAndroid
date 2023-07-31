package com.intellicoder.vydeondownloader.models;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.Keep;

import com.intellicoder.vydeondownloader.R;
import com.intellicoder.vydeondownloader.utils.iUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

import static com.intellicoder.vydeondownloader.tasks.downloadVideo.Mcontext;
import static com.intellicoder.vydeondownloader.tasks.downloadVideo.fromService;
import static com.intellicoder.vydeondownloader.tasks.downloadVideo.pd;

@Keep
public class TikTokNewTestDownloader {

    private Context context;
    private String FinalURL;
    private String VideoURL;
    static Map<String, String> map = new HashMap<>();
    static Map<String, String> mapData = new HashMap<>();
    static Map<String, String> cookiesmap = new HashMap<>();

    public TikTokNewTestDownloader(Context context, String vid) {
        this.context = context;
        VideoURL = vid;
        //headers
        map.put("cache-contro", "no-cache");
        map.put("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        map.put("origin", "https://tiktokdownload.online");
        map.put("postman-token", "c866af6b-b900-cf0f-2043-1296b0e5362a");
        map.put("sec-fetch-dest", "empty");
        map.put("sec-fetch-mode", "cors");
        map.put("sec-fetch-site", "same-origin");
        map.put("user-agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; rv:1.7.3) Gecko/20041001 Firefox/0.10.1");
        map.put("x-http-method-override", "POST");
        map.put("x-ic-request", "true");
        map.put("x-requested-with", "XMLHttpRequest");
        map.put("Cookie", "__cfduid=d978d3a2463cda3bf2605f51e1f07fff21615866354; PHPSESSID=203578fe76f7652faf96acd390942dd2");
        //formdata
        mapData.put("ic-request", "true");
        mapData.put("id", VideoURL);
        mapData.put("ic-element-id", "main_page_form");
        mapData.put("ic-id", "1");
        mapData.put("ic-target-id", "active_container");
        mapData.put("ic-trigger-id", "main_page_form");
        mapData.put("token", "493eaebbf47aa90e1cdfa0f8faf7d04cle0f45a38aa759c6a13fea91d5036dc3b");
        mapData.put("ic-current-url", "");
        mapData.put("ic-select-from-response", "#id4fbbea");
        mapData.put("_method", "nPOST");

        //cookies
        //  cookiesmap.put()

    }

    public void DownloadVideo() {
        new CalltikinData().execute("https://tiktokdownload.online/results");
    }


    public static class CalltikinData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";


        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0])
                        //   .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36")
                        .headers(map)
                        .data(mapData)
                        //   .cookies(cookiesmap)
                        .post();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            try {

                if (!fromService) {

                    pd.dismiss();
                }
                // System.out.println("myresponseis111 exp166 " + document.outerHtml());
                System.out.println("myresponseis111 exp166 3333 " + document.select("a").get(7));
                //  System.out.println("myresponseis111 exp166 3333 " + document.getElementsByClass("btn btn-primary download_link without_watermark"));
//                System.out.println("myresponseis111 exp177 " + document.select("video[src]").first().attr("src"));


//                String data = "";
//                ArrayList<String> arrayList = new ArrayList<>();
//

                Elements elements = document.getElementsByAttributeValue("href", "http");

                for (Element element : elements) {
                    System.out.println("myresponseis111 exp166 4466 " + element.getElementsByAttribute("href").attr("href"));


                    if (element.attr("href").contains("http")) {
                        //Save As you want to

                        String myurlis = element.getElementsByAttribute("href").attr("href");
                        System.out.println("myresponseis111 exp166 44 " + myurlis);

                        //  new downloadFile().Downloading(Mcontext, myurlis, "Tiktok_" + System.currentTimeMillis(), ".mp4");
                    }
                }
////
//

//                CharSequence[] charSequenceArr = new CharSequence[arrayList.size()];
//
//                charSequenceArr[0] = "1080p quality";
//                charSequenceArr[1] = "720p quality";
//                charSequenceArr[2] = "480p quality";
//
//                new AlertDialog.Builder(Mcontext).setTitle("Quality!").setItems(charSequenceArr, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        new downloadFile().Downloading(Mcontext, arrayList.get(i), "Mashable_" + System.currentTimeMillis(), ".mp4");
//
//                    }
//                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (!fromService) {
//
//                            pd.dismiss();
//                        }
//                    }
//                }).setCancelable(false).show();


                //   iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
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
