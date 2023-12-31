package com.intellicoder.vydeondownloader.tasks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.upstream.DataSchemeDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;
import com.intellicoder.vydeondownloader.R;
import com.intellicoder.vydeondownloader.TikTokDownloadWebview;
import com.intellicoder.vydeondownloader.adapters.QualityBottomsheetAdapter;
import com.intellicoder.vydeondownloader.models.AllocineDownloader;
import com.intellicoder.vydeondownloader.models.AparatDownloader;
import com.intellicoder.vydeondownloader.models.AudioboomDownloader;
import com.intellicoder.vydeondownloader.models.DailyMotionDownloader;
import com.intellicoder.vydeondownloader.models.DlApismodels.DLDataParser;
import com.intellicoder.vydeondownloader.models.DlApismodels.Format;
import com.intellicoder.vydeondownloader.models.DlApismodels.Video;
import com.intellicoder.vydeondownloader.models.FbVideoDownloader;
import com.intellicoder.vydeondownloader.models.KickstarterDownloader;
import com.intellicoder.vydeondownloader.models.KooDownloader;
import com.intellicoder.vydeondownloader.models.MashableDownloader;
import com.intellicoder.vydeondownloader.models.SolidfilesDownloader;
import com.intellicoder.vydeondownloader.models.TopBuzzDownloader;
import com.intellicoder.vydeondownloader.models.Twenty_min_ch_Downloader;
import com.intellicoder.vydeondownloader.models.TwitterVideoDownloader;
import com.intellicoder.vydeondownloader.restapiclientsstuff.RetrofitApiInterface;
import com.intellicoder.vydeondownloader.restapiclientsstuff.RetrofitClient;
import com.intellicoder.vydeondownloader.utils.Constants;
import com.intellicoder.vydeondownloader.utils.iUtils;
import com.yxcorp.gifshow.util.CPU;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;
import static com.intellicoder.vydeondownloader.utils.Constants.DlApisUrl;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class downloadVideo {


    private static final int DOWNLOAD_NOTIFICATION_ID = 231;
    public static Context Mcontext;
    public static ProgressDialog pd;
    public static Dialog dialog;
    public static SharedPreferences prefs;
    public static Boolean fromService;
    static String SessionID, Title;
    static int error = 1;
    static LinearLayout mainLayout;
    static Dialog dialogquality;
    static WindowManager windowManager2;
    static WindowManager.LayoutParams params;
    static View mChatHeadView;
    static ImageView img_dialog;
    static ArrayList dataModelArrayList;

    static String myURLIS = "";
    static Dialog dialog_quality_allvids;
    static ImageView show_ytd_inpip;
    public static String VideoUrl;

    public static StringBuilder m2858a(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        return sb;
    }



    /*
    TODO To Add Supported Networks (Total 35)

//todo pending checking
FairTok
raask

//todo
vidoza
resso


TODO not avaliable
dtube
veer.tv


     */


    public static String m2854a(String str, String str2, String str3) {
        return str + str2 + str3;
    }

    public static final String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & 255);
                while (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getLocalizedMessage());
            return "";
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public static void Start(final Context context, String url, Boolean service) {

        Mcontext = context;
        fromService = service;
        Log.i("LOGClipboard111111 clip", "work 2");

        myURLIS = url;
        if (!fromService) {
            pd = new ProgressDialog(context);
            pd.setMessage(Mcontext.getResources().getString(R.string.genarating_download_link));
            pd.setCancelable(false);
            pd.show();
        }
        if(myURLIS != null)
        if (url.contains("tiktok")) {

//
//            iUtils.ShowToast(context, context.getString(R.string.error_occ));
//
//            if (!fromService) {
//                pd.dismiss();
//            }


            Dialog dialog = new Dialog(Mcontext);

            dialog.setContentView(R.layout.tiktok_optionselect_dialog);

            String finalUrl1 = url;


            Button methode2 = dialog.findViewById(R.id.dig_btn_met2);
            Button methode1 = dialog.findViewById(R.id.dig_btn_met1);
            Button dig_btn_cancel = dialog.findViewById(R.id.dig_btn_cancel);
            methode1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();


                    System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);

                    if (!fromService) {
                        pd.dismiss();
                    }
                    Intent intent = new Intent(Mcontext, TikTokDownloadWebview.class);
                    intent.putExtra("myvidurl", finalUrl1);
                    Mcontext.startActivity(intent);

//
//                    TikTokNewTestDownloader tikTokNewTestDownloader = new TikTokNewTestDownloader(Mcontext, finalUrl1);
//                    tikTokNewTestDownloader.DownloadVideo();
                }
            });


            methode2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    AndroidNetworking.post("https://savetik.app/go.php")
                            .addBodyParameter("url", finalUrl1)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {


                                    System.out.println("fjhjfhjsdfsdhf " + response);


                                    if (!fromService) {
                                        pd.dismiss();
                                    }
                                    String matag;
                                    try {

                                        JSONObject jsonObject = new JSONObject(response.toString());

                                        matag = jsonObject.getString("vUrl");
                                        System.out.println("wojfdjhfdjh " + matag);

                                        downloadFile.Downloading(context, matag, "Tiktok_" + System.currentTimeMillis(), ".mp4");


                                    } catch (Exception e) {
                                        matag = "";
                                        if (!fromService) {
                                            pd.dismiss();
                                        }

                                        iUtils.ShowToast(context, context.getString(R.string.error_occ));
                                        e.printStackTrace();
                                        System.out.println("wojfdjhfdjhv rrrrr " + matag);


                                    }


                                }

                                @Override/**/
                                public void onError(ANError error) {
                                    if (!fromService) {
                                        pd.dismiss();
                                    }

                                    iUtils.ShowToast(context, context.getString(R.string.error_occ));

                                    System.out.println("wojfdjhfdjh 55555" + error);

                                }
                            });
                }
            });

            dig_btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (!fromService) {
                        pd.dismiss();
                    }
                }
            });

            dialog.setCancelable(false);
            dialog.show();


//            new AlertDialog.Builder(context)
//                    .setTitle("Tiktok")
//                    .setMessage("Tiktok download is moved to another section, kindly use the 3 dots and click on Tiktok Downlaod")
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();

            System.out.println("wojfdjhfdjh url=" + url);

//            AndroidNetworking.get(Constants.TiktokApiNowatermark + url)
//                    .setPriority(Priority.HIGH)
//                    .build()
//                    .getAsString(new StringRequestListener() {
//                        @Override
//                        public void onResponse(String response) {
//                            // echo $jsonobj['rpcRes']['res']['ItemInfo']['ItemStruct']['Video']['PlayAddr']['Uri'];
//                            if (!fromService) {
//                                pd.dismiss();
//                            }
//
//
//
//                            System.out.println("wojfdjhfdjh " + response);
//
//
//                            try {
//
//
//                                JSONObject jsonObject = new JSONObject(response);
//
//                                String video_id = jsonObject
//                                        .getJSONObject("rpcRes")
//                                        .getJSONObject("res")
//                                        .getJSONObject("ItemInfo")
//                                        .getJSONObject("ItemStruct")
//                                        .getJSONObject("Video")
//                                        .getJSONObject("PlayAddr")
//                                        .getString("Uri");
//
//                              String ddurl =   "https://api2-16-h2.musical.ly/aweme/v1/play/?video_id="+video_id+"&vr_type=0&is_play_url=1&source=PackSourceEnum_PUBLISH&media_type=4&ratio=default&improve_bitrate=1";
//
//                                System.out.println("wojfdjhfdjh 111 " + ddurl);
//
//
//                                new downloadFile().Downloading(context, ddurl, "Tiktok_" + System.currentTimeMillis(), ".mp4");
//
//
//                            } catch (Exception e) {
//                                if (!fromService) {
//                                    pd.dismiss();
//                                }
//
//                                iUtils.ShowToast(context, context.getString(R.string.error_occ));
//
//
//                            }
//
//
//                        }
//
//                        @Override
//                        public void onError(ANError anError) {
//                            System.out.println("wojfdjhfdjh " + anError);
//
//                            iUtils.ShowToast(context, context.getString(R.string.error_occ));
//
//                            if (!fromService) {
//                                pd.dismiss();
//                            }
//                        }
//                    });


            //  CalldlApisDataData(url,true);


//            RetrofitApiInterface apiService = RetrofitClient.getClientTiktok().create(RetrofitApiInterface.class);
//
//
//            Call<Html> callResult = apiService.getVideo_Info_tiktok(map);
//
//
//            callResult.enqueue(new Callback<Html>() {
//                @Override
//                public void onResponse(Call<Html> call, retrofit2.Response<Html> response) {
//                    Log.d("REQUEST_ORIGIN_resnse1", "ddd" + response+"______"+call);
//                }
//
//                @Override
//                public void onFailure(Call<Html> call, Throwable t) {
//                    Log.d("REQUEST_ORIGIN_err1", "ddd" + t+"______"+call);
//                }
//            });


