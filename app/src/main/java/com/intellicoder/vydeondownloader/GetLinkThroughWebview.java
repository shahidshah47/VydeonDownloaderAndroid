

package com.intellicoder.vydeondownloader;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.intellicoder.vydeondownloader.databinding.ActivityGetLinkThroughWebviewBinding;
import com.intellicoder.vydeondownloader.models.DlApismodels.VideoModel;
import com.intellicoder.vydeondownloader.tasks.downloadFile;
import com.intellicoder.vydeondownloader.utils.iUtils;

import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellicoder.vydeondownloader.tasks.downloadVideo.fromService;
import static com.intellicoder.vydeondownloader.tasks.downloadVideo.pd;

public class GetLinkThroughWebview extends AppCompatActivity {
    String url = "";
    ProgressDialog progressDialog;
    boolean isOnetime = false;
    private ArrayList<VideoModel> videoModelArrayList = new ArrayList<>();
    private ActivityGetLinkThroughWebviewBinding binding;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetLinkThroughWebviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(GetLinkThroughWebview.this);
        progressDialog.setMessage(getString(R.string.nodeifittakeslonger));
        progressDialog.show();

        url = "https://audiomack.com/lightskinkeisha/song/fdh";


        // url = "https://audiomack.com/embed/song/lightskinkeisha/fdh?background=1";