//TODO methode 2


        } else if (url.contains("popcornflix")) {


            url = url.substring(url.lastIndexOf("/") + 1);

            System.out.println("fjhjfhjsdfsdhf " + url);

            AndroidNetworking.get("https://api.unreel.me/v2/sites/popcornflix/videos/" + url + "/play-url?__site=popcornflix&__source=web&embed=false&protocol=https&tv=false")
                    .addHeaders("User-Agent", "Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {


                            System.out.println("fjhjfhjsdfsdhf " + response);


                            if (!fromService) {
                                pd.dismiss();
                            }
                            String matag;
                            try {

                                JSONObject jsonObject = new JSONObject(response.toString());

                                matag = jsonObject.getString("url");
                                System.out.println("wojfdjhfdjh " + matag);
                                new downloadFile().Downloading(context, matag, "Popcornflex_" + System.currentTimeMillis(), ".mp4");


                            } catch (Exception e) {
                                matag = "";
                                if (!fromService) {
                                    pd.dismiss();
                                }
                                e.printStackTrace();
                            }


                        }

                        @Override/**/
                        public void onError(ANError error) {
                            if (!fromService) {
                                pd.dismiss();
                            }
                        }
                    });


        } else if (url.contains("veoh")) {


            url = url.substring(url.lastIndexOf("/") + 1);

            System.out.println("fjhjfhjsdfsdhf " + url);

            AndroidNetworking.get("http://www.veoh.com/watch/getVideo/" + url)
                    .addHeaders("User-Agent", "Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {


                            System.out.println("fjhjfhjsdfsdhf " + response);


                            if (!fromService) {
                                pd.dismiss();
                            }
                            String matag;
                            try {

                                JSONObject jsonObject = new JSONObject(response.toString());


                                matag = jsonObject.getJSONObject("video").getJSONObject("src").getString("HQ");
                                System.out.println("wojfdjhfdjh " + matag);
                                new downloadFile().Downloading(context, matag, "Veoh_" + System.currentTimeMillis(), ".mp4");


                            } catch (Exception e) {
                                matag = "";
                                if (!fromService) {
                                    pd.dismiss();
                                }
                                e.printStackTrace();
                            }


                        }

                        @Override/**/
                        public void onError(ANError error) {
                            if (!fromService) {
                                pd.dismiss();
                            }
                        }
                    });


        } else if (url.contains("moj")) {

            try {
                url = url.substring(url.lastIndexOf("/") + 1);

                if (url.contains("?referrer=share")) {
                    url = url.substring(0, url.indexOf("?"));
                    System.out.println("fjhjfhjsdfsdhf 000=" + url + " size " + url.indexOf("?"));
                    System.out.println("fjhjfhjsdfsdhf 000=" + "https://moj-apis.sharechat.com/videoFeed?postId=" + url + "&firstFetch=true");
                }

                JSONObject jsonObject = new JSONObject("{\"appVersion\":83,\"bn\":\"broker1\",\"client\":\"android\",\"deviceId\":\"ebb088d29e7287b1\",\"message\":{\"adData\":{\"adsShown\":0,\"firstFeed\":false},\"deviceInfoKey\":\"OSyQoHJLJ4NsXPLyQePkAICh3Q0ih0bveFwm1KEV+vReMuldqo+mSyMjdhb4EeryKxk1ctAbYaDH\\\\nTI+PMRPZVYH5pBccAm7OT2uz69vmD/wPqGuSgWV2aVNMdM75DMb8NZn1JU2b1bo/oKs80baklsvx\\\\n1X7jrFPL6M5EDTdPDhs=\\\\n\",\"deviceInfoPayload\":\"M6g+6j6irhFT/H6MsQ/n/tEhCl7Z5QgtVfNKU8M90zTJHxqljm2263UkjRR9bRXAjmQFXXOTXJ25\\\\nOHRjV7L5Lw+tUCONoYfyUEzADihWfAiUgXJEcKePfZONbdXXuwGgOPeD0k4iSvI7JdzroRCScKXd\\\\n41CkmXFayPaRL9aqgAgs6kSoIncCWBU2gEXiX1lgPVvdmUzCZ+yi2hFA+uFOmv1MJ6dcFKKcpBM6\\\\nHSPIrGV+YtTyfd8nElx0kyUbE4xmjOuMrctkjnJkd2tMdxB8qOFKeYrcLzy4LZJNXyUmzs29XSE+\\\\nhsrMZib8fFPJhJZIyGCWqfWiURut4Bg5HxYhYhg3ejPxFjNyXxS3Ja+/pA+A0olt5Uia7ync/Gui\\\\n58tlDQ4SKPthCzGa1tCVN+2y/PW30+LM79t0ltJ/YrNZivQx4eEnszlM9nwmIuj5z5LPniQghA6x\\\\nrfQ8IqVUZfiitXj/Fr7UjKg1cs/Ajj8g4u/KooRvVkg9tMwWePtJFqrkk1+DU4cylnSEG3XHgfer\\\\nslrzj5NNZessMEi+4Nz0O2D+b8Y+RjqN6HqpwZPDHhZwjz0Iuj2nhZLgu1bgNJev5BwxAr8akDWv\\\\nvKsibrJS9auQOYVzbYZFdKMiBnh+WHq0qO2aW1akYWCha3ZsSOtsnyPnFC+1PnMbBv+FiuJmPMXg\\\\nSODFoRIXfxgA/qaiKBipS+kIyfaPxn6O1i6MOwejVuQiWdAPTO132Spx0cFtdyj2hX6wAMe21cSy\\\\n8rs3KQxiz+cq7Rfwzsx4wiaMryFunfwUwnauGwTFOW98D5j6oO8=\\\\n\",\"lang\":\"Hindi\",\"playEvents\":[{\"authorId\":\"18326559001\",\"networkBitrate\":1900000,\"initialBufferPercentage\":100,\"isRepost\":false,\"sg\":false,\"meta\":\"NotifPostId\",\"md\":\"Stream\",\"percentage\":24.68405,\"p\":\"91484006\",\"radio\":\"wifi\",\"r\":\"deeplink_VideoPlayer\",\"repeatCount\":0,\"timeSpent\":9633,\"duration\":15,\"videoStartTime\":3916,\"t\":1602255552820,\"clientType\":\"Android\",\"i\":79,\"appV\":83,\"sessionId\":\"72137847101_8863b3f5-ad2d-4d59-aa7c-cf1fb9ef32ea\"},{\"authorId\":\"73625124001\",\"networkBitrate\":1900000,\"initialBufferPercentage\":100,\"isRepost\":false,\"sg\":false,\"meta\":\"list2\",\"md\":\"Stream\",\"percentage\":17.766666,\"p\":\"21594412\",\"radio\":\"wifi\",\"r\":\"First Launch_VideoPlayer\",\"repeatCount\":0,\"tagId\":\"0\",\"tagName\":\"\",\"timeSpent\":31870,\"duration\":17,\"videoStartTime\":23509,\"t\":1602218215942,\"clientType\":\"Android\",\"i\":79,\"appV\":83,\"sessionId\":\"72137847101_db67c0c9-a267-4cec-a3c3-4c0fa4ea16e1\"}],\"r\":\"VideoFeed\"},\"passCode\":\"9e32d6145bfe53d14a0c\",\"resTopic\":\"response/user_72137847101_9e32d6145bfe53d14a0c\",\"userId\":\"72137847101\"}");


                AndroidNetworking.post("https://moj-apis.sharechat.com/videoFeed?postId=" + url + "&firstFetch=true")
                        .addHeaders("X-SHARECHAT-USERID", "72137847101")
                        .addHeaders("X-SHARECHAT-SECRET", "9e32d6145bfe53d14a0c")
                        .addHeaders("APP-VERSION", "83")
                        .addHeaders("PACKAGE-NAME", "in.mohalla.video")
                        .addHeaders("DEVICE-ID", "ebb088d29e7287b1")
                        .addHeaders("CLIENT-TYPE", "Android:")
                        .addHeaders("Content-Type", "application/json; charset=UTF-8")
                        .addHeaders("Host", "moj-apis.sharechat.com")
                        .addHeaders("Connection", "Keep-Alive:")
                        .addHeaders("User-Agent", "okhttp/3.12.12app-version:83")
                        .addHeaders("cache-control", "no-cache")
                        .addHeaders("client-type", "Android")
                        .addHeaders("connection", "Keep-Alive")
                        .addHeaders("content-type", "application/json;charset=UTF-8")
                        .addHeaders("device-id", "ebb088d29e7287b1")
                        .addHeaders("host", "moj-apis.sharechat.com")
                        .addHeaders("package-name", "in.mohalla.video")
                        .addHeaders("postman-token", "37d59a7c-f247-3b70-ab3c-1dedf4079852")
                        .addHeaders("user-agent", "okhttp/3.12.12")
                        .addHeaders("x-sharechat-secret", "9e32d6145bfe53d14a0c")
                        .addHeaders("x-sharechat-userid", "72137847101")
                        .addJSONObjectBody(jsonObject)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {


                                System.out.println("fjhjfhjsdfsdhf " + response);


                                if (!fromService) {
                                    pd.dismiss();
                                }
                                String matag;
                                try {

                                    JSONObject jsonObject = new JSONObject(response.toString());

                                    matag = jsonObject.getJSONObject("payload")
                                            .getJSONArray("d")
                                            .getJSONObject(0)
                                            .getJSONArray("bandwidthParsedVideos")
                                            .getJSONObject(3)
                                            .getString("url");

                                    System.out.println("wojfdjhfdjh " + matag);
                                    new downloadFile().Downloading(context, matag, "Moj_" + System.currentTimeMillis(), ".mp4");


                                } catch (Exception e) {
                                    matag = "";
                                    if (!fromService) {
                                        pd.dismiss();
                                    }
                                    e.printStackTrace();
                                }


                            }

                            @Override/**/
                            public void onError(ANError error) {
                                if (!fromService) {
                                    pd.dismiss();
                                }
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
                if (!fromService) {
                    pd.dismiss();
                }
            }
        } else if (url.contains("fairtok")) {

            try {
                url = url.substring(url.lastIndexOf("/") + 1);


                JSONObject jsonObject = new JSONObject("{\"device_fingerprint_id\":\"838529202513017602\",\"identity_id\":\"838529202537882206\",\"hardware_id\":\"ebb088d29e7287b1\",\"is_hardware_id_real\":true,\"brand\":\"samsung\",\"model\":\"SM-J200G\",\"screen_dpi\":240,\"screen_height\":960,\"screen_width\":540,\"wifi\":true,\"ui_mode\":\"UI_MODE_TYPE_NORMAL\",\"os\":\"Android\",\"os_version\":22,\"cpu_type\":\"armv7l\",\"build\":\"LMY47X.J200GDCU2ARL1\",\"locale\":\"en_GB\",\"connection_type\":\"wifi\",\"os_version_android\":\"5.1.1\",\"country\":\"GB\",\"language\":\"en\",\"local_ip\":\"192.168.43.18\",\"app_version\":\"1.19\",\"facebook_app_link_checked\":false,\"is_referrable\":0,\"debug\":false,\"update\":1,\"latest_install_time\":1601158937336,\"latest_update_time\":1601158937336,\"first_install_time\":1601158937336,\"previous_update_time\":1601158937336,\"environment\":\"FULL_APP\",\"android_app_link_url\":\"https:\\/\\/fairtok.app.link\\/" + url + "\",\"external_intent_uri\":\"https:\\/\\/fairtok.app.link\\/Y7ov2al149\",\"cd\":{\"mv\":\"-1\",\"pn\":\"com.fairtok\"},\"metadata\":{},\"advertising_ids\":{\"aaid\":\"094dfa1f-77cf-4f84-b373-2c15bf74f9d1\"},\"lat_val\":0,\"google_advertising_id\":\"094dfa1f-77cf-4f84-b373-2c15bf74f9d1\",\"instrumentation\":{\"v1\\/open-qwt\":\"0\"},\"sdk\":\"android5.0.1\",\"branch_key\":\"key_live_hjLYp0Wi3i6R1qQ1Lr51TlpcBvkxEp53\",\"retryNumber\":0}");

                AndroidNetworking.post("https://api2.branch.io/v1/open")
                        .addHeaders("cache-control", "no-cache")

                        .addJSONObjectBody(jsonObject)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {


                                System.out.println("fjhjfhjsdfsdhf " + response);


                                if (!fromService) {
                                    pd.dismiss();
                                }
                                String matag;
                                try {

                                    JSONObject jsonObject = new JSONObject(response.toString());

                                    matag = "https://bucket-fairtok.s3.ap-south-1.amazonaws.com/" + jsonObject.getString("post_video");

                                    System.out.println("wojfdjhfdjh " + matag);
                                    new downloadFile().Downloading(context, matag, "Fairtok_" + System.currentTimeMillis(), ".mp4");


                                } catch (Exception e) {
                                    matag = "";
                                    if (!fromService) {
                                        pd.dismiss();
                                    }
                                    e.printStackTrace();
                                }


                            }

                            @Override/**/
                            public void onError(ANError error) {
                                if (!fromService) {
                                    pd.dismiss();
                                }
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
                if (!fromService) {
                    pd.dismiss();
                }
            }
        } else if (url.contains("vlipsy")) {

            try {
                url = url.substring(url.lastIndexOf("/") + 1);
                if (url.length() > 8) {
                    String[] aa = url.split("-");

                    url = aa[aa.length - 1];

                }


                AndroidNetworking.get("https://apiv2.vlipsy.com/v1/vlips/" + url + "?key=vl_R8daJGhs67i7Ej7y")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {


                                //  System.out.println("fjhjfhjsdfsdhf " + response);


                                if (!fromService) {
                                    pd.dismiss();
                                }
                                String matag;
                                try {

                                    JSONObject jsonObject = new JSONObject(response.toString());

                                    matag = jsonObject.getJSONObject("data").getJSONObject("media").getJSONObject("mp4").getString("url");

                                    System.out.println("wojfdjhfdjh " + matag);
                                    new downloadFile().Downloading(context, matag, "Vlipsy_" + System.currentTimeMillis(), ".mp4");


                                } catch (Exception e) {
                                    matag = "";
                                    if (!fromService) {
                                        pd.dismiss();
                                    }
                                    e.printStackTrace();
                                }


                            }

                            @Override/**/
                            public void onError(ANError error) {
                                if (!fromService) {
                                    pd.dismiss();
                                }
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
                if (!fromService) {
                    pd.dismiss();
                }
            }
        } else if (url.contains("likee")) {


//            LikeeVideoDownloader lIkeeVideoDownloader = new LikeeVideoDownloader(Mcontext, url);
//            lIkeeVideoDownloader.DownloadVideo();

            //new CallLikeeData().execute(url);
            CalldlApisDataData(url, true);
            //   getAllDataForLikee(url);
        } else if (url.contains("gfycat")) {

            CalldlApisDataData(url, true);
            //   getAllDataForLikee(url);
        } else if (url.contains("funimate")) {


            CalldlApisDataData(url, true);
        } else if (url.contains("kooapp")) {


            KooDownloader kooDownloader = new KooDownloader(Mcontext, url);
            kooDownloader.DownloadVideo();

        } else if (url.contains("wwe")) {


            CalldlApisDataData(url, true);
        } else if (url.contains("1tv.ru")) {


            CalldlApisDataData(url, true);
        } else if (url.contains("naver")) {


            CalldlApisDataData(url, true);
        } else if (url.contains("gloria.tv")) {


            CalldlApisDataData(url, true);
        } else if (url.contains("vidcommons.org")) {


            CalldlApisDataData(url, true);
        } else if (url.contains("media.ccc.de")) {


            CalldlApisDataData(url, true);
        } else if (url.contains("vlive")) {


            CalldlApisDataData(url, true);
        } else if (url.contains("sharechat.com")) {
            Log.i("LOGClipboard111111 clip", "work 66666");

            new callGetShareChatData().execute(url);
            Log.i("LOGClipboard111111 clip", "work 1111111");


//            TiktokVideoDownloader downloader = new TiktokVideoDownloader(Mcontext, url);
//            downloader.DownloadVideo();

            //   getAllDataForLikee(url);
        } else if (url.contains("roposo.com")) {
            Log.i("LOGClipboard111111 clip", "work 66666");

            new callGetRoposoData().execute(url);
            Log.i("LOGClipboard111111 clip", "work 1111111");

//
//            TiktokVideoDownloader downloader = new TiktokVideoDownloader(Mcontext, url);
//            downloader.DownloadVideo();

            //   getAllDataForLikee(url);
        } else if (url.contains("snackvideo.com") || url.contains("sck.io")) {

            //    new callGetSnackAppData().execute(url);

            if (url.contains("snackvideo.com") || url.contains("sck.io")) {
                new callGetSnackAppData().execute(url);
            } else if (url.contains("sck.io")) {
                getSnackVideoData(url, Mcontext);


            }

        } else if (url.contains("facebook.com")) {


//            //   GetFacebookData(url);
//            //new CallFacebookData().execute(url);
//            // getAllDataForLikee2(url, true);
            FbVideoDownloader fbVideoDownloader = new FbVideoDownloader(Mcontext, url);
            fbVideoDownloader.DownloadVideo();
            //    CallDailymotionData(url, true);
            //   CalldlApisDataData(url, true);
        } else if (url.contains("blogspot.com")) {


            CalldlApisDataData(url, true);
        } else if (url.contains("instagram.com")) {

            new GetInstagramVideo().execute(url);

            // downloadVideo.getAllDataForLikee(url);
        } else if (url.contains("bilibili.com")) {

            new callGetbilibiliAppData().execute(url);

            // downloadVideo.getAllDataForLikee(url);
        } else if (url.contains("mitron.tv")) {

            new CallMitronData().execute(url);
        } else if (url.contains("josh")) {

            new CallJoshData().execute(url);
        } else if (url.contains("triller")) {

            new CallTrillerData().execute(url);
        } else if (url.contains("rizzle")) {

            new CallRizzleData().execute(url);
        } else if (url.contains("solidfiles")) {

            SolidfilesDownloader solidfilesDownloader = new SolidfilesDownloader(Mcontext, url);
            solidfilesDownloader.DownloadVideo();

        } else if (url.contains("audioboom")) {

            AudioboomDownloader audioboomDownloader = new AudioboomDownloader(Mcontext, url);
            audioboomDownloader.DownloadVideo();

        } else if (url.contains("ifunny")) {

            new CallIfunnyData().execute(url);
        } else if (url.contains("trell.co")) {

            new CalltrellData().execute(url);
        } else if (url.contains("boloindya.com")) {

            new CallBoloindyaData().execute(url);
        } else if (url.contains("chingari.io")) {

            new CallchingariData().execute(url);
        } else if (url.contains("dubsmash")) {

            new CalldubsmashData().execute(url);
        } else if (url.contains("bittube")) {

            String myurlis1 = url;
            if (myurlis1.contains(".tv")) {
                String str = "/";
                myurlis1 = myurlis1.split(str)[myurlis1.split(str).length - 1];
                myurlis1 = "https://bittube.video/videos/watch/" +
                        myurlis1;
            }
            new CallgdriveData().execute(myurlis1);

        } else if (url.contains("drive.google.com") ||
                url.contains("mp4upload") ||

                url.contains("ok.ru") ||

                url.contains("mediafire") ||
                url.contains("gphoto") ||
                url.contains("uptostream") ||

                url.contains("fembed") ||
                url.contains("cocoscope") ||
                url.contains("sendvid") ||

                url.contains("vivo") ||
                url.contains("fourShared")) {


            new CallgdriveData().execute(url);
        } else if (url.contains("hind")) {

            new CallhindData().execute(url);
        } else if (url.contains("topbuzz.com")) {

            TopBuzzDownloader downloader = new TopBuzzDownloader(Mcontext, url, 12);
            downloader.DownloadVideo();

        } else if (url.contains("vimeo.com")) {
            // VimeoVideoDownloader downloader = new VimeoVideoDownloader(Mcontext, url);
            //  downloader.DownloadVideo();
            CalldlApisDataData(url, true);

        } else if (url.contains("twitter.com")) {
            TwitterVideoDownloader downloader = new TwitterVideoDownloader(Mcontext, url);
            downloader.DownloadVideo();
            //  CallDailymotionData(url, true);
            //CalldlApisDataData(url, true);

        }
        //new
        //working
        else if (url.contains("gag.com")) {
//            https://9gag.com/gag/aXowVXz
            //  CalldlApisDataData(url, false);
            new Call9gagData().execute(url);


        } else if (url.contains("buzzfeed.com")) {


            if (!fromService) {
                pd.dismiss();

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

            }

        }

        //TODO Add quality list
        else if (url.contains("flickr") && url.contains("flic.kr")) {
            // CallDailymotionData(url, true);
            CalldlApisDataData(url, true);

        } else if (url.contains("streamable")) {
            // CallDailymotionData(url, true);
            CalldlApisDataData(url, true);

        } else if (url.contains("vk.com")) {


            CallVKData(url, true);


        } else if (url.contains("redd.it") || url.contains("reddit")) {

            CalldlApisDataData(url, true);

            //  CallREditData(url, true);


        } else if (url.contains("soundcloud")) {

            url = url.replace("//m.", "//");

            CalldlApisDataData(url, true);

            // CallsoundData(url, false);


        } else if (url.contains("bandcamp")) {
//TODO Has multiple video json array
            CalldlApisDataData(url, true);
            //   CallsoundData(url, false);


        } else if (url.contains("mxtakatak")) {


            //  new CallmxtaktakData().execute(url);

            System.out.println("wojfdjhfdjh taktak" + url);

            AndroidNetworking.post("https://takatak.katarmal.in/v1/processUrl")
                    .addBodyParameter("takatakurl", url)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (!fromService) {
                                pd.dismiss();
                            }
                            String matag;
                            try {

                                JSONObject jsonObject = new JSONObject(response.toString());

                                matag = jsonObject.getString("videourl");
                                System.out.println("wojfdjhfdjh " + matag);
                                new downloadFile().Downloading(context, matag, "Mxtakatak_" + System.currentTimeMillis(), ".mp4");


                            } catch (Exception e) {
                                matag = "";

                                e.printStackTrace();
                                if (!fromService) {
                                    pd.dismiss();
                                }
                            }


                        }

                        @Override/**/
                        public void onError(ANError error) {
                            if (!fromService) {
                                pd.dismiss();
                            }
                        }
                    });


        } else if (url.contains("cocoscope")) {


            // CallsoundData(url, false);
            CalldlApisDataData(url, true);

        } else if (url.contains("test.com")) {

            new CallgaanaData().execute(url);
            // CallsoundData(url, false);
            //  CalldlApisDataData(url, true);

        } else if (url.contains("20min.ch")) {


            Twenty_min_ch_Downloader twenty_min_ch_downloader = new Twenty_min_ch_Downloader(Mcontext, url);
            twenty_min_ch_downloader.DownloadVideo();

        } else if (url.contains("gaana")) {


            String finalUrl = url;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection con = null;
                    try {
                        con = (HttpURLConnection) (new URL("https://tinyurl.com/f67p797b").openConnection());

                        con.setInstanceFollowRedirects(false);
                        con.connect();
                        int responseCode = con.getResponseCode();
                        System.out.println(responseCode);
                        String location = con.getHeaderField("Location");
                        System.out.println(location);


                        AndroidNetworking.post(location)
                                .addBodyParameter("url", finalUrl)
                                .addBodyParameter("weburl", "https://video.infusiblecoder.com/")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        if (!fromService) {
                                            pd.dismiss();
                                        }
                                        String matag;
                                        try {

                                            JSONObject jsonObject = new JSONObject(response.toString());

                                            matag = jsonObject.getJSONArray("songlinks").getJSONObject(0).getString("songurl");
                                            System.out.println("wojfdjhfdjh " + matag);
                                            new downloadFile().Downloading(context, matag, "Gaana_" + System.currentTimeMillis(), ".mp3");


                                        } catch (Exception e) {
                                            matag = "";

                                            e.printStackTrace();
                                            if (!fromService) {
                                                pd.dismiss();
                                            }
                                        }


                                    }

                                    @Override/**/
                                    public void onError(ANError error) {

                                        System.out.println("wojfdjhfdjh error = " + error.getMessage());


                                        if (!fromService) {
                                            pd.dismiss();
                                        }
                                    }
                                });


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


            //     AndroidNetworking.post("https://video.infusiblecoder.com/apiganna.php")


//            AndroidNetworking.get("https://gaana.com/apiv2?seokey=lamborghini-51&type=songdetails&isChrome=1")
//                    .addHeaders("User-Agent", "Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36")
//                    .setPriority(Priority.MEDIUM)
//                    .build()
//                    .getAsJSONObject(new JSONObjectRequestListener() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            if (!fromService) {
//                                pd.dismiss();
//                            }
//                            String matag;
//                            try {
//
////                                ["tracks"][0]["urls"]["high"]["message"]);
//
//                                JSONObject jsonObject = new JSONObject(response.toString());
//                                JSONObject tracks0 = jsonObject.getJSONArray("tracks").getJSONObject(0);
//                                String encrypurl = tracks0.getJSONObject("urls").getJSONObject("high").getString("message");
//
//                                String decryptedurl = iUtils.decryptSoundUrl(encrypurl);
//                                if (!decryptedurl.equals("")) {
//                                    System.out.println("wojfdjhfdjh decrypt " + decryptedurl);
//                                    //   new downloadFile().Downloading(context, matag, "Mxtakatak_" + System.currentTimeMillis(), ".mp4");
//
//
//                                    new Thread(new Runnable() {
//                                        public void run() {
//                                            //  ArrayList<VideoModel> myvidmodel = DownloadFiles(decryptedurl);
//                                            download_hlsganna(decryptedurl, ".mp4");
//
//                                        }
//                                    }).start();
//
//
//                                }
//
//                            } catch (Exception e) {
//                                matag = "";
//                                System.out.println("wojfdjhfdjh error " + e.getMessage());
//
//                                e.printStackTrace();
//                                if (!fromService) {
//                                    pd.dismiss();
//                                }
//                            }
//
//
//                        }
//
//                        @Override/**/
//                        public void onError(ANError error) {
//                            System.out.println("wojfdjhfdjh error " + error.getMessage());
//
//                            if (!fromService) {
//                                pd.dismiss();
//                            }
//                        }
//                    });
//
//