        if (getIntent().hasExtra("myurlis")) {
            url = getIntent().getStringExtra("myurlis");


            if (url.contains("audiomack")) {

                String[] urlarray = url.split("/");
                System.out.println("length ksdjjfsdfsd 6" + urlarray[5]);
                System.out.println("length ksdjjfsdfsd 5" + urlarray[3]);
                url = "https://audiomack.com/embed/song/" + urlarray[3] + "/" + urlarray[5] + "?background=1";

                System.out.println("length ksdjjfsdfsd 77 =" + url);
            }


        } else {
            url = "https://audiomack.com/embed/song/lightskinkeisha/fdh?background=1";
        }


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(binding.browser, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
        binding.browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        binding.browser.getSettings().setJavaScriptEnabled(true);
        binding.browser.getSettings().getAllowFileAccess();
        binding.browser.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36");

        binding.browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        if (url.contains("audiomack")) {

                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('audio')[0].getAttribute(\"src\"));");
                        } else if (url.contains("zili")) {

                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");

                        } else if (url.contains("bemate")) {

                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");

                        } else if (url.contains("byte.co")) {

                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[1].getAttribute(\"src\"));");

                        } else if (url.contains("vidlit")) {

                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('source')[0].getAttribute(\"src\"));");

                        } else if (url.contains("veer.tv")) {

                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");

                        } else if (url.contains("fthis.gr")) {

                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('source')[0].getAttribute(\"src\"));");

                        } else if (url.contains("fw.tv") || url.contains("firework.tv")) {

                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('source')[0].getAttribute(\"src\"));");

                        } else if (url.contains("rumble")) {

                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");

                        } else if (url.contains("traileraddict")) {

                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");

                        } else if (url.contains("zingmp3")) {

                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('audio')[0].getAttribute(\"src\"));");

                        } else {
                            progressDialog.dismiss();

                            if (!fromService) {
                                pd.dismiss();
                                iUtils.ShowToast(GetLinkThroughWebview.this, getResources().getString(R.string.somthing));
                            }

                            Intent intent = new Intent();
                            setResult(2, intent);
                            finish();
                        }

                    }
                }, 1000);

            }
        });

        binding.browser.loadUrl(url);


    }


    public class ParseM3u8 extends AsyncTask<Object, Integer, ArrayList<VideoModel>> {
        String DailyMotionUrl;
        private Document doc;
        private FrameLayout dwn_lyt;
        private final JSONObject emp = null;
        private Elements scriptElements;

        ParseM3u8(String str) {
            this.DailyMotionUrl = str;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected ArrayList<VideoModel> doInBackground(Object... objArr) {
            String str = "TAG";
            ArrayList<VideoModel> arrayList = new ArrayList();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.DailyMotionUrl).openConnection();
                httpURLConnection.setConnectTimeout(60000);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                HashMap hashMap = null;
                Pattern compile = Pattern.compile("\\d+");
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    } else if (readLine.equals("#EXTM3U")) {
                        hashMap = new HashMap();
                    } else if (readLine.contains("#EXT-X-STREAM-INF")) {
                        Matcher matcher = compile.matcher(readLine);
                        matcher.find();
                        if (hashMap != null) {
                            hashMap.put(readLine, Integer.valueOf(Integer.parseInt(matcher.group(0))));
                            VideoModel videoModel = new VideoModel();
                            if (readLine.contains("PROGRESSIVE-URI")) {
                                String[] split = readLine.split(",");
                                String[] split2 = split[3].split("=");
                                String str2 = split[0];
                                split = split[5].split("=");
                                //videoModel.setName(Uri.parse(split[1].substring(1)).getLastPathSegment());
                                videoModel.setQuality(split2[1].replace("\"", ""));
                                videoModel.setUrl(split[1].substring(1));
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("PROGRESSIVE-URI: ");
                                stringBuilder.append(split[1].substring(1));
                                Log.e(str, stringBuilder.toString());
                                videoModel.setSize(String.valueOf(iUtils.getRemoteFileSize(split[1].substring(1))));
                                arrayList.add(videoModel);
                            }
                        }
                    }
                }
                bufferedReader.close();
            } catch (Exception objArr2) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("doInBackground: ");
                stringBuilder2.append(objArr2.getMessage());
                objArr2.printStackTrace();
            }
            return arrayList;
        }


        public ArrayList<VideoModel> removeDuplicates(ArrayList<VideoModel> arrayList) {
            TreeSet treeSet = new TreeSet(new Comparator<VideoModel>() {
                public int compare(VideoModel videoModel, VideoModel videoModel2) {
                    return videoModel.getQuality().equalsIgnoreCase(videoModel2.getQuality()) ? 0 : 1;
                }
            });
            treeSet.addAll(arrayList);
            return new ArrayList<>(treeSet);
        }


        protected void onPostExecute(ArrayList<VideoModel> arrayList) {
            super.onPostExecute(arrayList);
            if (arrayList != null) {


            }
            GetLinkThroughWebview.this.videoModelArrayList = this.removeDuplicates(arrayList);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onPausing() {
        if (binding.browser != null) {
            binding.browser.evaluateJavascript("javascript:document.querySelector('video').pause();", new ValueCallback() {
                public void onReceiveValue(Object obj) {
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResuming() {
        if (binding.browser != null) {
            binding.browser.evaluateJavascript("javascript:document.querySelector('video').play();", new ValueCallback() {
                public void onReceiveValue(Object obj) {
                }
            });
        }
    }


    class MyJavaScriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void showHTML(final String myurl, final String html) {
            progressDialog.dismiss();

            System.out.println("myhtml res =" + html);
            if (!isOnetime) {
                isOnetime = true;
                if (myurl.contains("audiomack")) {

                    new downloadFile().Downloading(GetLinkThroughWebview.this, html, "Audiomack_" + System.currentTimeMillis(), ".mp3");

                } else if (myurl.contains("zili")) {

                    new downloadFile().Downloading(GetLinkThroughWebview.this, html, "Zilivideo_" + System.currentTimeMillis(), ".mp4");

                } else if (myurl.contains("bemate")) {

                    new downloadFile().Downloading(GetLinkThroughWebview.this, html, "Bemate_" + System.currentTimeMillis(), ".mp4");

                } else if (myurl.contains("byte.co")) {

                    new downloadFile().Downloading(GetLinkThroughWebview.this, html, "Byte_" + System.currentTimeMillis(), ".mp4");

                } else if (myurl.contains("vidlit")) {

                    new downloadFile().Downloading(GetLinkThroughWebview.this, html, "Vidlit_" + System.currentTimeMillis(), ".mp4");

                } else if (myurl.contains("veer.tv")) {

                    String jj = html.replace("&amp;", "&");

                    new downloadFile().Downloading(GetLinkThroughWebview.this, jj, "Veer_" + System.currentTimeMillis(), ".mp4");
                } else if (myurl.contains("fthis.gr")) {

                    new downloadFile().Downloading(GetLinkThroughWebview.this, html, "Fthis_" + System.currentTimeMillis(), ".mp4");

                } else if (myurl.contains("fw.tv") || myurl.contains("firework.tv")) {

                    new downloadFile().Downloading(GetLinkThroughWebview.this, html, "Firework_" + System.currentTimeMillis(), ".mp4");

                } else if (myurl.contains("traileraddict")) {

                    new downloadFile().Downloading(GetLinkThroughWebview.this, html, "Traileraddict_" + System.currentTimeMillis(), ".mp4");

                } else if (myurl.contains("rumble")) {

                    String jj = html.replace("&amp;", "&");

                    new downloadFile().Downloading(GetLinkThroughWebview.this, jj, "Rumble_" + System.currentTimeMillis(), ".mp4");
                } else if (myurl.contains("zingmp3")) {


                    new downloadFile().Downloading(GetLinkThroughWebview.this, "https:" + html, "Zingmp3_" + System.currentTimeMillis(), ".mp3");
                } else {

                    progressDialog.dismiss();

                    if (!fromService) {
                        pd.dismiss();
                        iUtils.ShowToast(GetLinkThroughWebview.this, getResources().getString(R.string.somthing));
                    }

                    Intent intent = new Intent();
                    setResult(2, intent);
                    finish();

                }

                System.out.println("htmlissss vid_url=" + html + " url=" + myurl);


                Intent intent = new Intent();
                intent.putExtra("MESSAGE", html);
                setResult(2, intent);
                finish();
            }

        }
    }


}