//            if (!fromService) {
//                pd.dismiss();
//            }
//
//            Toast.makeText(context, context.getString(R.string.error_occ), Toast.LENGTH_SHORT).show();

        } else if (url.contains("izlesene")) {


            //CallsoundData(url, false);
            CalldlApisDataData(url, false);

        } else if (url.contains("linkedin")) {


            //   CalldlApisDataData(url, false);
            new CalllinkedinData().execute(url);

        } else if (url.contains("kwai") || url.contains("kw.ai")) {


            CalldlApisDataData(url, false);


        } else if (url.contains("bitchute")) {


            CalldlApisDataData(url, false);


        } else if (url.contains("douyin")) {

            try {


                String[] idis = url.split("/");


                AndroidNetworking.get("https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=" + idis[idis.length - 1])
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                if (!fromService) {
                                    pd.dismiss();
                                }
                                String matag;
                                try {

//                                $video_info["item_list"][0]["video"]["play_addr"]["url_list"][0];
                                    JSONObject jsonObject = new JSONObject(response.toString());

                                    JSONArray itemlist = jsonObject.getJSONArray("item_list");
                                    matag = itemlist.getJSONObject(0).getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list").getString(0);


                                    System.out.println("wojfdjhfdjh " + matag);
                                    new downloadFile().Downloading(context, matag, "Douyin_" + System.currentTimeMillis(), ".mp4");


                                } catch (Exception e) {
                                    matag = "";

                                    e.printStackTrace();
                                    if (!fromService) {
                                        pd.dismiss();
                                    }
                                }


                            }

                            @Override/**/
                            public void onError(ANError error) {
                                if (!fromService) {
                                    pd.dismiss();
                                }
                            }
                        });


            } catch (Exception e) {

            }


        } else if (url.contains("dailymotion") || url.contains("dai.ly")) {


            DailyMotionDownloader mashableDownloader = new DailyMotionDownloader(Mcontext, url);
            mashableDownloader.DownloadVideo();


//            AndroidNetworking.get("http://keepsaveit.com/api?api_key=OlfZ0U6RbbV8wA7U4rquAAOQTCp5z7JPl7NNDmx39qgfaxIEqh&url=" + url)
//                    .addHeaders("User-Agent", "Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36")
//                    .setPriority(Priority.MEDIUM)
//                    .build()
//                    .getAsJSONObject(new JSONObjectRequestListener() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            if (!fromService) {
//                                pd.dismiss();
//
//                                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//
//                            }
//                            try {
//
//
//                                JSONObject jsonObject = new JSONObject(response.toString());
//                                JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("links");
//
//                                ArrayList<String> urls = new ArrayList<>();
//                                ArrayList<String> qualities = new ArrayList<>();
//
//
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    String urlis = jsonArray.getJSONObject(i).getString("url");
//                                    urls.add(urlis);
//                                    String resolutionis = jsonArray.getJSONObject(i).getString("resolution");
//                                    qualities.add(resolutionis);
//
//
//                                }
//
//                                String[] arr = new String[qualities.size()];
//                                arr = qualities.toArray(arr);
//
//                                new AlertDialog.Builder(Mcontext).setTitle("Quality!").setItems(arr, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                        new downloadFile().Downloading(Mcontext, urls.get(i), "Dailymotion_" + System.currentTimeMillis(), ".mp4");
//
//                                    }
//                                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        if (!fromService) {
//
//                                            pd.dismiss();
//                                        }
//                                    }
//                                }).setCancelable(false).show();
//
//
//                            } catch (Exception e) {
//
//                            }
//                        }
//
//                        @Override
//                        public void onError(ANError anError) {
//                            if (!fromService) {
//                                pd.dismiss();
//
//                                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//
//                            }
//                        }
//                    });


//            if (!fromService) {
//                pd.dismiss();
//                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//            }


        } else if (url.contains("espn.com")) {
            CalldlApisDataData(url, true);

        } else if (url.contains("mashable.com")) {
            //   CalldlApisDataData(url, true);
            MashableDownloader mashableDownloader = new MashableDownloader(Mcontext, url);
            mashableDownloader.DownloadVideo();


        } else if (url.contains("coub")) {
            CalldlApisDataData(url, true);
//            CoubDownloader coubDownloader = new CoubDownloader(Mcontext, url);
//            coubDownloader.DownloadVideo();


        } else if (url.contains("kickstarter")) {
            KickstarterDownloader kickstarterDownloader = new KickstarterDownloader(Mcontext, url);
            kickstarterDownloader.DownloadVideo();


        } else if (url.contains("aparat")) {
            AparatDownloader aparatDownloader = new AparatDownloader(Mcontext, url);
            aparatDownloader.DownloadVideo();


        } else if (url.contains("allocine.fr")) {
            AllocineDownloader allocineDownloader = new AllocineDownloader(Mcontext, url);
            allocineDownloader.DownloadVideo();


        } else if (url.contains("ted.com")) {

            CalldlApisDataData(url, true);
        } else if (url.contains("twitch")) {
            CalldlApisDataData(url, true);

        } else if (url.contains("imdb.com")) {
            CalldlApisDataData(url, false);

        } else if (url.contains("camdemy")) {
            CalldlApisDataData(url, false);

        } else if (url.contains("pinterest") || url.contains("pin.it")) {
            CalldlApisDataData(url, false);

        } else if (url.contains("imgur.com")) {
            url = url.replace("//m.", "//");
            CalldlApisDataData(url, false);

        } else if (url.contains("tumblr.com")) {

            new CalltumblerData().execute(url);
        }


//TODO youtube from here

        else if (url.contains("youtube.com") || url.contains("youtu.be")) {
            //  String youtubeLink = "https://www.youtube.com/watch?v=668nUCeBHyY";
            if (Constants.showyoutube) {
                Log.i("LOGClipboard111111 clip", "work 3");
                // getYoutubeDownloadUrl(url);
                CalldlApisDataData(url, true);

            } else {
                if (!fromService) {
                    pd.dismiss();

                    iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

                }
            }

        }


//TODO Till Here
        else {


            //  CalldlApisDataData(url, true);


            if (!fromService) {
                pd.dismiss();


                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

            }
        }


        prefs = Mcontext.getSharedPreferences("AppConfig", MODE_PRIVATE);
    }

//
//    static void download_hlsganna(String urlis, String extension) {
//
//        //String M3U8URL = "https://68vod-adaptive.akamaized.net/exp=1616574881~acl=%2F115074667%2F%2A~hmac=a5f9ea0179c7aaeea7374a0a4bd565e600def9d380564e005141a36e93f4add5/115074667/sep/audio/319228990/playlist.m3u8";
//         String M3U8URL = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
//        //String M3U8URL = "https://videozmcdn.stz8.com:8091/20191127/PK7a0LKQ/index.m3u8";
//        //  String M3U8URL = "https://e1v-h.phncdn.com/hls/videos/202103/18/385313421/480P_2000K_385313421.mp4/master.m3u8?validfrom=1616502433&validto=1616509633&ip=52.211.71.244&hdl=-1&hash=Rglvlmx4WVSqIjxpRt8h2XEUXn8%3D";
//
//
//        String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/test";
//        File dir = new File(downloadPath);
//        if (!dir.exists()) {
//            //noinspection ResultOfMethodCallIgnored
//            dir.mkdirs();
//        }
//
//        //Run following command: $ ffmpeg -i audioInput.mp3 -i videoInput.avi -acodec copy -vcodec copy outputFile.avi This is it. outputFile.avi will be the resulting file.
//        String cmd = String.format("-i %s -acodec %s -bsf:a aac_adtstoasc -vcodec %s %s", M3U8URL, "copy", "copy", dir.toString() + "/" + MY_ANDROID_10_IDENTIFIER_OF_FILE + System.currentTimeMillis() + extension);
//        //  String cmd = String.format("-i %s -acodec %s -bsf:a aac_adtstoasc -vcodec %s %s", M3U8URL, "copy", "copy", dir.toString() + "/bigBuckBunny.mp4");
//        String[] command = cmd.split(" ");
//
//
//        long executionId = FFmpeg.executeAsync(command, new ExecuteCallback() {
//
//            @Override
//            public void apply(final long executionId, final int returnCode) {
//                if (returnCode == Config.RETURN_CODE_SUCCESS) {
//                    Log.i(Config.TAG, "Async command execution completed successfully.");
//                } else if (returnCode == Config.RETURN_CODE_CANCEL) {
//                    Log.i(Config.TAG, "Async command execution cancelled by user.");
//                } else {
//                    Log.i(Config.TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
//                }
//            }
//        });
//
//        Config.enableStatisticsCallback(new StatisticsCallback() {
//            public void apply(Statistics newStatistics) {
//
//                if (!fromService) {
//                    pd.dismiss();
//                }
//
//
//                Log.d(Config.TAG+"usa", String.format("frame: %d, time: %d", newStatistics.getVideoFrameNumber(), newStatistics.getTime()));
//              //  showdownload_progress(newStatistics.getTime(),newStatistics.getSize());
//            }
//        });
//
//        //            System.out.println("downloadURL" + downloadUrl + "\tdownloaded "+ finished + "个\taltogether" + sum + "个\tcompleted" + percent + "%");
//
//
//    }
//    public static void download_video_with_audio_ytd(String urlis, String extension) {
//
//        String M3U8URL = "https://68vod-adaptive.akamaized.net/exp=1616574881~acl=%2F115074667%2F%2A~hmac=a5f9ea0179c7aaeea7374a0a4bd565e600def9d380564e005141a36e93f4add5/115074667/sep/audio/319228990/playlist.m3u8";
//        // String M3U8URL = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
//        //String M3U8URL = "https://videozmcdn.stz8.com:8091/20191127/PK7a0LKQ/index.m3u8";
//        //  String M3U8URL = "https://e1v-h.phncdn.com/hls/videos/202103/18/385313421/480P_2000K_385313421.mp4/master.m3u8?validfrom=1616502433&validto=1616509633&ip=52.211.71.244&hdl=-1&hash=Rglvlmx4WVSqIjxpRt8h2XEUXn8%3D";
//
//
//        String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/test";
//        File dir = new File(downloadPath);
//        if (!dir.exists()) {
//            //noinspection ResultOfMethodCallIgnored
//            dir.mkdirs();
//        }
//
//        //Run following command: $ ffmpeg -i audioInput.mp3 -i videoInput.avi -acodec copy -vcodec copy outputFile.avi This is it. outputFile.avi will be the resulting file.
//        String withaudio = String.format("-i %s -i %s -acodec %s -vcodec %s %s", urlis, urlis, "copy", "copy", dir.toString() + "/" + MY_ANDROID_10_IDENTIFIER_OF_FILE + System.currentTimeMillis() + extension);
//        //   String cmd = String.format("-i %s -acodec %s -bsf:a aac_adtstoasc -vcodec %s %s", urlis, "copy", "copy", dir.toString() + "/"+MY_ANDROID_10_IDENTIFIER_OF_FILE+System.currentTimeMillis()+extension);
//        //  String cmd = String.format("-i %s -acodec %s -bsf:a aac_adtstoasc -vcodec %s %s", M3U8URL, "copy", "copy", dir.toString() + "/bigBuckBunny.mp4");
//        String[] command = withaudio.split(" ");
//
//
//        long executionId = FFmpeg.executeAsync(command, new ExecuteCallback() {
//
//            @Override
//            public void apply(final long executionId, final int returnCode) {
//                if (returnCode == Config.RETURN_CODE_SUCCESS) {
//                    Log.i(Config.TAG, "Async command execution completed successfully.");
//                } else if (returnCode == Config.RETURN_CODE_CANCEL) {
//                    Log.i(Config.TAG, "Async command execution cancelled by user.");
//                } else {
//                    Log.i(Config.TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
//                }
//            }
//        });
//
//        Config.enableStatisticsCallback(new StatisticsCallback() {
//            public void apply(Statistics newStatistics) {
//                Log.d(Config.TAG, String.format("frame: %d, time: %d", newStatistics.getVideoFrameNumber(), newStatistics.getTime()));
//            }
//        });
//
//        //            System.out.println("downloadURL" + downloadUrl + "\tdownloaded "+ finished + "个\taltogether" + sum + "个\tcompleted" + percent + "%");
//
//
//    }


    private static void showdownload_progress(int timeelipsed, long filesize) {
        Thread thread = new Thread() {
            @Override
            public void run() {

                String filesize2 = iUtils.getStringSizeLengthFile(filesize);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Mcontext);

                String contentTitle = "Downloaded";
                Intent notifyIntent = new Intent();
                PendingIntent notifyPendingIntent = PendingIntent.getActivity(Mcontext, DOWNLOAD_NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder notificationBuilder = createNotificationBuilder("downloader_channel");
                notificationBuilder.setContentIntent(notifyPendingIntent);
                notificationBuilder.setTicker("Start downloading from the server");
                notificationBuilder.setOngoing(true);
                notificationBuilder.setAutoCancel(false);
                notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
                notificationBuilder.setContentTitle(contentTitle);
                //   notificationBuilder.setContentText("0%");
                //   notificationBuilder.setProgress(100, 0, false);
                notificationManagerCompat.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());


                long total = 0;
                int count, tmpPercentage = 0;


                System.out.println("mobile-ffmpegusa prog :" + "time elipsed: " + iUtils.formatDuration(timeelipsed) + " downloaded : " + filesize2 + "%");
                notificationBuilder.setContentText("time elipsed: " + iUtils.formatDuration(timeelipsed) + " downloaded : " + filesize2);
                notificationBuilder.setProgress(100, timeelipsed, false);
                notificationManagerCompat.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());


                notificationBuilder.setContentTitle(contentTitle);
                notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                notificationBuilder.setOngoing(false);
                notificationBuilder.setAutoCancel(true);
                //notificationBuilder.setContentText("0");
                // notificationBuilder.setProgress(0, 0, false);
                notificationManagerCompat.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());

            }
        };

        thread.start();
    }


    private static NotificationCompat.Builder createNotificationBuilder(String channelId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelName = Mcontext.getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager notificationManager = (NotificationManager) Mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        return new NotificationCompat.Builder(Mcontext, channelId);
    }


//    private static ArrayList<VideoModel> DownloadFiles(String decryptedurl) {
//        ArrayList<VideoModel> videoModels1 = new ArrayList();
//
//        try {
////            URL url1 = new URL(decryptedurl);
////
////            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
////            connection.connect();
////            InputStream stream = connection.getInputStream();
////
////            HlsPlaylistParser hlsPlaylistParser = new HlsPlaylistParser();
////          HlsPlaylist hlsPlaylist= hlsPlaylistParser.parse(Uri.parse(decryptedurl),stream);
////
////            for (int i = 0; i < hlsPlaylist.tags.size(); i++) {
////                System.out.println("myfinalurlis 1 datat =" + hlsPlaylist.tags.get(i));
////
////            }
//
//
//            //
////            List<String> lines = Collections.emptyList();
////
////                lines =
////                        Files.readAllLines(Paths.get(file.toString()), StandardCharsets.UTF_8);
////
////                for (int i = 0; i < lines.size(); i++) {
////                    if (lines.get(i).contains("http")) {
////                        System.out.println("wojfdjhfdjh qqqq master data = " + lines.get(i));
////                    }
////                }
//
//
//            URL u2 = new URL(decryptedurl);
//            InputStream is2 = u2.openStream();
//
//            DataInputStream dis2 = new DataInputStream(is2);
//
//            byte[] buffer2 = new byte[1024];
//            int length2;
//
//            FileOutputStream fos2 = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/master.m3u8"));
//            while ((length2 = dis2.read(buffer2)) > 0) {
//                fos2.write(buffer2, 0, length2);
//            }
//
//            File masterfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/master.m3u8");
////
////                List<String> lines2 = Collections.emptyList();
////
////                    lines2 =
////                            Files.readAllLines(Paths.get(fileindex.toString()), StandardCharsets.UTF_8);
////
////
////                    for (int i = 0; i < lines2.size(); i++) {
////                        if (lines2.get(i).contains("http")) {
////                            System.out.println("wojfdjhfdjh qqqq datanew = " + lines2.get(i));
////                        } else {
////                            lines2.remove(i);
////                        }
////                    }
//
//
////                    new callM3u8Parser().execute(lines.get(2));
//
//
//            MasterPlaylistParser parser = new MasterPlaylistParser();
//
//// Parse playlist
//            MasterPlaylist playlist = parser.readPlaylist(Paths.get(masterfile.toString()));
//
//
//            MasterPlaylist updated = MasterPlaylist.builder()
//                    .from(playlist)
//                    .version(2)
//                    .build();
//
//
//            for (int i = 0; i < updated.variants().size(); i++) {
//
//                System.out.println("myfinalurlis playlist media =" + updated.variants().get(i).uri());
//
//
//                URL u3 = new URL(updated.variants().get(i).uri());
//                InputStream is3 = u3.openStream();
//
//                DataInputStream dis3 = new DataInputStream(is3);
//
//                byte[] buffer3 = new byte[1024];
//                int length3;
//
//                FileOutputStream fos3 = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/index_0_a.m3u8"));
//                while ((length3 = dis3.read(buffer3)) > 0) {
//                    fos3.write(buffer2, 0, length3);
//                }
//
//                File indexfilew = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/index_0_a.m3u8");
//
//
//                List<String> lines2 = Collections.emptyList();
//
//                lines2 =
//                        Files.readAllLines(Paths.get(indexfilew.toString()), StandardCharsets.UTF_8);
//
//
//                for (int j = 0; j < lines2.size(); j++) {
//                    if (lines2.get(j).contains("http")) {
//                        System.out.println("myfinalurlis qqqq datanew = " + lines2.get(j));
//                    } else {
//                        lines2.remove(j);
//                    }
//                }
//
//
////                MasterPlaylist playlist3 = parser.readPlaylist(Paths.get(indexfilew.toString()));
////
////
////                MasterPlaylist updated3 = MasterPlaylist.builder()
////                        .from(playlist3)
////                        .version(2)
////                        .build();
////
////                System.out.println("myfinalurlis playlist media 222 ="+updated3.variants().get(0).uri());
//
////
////                MasterPlaylist masterPlaylist = MasterPlaylist.builder()
////                        .version(4)
////                        .independentSegments(true)
////                        .addAlternativeRenditions(AlternativeRendition.builder()
////                                .type(MediaType.AUDIO)
////                                .name("Default audio")
////                                .groupId("AUDIO")
////                                .build())
////                        .addVariants(
////                                Variant.builder()
////                                        .addCodecs("avc1.4d401f", "mp4a.40.2")
////                                        .bandwidth(900000)
////                                        .uri("v0.m3u8")
////                                        .build(),
////                                Variant.builder()
////                                        .addCodecs("avc1.4d401f", "mp4a.40.2")
////                                        .bandwidth(900000)
////                                        .uri("v1.m3u8")
////                                        .resolution(1280, 720)
////                                        .build())
////                        .build();
////
////                MasterPlaylistParser masterPlaylistParser2 = new MasterPlaylistParser();
////                System.out.println(masterPlaylistParser2.writePlaylistAsString(masterPlaylist));
//
//
//            }
//
//
//        } catch (Exception e) {
//            Log.e("MyTag", e.toString());
//        }
//        return videoModels1;
//
//
//    }
//

//    private static class callM3u8Parser extends AsyncTask<String, Void, Playlist> {
//        Playlist ShareChatDoc;
//
//        callM3u8Parser() {
//        }
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        protected Playlist doInBackground(String... strArr) {
//            InputStream in = null;
//            try {
//                in = new FileInputStream(strArr.toString());
//                ShareChatDoc = Playlist.parse(in);
//                in.close();
//                return ShareChatDoc;
//            } catch (Exception e) {
//
//            }
//            return this.ShareChatDoc;
//        }
//
//        protected void onPostExecute(Playlist document) {
//            String charSequence = "";
//
//            try {
//
//                if (!fromService) {
//
//                    pd.dismiss();
//                }
//
//                charSequence = new PlaylistFormat(document).format();
//
//                System.out.println("dsahjdasdagdasd =" + charSequence);
//
//            } catch (Exception document22) {
//                document22.printStackTrace();
//                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//            }
//        }
//    }
//
//
//    static class PlaylistFormat {
//        private final Playlist pl;
//
//        PlaylistFormat(Playlist pl) {
//            if (pl == null) {
//                throw new NullPointerException("pl");
//            }
//            this.pl = pl;
//        }
//
//        public String format() {
//            StringBuffer buf = new StringBuffer(100);
//
//            String NEW_LINE = "\n";
//            String ELEMENT = "\n\t";
//
//            buf.append("Playlist").append(NEW_LINE).append("Media Sequence No: ").append(pl.getMediaSequenceNumber())
//                    .append(" Target Duration: ").append(pl.getTargetDuration()).append(NEW_LINE);
//
//
//            int index = 0;
//            for (net.chilicat.m3u8.Element el : pl) {
//                buf.append(ELEMENT).append(index).append(": ").append(" Dur: ").append(el.getExactDuration()).append(" DIS: ").append(el.isDiscontinuity()).append(" URI: ").append(el.getURI()).append(" Title: ").append(el.getTitle());
//            }
//            return buf.toString();
//        }
//    }


    //TODO youtube comment them from here

//    private static void getYoutubeDownloadUrl(String youtubeLink) {
//
//        new YouTubeExtractor(Mcontext) {
//
//            @Override
//            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
//                //    mainProgressBar.setVisibility(View.GONE);
//
//                if (ytFiles != null) {
//
//                    if (!fromService) {
//                        pd.dismiss();
//                    }
//
//
//                    windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);
//                    LayoutInflater layoutInflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                    mChatHeadView = layoutInflater.inflate(R.layout.dialog_quality_ytd, null);
//
//                    mainLayout = mChatHeadView.findViewById(R.id.linlayout_dialog);
//
//                    img_dialog = mChatHeadView.findViewById(R.id.img_dialog);
//
//
//                    dialogquality = new Dialog(Mcontext);
//                    dialogquality.setContentView(R.layout.dialog_quality_ytd);
//                    mainLayout = dialogquality.findViewById(R.id.linlayout_dialog);
//                    img_dialog = dialogquality.findViewById(R.id.img_dialog);
//                    show_ytd_inpip = dialogquality.findViewById(R.id.show_ytd_inpip);
//
//                    show_ytd_inpip.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            for (int i = 0, itag; i < ytFiles.size(); i++) {
//                                itag = ytFiles.keyAt(i);
//                                // ytFile represents one file with its url and meta data
//                                YtFile ytFile = ytFiles.get(itag);
//
//                                // Just add videos in a decent format => height -1 = audio
//                                if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
//                                    // addButtonToMainLayouttest(vMeta.getTitle(), ytFile);
//
//                                    Mcontext.startActivity(new Intent(Mcontext, PlayActivity.class).putExtra("videourl", ytFile.getUrl()).putExtra(AppMeasurementSdk.ConditionalUserProperty.NAME, vMeta.getTitle()));
//                                    return;
//                                }
//                            }
//
//
//                        }
//                    });
//
//
//                    int size = 0;
//
//                    try {
//                        DisplayMetrics displayMetrics = new DisplayMetrics();
//                        ((Activity) Mcontext).getWindowManager()
//                                .getDefaultDisplay()
//                                .getMetrics(displayMetrics);
//
//                        int height = displayMetrics.heightPixels;
//                        int width = displayMetrics.widthPixels;
//
//                        size = width / 2;
//
//                    } catch (Exception e) {
//                        size = WindowManager.LayoutParams.WRAP_CONTENT;
//                    }
//
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        params = new WindowManager.LayoutParams(
//                                size,
//                                WindowManager.LayoutParams.WRAP_CONTENT,
//                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                                PixelFormat.TRANSLUCENT);
//
//                        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                        params.x = 0;
//                        params.y = 100;
//                    } else {
//                        params = new WindowManager.LayoutParams(
//                                size,
//                                WindowManager.LayoutParams.WRAP_CONTENT,
//                                WindowManager.LayoutParams.TYPE_PHONE,
//                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                                PixelFormat.TRANSLUCENT);
//
//                        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                        params.x = 0;
//                        params.y = 100;
//                    }
//
//
//                    // mainLayout.setLayoutParams(params);
//
//
//                    for (int i = 0, itag; i < ytFiles.size(); i++) {
//                        itag = ytFiles.keyAt(i);
//                        // ytFile represents one file with its url and meta data
//                        YtFile ytFile = ytFiles.get(itag);
//
//                        // Just add videos in a decent format => height -1 = audio
//                        if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
//                            addButtonToMainLayouttest(vMeta.getTitle(), ytFile);
//                        }
//                    }
//
//                    img_dialog.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialogquality.dismiss();
//                        }
//                    });
//
//                    dialogquality.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//                    dialogquality.getWindow().setAttributes(params);
//                    //  dialogquality.getWindow().setColorMode(Mcontext.getResources().getColor(R.color.colorAccent));
//
//                    dialogquality.show();
//
//                } else {
//                    if (!fromService) {
//                        pd.dismiss();
//                    }
//
//                    iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//
//
//                }
//
//
//            }
//        }.extract(youtubeLink, true, false);
//    }
//
//    private static void addButtonToMainLayout(final String videoTitle, final YtFile ytfile) {
//
//
//        // Display some buttons and let the user choose the format
//        String btnText = (ytfile.getFormat().getHeight() == -1) ? "Audio " +
//                ytfile.getFormat().getAudioBitrate() + " kbit/s" :
//                ytfile.getFormat().getHeight() + "p";
//        btnText += (ytfile.getFormat().isDashContainer()) ? " dash" : "";
//        Button btn = new Button(Mcontext);
//
//        btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//
//        btn.setText(btnText);
//        btn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (windowManager2 != null) {
//                    try {
//                        windowManager2.removeView(mChatHeadView);
//                    } catch (Exception e) {
//                        Log.i("LOGClipboard111111", "error is " + e.getMessage());
//
//                    }
//                }
//
//                String filename;
//                if (videoTitle.length() > 55) {
//                    filename = videoTitle.substring(0, 55) + "." + ytfile.getFormat().getExt();
//                } else {
//                    filename = videoTitle + "." + ytfile.getFormat().getExt();
//                }
//                filename = filename.replaceAll("[\\\\><\"|*?%:#/]", "");
//
////                downloadFromUrl(ytfile.getUrl(), videoTitle, filename);
////
////
////                String downloadUrl = ytFiles.get(itag).getUrl();
//
//
//                new downloadFile().Downloading(Mcontext, ytfile.getUrl(), filename, ".mp4");
//
//
//                dialogquality.dismiss();
//            }
//        });
//        mainLayout.addView(btn);
//    }
//
//    private static void addButtonToMainLayouttest(final String videoTitle, final YtFile ytfile) {
//
//
//        // Display some buttons and let the user choose the format
//        String btnText = (ytfile.getFormat().getHeight() == -1) ? "MP3 " +
//                ytfile.getFormat().getAudioBitrate() + " kbit/s" :
//                ytfile.getFormat().getHeight() + "p";
//        btnText += (ytfile.getFormat().isDashContainer()) ? " No Audio" : "";
//        Button btn = new Button(Mcontext);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        params.setMargins(8, 8, 8, 8);
//        btn.setLayoutParams(params);
//
//        // btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        btn.setBackground(Mcontext.getResources().getDrawable(R.drawable.btn_bg_download_screen));
//        btn.setTextColor(Color.WHITE);
//
//        btn.setText(btnText);
//        btn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (windowManager2 != null) {
//                    try {
//                        windowManager2.removeView(mChatHeadView);
//                    } catch (Exception e) {
//                        Log.i("LOGClipboard111111", "error is " + e.getMessage());
//
//                    }
//                }
//
//                String filename;
//                if (videoTitle.length() > 55) {
//                    filename = videoTitle.substring(0, 55);
//                } else {
//                    filename = videoTitle;
//                }
//                filename = filename.replaceAll("[\\\\><\"|*?%:#/]", "");
//
////                downloadFromUrl(ytfile.getUrl(), videoTitle, filename);
////
////
////                String downloadUrl = ytFiles.get(itag).getUrl();
//
//
//                if (ytfile.getFormat().getExt().equals("m4a")) {
//                    new downloadFile().Downloading(Mcontext, ytfile.getUrl(), filename, ".mp3");
//                } else {
//                    new downloadFile().Downloading(Mcontext, ytfile.getUrl(), filename, "." + ytfile.getFormat().getExt());
//
//                }
//
//                dialogquality.dismiss();
//            }
//        });
//        mainLayout.addView(btn);
//    }


    //TODO youtube comment till here

    public static void download(String url12) {
        String readLine;
        URL url = null;
        try {
            url = new URL(url12);


            Log.d("ThumbnailURL11111_1 ", url12);


//        URLConnection openConnection = url.openConnection();
//        openConnection.setRequestProperty("ModelUserInstagram-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            //       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));


            URL url1 = new URL(url12);
            URLConnection connection = url1.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));


            while ((readLine = bufferedReader.readLine()) != null) {
                //  readLine = bufferedReader.readLine();
                Log.d("ThumbnailURL11111_2  ", readLine);


                readLine = readLine.substring(readLine.indexOf("VideoObject"));
                String substring = readLine.substring(readLine.indexOf("thumbnailUrl") + 16);
                substring = substring.substring(0, substring.indexOf("\""));

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ThumbnailURL: ");
                stringBuilder.append(substring);

                Log.d("ThumbnailURL", substring);
                readLine = readLine.substring(readLine.indexOf("contentUrl") + 13);
                readLine = readLine.substring(0, readLine.indexOf("?"));
                stringBuilder = new StringBuilder();
                stringBuilder.append("ContentURL: ");
                stringBuilder.append(readLine);

                Log.d("ContentURL1111 thumb  ", substring);
                Log.d("ContentURL1111", stringBuilder.toString());


                if (readLine == null) {
                    break;
                }
            }
            bufferedReader.close();

        } catch (Exception e) {
//            Log.d("ContentURL1111 errrr", e.getMessage());
            e.printStackTrace();
        }
        // new downloadFile().Downloading(Mcontext, URL, Title, ".mp4");
        //   new DownloadFileFromURL().execute(new String[]{readLine});
    }

//    public static void getAllData(String url, String watermark) {
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, TiktokApiNowatermark, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                System.out.println("rescccccccc " + response);
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//
//
//                    if (jsonObject.getString("status").equals("success")) {
//
//                        Gson gson = new Gson();
//                        TikTokNoWaterMarkApi tikTokNoWaterMarkApidata = gson.fromJson(jsonObject.toString(), TikTokNoWaterMarkApi.class);
//
//                        System.out.println("resccccccccdataFull_Vide " + tikTokNoWaterMarkApidata.video_full_title);
//                        System.out.println("resccccccccdataORG " + tikTokNoWaterMarkApidata.ogvideourl);
//                        System.out.println("resccccccccdatawaterORG " + tikTokNoWaterMarkApidata.videourl);
//                        System.out.println("resccccccccdatamusicORG " + tikTokNoWaterMarkApidata.musicplayurl);
//
//                        if (watermark.equals("true")) {
////
////                            if (tikTokNoWaterMarkApidata.video_full_title.contains("null")){
////                                tikTokNoWaterMarkApidata.video_full_title = "Tiktokvideo"+System.currentTimeMillis();
////
////                            }
//
//
//                            new downloadFile().Downloading(Mcontext, tikTokNoWaterMarkApidata.ogvideourl, tikTokNoWaterMarkApidata.video_full_title + "_" + tikTokNoWaterMarkApidata.username, ".mp4");
//
//                            if (!fromService) {
//                                pd.dismiss();
//
//
//                                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));
//
//
//                            }
//
//                        } else if (watermark.equals("false") && tikTokNoWaterMarkApidata.watermark_removed.equals("yes")) {
//
////                            if (tikTokNoWaterMarkApidata.video_full_title.contains("null")){
////                                tikTokNoWaterMarkApidata.video_full_title = "Tiktokvideo"+System.currentTimeMillis();
////
////                            }
//                            new downloadFile().Downloading(Mcontext, tikTokNoWaterMarkApidata.videourl, tikTokNoWaterMarkApidata.video_full_title + "_" + tikTokNoWaterMarkApidata.username, ".mp4");
//                            //   new DownloadFileFromURL(Mcontext,tikTokNoWaterMarkApidata.name).execute(tikTokNoWaterMarkApidata.videourl);
//
//
//                            if (!fromService) {
//                                pd.dismiss();
//
//
//                                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));
//
//                            }
//
//                        } else if (watermark.equals("mp3")) {
//
////                            if (tikTokNoWaterMarkApidata.video_full_title.contains("null")){
////                                tikTokNoWaterMarkApidata.video_full_title = "Tiktokmp3"+System.currentTimeMillis();
////
////                            }
//                            new downloadFile().Downloading(Mcontext, tikTokNoWaterMarkApidata.musicplayurl, tikTokNoWaterMarkApidata.video_full_title + "_" + tikTokNoWaterMarkApidata.username, ".mp3");
//
//
//                            if (!fromService) {
//                                pd.dismiss();
//
//
//                                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));
//
//
//                            }
//
//
//                        }
//
//
//                    } else {
//                        if (!fromService) {
//                            pd.dismiss();
//
//
//                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//
//
//                        }
//                    }
//
//
//                } catch (Exception e) {
//                    if (!fromService) {
//                        pd.dismiss();
//
//
//                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//
//
//                    }
//                    System.out.println("i ah error " + e.getMessage());
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("i ah error " + error.getMessage());
//
//                if (!fromService) {
//                    pd.dismiss();
//
//
//                    iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//
//
//                }
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("tikurl", url);
//
//                return params;
//            }
//        };
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                3000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        VollySingltonClass.getmInstance(Mcontext).addToRequsetque(stringRequest);
//
//
//    }


//    @Keep
//    public static void getAllDataForLikee2(String url, boolean hasQualityOption) {
//
//        AndroidNetworking.get(LikeeApiUrl + url)
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        System.out.println("reccccc VVKK " + response);
//
//
//                        System.out.println("rescccccccc " + response + "     myurl is " + LikeeApiUrl + url);
//
//                        ArrayList<VideoModel> videoModelArrayList = new ArrayList<>();
//
//                        try {
//                            // JSONObject jSONObject = new JSONObject(response);
//
//                            String videotitleis = response.getString("title");
//
//                            JSONArray str = response.getJSONArray("links");
//
//
//                            for (int i = 0; i < str.length(); i++) {
//                                VideoModel videoModel = new VideoModel();
//                                JSONObject jSONObject2 = str.getJSONObject(i);
//                                videoModel.setTitle(videotitleis);
//                                videoModel.setUrl(jSONObject2.getString("url"));
//
//
//                                System.out.println("reccccc VVKK URLLL " + jSONObject2.getString("url"));
//
//
//                                videoModel.setType(jSONObject2.getString("type"));
//                                videoModel.setSize(jSONObject2.getString("size"));
//                                videoModel.setQuality(jSONObject2.getString("quality"));
//
//                                videoModelArrayList.add(videoModel);
//
//                            }
//
//
//                            if (hasQualityOption) {
//
//                                dialog_quality_allvids = new Dialog(Mcontext);
//
//
//                                if (!fromService) {
//                                    pd.dismiss();
//                                }
//
//
//                                windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);
//                                LayoutInflater layoutInflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                                mChatHeadView = layoutInflater.inflate(R.layout.dialog_quality_ytd, null);
//
//
//                                dialog_quality_allvids.setContentView(mChatHeadView);
//
//
//                                mainLayout = mChatHeadView.findViewById(R.id.linlayout_dialog);
//
//                                img_dialog = mChatHeadView.findViewById(R.id.img_dialog);
//
//                                mainLayout = dialog_quality_allvids.findViewById(R.id.linlayout_dialog);
//                                img_dialog = dialog_quality_allvids.findViewById(R.id.img_dialog);
//
//
//                                int size = 0;
//
//                                try {
//                                    DisplayMetrics displayMetrics = new DisplayMetrics();
//                                    ((Activity) Mcontext).getWindowManager()
//                                            .getDefaultDisplay()
//                                            .getMetrics(displayMetrics);
//
//                                    int height = displayMetrics.heightPixels;
//                                    int width = displayMetrics.widthPixels;
//
//                                    size = width / 2;
//
//                                } catch (Exception e) {
//                                    size = WindowManager.LayoutParams.WRAP_CONTENT;
//                                }
//
//
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                    params = new WindowManager.LayoutParams(
//                                            size,
//                                            WindowManager.LayoutParams.WRAP_CONTENT,
//                                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                                                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                                                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                                            PixelFormat.TRANSLUCENT);
//
//                                    params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                                    params.x = 0;
//                                    params.y = 100;
//                                } else {
//                                    params = new WindowManager.LayoutParams(
//                                            size,
//                                            WindowManager.LayoutParams.WRAP_CONTENT,
//                                            WindowManager.LayoutParams.TYPE_PHONE,
//                                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                                                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                                                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                                            PixelFormat.TRANSLUCENT);
//
//                                    params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                                    params.x = 0;
//                                    params.y = 100;
//                                }
//
//
//                                // mainLayout.setLayoutParams(params);
//
//
//                                for (int i = 0; i < videoModelArrayList.size(); i++) {
//
//
//
//                                    addButtonToMainLayouttest_allvideo(videoModelArrayList.get(i).getQuality(), videoModelArrayList.get(i).getUrl(), videoModelArrayList.get(i).getTitle());
//
//                                }
//
//                                img_dialog.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_quality_allvids.dismiss();
//                                    }
//                                });
//
//                                dialog_quality_allvids.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//                                dialog_quality_allvids.getWindow().setAttributes(params);
//                                //  dialogquality.getWindow().setColorMode(Mcontext.getResources().getColor(R.color.colorAccent));
//
//                                dialog_quality_allvids.show();
//
//
//                                dialog_quality_allvids.show();
//                            } else {
//                                if (url.contains("tiktok")) {
//
//                                    String outputFileName = MY_ANDROID_10_IDENTIFIER_OF_FILE + getFilenameFromURL("https://www.tiktok.com/@beauty_0f_nature/video/6825315100933639426") + ".mp4";
//                                    String output = "";
//
//                                    if (outputFileName.length() > 100)
//                                        outputFileName = outputFileName.substring(0, 100);
//
//
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                        output = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + outputFileName;
//
//                                    } else {
//                                        output = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + outputFileName;
//
//                                        //   output = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + DOWNLOAD_DIRECTORY + "/" + outputFileName));
//
//                                    }
//
//
//                                    //  new DownloadFileFromURL1().execute("https://v16-web-newkey.tiktokcdn.com/99818623022622f41b12900e3d086eb4/5f6e1cf9/video/tos/useast2a/tos-useast2a-ve-0068c003/590e2a192cb8428ca4b2509167b01bfa/?a=1988&br=4926&bt=2463&cr=0&cs=0&cv=1&dr=0&ds=3&er=&l=202009251038010101151510600F065F52&lr=tiktok_m&mime_type=video_mp4&qs=0&rc=ajVqbW52cWdzdTMzZzczM0ApNTQ3ZTo2aDxoNzVoaDs6NWdsMWwzajNnc29fLS1fMTZzc19hYGExNTUxX2MxLl8zMDY6Yw%3D%3D&vl=&vr=");
//                                    AndroidNetworking
//                                            .download("https://v16-web-newkey.tiktokcdn.com/99818623022622f41b12900e3d086eb4/5f6e1cf9/video/tos/useast2a/tos-useast2a-ve-0068c003/590e2a192cb8428ca4b2509167b01bfa/?a=1988&br=4926&bt=2463&cr=0&cs=0&cv=1&dr=0&ds=3&er=&l=202009251038010101151510600F065F52&lr=tiktok_m&mime_type=video_mp4&qs=0&rc=ajVqbW52cWdzdTMzZzczM0ApNTQ3ZTo2aDxoNzVoaDs6NWdsMWwzajNnc29fLS1fMTZzc19hYGExNTUxX2MxLl8zMDY6Yw%3D%3D&vl=&vr="
//                                                    , output, outputFileName)
//                                            .setTag("downloadTest")
//                                            .setPriority(Priority.MEDIUM)
//                                            .build()
//                                            .setDownloadProgressListener(new DownloadProgressListener() {
//                                                @Override
//                                                public void onProgress(long bytesDownloaded, long totalBytes) {
//                                                    // do anything with progress
//                                                }
//                                            })
//                                            .startDownload(new DownloadListener() {
//                                                @Override
//                                                public void onDownloadComplete() {
//                                                    if (!fromService) {
//                                                        pd.dismiss();
//
//
//                                                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.exo_download_completed));
//
//
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onError(ANError error) {
//                                                    if (!fromService) {
//                                                        pd.dismiss();
//
//
//                                                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));
//
//
//                                                    }
//                                                }
//                                            });
//                                } else {
//
//                                    new downloadFile().Downloading(Mcontext, videoModelArrayList.get(0).getUrl(), getFilenameFromURL(videoModelArrayList.get(0).getUrl()), ".mp4");
//                                }
//                                if (!fromService) {
//                                    pd.dismiss();
//
//
//                                    iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));
//
//
//                                }
//                            }
//
//
//                        } catch (Exception str2) {
//                            str2.printStackTrace();
//                            // Toast.makeText(Mcontext, "Invalid URL", 0).show();
//
//                            if (!fromService) {
//                                pd.dismiss();
//                                iUtils.ShowToast(Mcontext, response + ":   " + Mcontext.getResources().getString(R.string.invalid_url));
//                            }
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        System.out.println("reccccc VVKK error " + error);
//
//                    }
//                });
//    }
//
//
//    public static void getAllDataForLikee(String urlp, boolean hasQualityOption) {
//
//        System.out.println("resccccccccURL " + urlp);
//        //  url = "https://vm.tiktok.com/KEpK7n/";
//
//
//        System.out.println("resccccccccmyurl is " + LikeeApiUrl + urlp);
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.nobots.in/videodownloader/system/api.php?url=" + urlp, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                System.out.println("rescccccccc " + response + "     myurl is " + LikeeApiUrl + urlp);
//
//                ArrayList<VideoModel> videoModelArrayList = new ArrayList<>();
//
//                try {
//                    JSONObject jSONObject = new JSONObject(response);
//
//                    String videotitleis = jSONObject.getString("title");
//
//                    JSONArray str = jSONObject.getJSONArray("links");
//
//
//                    for (int i = 0; i < str.length(); i++) {
//                        VideoModel videoModel = new VideoModel();
//                        JSONObject jSONObject2 = str.getJSONObject(i);
//                        videoModel.setTitle(videotitleis);
//                        videoModel.setUrl(jSONObject2.getString("url"));
//                        videoModel.setType(jSONObject2.getString("type"));
//                        videoModel.setSize(jSONObject2.getString("size"));
//                        videoModel.setQuality(jSONObject2.getString("quality"));
//
//                        videoModelArrayList.add(videoModel);
//
//                    }
//
//
//                    if (hasQualityOption) {
//
//                        dialog_quality_allvids = new Dialog(Mcontext);
//
//
//                        if (!fromService) {
//                            pd.dismiss();
//                        }
//
//
//                        windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);
//                        LayoutInflater layoutInflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                        mChatHeadView = layoutInflater.inflate(R.layout.dialog_quality_ytd, null);
//
//
//                        dialog_quality_allvids.setContentView(mChatHeadView);
//
//
//                        mainLayout = mChatHeadView.findViewById(R.id.linlayout_dialog);
//
//                        img_dialog = mChatHeadView.findViewById(R.id.img_dialog);
//
//                        mainLayout = dialog_quality_allvids.findViewById(R.id.linlayout_dialog);
//                        img_dialog = dialog_quality_allvids.findViewById(R.id.img_dialog);
//
//
//                        int size = 0;
//
//                        try {
//                            DisplayMetrics displayMetrics = new DisplayMetrics();
//                            ((Activity) Mcontext).getWindowManager()
//                                    .getDefaultDisplay()
//                                    .getMetrics(displayMetrics);
//
//                            int height = displayMetrics.heightPixels;
//                            int width = displayMetrics.widthPixels;
//
//                            size = width / 2;
//
//                        } catch (Exception e) {
//                            size = WindowManager.LayoutParams.WRAP_CONTENT;
//                        }
//
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            params = new WindowManager.LayoutParams(
//                                    size,
//                                    WindowManager.LayoutParams.WRAP_CONTENT,
//                                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                                    PixelFormat.TRANSLUCENT);
//
//                            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                            params.x = 0;
//                            params.y = 100;
//                        } else {
//                            params = new WindowManager.LayoutParams(
//                                    size,
//                                    WindowManager.LayoutParams.WRAP_CONTENT,
//                                    WindowManager.LayoutParams.TYPE_PHONE,
//                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                                    PixelFormat.TRANSLUCENT);
//
//                            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                            params.x = 0;
//                            params.y = 100;
//                        }
//
//
//                        // mainLayout.setLayoutParams(params);
//
//
//                        for (int i = 0; i < videoModelArrayList.size(); i++) {
//
//
//                            addButtonToMainLayouttest_allvideo(videoModelArrayList.get(i).getQuality(), videoModelArrayList.get(i).getUrl(), videoModelArrayList.get(i).getTitle());
//
//                        }
//
//                        img_dialog.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog_quality_allvids.dismiss();
//                            }
//                        });
//
//                        dialog_quality_allvids.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//                        dialog_quality_allvids.getWindow().setAttributes(params);
//                        //  dialogquality.getWindow().setColorMode(Mcontext.getResources().getColor(R.color.colorAccent));
//
//                        dialog_quality_allvids.show();
//
//
//                        dialog_quality_allvids.show();
//                    } else {
//
//
//                        new downloadFile().Downloading(Mcontext, videoModelArrayList.get(0).getUrl(), getFilenameFromURL(videoModelArrayList.get(0).getUrl()), ".mp4");
//                    }
//                    if (!fromService) {
//                        pd.dismiss();
//
//
//                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));
//
//
//                    }
//
//
//                } catch (Exception str2) {
//                    str2.printStackTrace();
//                    // Toast.makeText(Mcontext, "Invalid URL", 0).show();
//
//                    if (!fromService) {
//                        pd.dismiss();
//                        iUtils.ShowToast(Mcontext, response + ":   " + Mcontext.getResources().getString(R.string.invalid_url));
//                    }
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("i ah error " + error.getMessage());
//
//                if (!fromService) {
//                    pd.dismiss();
//
//                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//
//                }
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                //  params.put("url", urlp);
//
//
//                return params;
//            }
//        };
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                3000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        VollySingltonClass.getmInstance(Mcontext).addToRequsetque(stringRequest);
//
//
//    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static void addButtonToMainLayouttest_allvideo(final String videoTitle, String ytfile, String video_title) {


        // Display some buttons and let the user choose the format
        String btnText = videoTitle;
        Button btn = new Button(Mcontext);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        btn.setLayoutParams(params);

        // btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setBackground(Mcontext.getResources().getDrawable(R.drawable.btn_bg_download_screen));
        btn.setTextColor(Color.WHITE);

        btn.setText(btnText);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (windowManager2 != null) {
                    try {
                        windowManager2.removeView(mChatHeadView);
                    } catch (Exception e) {
                        Log.i("LOGClipboard111111", "error is " + e.getMessage());

                    }
                }


//                downloadFromUrl(ytfile.getUrl(), videoTitle, filename);
//
//
//                String downloadUrl = ytFiles.get(itag).getUrl();

                if (btnText.equals("audio/mp4")) {
                    new downloadFile().Downloading(Mcontext, ytfile, video_title + "_" + videoTitle, ".mp3");
                } else {
                    new downloadFile().Downloading(Mcontext, ytfile, video_title + "_" + videoTitle, ".mp4");

                }
                dialog_quality_allvids.dismiss();
            }
        });
        mainLayout.addView(btn);
    }


    private static class GetInstagramVideo extends AsyncTask<String, Void, Document> {
        Document doc;
        private Handler mHandler;

        @Override
        protected Document doInBackground(String... urls) {

            System.out.println("mydahjsdgadashas2244  " + urls[0]);

            try {
                doc = Jsoup.connect(urls[0]).get();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error" + e.getMessage());
            }
            return doc;

        }

        protected void onPostExecute(Document result) {
            if (!fromService) {

                pd.dismiss();
            }

            try {
                String URL = result.select("meta[property=\"og:video\"]").last().attr("content");
                Title = result.title();
                System.out.println("mydahjsdgadashas  " + Title);

                new downloadFile().Downloading(Mcontext, URL, Title + ".mp4", ".mp4");

            } catch (Exception e) {
                System.out.println("mydahjsdgadashas22  " + e.getMessage());
                e.printStackTrace();

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));

            }
        }
    }


    private static class callGetShareChatData extends AsyncTask<String, Void, Document> {
        Document ShareChatDoc;

        callGetShareChatData() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Document doInBackground(String... strArr) {
            try {
                this.ShareChatDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception strArr2) {
                strArr2.printStackTrace();
                Log.d("ContentValues", "doInBackground: Error");
            }
            return this.ShareChatDoc;
        }

        protected void onPostExecute(Document document) {
            String charSequence = "";

            try {

                if (!fromService) {

                    pd.dismiss();
                }


                VideoUrl = document.select("meta[property=\"og:video:secure_url\"]").last().attr("content");
                Log.e("onPostExecute: ", VideoUrl);
                if (!VideoUrl.equals(charSequence)) {


                    try {
                        String myurldocument = VideoUrl;


                        String nametitle = "sharechat_" +
                                System.currentTimeMillis() +
                                ".mp4";

                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = charSequence;
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        document2.printStackTrace();
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }
                }
            } catch (Exception document22) {
                document22.printStackTrace();
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }
    }


    private static class callGetRoposoData extends AsyncTask<String, Void, Document> {
        Document ShareChatDoc;

        callGetRoposoData() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Document doInBackground(String... strArr) {
            try {
                this.ShareChatDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception strArr2) {
                strArr2.printStackTrace();
                Log.d("ContentValues roposo_", "doInBackground: Error");
            }
            return this.ShareChatDoc;
        }

        protected void onPostExecute(Document document) {
            String charSequence = "";

            try {

                if (!fromService) {

                    pd.dismiss();
                }


                VideoUrl = document.select("meta[property=\"og:video\"]").last().attr("content");
                Log.e("onPostExecute:roposo_ ", VideoUrl);
                if (!VideoUrl.equals(charSequence)) {


                    try {
                        String myurldocument = VideoUrl;


                        String nametitle = "roposo_" +
                                System.currentTimeMillis() +
                                ".mp4";

                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = charSequence;
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        document2.printStackTrace();
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }
                }
            } catch (Exception document22) {
                document22.printStackTrace();
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }
    }


    public static class CallMitronData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            String str;
            try {
                String str2 = strArr[0];
                if (str2.contains("api.mitron.tv")) {
                    String[] split = str2.split("=");
                    str = "https://web.mitron.tv/video/" + split[split.length - 1];
                } else {
                    str = strArr[0];
                }
                this.RoposoDoc = Jsoup.connect(str).get();
            } catch (IOException e) {
                e.printStackTrace();
                if (!fromService) {

                    pd.dismiss();
                }
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            //   System.out.println("myresponseis111 " + document.html());

            try {

                if (!fromService) {

                    pd.dismiss();
                }

                String html = document.select("script[id=\"__NEXT_DATA__\"]").last().html();
                if (!html.equals("")) {
                    this.VideoUrl = String.valueOf(new JSONObject(html).getJSONObject("props").getJSONObject("pageProps").getJSONObject(MimeTypes.BASE_TYPE_VIDEO).get("videoUrl"));
                    if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                        try {


                            String myurldocument = VideoUrl;


                            String nametitle = "mitron_" +
                                    System.currentTimeMillis();

                            new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                            //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                            VideoUrl = "";
                            //   binding.etText.setText(charSequence);

                        } catch (Exception document2) {
                            System.out.println("myresponseis111 exp1 " + document2.getMessage());

                            document2.printStackTrace();
                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                        }

                        return;
                    }
                    return;
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }


    public static class CallJoshData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                String html = document.select("script[id=\"__NEXT_DATA__\"]").last().html();
                if (!html.equals("")) {
                    this.VideoUrl = String.valueOf(new JSONObject(html).getJSONObject("props").getJSONObject("pageProps").getJSONObject("detail").getJSONObject(DataSchemeDataSource.SCHEME_DATA).get("mp4_url"));
                    this.VideoUrl = this.VideoUrl.replace("{quality}", "720");
                    this.VideoUrl = this.VideoUrl.replace("{resolution}", "720");

                    if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                        try {


                            String myurldocument = VideoUrl;


                            String nametitle = "joshvideo_" +
                                    System.currentTimeMillis();

                            new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                            //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                            VideoUrl = "";
                            //   binding.etText.setText(charSequence);

                        } catch (Exception document2) {
                            System.out.println("myresponseis111 exp1 " + document2.getMessage());

                            document2.printStackTrace();
                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                        }

                        return;
                    }
                    return;
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }


    public static class CallTrillerData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                System.out.println("myresponseis111 0exp1 " + strArr[0]);

                this.RoposoDoc = Jsoup.connect(strArr[0].replace("-", "")).get();
                System.out.println("myresponseis111 1exp1 " + strArr[0].replace("-", ""));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {
            System.out.println("myresponseis111 2exp1 " + document.body());


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                this.VideoUrl = document.select(MimeTypes.BASE_TYPE_VIDEO).last().attr("src");
                if (this.VideoUrl.startsWith("//")) {
                    this.VideoUrl = "https:" + this.VideoUrl;
                }

                if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                    try {


                        String myurldocument = VideoUrl;


                        String nametitle = "trillervideo_" +
                                System.currentTimeMillis();

                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());

                        document2.printStackTrace();
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }

                    return;
                }

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    public static class CallRizzleData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                String html = document.select("script[id=\"__NEXT_DATA__\"]").last().html();
                if (!html.equals("")) {
                    this.VideoUrl = String.valueOf(new JSONObject(html).getJSONObject("props").getJSONObject("pageProps").getJSONObject("post").getJSONObject(MimeTypes.BASE_TYPE_VIDEO).get("originalUrl"));
                    if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                        try {


                            String myurldocument = VideoUrl;


                            String nametitle = "rizzlevideo_" +
                                    System.currentTimeMillis();

                            new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                            //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                            VideoUrl = "";
                            //   binding.etText.setText(charSequence);

                        } catch (Exception document2) {
                            System.out.println("myresponseis111 exp1 " + document2.getMessage());

                            document2.printStackTrace();
                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                        }

                        return;
                    }
                    return;
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    public static class CallIfunnyData extends AsyncTask<String, Void, Document> {
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


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                String html = document.select("script[class=\"js-media-template\"]").first().html();
                new Element(html);
                Matcher matcher = Pattern.compile("<video[^>]+poster\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>").matcher(html);
                while (matcher.find()) {
                    this.VideoUrl = matcher.group(1).replace("jpg", "mp4").replace("images", "videos").replace("_3", "_1");
                }
                if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                    try {


                        String myurldocument = VideoUrl;


                        String nametitle = "ifunnyvideo_" +
                                System.currentTimeMillis();

                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());

                        document2.printStackTrace();
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }

                    return;
                }

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    public static class CallLikeeData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                String str = strArr[0];
                if (str.contains("com")) {
                    str = str.replace("com", MimeTypes.BASE_TYPE_VIDEO);
                }
                this.RoposoDoc = Jsoup.connect("https://likeedownloader.com/results").data("id", str).userAgent("Mozilla").post();
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

                this.VideoUrl = document.select("a.without_watermark").last().attr("href");

                if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                    try {


                        String myurldocument = VideoUrl;


                        String nametitle = "Likeevideo_" +
                                System.currentTimeMillis();

                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());

                        document2.printStackTrace();
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }

                    return;
                }

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }


    public static class CalltrellData extends AsyncTask<String, Void, Document> {
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


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                String html = document.select("script[id=\"__NEXT_DATA__\"]").last().html();
                if (!html.equals("")) {
                    this.VideoUrl = String.valueOf(new JSONObject(new JSONObject(html)
                            .getJSONObject("props")
                            .getJSONObject("pageProps")
                            .getJSONObject("result")
                            .getJSONObject("result").getJSONObject("trail")
                            .getJSONArray("posts").get(0).toString())
                            .get(MimeTypes.BASE_TYPE_VIDEO));

                    System.out.println("myresponseis111 exp991 " + VideoUrl);


                    if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                        try {


                            String myurldocument = VideoUrl;


                            String nametitle = "trellvideo_" +
                                    System.currentTimeMillis();

                            new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                            //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                            VideoUrl = "";
                            //   binding.etText.setText(charSequence);

                        } catch (Exception document2) {
                            System.out.println("myresponseis111 exp1 " + document2.getMessage());

                            document2.printStackTrace();
                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                        }

                        return;
                    }
                    return;
                }

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    public static class CallBoloindyaData extends AsyncTask<String, Void, Document> {
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


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                Iterator it = document.getElementsByTag("script").iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Element element = (Element) it.next();
                    if (element.data().contains("videoFileCDN")) {
                        for (String str : element.data().split(StringUtils.LF)) {
                            if (str.contains("var videoFileCDN=\"https")) {
                                this.VideoUrl = str.split("=")[1]
                                        .replace("\"", "")
                                        .replace("\"", "")
                                        .replace(";", "");
                            }
                        }
                    }
                }
                if (this.VideoUrl.startsWith("//")) {
                    this.VideoUrl = "https:" + this.VideoUrl;
                }
                if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                    try {


                        String myurldocument = VideoUrl;


                        String nametitle = "Boloindyavideo_" +
                                System.currentTimeMillis();

                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());

                        document2.printStackTrace();
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }

                    return;
                }

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    public static class CallchingariData extends AsyncTask<String, Void, Document> {
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


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                this.VideoUrl = document.select("meta[property=\"og:video\"]").last().attr("content");
                if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                    try {


                        String myurldocument = VideoUrl;


                        String nametitle = "chingarivideo_" +
                                System.currentTimeMillis();

                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());

                        document2.printStackTrace();
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }

                    return;
                }

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    public static class CallhindData extends AsyncTask<String, Void, Document> {
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


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                Iterator it = document.getElementsByTag("script").iterator();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    if (element.data().contains("window.__STATE__")) {
                        String replace = element.html().replace("window.__STATE__", "").replace(";", "")
                                .replace("=", "");
                        this.VideoUrl = String.valueOf(new JSONObject(new JSONObject(new JSONArray("[" + replace + "]")
                                .get(0).toString()).getJSONObject("feed").getJSONArray("feed")
                                .get(0).toString()).get("download_media"));
                        if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                            try {


                                String myurldocument = VideoUrl;


                                String nametitle = "hindvideo_" +
                                        System.currentTimeMillis();

                                new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                                //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                                VideoUrl = "";
                                //   binding.etText.setText(charSequence);

                            } catch (Exception document2) {
                                System.out.println("myresponseis111 exp1 " + document2.getMessage());

                                document2.printStackTrace();
                                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                            }

                            return;
                        }
                    }
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }


    public static class CalldubsmashData extends AsyncTask<String, Void, Document> {
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


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                this.VideoUrl = document.select(MimeTypes.BASE_TYPE_VIDEO).last().attr("src");
                if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                    try {


                        String myurldocument = VideoUrl;


                        String nametitle = "dubsmashvideo_" +
                                System.currentTimeMillis();

                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());

                        document2.printStackTrace();
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }

                    return;
                }

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }


    public static class CalltumblerData extends AsyncTask<String, Void, Document> {
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


            try {

                if (!fromService) {

                    pd.dismiss();
                }
                // System.out.println("myresponseis111 exp166 " + document);
//                System.out.println("myresponseis111 exp166 " + document.select("source[src]").first().attr("src"));
//                System.out.println("myresponseis111 exp177 " + document.select("video[src]").first().attr("src"));

                this.VideoUrl = document.select("source").last().attr("src");

                System.out.println("myresponseis111 exp1 " + VideoUrl);

                if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                    try {


                        String myurldocument = VideoUrl;

                        String nametitle = "tumbler_" +
                                System.currentTimeMillis();

                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());

                        document2.printStackTrace();
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }

                    return;
                }

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }


    public static class CalllinkedinData extends AsyncTask<String, Void, Document> {
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


            try {

                if (!fromService) {

                    pd.dismiss();
                }
                // System.out.println("myresponseis111 exp166 " + document);
//                System.out.println("myresponseis111 exp166 " + document.select("source[src]").first().attr("src"));
//                System.out.println("myresponseis111 exp177 " + document.select("video[src]").first().attr("src"));

                this.VideoUrl = document.select("video").last().attr("data-sources");


                JSONArray jsonArray = new JSONArray(VideoUrl);
                System.out.println("myresponseis111 exp1 " + jsonArray.getJSONObject(0).getString("src"));


                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(jsonArray.getJSONObject(0).getString("src"));
                arrayList.add(jsonArray.getJSONObject(1).getString("src"));

                CharSequence[] charSequenceArr = new CharSequence[arrayList.size()];

                charSequenceArr[0] = "Low quality";
                charSequenceArr[1] = "High quality";

                new AlertDialog.Builder(Mcontext).setTitle("Quality!").setItems(charSequenceArr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new downloadFile().Downloading(Mcontext, arrayList.get(i), "Linkedin_" + System.currentTimeMillis(), ".mp4");

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!fromService) {

                            pd.dismiss();
                        }
                    }
                }).setCancelable(false).show();


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


    public static class CallgaanaData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                //   this.RoposoDoc = Jsoup.connect(strArr[0]).get();
                //   download_hlsganna("", ".mp4");

            } catch (Exception e) {

                System.out.println("jskdfhksdhfkshdfkhsdj " + e.getMessage());
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {


            if (!fromService) {

                pd.dismiss();
            }


        }
    }

    public static class CallmxtaktakData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();
                //   download_hlsganna("", ".mp4");

            } catch (Exception e) {

                System.out.println("jskdfhksdhfkshdfkhsdj " + e.getMessage());
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            String charSequence = "";

            try {


                String data = "";

                Iterator<Element> documentitrator = document.select("script").iterator();

                do {
                    if (!documentitrator.hasNext()) {

                        break;
                    }
                    data = documentitrator.next().data();
                    Log.e("onP4342424te:datais ", data);


                } while (!data.contains("window._state"));


                String stringbuil = data.substring(data.indexOf("{"), data.indexOf("};")) + "}";
                Log.e("onPostbjnkjh:oso_11 ", stringbuil);


                if (!document.equals("")) {
                    try {
                        JSONObject jSONObject = new JSONObject(stringbuil);
                        VideoUrl = jSONObject.getJSONObject("sharePhoto").getString("mp4Url");

                        Log.e("onPostExecute:roposo_ ", VideoUrl);

                        getSnackVideoData(jSONObject.getString("shortUrl"), Mcontext);
                        VideoUrl = charSequence;

                    } catch (Exception document2) {
                        document2.printStackTrace();

                        System.out.println("respossss112212121qerrr " + document2.getMessage());

                        if (!fromService) {

                            pd.dismiss();
                        }
                    }
                }


            } catch (Exception document22) {
                if (!fromService) {

                    pd.dismiss();
                }
                document22.printStackTrace();
                System.out.println("respossss112212121qerrr " + document22.getMessage());

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }


        }
    }

    public static class CallgdriveData extends AsyncTask<String, Void, String> {
        String VideoUrl = "";
        LowCostVideo xGetter;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            this.xGetter = new LowCostVideo(Mcontext);
            this.xGetter.onFinish(new LowCostVideo.OnTaskCompleted() {
                public void onTaskCompleted(ArrayList<XModel> arrayList, boolean z) {
                    if (!z) {

                        System.out.println("myresponseis111 exp122 " + arrayList.get(0));

                        CallgdriveData.this.done(arrayList.get(0));


                    } else if (arrayList != null) {
                        System.out.println("myresponseis111 exp133 " + arrayList.get(0));

                        CallgdriveData.this.multipleQualityDialog(arrayList);
                    } else {


                        if (!fromService) {

                            pd.dismiss();
                        }
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }
                }

                public void onError() {


                    if (!fromService) {

                        pd.dismiss();
                    }
                    iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                }
            });

        }

        public String doInBackground(String... strArr) {
            return strArr[0];
        }

        public void onPostExecute(String str) {
            System.out.println("myresponseis111 exp13344 " + str);

            if (xGetter != null) {
                this.xGetter.find(str);
                //   System.out.println("myresponseis111 exp13344 " + xGetter.find(str));

            } else {
                this.xGetter = new LowCostVideo(Mcontext);
                this.xGetter.onFinish(new LowCostVideo.OnTaskCompleted() {
                    public void onTaskCompleted(ArrayList<XModel> arrayList, boolean z) {

                        System.out.println("myresponseis111 exp133 " + arrayList.get(0));

                        if (!z) {

                            System.out.println("myresponseis111 exp122 " + arrayList.get(0));

                            CallgdriveData.this.done(arrayList.get(0));


                        } else if (arrayList != null) {
                            System.out.println("myresponseis111 exp133 " + arrayList.get(0));

                            CallgdriveData.this.multipleQualityDialog(arrayList);
                        } else {


                            if (!fromService) {

                                pd.dismiss();
                            }
                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                        }


                    }

                    public void onError() {


                        if (!fromService) {

                            pd.dismiss();
                        }
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }
                });
                this.xGetter.find(str);
            }
        }

        public void multipleQualityDialog(final ArrayList<XModel> arrayList) {
            CharSequence[] charSequenceArr = new CharSequence[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                charSequenceArr[i] = arrayList.get(i).getQuality();
            }
            new AlertDialog.Builder(Mcontext).setTitle("Quality!").setItems(charSequenceArr, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    CallgdriveData.this.done(arrayList.get(i));
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (!fromService) {

                        pd.dismiss();
                    }
                }
            }).setCancelable(false).show();
        }


        public void done(XModel xModel) {


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                this.VideoUrl = xModel.getUrl();
                if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                    try {


                        String myurldocument = VideoUrl;


                        String nametitle = "Allvideo_" +
                                System.currentTimeMillis();

                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());

                        document2.printStackTrace();
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }

                    return;
                }

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }

        }

    }


    private static class callGetSnackAppData extends AsyncTask<String, Void, Document> {
        Document ShareChatDoc;
        private Iterator<Element> abk;

        callGetSnackAppData() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Document doInBackground(String... strArr) {
            try {
                this.ShareChatDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception strArr2) {
                strArr2.printStackTrace();
                Log.d("ContentValues roposo_", "doInBackground: Error");
            }
            return this.ShareChatDoc;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected void onPostExecute(Document document) {
            String charSequence = "";

            try {


                String data = "";

                Iterator<Element> documentitrator = document.select("script").iterator();

                do {
                    if (!documentitrator.hasNext()) {

                        break;
                    }
                    data = documentitrator.next().data();
                    Log.e("onP4342424te:datais ", data);


                } while (!data.contains("window.__INITIAL_STATE__"));


                String stringbuil = data.substring(data.indexOf("{"), data.indexOf("};")) + "}";
                Log.e("onPostbjnkjh:oso_11 ", stringbuil);


                if (!document.equals("")) {
                    try {
                        JSONObject jSONObject = new JSONObject(stringbuil);
                        VideoUrl = jSONObject.getJSONObject("sharePhoto").getString("mp4Url");

                        Log.e("onPostExecute:roposo_ ", VideoUrl);

                        getSnackVideoData(jSONObject.getString("shortUrl"), Mcontext);
                        VideoUrl = charSequence;

                    } catch (Exception document2) {
                        document2.printStackTrace();

                        System.out.println("respossss112212121qerrr " + document2.getMessage());

                        if (!fromService) {

                            pd.dismiss();
                        }
                    }
                }


                //   VideoUrl = document.select("video[src]").first().attr("src");


                //  VideoUrl = document.select("meta[property=\"og:video\"]").last().attr("content");
//                Log.e("onPostExecute:roposo_ ", VideoUrl);
//                if (!VideoUrl.equals(charSequence)) {
//
//
//                    try {
//                        String myurldocument = VideoUrl;
//
//
//                        String nametitle = "snackvideo_" +
//                                System.currentTimeMillis() +
//                                ".mp4";
//
//                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");
//
//                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
//                        VideoUrl = charSequence;
//                        //   binding.etText.setText(charSequence);
//
//                    } catch (Exception document2) {
//                        document2.printStackTrace();
//                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//                    }
//                }


            } catch (Exception document22) {
                if (!fromService) {

                    pd.dismiss();
                }
                document22.printStackTrace();
                System.out.println("respossss112212121qerrr " + document22.getMessage());

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }
    }


    private static class callGetbilibiliAppData extends AsyncTask<String, Void, Document> {
        Document ShareChatDoc;
        private Iterator<Element> abk;

        callGetbilibiliAppData() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Document doInBackground(String... strArr) {
            try {
                this.ShareChatDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception strArr2) {
                strArr2.printStackTrace();
                Log.d("ContentValues roposo_", "doInBackground: Error");
            }
            return this.ShareChatDoc;
        }

        protected void onPostExecute(Document document) {
            String charSequence = "";

            try {

                ArrayList<String> mp4List = new ArrayList<>();
                ArrayList<String> qualitylist = new ArrayList<>();

                String data = "";

                Iterator<Element> documentitrator = document.select("script").iterator();

                do {
                    if (!documentitrator.hasNext()) {

                        break;
                    }
                    data = documentitrator.next().data();
                    Log.e("onP4342424te:datais ", data);


                } while (!data.contains("window.__playinfo__="));


                String stringbuil = data.substring(data.indexOf("{"), data.lastIndexOf("}"));

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(stringbuil);
                stringBuilder.append("}");

                Log.e("onPostbjnkjhoso_11 ", stringBuilder.toString());
                if (!document.equals("")) {
                    try {
                        JSONObject jSONObject = new JSONObject(stringBuilder.toString());
                        JSONObject datajSONObject = jSONObject.getJSONObject("data");
                        JSONObject dashjSONObject1 = datajSONObject.getJSONObject("dash");
                        JSONArray videojSONObject1 = dashjSONObject1.getJSONArray("video");


                        System.out.println("respossss112212121URL)) " + videojSONObject1.getJSONObject(0).getString("base_url"));


                        for (int i = 0; i < videojSONObject1.length(); i++) {


                            JSONObject jsonObject12 = videojSONObject1.getJSONObject(i);
                            mp4List.add(jsonObject12.getString("base_url"));
                            qualitylist.add(jsonObject12.getString("width"));


                            System.out.println("respossss112212121URL " + jsonObject12.getString("base_url"));

                        }

                        try {
                            JSONArray audiojSONObject1 = dashjSONObject1.getJSONArray("audio");
                            for (int i = 0; i < audiojSONObject1.length(); i++) {


                                JSONObject jsonObject12 = audiojSONObject1.getJSONObject(i);
                                mp4List.add(jsonObject12.getString("base_url"));
                                qualitylist.add(jsonObject12.getString("mime_type"));


                                System.out.println("respossss112212121URL " + jsonObject12.getString("base_url"));

                            }

                        } catch (Exception e) {

                        }


                        if (videojSONObject1.length() > 0) {

                            dialog_quality_allvids = new Dialog(Mcontext);


                            if (!fromService) {
                                pd.dismiss();
                            }


                            windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);
                            LayoutInflater layoutInflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            mChatHeadView = layoutInflater.inflate(R.layout.dialog_quality_ytd, null);


                            dialog_quality_allvids.setContentView(mChatHeadView);


                            mainLayout = mChatHeadView.findViewById(R.id.linlayout_dialog);

                            img_dialog = mChatHeadView.findViewById(R.id.img_dialog);

                            mainLayout = dialog_quality_allvids.findViewById(R.id.linlayout_dialog);
                            img_dialog = dialog_quality_allvids.findViewById(R.id.img_dialog);


                            int size = 0;

                            try {
                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                ((Activity) Mcontext).getWindowManager()
                                        .getDefaultDisplay()
                                        .getMetrics(displayMetrics);

                                int height = displayMetrics.heightPixels;
                                int width = displayMetrics.widthPixels;

                                size = width / 2;

                            } catch (Exception e) {
                                size = WindowManager.LayoutParams.WRAP_CONTENT;
                            }


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                params = new WindowManager.LayoutParams(
                                        size,
                                        WindowManager.LayoutParams.WRAP_CONTENT,
                                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                        PixelFormat.TRANSLUCENT);

                                params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                                params.x = 0;
                                params.y = 100;
                            } else {
                                params = new WindowManager.LayoutParams(
                                        size,
                                        WindowManager.LayoutParams.WRAP_CONTENT,
                                        WindowManager.LayoutParams.TYPE_PHONE,
                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                        PixelFormat.TRANSLUCENT);

                                params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                                params.x = 0;
                                params.y = 100;
                            }


                            // mainLayout.setLayoutParams(params);


                            for (int i = 0; i < mp4List.size(); i++) {


                                addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4List.get(i), "Bilibili_" + qualitylist.get(i) + "_" + System.currentTimeMillis());

                            }

                            img_dialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog_quality_allvids.dismiss();
                                }
                            });

                            dialog_quality_allvids.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                            dialog_quality_allvids.getWindow().setAttributes(params);
                            //  dialogquality.getWindow().setColorMode(Mcontext.getResources().getColor(R.color.colorAccent));

                            dialog_quality_allvids.show();


                            dialog_quality_allvids.show();
                        } else {


                            new downloadFile().Downloading(Mcontext, mp4List.get(0), "Bilibili_" + System.currentTimeMillis(), ".mp4");

                            if (!fromService) {
                                pd.dismiss();


                                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));


                            }
                        }


                    } catch (Exception document2) {
                        document2.printStackTrace();

                        System.out.println("respossss112212121qerrr " + document2.getMessage());

                        if (!fromService) {

                            pd.dismiss();
                        }
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }
                }


            } catch (Exception document22) {
                if (!fromService) {

                    pd.dismiss();
                }
                document22.printStackTrace();
                System.out.println("respossss112212121qerrr " + document22.getMessage());

                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }
    }


    public static void callSnackVideoResult(String URL, String shortKey, String os, String sig, String client_key) {


        RetrofitApiInterface apiService = RetrofitClient.getClient().create(RetrofitApiInterface.class);


        Call<JsonObject> callResult = apiService.getsnackvideoresult(URL + "&" + shortKey + "&" + os + "&sig=" + sig + "&" + client_key);


        callResult.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {


                VideoUrl = response.body().getAsJsonObject("photo").get("main_mv_urls").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();


                System.out.println("response1122334455worURL:   " + VideoUrl);

                if (!VideoUrl.equals("")) {


                    try {

                        if (!fromService) {

                            pd.dismiss();
                        }


                        String myurldocument = VideoUrl;


                        String nametitle = "snackvideo_" +
                                System.currentTimeMillis();

                        new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        document2.printStackTrace();
                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("response1122334455:   " + "Failed0 " + call);

                if (!fromService) {

                    pd.dismiss();
                }

            }
        });

    }

    @SuppressLint("NewApi")
    public static void getSnackVideoData(String str, Context vc) {
        URI uri;
        try {
            uri = new URI(str);
        } catch (Exception e) {
            e.printStackTrace();
            uri = null;
            if (!fromService) {

                pd.dismiss();
            }
        }
        assert uri != null;
        String[] uripath = uri.getPath().split("/");
        String uripath2 = uripath[uripath.length - 1];
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add("mod=OnePlus(ONEPLUS A5000)");
        arrayList.add("lon=0");
        arrayList.add("country_code=in");
        String mydid = "did=" +
                "ANDROID_" + Settings.Secure.getString(vc.getContentResolver(), "android_id");

        arrayList.add(mydid);
        arrayList.add("app=1");
        arrayList.add("oc=UNKNOWN");
        arrayList.add("egid=");
        arrayList.add("ud=0");
        arrayList.add("c=GOOGLE_PLAY");
        arrayList.add("sys=KWAI_BULLDOG_ANDROID_9");
        arrayList.add("appver=2.7.1.153");
        arrayList.add("mcc=0");
        arrayList.add("language=en-in");
        arrayList.add("lat=0");
        arrayList.add("ver=2.7");


        ArrayList arrayList2 = new ArrayList(arrayList);

        String shortKey = "shortKey=" +
                uripath2;
        arrayList2.add(shortKey);

        String os = "os=" +
                "android";
        arrayList2.add(os);
        String client_key = "client_key=" +
                "8c46a905";
        arrayList2.add(client_key);

        try {
            Collections.sort(arrayList2);

        } catch (Exception str225) {
            str225.printStackTrace();
            if (!fromService) {

                pd.dismiss();
            }
        }


        String clockData = CPU.getClockData(Mcontext, TextUtils.join("", arrayList2).getBytes(StandardCharsets.UTF_8), 0);

        String nowaterurl = "https://g-api.snackvideo.com/rest/bulldog/share/get?" + TextUtils.join("&", arrayList);

        System.out.println("respossss112212121q " + nowaterurl + "_______" + shortKey + os + clockData + client_key);


        callSnackVideoResult(nowaterurl, shortKey, os, clockData, client_key);


    }

    @Keep
    public static void CallVKData(String url, boolean hasQualityOption) {
        AndroidNetworking.get("https://api.vk.com/method/video.search?q=" + url + "&from=wall-51189706_396016&oauth=1&search_own=0&adult=0&search_own=0&count=1&extended=1&files=1&access_token=d9f1c406aeec6341131a62556d9eb76c7fe6d53defca0d9ce54535299664abf46e0a37af79004c30eb9b3&v=5.124")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("reccccc VVKK " + response);

                        try {
                            JSONObject reponsobj = response.getJSONObject("response");
                            JSONObject itemsarr = reponsobj.getJSONArray("items").getJSONObject(0);
                            JSONObject filesobj = itemsarr.getJSONObject("files");


                            ArrayList<String> mp4List = new ArrayList<>();
                            ArrayList<String> qualitylist = new ArrayList<>();


                            if (!filesobj.getString("mp4_240").isEmpty()) {
                                String mp4_240 = filesobj.getString("mp4_240");
                                mp4List.add(mp4_240);
                                qualitylist.add("240p");
                            }
                            if (!filesobj.getString("mp4_360").isEmpty()) {
                                String mp4_360 = filesobj.getString("mp4_360");
                                mp4List.add(mp4_360);
                                qualitylist.add("360p");

                            }
                            if (!filesobj.getString("mp4_480").isEmpty()) {
                                String mp4_480 = filesobj.getString("mp4_480");
                                mp4List.add(mp4_480);
                                qualitylist.add("480p");

                            }
                            if (!filesobj.getString("mp4_720").isEmpty()) {
                                String mp4_720 = filesobj.getString("mp4_720");
                                mp4List.add(mp4_720);
                                qualitylist.add("720p");

                            }
                            if (!filesobj.getString("mp4_1080").isEmpty()) {
                                String mp4_1080 = filesobj.getString("mp4_1080");
                                mp4List.add(mp4_1080);
                                qualitylist.add("1080p");

                            }


                            if (hasQualityOption) {

                                dialog_quality_allvids = new Dialog(Mcontext);


                                if (!fromService) {
                                    pd.dismiss();
                                }


                                windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);
                                LayoutInflater layoutInflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                mChatHeadView = layoutInflater.inflate(R.layout.dialog_quality_ytd, null);


                                dialog_quality_allvids.setContentView(mChatHeadView);


                                mainLayout = mChatHeadView.findViewById(R.id.linlayout_dialog);

                                img_dialog = mChatHeadView.findViewById(R.id.img_dialog);

                                mainLayout = dialog_quality_allvids.findViewById(R.id.linlayout_dialog);
                                img_dialog = dialog_quality_allvids.findViewById(R.id.img_dialog);


                                int size = 0;

                                try {
                                    DisplayMetrics displayMetrics = new DisplayMetrics();
                                    ((Activity) Mcontext).getWindowManager()
                                            .getDefaultDisplay()
                                            .getMetrics(displayMetrics);

                                    int height = displayMetrics.heightPixels;
                                    int width = displayMetrics.widthPixels;

                                    size = width / 2;

                                } catch (Exception e) {
                                    size = WindowManager.LayoutParams.WRAP_CONTENT;
                                }


                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    params = new WindowManager.LayoutParams(
                                            size,
                                            WindowManager.LayoutParams.WRAP_CONTENT,
                                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                            PixelFormat.TRANSLUCENT);

                                    params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                                    params.x = 0;
                                    params.y = 100;
                                } else {
                                    params = new WindowManager.LayoutParams(
                                            size,
                                            WindowManager.LayoutParams.WRAP_CONTENT,
                                            WindowManager.LayoutParams.TYPE_PHONE,
                                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                            PixelFormat.TRANSLUCENT);

                                    params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                                    params.x = 0;
                                    params.y = 100;
                                }


                                // mainLayout.setLayoutParams(params);


                                for (int i = 0; i < mp4List.size(); i++) {


                                    addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4List.get(i), "VK_" + qualitylist.get(i) + "_" + System.currentTimeMillis());

                                }

                                img_dialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog_quality_allvids.dismiss();
                                    }
                                });

                                dialog_quality_allvids.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                                dialog_quality_allvids.getWindow().setAttributes(params);
                                //  dialogquality.getWindow().setColorMode(Mcontext.getResources().getColor(R.color.colorAccent));

                                dialog_quality_allvids.show();


                                dialog_quality_allvids.show();
                            } else {


                                new downloadFile().Downloading(Mcontext, mp4List.get(0), "VK_240p" + System.currentTimeMillis(), ".mp4");

                                if (!fromService) {
                                    pd.dismiss();


                                    iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));


                                }
                            }


                        } catch (Exception str2) {
                            str2.printStackTrace();
                            // Toast.makeText(Mcontext, "Invalid URL", 0).show();

                            if (!fromService) {
                                pd.dismiss();
                                iUtils.ShowToast(Mcontext, response + ":   " + Mcontext.getResources().getString(R.string.invalid_url));
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println("reccccc VVKK error " + error);

                    }
                });

    }


    public static class Call9gagData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("https://9gag.com/gag/aXowVXz")
                    .method("GET", null)
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();


                System.out.println("mybodyhh1111>>> " + response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {
            System.out.println("myresponseis111 exp12222 " + document);


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                Iterator it = document.getElementsByTag("script").iterator();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    if (element.data().contains("window.__STATE__")) {
                        String replace = element.html().replace("window.__STATE__", "").replace(";", "")
                                .replace("=", "");
                        this.VideoUrl = String.valueOf(new JSONObject(new JSONObject(new JSONArray("[" + replace + "]")
                                .get(0).toString()).getJSONObject("feed").getJSONArray("feed")
                                .get(0).toString()).get("download_media"));
                        if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                            try {


                                String myurldocument = VideoUrl;


                                String nametitle = "hindvideo_" +
                                        System.currentTimeMillis();

                                new downloadFile().Downloading(Mcontext, myurldocument, nametitle, ".mp4");

                                //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                                VideoUrl = "";
                                //   binding.etText.setText(charSequence);

                            } catch (Exception document2) {
                                System.out.println("myresponseis111 exp1 " + document2.getMessage());

                                document2.printStackTrace();
                                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                            }

                            return;
                        }
                    }
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }


//    @Keep
//    public static void CallDailymotionData(String url, boolean hasQualityOption) {
//        AndroidNetworking.get(DlApisUrl + url)
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        System.out.println("reccccc VVKK " + response);
//
//                        try {
//                            if (response.getJSONArray("links").length() > 0) {
//                                ArrayList<String> mp4urlList = new ArrayList<>();
//                                ArrayList<String> qualitylist = new ArrayList<>();
//
//                                for (int i = 0; i < response.getJSONArray("links").length(); i++) {
//                                    JSONObject itemsarr = response.getJSONArray("links").getJSONObject(i);
//
//
//                                    mp4urlList.add(itemsarr.getString("url"));
//                                    if (!itemsarr.getString("size").equals("")) {
//                                        qualitylist.add(itemsarr.getString("quality") + " (" + itemsarr.getString("size") + ")");
//                                    } else {
//                                        qualitylist.add(itemsarr.getString("quality") + " (Unknown)");
//                                    }
//                                }
//
//
//                                if (hasQualityOption) {
//
//                                    dialog_quality_allvids = new Dialog(Mcontext);
//
//
//                                    if (!fromService) {
//                                        pd.dismiss();
//                                    }
//
//
//                                    windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);
//                                    LayoutInflater layoutInflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                                    mChatHeadView = layoutInflater.inflate(R.layout.dialog_quality_ytd, null);
//
//
//                                    dialog_quality_allvids.setContentView(mChatHeadView);
//
//
//                                    mainLayout = mChatHeadView.findViewById(R.id.linlayout_dialog);
//
//                                    img_dialog = mChatHeadView.findViewById(R.id.img_dialog);
//
//                                    mainLayout = dialog_quality_allvids.findViewById(R.id.linlayout_dialog);
//                                    img_dialog = dialog_quality_allvids.findViewById(R.id.img_dialog);
//
//
//                                    int size = 0;
//
//                                    try {
//                                        DisplayMetrics displayMetrics = new DisplayMetrics();
//                                        ((Activity) Mcontext).getWindowManager()
//                                                .getDefaultDisplay()
//                                                .getMetrics(displayMetrics);
//
//                                        int height = displayMetrics.heightPixels;
//                                        int width = displayMetrics.widthPixels;
//
//                                        size = width / 2;
//
//                                    } catch (Exception e) {
//                                        size = WindowManager.LayoutParams.WRAP_CONTENT;
//                                    }
//
//
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                        params = new WindowManager.LayoutParams(
//                                                size,
//                                                WindowManager.LayoutParams.WRAP_CONTENT,
//                                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                                                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                                                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                                                PixelFormat.TRANSLUCENT);
//
//                                        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                                        params.x = 0;
//                                        params.y = 100;
//                                    } else {
//                                        params = new WindowManager.LayoutParams(
//                                                size,
//                                                WindowManager.LayoutParams.WRAP_CONTENT,
//                                                WindowManager.LayoutParams.TYPE_PHONE,
//                                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                                                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                                                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                                                PixelFormat.TRANSLUCENT);
//
//                                        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                                        params.x = 0;
//                                        params.y = 100;
//                                    }
//
//
//                                    // mainLayout.setLayoutParams(params);
//
//
//                                    for (int i = 0; i < mp4urlList.size(); i++) {
//
//                                        if (url.contains("dailymotion.com") || url.contains("dai.ly")) {
//                                            addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4urlList.get(i), "dailymotion_" + qualitylist.get(i) + "_" + System.currentTimeMillis());
//
//                                        } else if (url.contains("twitch")) {
//                                            addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4urlList.get(i), "twitch_" + qualitylist.get(i) + "_" + System.currentTimeMillis());
//
//                                        } else if (url.contains("ted")) {
//                                            addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4urlList.get(i), "ted_" + qualitylist.get(i) + "_" + System.currentTimeMillis());
//
//                                        } else if (url.contains("espn")) {
//                                            addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4urlList.get(i), "espn_" + qualitylist.get(i) + "_" + System.currentTimeMillis());
//
//                                        } else if (url.contains("flickr")) {
//                                            addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4urlList.get(i), "flickr_" + qualitylist.get(i) + "_" + System.currentTimeMillis());
//
//                                        } else if (url.contains("streamable")) {
//                                            addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4urlList.get(i), "streamable_" + qualitylist.get(i) + "_" + System.currentTimeMillis());
//
//                                        } else if (url.contains("facebook")) {
//                                            addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4urlList.get(i), "facebook_" + qualitylist.get(i) + "_" + System.currentTimeMillis());
//
//                                        } else if (url.contains("twitter")) {
//                                            addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4urlList.get(i), "twitter_" + qualitylist.get(i) + "_" + System.currentTimeMillis());
//
//                                        } else if (url.contains("blogspot")) {
//                                            addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4urlList.get(i), "blogspot_" + qualitylist.get(i) + "_" + System.currentTimeMillis());
//
//                                        }
//
//                                    }
//
//                                    img_dialog.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog_quality_allvids.dismiss();
//                                        }
//                                    });
//
//                                    dialog_quality_allvids.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//                                    dialog_quality_allvids.getWindow().setAttributes(params);
//                                    //  dialogquality.getWindow().setColorMode(Mcontext.getResources().getColor(R.color.colorAccent));
//
//                                    dialog_quality_allvids.show();
//
//
//                                    dialog_quality_allvids.show();
//                                } else {
//
//
//                                    new downloadFile().Downloading(Mcontext, mp4urlList.get(1), "Reddit_240p" + System.currentTimeMillis(), ".mp4");
//
//                                    if (!fromService) {
//                                        pd.dismiss();
//
//
//                                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));
//
//
//                                    }
//                                }
//
//
//                            } else {
//                                Toast.makeText(Mcontext, "No links found", Toast.LENGTH_SHORT).show();
//
//                            }
//
//
//                        } catch (Exception str2) {
//                            str2.printStackTrace();
//                            // Toast.makeText(Mcontext, "Invalid URL", 0).show();
//
//                            if (!fromService) {
//                                pd.dismiss();
//                                iUtils.ShowToast(Mcontext, response + ":   " + Mcontext.getResources().getString(R.string.invalid_url));
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        System.out.println("reccccc VVKK error " + error);
//
//                    }
//                });
//
//    }
//
//    @Keep
//    public static void CallsoundData(String url, boolean hasQualityOption) {
//        AndroidNetworking.get(DlApisUrl + url)
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        System.out.println("reccccc VVKK " + response);
//
//                        try {
//                            if (response.getJSONArray("links").length() > 0) {
//
//
//                                JSONObject itemsarr = response.getJSONArray("links").getJSONObject(0);
//
//                                //    mp4urlList.add(itemsarr.getString("url"));
//                                String myurlis = itemsarr.getString("url");
//
//                                if (url.contains("bitchute")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "bitchute_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("soundcloud")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "Soundcloud_" + System.currentTimeMillis(), ".mp3");
//
//                                } else if (url.contains("mxtakatak")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "Mxtakatak_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("ganna")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "Ganna_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("cocoscope")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "Cocoscope_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("20min.ch")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "20min_ch_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("bandcamp")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "bandcamp_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("douyin")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "douyin_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("izlesene")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "izlesene_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("linkedin")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "linkedin_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("kwai") || url.contains("kw.ai")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "kwai_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("mashable")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "mashable_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("gag")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "gag_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("imgur")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "imgur_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("imdb")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "imdb_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("pinterest") || url.contains("pin.it")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "pinterest_" + System.currentTimeMillis(), ".mp4");
//
//                                } else if (url.contains("tiktok")) {
//                                    new downloadFile().Downloading(Mcontext, myurlis, "tiktok_" + System.currentTimeMillis(), ".mp4");
//
//                                }
//
//                                if (!fromService) {
//                                    pd.dismiss();
//
//
//                                    iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));
//
//
//                                }
//
//
//                            } else {
//                                Toast.makeText(Mcontext, "No links found", Toast.LENGTH_SHORT).show();
//
//                            }
//
//
//                        } catch (Exception str2) {
//                            str2.printStackTrace();
//                            // Toast.makeText(Mcontext, "Invalid URL", 0).show();
//
//                            if (!fromService) {
//                                pd.dismiss();
//                                iUtils.ShowToast(Mcontext, response + ":   " + Mcontext.getResources().getString(R.string.invalid_url));
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        System.out.println("reccccc VVKK error " + error);
//                        if (!fromService) {
//                            pd.dismiss();
//                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.invalid_url));
//                        }
//                    }
//                });
//
//    }


    private static void splitDataToVideoAndAudio_video(List<Video> videoList, RecyclerView recyclerView_video, RecyclerView recyclerView_audio, QualityBottomsheetAdapter qualityBottomsheetAdapter, String extractor) {

        List<Video> videoList_sub = new ArrayList<>();
        List<Video> videoList_sub_video = new ArrayList<>();
        for (int i = 0; i < videoList.size(); i++) {

            if (videoList.get(i).getProtocol().contains("http") && !videoList.get(i).getProtocol().contains("http_dash_segments") && !videoList.get(i).getURL().contains(".m3u8")) {

                System.out.println("fhnsdkjhffjhsfhsdfjksdfsdhfkjs vifro= " + videoList.get(i).getURL());


                if (videoList.get(i).getEXT().equals("m4a") ||
                        videoList.get(i).getEXT().equals("mp3") ||
                        videoList.get(i).getEXT().equals("wav")) {
                    videoList_sub.add(videoList.get(i));
                } else if (videoList.get(i).getEXT().equals("mp4") || videoList.get(i).getEXT().equals("mpeg")) {

                    videoList_sub_video.add(videoList.get(i));

                }
            }
        }

        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, videoList_sub_video.get(0).getExtractor(), false, videoList_sub_video, true);
        recyclerView_video.setAdapter(qualityBottomsheetAdapter);

        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, videoList_sub.get(0).getExtractor(), false, videoList_sub, true);
        recyclerView_audio.setAdapter(qualityBottomsheetAdapter);


    }

    private static void splitDataToVideoAndAudio_format(List<Format> formatList, RecyclerView recyclerView_video, RecyclerView recyclerView_audio, QualityBottomsheetAdapter qualityBottomsheetAdapter, String extractor) {


        List<Format> formatList_sub = new ArrayList<>();
        List<Format> formatList_sub_video = new ArrayList<>();
        for (int i = 0; i < formatList.size(); i++) {


            if (formatList.get(i).getProtocol().contains("http") && !formatList.get(i).getProtocol().contains("http_dash_segments") && !formatList.get(i).getURL().contains(".m3u8")) {
                System.out.println("fhnsdkjhffjhsfhsdfjksdfsdhfkjs formate= " + formatList.get(i).getAcodec());

                if (formatList.get(i).getAcodec() != null && !formatList.get(i).getAcodec().equals("none")) {

                    if (formatList.get(i).getEXT().equals("m4a") ||
                            formatList.get(i).getEXT().equals("mp3") ||
                            formatList.get(i).getEXT().equals("wav")) {
                        formatList_sub.add(formatList.get(i));
                    } else if (formatList.get(i).getEXT().equals("mp4") || formatList.get(i).getEXT().equals("mpeg")) {

                        formatList_sub_video.add(formatList.get(i));

                    }
                } else {

                    if (formatList.get(i).getEXT().equals("m4a") ||
                            formatList.get(i).getEXT().equals("mp3") ||
                            formatList.get(i).getEXT().equals("wav")) {
                        formatList_sub.add(formatList.get(i));
                    } else if (formatList.get(i).getEXT().equals("mp4") || formatList.get(i).getEXT().equals("mpeg")) {

                        formatList_sub_video.add(formatList.get(i));

                    }

                    formatList.get(i).setFormat("(no audio) " + formatList.get(i).getFormat());


                }

            }
        }


        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, formatList_sub_video, extractor, false);
        recyclerView_video.setAdapter(qualityBottomsheetAdapter);

        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, formatList_sub, extractor, false);
        recyclerView_audio.setAdapter(qualityBottomsheetAdapter);


    }


    @Keep
    public static void CalldlApisDataData(String url, boolean hasQualityOption) {

        System.out.println("reccc " + url);
        AndroidNetworking.get(DlApisUrl + url + "&flatten=True")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("reccccc VVKK " + response);
                        // Log.e("myresponse ",response.toString());

                        try {

                            try {
                                if (!response.getString("error").equals("")) {
                                    // Toast.makeText(Mcontext, "Invalid URL", 0).show();

                                    if (!fromService) {
                                        pd.dismiss();
                                        iUtils.ShowToast(Mcontext, "Error:  " + Mcontext.getResources().getString(R.string.invalid_url));
                                    }
                                }

                            } catch (Exception e) {

                                Gson gson = new Gson();

                                DLDataParser gsonObj = gson.fromJson(response.toString(), DLDataParser.class);

                                System.out.println("reccccc VVKK " + gsonObj.getURL());


                                View view = LayoutInflater.from(Mcontext).inflate(R.layout.bottomsheet_quality_layout, null);


                                Button btncancel_bottomsheet = view.findViewById(R.id.btncancel_bottomsheet);
                                TextView source_bottomsheet = view.findViewById(R.id.source_bottomsheet);
                                TextView title_bottomsheet = view.findViewById(R.id.bottomsheet_title);
                                TextView duration_bottomsheet = view.findViewById(R.id.bottomsheet_duration);
                                ImageView thumb_bottomsheet = view.findViewById(R.id.bottomsheet_thumbnail);

                                RecyclerView recyclerView = view.findViewById(R.id.recqualitybottomsheet);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Mcontext));

                                RecyclerView recyclerView_audio = view.findViewById(R.id.recqualitybottomsheet_aud);
                                recyclerView_audio.setHasFixedSize(true);
                                recyclerView_audio.setLayoutManager(new LinearLayoutManager(Mcontext));

                                QualityBottomsheetAdapter qualityBottomsheetAdapter = null;

                                System.out.println("reccc lengthe iss= " + response.getJSONArray("videos").length());

                                if (response.getJSONArray("videos").length() > 1) {

                                    System.out.println("reccccc VVKK 0 ");


                                    System.out.println("reccccc VVKK 1 ");

                                    if (response.getJSONArray("videos").getJSONObject(0).has("protocol")) {

                                        System.out.println("reccccc VVKK 2");

//                                            String ishttp = response.getJSONArray("videos").getJSONObject(0).getString("protocol");
//                                            if (ishttp.contains("http")) {

                                        System.out.println("reccccc VVKK 3 ");

                                        splitDataToVideoAndAudio_video(gsonObj.getVideos(), recyclerView, recyclerView_audio, qualityBottomsheetAdapter, gsonObj.getVideos().get(0).getExtractor());
//                                        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, gsonObj.getVideos().get(0).getExtractor(), false, gsonObj.getVideos(), true);
//                                        recyclerView.setAdapter(qualityBottomsheetAdapter);

//                                            } else {
//                                                qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, gsonObj.getVideos().get(0).getFormats(), gsonObj.getVideos().get(0).getExtractor(), false);
//                                                recyclerView.setAdapter(qualityBottomsheetAdapter);
//
//                                            }


                                    }
                                    //                                   else {
//                                        System.out.println("reccccc VVKK 4 ");
//
//                                        if (response.getJSONArray("videos").getJSONObject(0).getJSONObject("formats").has("protocol")) {
//
////                                            String ishttp = response.getJSONArray("videos").getJSONObject(0).getString("protocol");
////                                            if (ishttp.contains("http")) {
//                                                qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, gsonObj.getVideos().get(0).getFormats(), gsonObj.getVideos().get(0).getExtractor(), true);
//                                                recyclerView.setAdapter(qualityBottomsheetAdapter);
//
////                                            } else {
////                                                qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, gsonObj.getVideos().get(0).getURL(), gsonObj.getVideos().get(0).getExtractor(), true);
////                                                recyclerView.setAdapter(qualityBottomsheetAdapter);
////                                            }
//
//                                        }
//                                    }


                                    BottomSheetDialog dialog = new BottomSheetDialog(Mcontext);

                                    if (response.getJSONArray("videos").getJSONObject(0).has("extractor")) {
                                        String styledText = "Source: <font color='red'>" + gsonObj.getVideos().get(0).getExtractor() + "</font>";
                                        source_bottomsheet.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                                    }

                                    if (response.getJSONArray("videos").getJSONObject(0).has("duration")) {

                                        String mystring = gsonObj.getVideos().get(0).getDuration() + "";
                                        String[] correctstring = mystring.split("\\.");

                                        long hours = Long.parseLong(correctstring[0]) / 3600;
                                        long minutes = (Long.parseLong(correctstring[0]) % 3600) / 60;
                                        long seconds = Long.parseLong(correctstring[0]) % 60;

                                        String DurationstyledText = "Duration: <font color='red'>" + String.format("%02d:%02d:%02d", hours, minutes, seconds) + "</font>";
                                        duration_bottomsheet.setText(Html.fromHtml(DurationstyledText), TextView.BufferType.SPANNABLE);
                                    }

                                    if (response.getJSONArray("videos").getJSONObject(0).has("title")) {

                                        System.out.println("reccccc VVKKtttt " + gsonObj.getVideos().get(0).getTitle());


                                        String titletyledText = "Title: <font color='red'>" + String.format("%s", gsonObj.getVideos().get(0).getTitle()) + "</font>";
                                        title_bottomsheet.setText(Html.fromHtml(titletyledText), TextView.BufferType.SPANNABLE);
                                    }


                                    if (response.getJSONArray("videos").getJSONObject(0).has("thumbnail")) {

                                        Glide.with(Mcontext)
                                                .load(gsonObj.getVideos().get(0).getThumbnail())
                                                .into(thumb_bottomsheet);

                                    }
                                    // source_bottomsheet.setText(String.format("Source: %s", gsonObj.getVideos().get(0).getExtractor()));
                                    btncancel_bottomsheet.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                        }
                                    });

                                    dialog.setContentView(view);
                                    dialog.show();


                                    if (!fromService) {
                                        pd.dismiss();


                                        //iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));


                                    }


                                } else {

                                    System.out.println("reccccc VVKK 6 ");

                                    if (response.getJSONArray("videos").getJSONObject(0).has("formats")) {
                                        System.out.println("reccccc VVKK 7 ");

                                        //     if (response.getJSONArray("videos").getJSONObject(0).getJSONArray("formats").getJSONObject(0).has("protocol")) {


                                        System.out.println("reccccc VVKK 8 ");
//
//
//                                            String ishttp = response.getJSONArray("videos").getJSONObject(0).getString("protocol");
//                                            if (ishttp.contains("http")) {
                                        System.out.println("reccccc VVKK 9 ");
//                                               (Context context, List<Format> filesList, String source, boolean issingle)

                                        splitDataToVideoAndAudio_format(gsonObj.getVideos().get(0).getFormats(), recyclerView, recyclerView_audio, qualityBottomsheetAdapter, gsonObj.getVideos().get(0).getExtractor());

//                                        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, gsonObj.getVideos().get(0).getFormats(), gsonObj.getVideos().get(0).getExtractor(), false);
//                                        recyclerView.setAdapter(qualityBottomsheetAdapter);

//                                            } else {
//                                                qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, gsonObj.getVideos().get(0).getExtractor(), false, gsonObj.getVideos(), true);
//                                                recyclerView.setAdapter(qualityBottomsheetAdapter);
//
//                                            }

                                        //    }

                                    } else {
                                        if (response.getJSONArray("videos").getJSONObject(0).has("protocol")) {

                                            String ishttp = response.getJSONArray("videos").getJSONObject(0).getString("protocol");
                                            if (ishttp.contains("http")) {
                                                qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, gsonObj.getVideos().get(0).getURL(), gsonObj.getVideos().get(0).getExtractor(), true);
                                                recyclerView.setAdapter(qualityBottomsheetAdapter);

                                            }
//                                            else {
//                                                qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, gsonObj.getVideos().get(0).getURL(), gsonObj.getVideos().get(0).getExtractor(), true);
//                                                recyclerView.setAdapter(qualityBottomsheetAdapter);
//                                            }

                                        }
                                    }


                                    BottomSheetDialog dialog = new BottomSheetDialog(Mcontext);

                                    if (response.getJSONArray("videos").getJSONObject(0).has("extractor")) {
                                        String styledText = "Source: <font color='red'>" + gsonObj.getVideos().get(0).getExtractor() + "</font>";
                                        source_bottomsheet.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                                    }

                                    if (response.getJSONArray("videos").getJSONObject(0).has("duration")) {

                                        String mystring = gsonObj.getVideos().get(0).getDuration() + "";
                                        String[] correctstring = mystring.split("\\.");

                                        long hours = Long.parseLong(correctstring[0]) / 3600;
                                        long minutes = (Long.parseLong(correctstring[0]) % 3600) / 60;
                                        long seconds = Long.parseLong(correctstring[0]) % 60;

                                        String DurationstyledText = "Duration: <font color='red'>" + String.format("%02d:%02d:%02d", hours, minutes, seconds) + "</font>";
                                        duration_bottomsheet.setText(Html.fromHtml(DurationstyledText), TextView.BufferType.SPANNABLE);
                                    }

                                    if (response.getJSONArray("videos").getJSONObject(0).has("title")) {

                                        System.out.println("reccccc VVKKtttt " + gsonObj.getVideos().get(0).getTitle());


                                        String titletyledText = "Title: <font color='red'>" + String.format("%s", gsonObj.getVideos().get(0).getTitle()) + "</font>";
                                        title_bottomsheet.setText(Html.fromHtml(titletyledText), TextView.BufferType.SPANNABLE);
                                    }


                                    if (response.getJSONArray("videos").getJSONObject(0).has("thumbnail")) {

                                        Glide.with(Mcontext)
                                                .load(gsonObj.getVideos().get(0).getThumbnail())
                                                .into(thumb_bottomsheet);

                                    }
                                    // source_bottomsheet.setText(String.format("Source: %s", gsonObj.getVideos().get(0).getExtractor()));
                                    btncancel_bottomsheet.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                        }
                                    });

                                    dialog.setContentView(view);
                                    dialog.show();


                                    if (!fromService) {
                                        pd.dismiss();


                                        //iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.starting_download));


                                    }

                                }

                            }
                        } catch (Exception str2) {
                            // str2.printStackTrace();
                            // Toast.makeText(Mcontext, "Invalid URL", 0).show();
                            System.out.println("reccccc VVKK Error= " + str2);

                            if (!fromService) {
                                pd.dismiss();
                                iUtils.ShowToast(Mcontext, str2 + ":   " + Mcontext.getResources().getString(R.string.invalid_url));
                            }
                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println("reccccc VVKK error " + error);
                        if (!fromService) {
                            pd.dismiss();
                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.invalid_url));
                        }
                    }
                });

    }


}
