@file:Suppress("DEPRECATION")

package com.intellicoder.vydeondownloader.fragments

import android.app.*
import android.app.Activity.RESULT_OK
import android.content.*
import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.*
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdView
import com.facebook.ads.InterstitialAdListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellicoder.vydeondownloader.*
import com.intellicoder.vydeondownloader.Interfaces.UserListInStoryListner
import com.intellicoder.vydeondownloader.R
import com.intellicoder.vydeondownloader.adapters.ListAllStoriesOfUserAdapter
import com.intellicoder.vydeondownloader.adapters.StoryUsersListAdapter
import com.intellicoder.vydeondownloader.facebookstorysaver.fbadapters.FBstoryAdapter
import com.intellicoder.vydeondownloader.facebookstorysaver.fbadapters.FBuserRecyclerAdapter
import com.intellicoder.vydeondownloader.facebookstorysaver.fbinterfaces.OnFbUserClicked
import com.intellicoder.vydeondownloader.facebookstorysaver.fbmodels.FBStory
import com.intellicoder.vydeondownloader.facebookstorysaver.fbmodels.FBUserData
import com.intellicoder.vydeondownloader.facebookstorysaver.fbutils.FBhelper
import com.intellicoder.vydeondownloader.facebookstorysaver.fbutils.Facebookprefloader
import com.intellicoder.vydeondownloader.facebookstorysaver.fbutils.LoginWithFB
import com.intellicoder.vydeondownloader.models.storymodels.*
import com.intellicoder.vydeondownloader.receiver.Receiver
import com.intellicoder.vydeondownloader.services.ClipboardMonitor
import com.intellicoder.vydeondownloader.tasks.downloadFile
import com.intellicoder.vydeondownloader.tasks.downloadVideo
import com.intellicoder.vydeondownloader.utils.Constants
import com.intellicoder.vydeondownloader.utils.Constants.PREF_CLIP
import com.intellicoder.vydeondownloader.utils.Constants.STARTFOREGROUND_ACTION
import com.intellicoder.vydeondownloader.utils.Constants.STOPFOREGROUND_ACTION
import com.intellicoder.vydeondownloader.utils.SharedPrefsForInstagram
import com.intellicoder.vydeondownloader.utils.iUtils
import com.intellicoder.vydeondownloader.utils.iUtils.ShowToast
import com.intellicoder.vydeondownloader.utils.iUtils.isPackageInstalled
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_download.*
import kotlinx.android.synthetic.main.fragment_download.view.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.json.JSONObject
import java.lang.reflect.Type
import java.net.URI
import java.util.*


@Suppress("DEPRECATION", "NAME_SHADOWING")
@Keep
class download : Fragment(), UserListInStoryListner {
    private lateinit var fbstory_adapter: FBstoryAdapter
    private lateinit var fbuserlistadapter: FBuserRecyclerAdapter
    private lateinit var listAllStoriesOfUserAdapter: ListAllStoriesOfUserAdapter
    private var storyUsersListAdapter: StoryUsersListAdapter? = null
    private var mRewardedAd: RewardedAd? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var NotifyID = 1001
    private var type:Int = 0

    private var csRunning = false
    lateinit var progressDralogGenaratinglink: ProgressDialog

    lateinit var prefEditor: SharedPreferences.Editor
    lateinit var pref: SharedPreferences

    var fbdView: AdView? = null


    var myVideoUrlIs: String? = null

    var myPhotoUrlIs: String? = null

    var DEFAULT_BROADCAST = "NOTIFICATION_BROADCAST"


    private var fbInterstitialAd: com.facebook.ads.InterstitialAd? = null

    var fbstorieslist: List<FBStory> = ArrayList()

    private var clear:ImageView? = null
    private var clMain: LinearLayout? = null
    private var llTips: LinearLayout? = null
    private var llSearch: LinearLayout? = null
    private var cardView: CardView? = null
    private var purl: LinearLayout? = null
    private var ivLink: ImageView? = null
    private var vurl: LinearLayout? = null
    private var etURL: EditText? = null
    private var pbFetchingVideo: ProgressBar? = null
    private var btnDownload: Button? = null
    private var icInfoAutoDownload: ImageView? = null
    var chkAutoDownload: CheckBox? = null
    var chkdownload_private_media: CheckBox? = null
    var chkdownload_fbstories: CheckBox? = null
    private var checkboxlayoutinsta: RelativeLayout? = null
    private var icInfoDownloadPrivateMedia: ImageView? = null
    private var chkdownloadPrivateMedia: CheckBox? = null
    private var linlayoutInstaStories: LinearLayout? = null
    private var recUserList: RecyclerView? = null
    private var progressLoadingBar: ProgressBar? = null
    private var recStoriesList: RecyclerView? = null
    private var rvGallery: RelativeLayout? = null
    private var llFacebook: LinearLayout? = null
    private var fb1: ImageView? = null
    private var fb2: TextView? = null
    private var llTikTok: LinearLayout? = null
    private var llInstagram: LinearLayout? = null
    private var llTwitter: LinearLayout? = null
    private var likee: LinearLayout? = null
    private var llytdbtn: LinearLayout? = null
    private var llroposo: LinearLayout? = null
    private var llsharechat: LinearLayout? = null
    private var videomoreBtn: TextView? = null
    private var llMessage: LinearLayout? = null
    private var tvMessageTitle: TextView? = null
    private var tvMessageContent: TextView? = null
    private var cvDownload: CardView? = null
    private var llDownload: LinearLayout? = null
    private var imgAvatar: CircleImageView? = null
    private var tvAuthor: TextView? = null
    private var imgClose: ImageView? = null
    private var ivMusicNote: ImageView? = null
    private var tvMusic: TextView? = null
    private var tvDesc: TextView? = null
    private var llDownloadPercentage: LinearLayout? = null
    private var progressBar: ProgressBar? = null
    private var tvDownloadPercentage: TextView? = null
    private var llDownloadCompleted: LinearLayout? = null
    private var btnWatch: Button? = null
    private var fbsearch: SearchView? = null
//    var uiUpdate:UiUpdate? = null
var receiver: BroadcastReceiver? = null
    var intentFilter:IntentFilter? = null
    var broadcastReceiver:BroadcastReceiver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_download, container, false)

        clear = view.findViewById(R.id.clearIV)
        chkAutoDownload = view.chkAutoDownload
        chkdownload_private_media = view.chkdownload_private_media
        chkdownload_fbstories = view.chkdownload_fbstories

//        intentFilter = IntentFilter();
//        intentFilter!!.addAction(STOPFOREGROUND_ACTION)
//        uiUpdate = UiUpdate()

        val adRequest = AdRequest.Builder().build()

//        broadcastReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//              view.chkAutoDownload.isChecked = false
//            }
//        }

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                try {
                    if (intent != null && intent.action == DEFAULT_BROADCAST) {
                        view.chkAutoDownload.isChecked = false
                    }
                } catch (ignored: java.lang.Exception) {
                    Log.d("roadVast", ignored.localizedMessage)
                }
            }
        }
        getInterstitialAd()
//        mInterstitialAd = InterstitialAd(context!!)
//        mInterstitialAd.adUnitId = getString(R.string.AdmobInterstitial)
//        mInterstitialAd.loadAd(AdRequest.Builder().build())

        RewardedAd.load(
            context!!,
            resources.getString(R.string.AdmobRewardID),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {

                    mRewardedAd = rewardedAd
                }


            }

        )

        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {


            override fun onAdDismissedFullScreenContent() {

                //  Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show()
                iUtils.ShowToast(activity, getString(R.string.completad))
//
                val checked = view.chkAutoDownload?.isChecked
                if (checked!!) {
                    view.chkAutoDownload?.isChecked = false
                }
            }

            override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError?) {
                val checked = view.chkAutoDownload?.isChecked
                if (checked!!) {
                    view.chkAutoDownload?.isChecked = false
                }
            }

            override fun onAdShowedFullScreenContent() {

                iUtils.ShowToast(activity, getString(R.string.completad))

                    val checked = view.chkAutoDownload?.isChecked
                    if (checked!!) {
                        view.chkAutoDownload?.isChecked = false
                    }
//


                // Called when ad is dismissed.
                // Don't set the ad reference to null to avoid showing the ad a second time.
                mRewardedAd = null
            }
        }


        //fb add


        progressDralogGenaratinglink = ProgressDialog(activity)
        progressDralogGenaratinglink.setMessage(resources.getString(R.string.genarating_download_link))
        progressDralogGenaratinglink.setCancelable(false)
        fbsearch = view.findViewById(R.id.search_fbstory);

        //  addFbAd()

        pref = context!!.getSharedPreferences(PREF_CLIP, 0) // 0 - for private mode
        prefEditor = pref.edit()
        csRunning = pref.getBoolean("csRunning", false)

        createNotificationChannel(
            requireActivity(),
            NotificationManagerCompat.IMPORTANCE_LOW,
            true,
            getString(R.string.app_name),
            getString(R.string.aio_auto)
        )
//TODO
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(context)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context!!.packageName)
                )
                startActivityForResult(intent, 1234)
            }
        }
//        if (Build.VERSION.SDK_INT >= 30) {
//            if (!Environment.isExternalStorageManager()) {
//                val intent = Intent(
//                    Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
//                )
//                startActivityForResult(intent, 1238)
//            }
//        }
//TODO


        linlayoutInstaStories = view.findViewById(R.id.linlayout_insta_stories) as LinearLayout

        progressBar = view.findViewById(R.id.progressBar) as ProgressBar

        view.btnDownload.setOnClickListener { _ ->

//TODO Facebook uncomment the finction call code below
//            if (Constants.show_facebookads) {
//                addFbAd()
//            }

            val url = view.etURL.text.toString()
            DownloadVideo(url)


        }

        if (activity != null) {

            val activity: MainActivity? = activity as MainActivity?
            val strtext: String? = activity?.getMyData()

            println("mydatvgg222 " + strtext)
            if (strtext != null && !strtext.equals("")) {

                activity.setmydata("")
                view.etURL.setText(strtext)
                val url = view.etURL.text.toString()
                DownloadVideo(url)

            }
        }

        view.clearIV.setOnClickListener(View.OnClickListener {
            view.etURL.editableText.clear()
        })

        view.llFacebook.setOnClickListener { _ ->
            openAppFromPackedge(
                "com.facebook.katana",
                "facebook:/newsfeed",
                activity!!.resources.getString(R.string.install_fb)
            )


        }
        view.llTikTok.setOnClickListener { _ ->

            openAppFromPackedge(
                "com.zhiliaoapp.musically",
                "https://www.tiktok.com/",
                activity!!.resources.getString(R.string.install_tik)
            )


        }
        view.llInstagram.setOnClickListener { _ ->


            openAppFromPackedge(
                "com.instagram.android",
                "https://www.instagram.com/",
                activity!!.resources.getString(R.string.install_ins)
            )


        }
        view.llTwitter.setOnClickListener { _ ->

            openAppFromPackedge(
                "com.twitter.android",
                "https://www.twitter.com/",
                activity!!.resources.getString(R.string.install_twi)
            )


        }

        if (!Constants.showyoutube) {
            view.llytdbtn.visibility = View.GONE
        }

        view.llytdbtn.setOnClickListener { _ ->

            openAppFromPackedge(
                "com.google.android.youtube",
                "https://www.youtube.com/",
                activity!!.resources.getString(R.string.install_ytd)
            )

        }

        view.rvGallery.setOnClickListener { _ ->


            startActivity(Intent(context, GalleryActivity::class.java))


        }


        view.llroposo.setOnClickListener { _ ->


            openAppFromPackedge(
                "com.roposo.android",
                "https://www.roposo.com/",
                activity!!.resources.getString(R.string.install_roposo)
            )


        }

        view.llsharechat.setOnClickListener { _ ->

            openAppFromPackedge(
                "com.vimeo.android.videoapp",
                "https://vimeo.com/",
                activity!!.resources.getString(R.string.install_viemoo)
            )
        }

        view.likee.setOnClickListener { _ ->

            openAppFromPackedge(
                "video.like",
                "https://likee.com/",
                activity!!.resources.getString(R.string.install_likee)
            )

        }

        view.videomore_btn.setOnClickListener { _ ->


            val intent = Intent(context, AllSupportedApps::class.java)

            startActivity(intent)


        }




        view.ivLink.setOnClickListener(fun(_: View) {
            val clipBoardManager =
                context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val primaryClipData = clipBoardManager.primaryClip
            val clip = primaryClipData?.getItemAt(0)?.text.toString()

            view.etURL.text = Editable.Factory.getInstance().newEditable(clip)
            DownloadVideo(clip)
        })


//        if (csRunning) {
//            view.chkAutoDownload.isChecked = true
//            startClipboardMonitor()
//        } else {
//            view.chkAutoDownload.isChecked = false
//            stopClipboardMonitor()
//        }


        view.chkAutoDownload.setOnClickListener { view ->

            val checked = view?.chkAutoDownload?.isChecked
            if (!checked!!) {
                view.chkAutoDownload?.isChecked = false
                stopClipboardMonitor()
            } else {
                view.chkAutoDownload.isChecked = false
                showAdDialog(0)

            }

        }
        val sharedPrefsFor = SharedPrefsForInstagram(activity)
        if (sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE) == null) {
            sharedPrefsFor.clearSharePrefs()
        }


        val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
        if (map != null) {

            if (map.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN).equals("true")) {
                view.chkdownload_private_media.isChecked = true
                linlayoutInstaStories?.visibility = View.VISIBLE
                getallstoriesapicall()
            } else {
                view.chkdownload_private_media.isChecked = false
                linlayoutInstaStories?.visibility = View.GONE

            }
        }


        val sharedPrefsForfb = Facebookprefloader(activity)
        if (sharedPrefsForfb.getPreference(Facebookprefloader.fb_pref_cookie) == null) {
            sharedPrefsForfb.MakePrefEmpty()
        }


        val LoadPrefString = sharedPrefsForfb.LoadPrefString()
        val logedin = LoadPrefString.get(Facebookprefloader.fb_pref_isloggedin)


        if (logedin != null && logedin != "") {
            println("mydataiiii=" + logedin)
            if (logedin.equals("true")) {
                println("mydataiiiiddddd=" + logedin)
                view.chkdownload_fbstories.isChecked = true
                view.linlayout_fb_stories?.visibility = View.VISIBLE
                loadUserData()
            } else {

                println("mydataiiiidfalse=" + logedin)
                view.chkdownload_fbstories.isChecked = false
                view.linlayout_fb_stories?.visibility = View.GONE

            }
        } else {
            view.chkdownload_fbstories.isChecked = false
            view.linlayout_fb_stories?.visibility = View.GONE
        }





        view.chkdownload_private_media.setOnClickListener { view ->

            if(view.chkdownload_private_media.isChecked){
                showAdDialog(1)
                view.chkdownload_private_media.isChecked = false
            }else if(!view.chkdownload_private_media.isChecked){
                val ab = AlertDialog.Builder(
                    activity!!
                )
                ab.setPositiveButton(resources.getString(R.string.yes), object :
                    DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                        val sharedPrefsForInstagram2 = SharedPrefsForInstagram(activity)
                        val map2 =
                            sharedPrefsForInstagram2.getPreference(SharedPrefsForInstagram.PREFERENCE)

                        if (sharedPrefsForInstagram2.getPreference(SharedPrefsForInstagram.PREFERENCE) != null) {
                            sharedPrefsForInstagram2.clearSharePrefs()

                            linlayoutInstaStories?.visibility = View.GONE

                            if (map2 != null && map2.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN)
                                    .equals("true")
                            ) {
                                view.chkdownload_private_media.isChecked = true
                            } else {
                                view.chkdownload_private_media.isChecked = false
                                recUserList?.visibility = View.GONE
                                recStoriesList?.visibility = View.GONE

                            }
                            p0?.dismiss()

                            view.chkdownload_private_media.isChecked = false

                        } else {
                            sharedPrefsForInstagram2.clearSharePrefs()

                        }

                    }
                })

                ab.setNegativeButton(
                    resources.getString(R.string.cancel),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id: Int) {
                            dialog.cancel()
                            val asfd: Boolean = view.chkdownload_private_media.isChecked
                            view.chkdownload_private_media.isChecked = !asfd
                        }
                    })
                val alert = ab.create()
                alert.setTitle(getString(R.string.noprivatedownload))
                alert.setMessage(getString(R.string.no_private_insta))
                alert.show()
            }
        }

        view.chkdownload_fbstories.setOnClickListener { _ ->

            if(view.chkdownload_fbstories.isChecked){
                showAdDialog(2)
                view.chkdownload_fbstories.isChecked = false
            }else if(!view.chkdownload_fbstories.isChecked){
                val ab = AlertDialog.Builder(
                    activity!!
                )
                ab.setPositiveButton(resources.getString(R.string.yes), object :
                    DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                        val LoadPrefString = sharedPrefsForfb.LoadPrefString()
                        val logedin = LoadPrefString.get(Facebookprefloader.fb_pref_isloggedin)

                        if (logedin != null && logedin != "") {
                            sharedPrefsForfb.MakePrefEmpty()

                            view.linlayout_fb_stories?.visibility = View.GONE

                            if (logedin.equals("true") && logedin != ""
                            ) {
                                view.chkdownload_fbstories.isChecked = true
                                loadUserData()
                            } else {
                                view.chkdownload_fbstories.isChecked = false
                                view.rec_user_fblist?.visibility = View.GONE
                                view.rec_stories_fblist?.visibility = View.GONE

                            }
                            p0?.dismiss()

                            view.chkdownload_fbstories.isChecked = false

                        } else {
                            sharedPrefsForfb.MakePrefEmpty()

                        }

                    }
                })

                ab.setNegativeButton(
                    resources.getString(R.string.cancel),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id: Int) {
                            dialog.cancel()
                            logout()
                            val asfd: Boolean = view.chkdownload_fbstories.isChecked
                            view.chkdownload_fbstories.isChecked = !asfd
                        }
                    })
                val alert = ab.create()
                alert.setTitle(getString(R.string.fb_story))
                alert.setMessage(getString(R.string.no_fb_story))
                alert.show()

            }
        }



        view.bulb_icon.setOnClickListener {

            // getAllDataFormLink("", true)
            //  callGetShareChatDataURL().execute("")

            //   murl("http://sck.io/p/n1AcOs7M",activity)
        }


        view.search_story.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null && !text.equals("") && storyUsersListAdapter != null) {
                    storyUsersListAdapter!!.filter.filter(text)
                }
                return true
            }
        })



        fbsearch!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                println("dhsahdhashdk ss " + text)

                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {

                if (text != null && !text.equals("")) {

                    println("dhsahdhashdk " + text)


                    fbuserlistadapter.filter.filter(text)
                }
                return true
            }
        })

//
//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context!!)
//        mRewardedVideoAd.rewardedVideoAdListener = this
//        loadRewardedVideoAd()


//
//        if (mInterstitialAd.isLoaded) {
//            mInterstitialAd.show()
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.")
//        }


        return view
    }

    fun setCheckBox(){
        chkAutoDownload?.isChecked = false
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(activity!!).registerReceiver(
            receiver!!, IntentFilter(
                DEFAULT_BROADCAST
            )!!
        )

        val pref = context!!.getSharedPreferences("time_check", 0)
        val time:Long = pref.getLong("time",0)
        if(System.currentTimeMillis() > (time+10800000)){
            if (csRunning) {
                stopClipboardMonitor()
            }
            chkAutoDownload?.isChecked = false
            chkdownload_private_media?.isChecked = false
            chkdownload_fbstories?.isChecked = false
        }

        if (csRunning) {
            chkAutoDownload?.isChecked = true
            startClipboardMonitor()
        } else {
            chkAutoDownload?.isChecked = false
            stopClipboardMonitor()
        }

//        activity?.registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(receiver!!)
    }

    private fun getInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            activity!!,
            resources.getString(R.string.AdmobInterstitial),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("InterstitialAd Error: ", adError?.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.e(TAG, "InterstitialAd was loaded.")
                    mInterstitialAd = interstitialAd

                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad was dismissed.")
                                getInterstitialAd()
                                if (type == 1) {
                                    val sharedPrefsForInstagram = SharedPrefsForInstagram(
                                        activity
                                    )

                                    val map = sharedPrefsForInstagram.getPreference(
                                        SharedPrefsForInstagram.PREFERENCE
                                    )

                                    if (map != null && !map.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN)
                                            .equals("true")
                                    ) {
                                        val intent = Intent(
                                            activity,
                                            InstagramLoginActivity::class.java
                                        )
                                        startActivityForResult(intent, 200)
                                    }
                                } else if (type == 2) {
                                    val sharedPrefsForfb = Facebookprefloader(activity)

                                    Log.d(TAG, "Inte 0")


                                    val LoadPrefString = sharedPrefsForfb.LoadPrefString()
                                    val logedin =
                                        LoadPrefString.get(Facebookprefloader.fb_pref_isloggedin)

                                    if (!logedin.equals("true") && logedin != "") {
                                        val intent = Intent(
                                            activity,
                                            LoginWithFB::class.java
                                        )
                                        startActivityForResult(intent, 201)
                                    }
                                } else {
                                    view?.chkAutoDownload?.isChecked = true
                                    startClipboardMonitor()
                                }
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError?) {
                                Log.d(TAG, "Ad failed to show.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Ad showed fullscreen content.")
                                mInterstitialAd = null;
                            }
                        }
                }

            })

    }

    fun loadUserData() {
        view?.progress_loading_fbbar?.visibility = View.VISIBLE

        val cookie = CookieManager.getInstance().getCookie("https://www.facebook.com")
        if (!FBhelper.valadateCooki(cookie)) {
            Log.e("tag2", "cookie is not valid")
            iUtils.ShowToast(activity, getString(R.string.cookiesnotvalid))
            return
        }

        val sharedPrefsForfb = Facebookprefloader(activity)
        val LoadPrefStringol = sharedPrefsForfb.LoadPrefString()
        val LoadPrefString = LoadPrefStringol.get(Facebookprefloader.fb_pref_key)
        //     = sharedPrefsForfb.LoadPrefString( "key")

        Log.e("tag299", "cookie is not valid " + LoadPrefString)

        Log.e("tag2", "cookie is:$cookie")
        Log.e("tag2", "key is:$LoadPrefString")
        Log.e("tag2", "start getting user data")
        AndroidNetworking.post("https://www.facebook.com/api/graphql/")
                .addHeaders("accept-language", "en,en-US;q=0.9,fr;q=0.8,ar;q=0.7")
                .addHeaders("cookie", cookie)
                .addHeaders(
                    "user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36"
                )
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("fb_dtsg", LoadPrefString)
                .addBodyParameter(
                    "variables",
                    "{\"bucketsCount\":200,\"initialBucketID\":null,\"pinnedIDs\":[\"\"],\"scale\":3}"
                )
                .addBodyParameter("doc_id", "2893638314007950")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        Log.e("tag55", response.toString())
                        val parse = FBUserData.parse(response.toString())
                        if (parse != null) {
                            Log.e("tag1", "data succeed")
                            showFBSTORYData(parse)
                        }
                        view?.progress_loading_fbbar?.visibility = View.GONE

                    }

                    override fun onError(error: ANError) {
                        Log.e("tag1", "data faild$error")
                        view?.progress_loading_fbbar?.visibility = View.GONE

                    }
                })
    }


    fun loadFriendStories(str: String) {

        view?.progress_loading_fbbar?.visibility = View.VISIBLE

        val cookie = CookieManager.getInstance().getCookie("https://www.facebook.com")
        if (!FBhelper.valadateCooki(cookie)) {
            Log.e("tag2", "cookie is not valid")

            return
        }

        val sharedPrefsForfb = Facebookprefloader(activity)
        val LoadPrefStringol = sharedPrefsForfb.LoadPrefString()
        val LoadPrefString = LoadPrefStringol.get(Facebookprefloader.fb_pref_key)
        Log.e("tag2", "cookie is:$cookie")
        Log.e("tag2", "key is:$LoadPrefString")
        Log.e("tag2", "start getting user data")
        AndroidNetworking.post("https://www.facebook.com/api/graphql/")
                .addHeaders("accept-language", "en,en-US;q=0.9,fr;q=0.8,ar;q=0.7")
                .addHeaders("cookie", cookie)
                .addHeaders(
                    "user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36"
                )
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("fb_dtsg", LoadPrefString)
                .addBodyParameter(
                    "variables",
                    "{\"bucketID\":\"$str\",\"initialBucketID\":\"$str\",\"initialLoad\":false,\"scale\":5}"
                )
                .addBodyParameter("doc_id", "2558148157622405")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        Log.e("tag55", response.toString())

                        fbstorieslist = FBStory.parseBulk(response.toString())
                        if (fbstorieslist != null) {

                            fbstory_adapter = FBstoryAdapter(

                                activity,
                                fbstorieslist
                            )
                            view?.rec_stories_fblist?.layoutManager = GridLayoutManager(context, 3)
                            view?.rec_stories_fblist?.adapter = fbstory_adapter

                            view?.progress_loading_fbbar?.visibility = View.GONE

                        }
                    }

                    override fun onError(error: ANError) {
                        ShowToast(activity, "Failed to load stories")
                        Log.e("tag1", "data faild$error")
                        view?.progress_loading_fbbar?.visibility = View.GONE

                    }
                })
    }


    fun showFBSTORYData(FBUserData: FBUserData) {

        this.rec_user_fblist.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        this.fbuserlistadapter =
                FBuserRecyclerAdapter(
                    activity,
                    FBUserData.friends,
                    OnFbUserClicked { id: String? ->

                        loadFriendStories(id!!)

                    })
        this.rec_user_fblist.adapter = this.fbuserlistadapter

        view?.progress_loading_fbbar?.visibility = View.GONE

    }


    private fun logout() {
        if (Build.VERSION.SDK_INT >= 22) {
            CookieManager.getInstance().removeAllCookies(null as ValueCallback<Boolean>?)
            CookieManager.getInstance().flush()
        } else {
            val createInstance = CookieSyncManager.createInstance(activity)
            createInstance.startSync()
            val instance: CookieManager = CookieManager.getInstance()
            instance.removeAllCookie()
            instance.removeSessionCookie()
            createInstance.stopSync()
            createInstance.sync()
        }

        val sharedPrefsForfb = Facebookprefloader(activity)

        sharedPrefsForfb.MakePrefEmpty()

    }


    fun openAppFromPackedge(packedgename: String, urlofwebsite: String, installappmessage: String) {


        if (isPackageInstalled(context!!, packedgename)) {

            try {
                val pm: PackageManager = activity!!.packageManager
                val launchIntent: Intent = pm.getLaunchIntentForPackage(packedgename)!!

                activity!!.startActivity(launchIntent)
            } catch (e: ActivityNotFoundException) {
                iUtils.ShowToast(
                    context!!,
                    activity?.resources?.getString(R.string.error_occord_while)
                )


                val uri = urlofwebsite
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                activity!!.startActivity(intent)

            }


        } else {
            iUtils.ShowToast(context!!, installappmessage)
            val appPackageName =
                    packedgename
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }


        }

    }


    fun addFbAd() {

        //ads
        fbInterstitialAd = com.facebook.ads.InterstitialAd(
            activity,
            resources.getString(R.string.fbAdmobInterstitial)
        )

        val adListener = object : AdListener(), com.facebook.ads.AdListener,
                InterstitialAdListener {
            override fun onError(ad: Ad, adError: AdError) {
                Toast.makeText(
                    activity,
                    "Facebook App Is not Installed OR " + adError.errorMessage,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!")
                // Show the ad
                if (fbInterstitialAd!!.isAdLoaded) {
                    fbInterstitialAd!!.show()
                } else {
                    fbInterstitialAd!!.loadAd()
                }
            }

            override fun onAdClicked(ad: Ad) {

            }

            override fun onLoggingImpression(ad: Ad) {

            }

            override fun onInterstitialDisplayed(p0: Ad?) {
                TODO("Not yet implemented")
            }

            override fun onInterstitialDismissed(p0: Ad?) {
                TODO("Not yet implemented")
            }
        }

        val loadAdConfig = fbInterstitialAd?.buildLoadAdConfig()
                ?.withAdListener(adListener)
                ?.build()

        fbInterstitialAd?.loadAd(loadAdConfig)


    }

    fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 2
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // 3
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.e("loged112211", "Notificaion Channel Created!")
        }
    }

    private fun setNofication(b: Boolean) {

        if (b) {
            val channelId = "${context!!.packageName}-${context!!.getString(R.string.app_name)}"
            val notificationBuilder = NotificationCompat.Builder(context!!, channelId).apply {
                setSmallIcon(R.drawable.notification_template_icon_bg) // 3
                // setStyle(NotificationCompat.)
                setLargeIcon(
                    BitmapFactory.decodeResource(
                        context!!.resources,
                        R.drawable.notification_template_icon_bg
                    )
                )
                setContentTitle(activity?.resources?.getString(R.string.auto_download_title_notification)) // 4

                setContentText(activity?.resources?.getString(R.string.auto_download_title_notification_start)) // 5
                setOngoing(true)
                priority = NotificationCompat.PRIORITY_LOW // 7
                setSound(null)
                setOnlyAlertOnce(true)
                setAutoCancel(false)
                addAction(
                    R.drawable.navigation_empty_icon,
                    "Stop",
                    makePendingIntent("quit_action")
                )

                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                val pendingIntent = PendingIntent.getActivity(requireActivity(), 0, intent, 0)

                setContentIntent(pendingIntent)
            }
            with(NotificationManagerCompat.from(requireActivity())) {
                // notificationId is a unique int for each notification that you must define
                notify(NotifyID, notificationBuilder.build())

                Log.e("loged", "testing notification notify!")


            }


        } else {
            NotificationManagerCompat.from(requireActivity()).cancel(NotifyID)
        }
    }

    fun startClipboardMonitor() {
        csRunning = true;
        prefEditor.putBoolean("csRunning", true)
        prefEditor.commit()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(
                Intent(
                    requireContext(),
                    ClipboardMonitor::class.java
                ).setAction(STARTFOREGROUND_ACTION)
            )
        } else {
            requireActivity().startService(
                Intent(
                    requireContext(),
                    ClipboardMonitor::class.java
                )
            )
        }

    }

    fun stopClipboardMonitor() {
        csRunning = false;
        prefEditor.putBoolean("", false)
        prefEditor.commit()

        requireActivity().stopService(
            Intent(
                requireContext(),
                ClipboardMonitor::class.java
            ).setAction(STOPFOREGROUND_ACTION)
        )


    }

    fun makePendingIntent(name: String): PendingIntent {
        val intent = Intent(requireActivity(), Receiver::class.java)
        intent.action = name
        return PendingIntent.getBroadcast(requireActivity(), 0, intent, 0)
    }

    fun DownloadVideo(url: String) {


        if (url.equals("") && iUtils.checkURL(url)) {
            iUtils.ShowToast(context!!, activity?.resources?.getString(R.string.enter_valid))


        } else {


            Log.d("mylogissssss", "The interstitial wasn't loaded yet.")



            if (url.contains("instagram.com")) {
                progressDralogGenaratinglink.show()
                startInstaDownload(url)

            } else if (url.contains("myjosh.in")) {
                var myurl = url
                myurl = myurl.substring(myurl.indexOf("http"))
                myurl = myurl.substring(
                    myurl.indexOf("http://share.myjosh.in/"),
                    myurl.indexOf("Download Josh for more videos like this!")
                )


                downloadVideo.Start(context!!, myurl.trim(), false)
                Log.e("downloadFileName12", url.trim())
            } else if (url.contains("audiomack")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("zili")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            }

//            else if (url.contains("tiktok")) {
//                if (progressDralogGenaratinglink != null) {
//                    progressDralogGenaratinglink.dismiss()
//                }
//
////                val intent = Intent(activity, GetTiktokLinkThroughWebview::class.java)
////                intent.putExtra("myurlis", url)
////                startActivityForResult(intent, 2)
//
//                val intent = Intent(activity, TikTokDownloadWebview::class.java)
//                intent.putExtra("myvidurl", url)
//                startActivityForResult(intent, 2)
//
//            }

            else if (url.contains("zingmp3")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("vidlit")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("byte.co")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("fthis.gr")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("fw.tv") || url.contains("firework.tv")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("rumble")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("traileraddict")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            }
//            else if (url.contains("veer.tv")) {
//                if (progressDralogGenaratinglink != null) {
//                    progressDralogGenaratinglink.dismiss()
//                }
//
//                val intent = Intent(activity, GetLinkThroughWebview::class.java)
//                intent.putExtra("myurlis", url)
//                startActivityForResult(intent, 2)
//
//            }
            //ojoo video app
            else if (url.contains("bemate")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val urlq = url.substring(url.indexOf("https"), url.length)
                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", urlq)
                startActivityForResult(intent, 2)

            } else if (url.contains("chingari")) {
                var myurl = url

                myurl = myurl.substring(
                    myurl.indexOf("https://chingari.io/"),
                    myurl.indexOf("For more such entertaining")
                )


                downloadVideo.Start(context!!, myurl.trim(), false)
                Log.e("downloadFileName12", myurl.trim())
            } else if (url.contains("sck.io") || url.contains("snackvideo")) {
                var myurl = url
                try {
                    if (myurl.length > 30) {
                        myurl = myurl.substring(
                            myurl.indexOf("http"),
                            myurl.indexOf("Click this")
                        )
                    }
                } catch (e: Exception) {

                }

                downloadVideo.Start(context!!, myurl.trim(), false)
                Log.e("downloadFileName12", myurl.trim())
            } else {
                Log.d("mylogissssss33", "Thebbbbbbbloaded yet.")

                var myurl = url
                try {
                    myurl = myurl.substring(myurl.indexOf("http")).trim()
                } catch (e: Exception) {

                }
                //  Log.e("downloadFileName12", myurl.trim())

                downloadVideo.Start(context!!, myurl, false)
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("proddddd11111222 $resultCode __" + data)

        if (requestCode == 200 && resultCode == RESULT_OK) {

            println("proddddd11111200 $resultCode __" + data)

            val sharedPrefsForInstagram = SharedPrefsForInstagram(activity)

            val map =
                    sharedPrefsForInstagram.getPreference(SharedPrefsForInstagram.PREFERENCE)

            if (map != null) {
                println("proddddd11111  " + map.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN))

                if (!map.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN)
                                .equals("true")
                ) {
                    view?.chkdownload_private_media?.isChecked = false
                    linlayoutInstaStories?.visibility = View.GONE
                } else {
                    view?.chkdownload_private_media?.isChecked = true
                    linlayoutInstaStories?.visibility = View.VISIBLE
                    getallstoriesapicall()
                }
            }
        }

        if (requestCode == 201 && resultCode == RESULT_OK) {

            println("proddddd11111201 $resultCode __" + data)

            val sharedPrefsForfb = Facebookprefloader(activity)
            val LoadPrefStringol = sharedPrefsForfb.LoadPrefString()
            val logedin = LoadPrefStringol.get(Facebookprefloader.fb_pref_isloggedin)

            println("proddddd11111201-1=" + logedin)
            if (logedin != null && logedin != "") {

                if (logedin.equals("true")) {
                    view?.chkdownload_fbstories?.isChecked = true
                    view?.linlayout_fb_stories?.visibility = View.VISIBLE
                    loadUserData()
                } else {
                    view?.chkdownload_fbstories?.isChecked = false
                    view?.linlayout_fb_stories?.visibility = View.GONE
                }

            } else {
                view?.chkdownload_fbstories?.isChecked = false
                view?.linlayout_fb_stories?.visibility = View.GONE
            }


        }

    }


    private fun showAdDialog(i: Int) {

        val pref = context!!.getSharedPreferences("time_check", 0)
        val prefEditor = pref.edit()
        prefEditor.putLong("time",System.currentTimeMillis())
        prefEditor.apply()

        val dialogBuilder = AlertDialog.Builder(context!!)

        if(i == 1){
            dialogBuilder.setMessage("To connect your Instagram account You Must Watch An Ad. Do You Want To See One?")
        }else if(i == 2){
            dialogBuilder.setMessage("To connect your Facebook account You Must Watch An Ad. Do You Want To See One?")
        }else{
            dialogBuilder.setMessage(getString(R.string.doyouseead))
        }
                dialogBuilder
                    .setCancelable(false)
                    .setPositiveButton(
                        getString(R.string.watchad)
                    ) { _, _ ->


                    if (mRewardedAd != null && mRewardedAd!!.isLoaded) {

                        val adCallback = object: RewardedAdCallback() {
                            override fun onRewardedAdOpened() {
                                // Ad opened.
                                Log.e(TAG, "onRewardedAdOpened: ")
                            }
                            override fun onRewardedAdClosed() {
                                // Ad closed.
                                Log.e(TAG, "onRewardedAdClosed: ")
                            }
                            override fun onUserEarnedReward(p0: com.google.android.gms.ads.rewarded.RewardItem) {
                                // User earned reward.

                                if(i == 1){
                                    val sharedPrefsForInstagram = SharedPrefsForInstagram(activity)

                                    val map = sharedPrefsForInstagram.getPreference(
                                        SharedPrefsForInstagram.PREFERENCE
                                    )


                                    if (map != null && !map.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN)
                                            .equals("true")
                                    ) {
                                        val intent = Intent(
                                            activity,
                                            InstagramLoginActivity::class.java
                                        )
                                        startActivityForResult(intent, 200)
                                    }
                                }else if(i == 2){
                                    val sharedPrefsForfb = Facebookprefloader(activity)

                                    Log.d(TAG, "Inte 0")

                                    val LoadPrefString = sharedPrefsForfb.LoadPrefString()
                                    val logedin = LoadPrefString.get(Facebookprefloader.fb_pref_isloggedin)

                                    if (!logedin.equals("true") && logedin != "") {
                                        val intent = Intent(
                                            activity,
                                            LoginWithFB::class.java
                                        )
                                        startActivityForResult(intent, 201)
                                    }
                                }else{
                                    view?.chkAutoDownload?.isChecked = true
                                    val checked = view?.chkAutoDownload?.isChecked
                                    if (checked!!) {
                                        startClipboardMonitor()
                                    } else {
                                        stopClipboardMonitor()
                                    }
                                }
                            }
                            override fun onRewardedAdFailedToShow(p0: com.google.android.gms.ads.AdError?) {
                                // Ad failed to display.
                                Log.e(TAG, "onRewardedAdFailedToShow: ")
                            }
                        }
                        mRewardedAd!!.show(activity!!, adCallback)

//                        mRewardedAd?.show(activity!!) {
//                            fun onUserEarnedReward(rewardItem: RewardItem) {
//                                var rewardAmount = rewardItem.amount
//                                var rewardType = rewardItem.type
//                                Log.e("none", "User earned the reward.")
//
////                                view?.chkAutoDownload?.isChecked = true
////
////                                startClipboardMonitor()
//                            }
//                        }

                    } else {

                        if (mInterstitialAd != null) {
                            type = i
                            mInterstitialAd?.show(activity!!)

                        } else {
                            Handler(Looper.getMainLooper()).postDelayed({
                                if (mInterstitialAd != null) {
                                    type = i
                                    mInterstitialAd?.show(activity!!)

                                } else {
                                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
//                                    iUtils.ShowToast(
//                                        context!!,"The interstitial ad wasn't ready yet."
//                                    )
                                    if (i != 1 && i != 2) {
                                        view?.chkAutoDownload?.isChecked = true
                                        startClipboardMonitor()
                                    } else if (i == 1) {
                                        val sharedPrefsForInstagram = SharedPrefsForInstagram(
                                            activity
                                        )

                                        val map = sharedPrefsForInstagram.getPreference(
                                            SharedPrefsForInstagram.PREFERENCE
                                        )


                                        if (map != null && !map.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN)
                                                .equals("true")
                                        ) {
                                            val intent = Intent(
                                                activity,
                                                InstagramLoginActivity::class.java
                                            )
                                            startActivityForResult(intent, 200)
                                        }
                                    } else if (i == 2) {
                                        val sharedPrefsForfb = Facebookprefloader(activity)

                                        Log.d(TAG, "Inte 0")


                                        val LoadPrefString = sharedPrefsForfb.LoadPrefString()
                                        val logedin =
                                            LoadPrefString.get(Facebookprefloader.fb_pref_isloggedin)

                                        if (!logedin.equals("true") && logedin != "") {
                                            val intent = Intent(
                                                activity,
                                                LoginWithFB::class.java
                                            )
                                            startActivityForResult(intent, 201)
                                        }
                                    }
                                }
                            }, 3000)
                        }

//                        iUtils.ShowToast(
//                                context!!,
//                                activity?.resources?.getString(R.string.videonotavaliabl)
//                        )

//                        view?.chkAutoDownload?.isChecked = true
//                        val checked = view?.chkAutoDownload?.isChecked
//
//                        if (checked!!) {
//                            Log.e("loged", "testing checked!")
//                            startClipboardMonitor()
//                        } else {
//                            Log.e("loged", "testing unchecked!")
//
//
//                            stopClipboardMonitor()
//                            // setNofication(false);
//                        }
//


                        Log.d("TAG", "The rewarded ad wasn't ready yet.")
                    }


//
//
//                if (mRewardedVideoAd.isLoaded) {
//                    mRewardedVideoAd.show()
//                } else {
//
//
//                }


                }

                .setNegativeButton(
                    getString(R.string.cancel)
                ) { dialog, _ ->
                    dialog.cancel()

                    val checked = view?.chkAutoDownload?.isChecked
                    if (checked!!) {
                        view?.chkAutoDownload?.isChecked = false
                    }

                }


        val alert = dialogBuilder.create()
        if(i == 1){
            alert.setTitle("Connect with Instagram")
        }else if(i == 2){
            alert.setTitle("Connect with Facebook")
        }else{
            alert.setTitle(getString(R.string.enabAuto))
        }
        alert.show()

    }


    //insta finctions


    fun startInstaDownload(Url: String) {


//         https://www.instagram.com/p/CLBM34Rhxek/?igshid=41v6d50y6u4w
//          https://www.instagram.com/p/CLBM34Rhxek/
//           https://www.instagram.com/p/CLBM34Rhxek/?__a=1


        var Urlwi: String?
        try {

            val uri = URI(Url)
            Urlwi = URI(
                uri.scheme,
                uri.authority,
                uri.path,
                null,  // Ignore the query part of the input url
                uri.fragment
            ).toString()


        } catch (ex: java.lang.Exception) {
            Urlwi = ""
            iUtils.ShowToast(activity, "Please Enter A Valid Url")
            return
        }

        System.err.println("workkkkkkkkk 1122112 " + Url)

        var urlwithoutlettersqp: String? = Urlwi
        System.err.println("workkkkkkkkk 1122112 " + urlwithoutlettersqp)

        urlwithoutlettersqp = "$urlwithoutlettersqp?__a=1"
        System.err.println("workkkkkkkkk 87878788 " + urlwithoutlettersqp)

        try {
            System.err.println("workkkkkkkkk 4")


            val sharedPrefsFor = SharedPrefsForInstagram(activity)
            val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
            if (map != null) {

                downloadInstagramImageOrVideodata(
                    urlwithoutlettersqp, "ds_user_id=" + map.get(
                        SharedPrefsForInstagram.PREFERENCE_USERID
                    )
                            + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID)
                )

            } else {


                downloadInstagramImageOrVideodata(urlwithoutlettersqp, "")

            }


        } catch (e: java.lang.Exception) {
            System.err.println("workkkkkkkkk 5")
            e.printStackTrace()
        }
    }


    private fun callStoriesDetailApi(UserId: String) {
        try {
            view?.progress_loading_bar?.visibility = View.VISIBLE

            val sharedPrefsFor = SharedPrefsForInstagram(activity)
            val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
            if (map != null) {

                getFullDetailsOfClickedFeed(
                    UserId,
                    "ds_user_id=" + map.get(SharedPrefsForInstagram.PREFERENCE_USERID)
                        .toString() + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID)
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun downloadInstagramImageOrVideodata(URL: String?, Cookie: String?) {


        AndroidNetworking.get(URL)
                .setPriority(Priority.LOW)
                .addHeaders("Cookie", Cookie)
                .addHeaders(
                    "User-Agent",
                    "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\""
                )
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        println("response1122334455_jsomobj:   ${response}")
                        try {
                            val listType: Type =
                                object : TypeToken<ModelInstagramResponse?>() {}.type
                            val modelInstagramResponse: ModelInstagramResponse = Gson().fromJson(
                                response.toString(),
                                listType
                            )


                            if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                val modelGetEdgetoNode: ModelGetEdgetoNode =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children

                                val modelEdNodeArrayList: List<ModelEdNode> =
                                    modelGetEdgetoNode.modelEdNodes
                                for (i in 0 until modelEdNodeArrayList.size) {
                                    if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                        myVideoUrlIs = modelEdNodeArrayList[i].modelNode.video_url
                                        downloadFile.DownloadingInsta(
                                            activity,
                                            myVideoUrlIs,
                                            iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                            ".mp4"
                                        )
                                        // etText.setText("");
                                        if (progressDralogGenaratinglink != null) {
                                            progressDralogGenaratinglink.dismiss()
                                        }



                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                        downloadFile.DownloadingInsta(
                                            activity,
                                            myPhotoUrlIs,
                                            iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                            ".png"
                                        )
                                        myPhotoUrlIs = ""
                                        if (progressDralogGenaratinglink != null) {
                                            progressDralogGenaratinglink.dismiss()
                                        }
                                        // etText.setText("");
                                    }
                                }
                            } else {
                                val isVideo =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                                if (isVideo) {
                                    myVideoUrlIs =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                    downloadFile.DownloadingInsta(
                                        activity,
                                        myVideoUrlIs,
                                        iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                        ".mp4"
                                    )
                                    if (progressDralogGenaratinglink != null) {
                                        progressDralogGenaratinglink.dismiss()
                                    }
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                    downloadFile.DownloadingInsta(
                                        activity,
                                        myPhotoUrlIs,
                                        iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                        ".png"
                                    )
                                    if (progressDralogGenaratinglink != null) {
                                        progressDralogGenaratinglink.dismiss()
                                    }
                                    myPhotoUrlIs = ""
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            view?.progress_loading_bar?.visibility = View.GONE
                            e.printStackTrace()
                            if (progressDralogGenaratinglink != null) {
                                progressDralogGenaratinglink.dismiss()
                            }
                        }


                    }

                    override fun onError(error: ANError) {
                        println("response1122334455:   " + "Failed1")
                        view?.progress_loading_bar?.visibility = View.GONE
                    }
                })


    }


    private fun getallstoriesapicall() {
        try {
            view?.progress_loading_bar?.visibility = View.VISIBLE

            val sharedPrefsFor = SharedPrefsForInstagram(activity)
            val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
            if (map != null) {

                getallStories(
                    "ds_user_id=" + map.get(SharedPrefsForInstagram.PREFERENCE_USERID)
                        .toString() + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID)
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun getallStories(Cookie: String?) {
        var Cookie = Cookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }

        AndroidNetworking.get("https://i.instagram.com/api/v1/feed/reels_tray/")
                .setPriority(Priority.LOW)
                .addHeaders("Cookie", Cookie)
                .addHeaders(
                    "User-Agent",
                    "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\""
                )
                .build()
                .getAsObject(
                    InstaStoryModelClass::class.java,
                    object : ParsedRequestListener<InstaStoryModelClass> {
                        override fun onResponse(response: InstaStoryModelClass) {
                            // do anything with response


                            try {

                                println(
                                    "response1122334455_story:  " + response.tray
                                )

                                view?.rec_user_list?.visibility = View.VISIBLE
                                view?.progress_loading_bar?.visibility = View.GONE
                                storyUsersListAdapter = StoryUsersListAdapter(
                                    activity,
                                    response.tray, this@download
                                )
                                val linearLayoutManager =
                                    LinearLayoutManager(
                                        context,
                                        RecyclerView.HORIZONTAL,
                                        false
                                    )

                                view?.rec_user_list?.layoutManager = linearLayoutManager
                                view?.rec_user_list?.adapter = storyUsersListAdapter
                                storyUsersListAdapter!!.notifyDataSetChanged()
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                                println("response1122334455_storyERROR:  " + e.message)

                                view?.progress_loading_bar?.visibility = View.GONE
                            }


                        }

                        override fun onError(anError: ANError) {
                            // handle error
                        }
                    })


    }


    fun getFullDetailsOfClickedFeed(UserId: String, Cookie: String?) {


        AndroidNetworking.get("https://i.instagram.com/api/v1/users/$UserId/full_detail_info?max_id=")
                .setPriority(Priority.LOW)
                .addHeaders("Cookie", Cookie)
                .addHeaders(
                    "User-Agent",
                    "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\""
                )
                .build()
                .getAsObject(
                    ModelFullDetailsInstagram::class.java,
                    object : ParsedRequestListener<ModelFullDetailsInstagram> {
                        override fun onResponse(response: ModelFullDetailsInstagram) {
                            // do anything with response

                            try {

                                view?.rec_user_list?.visibility = View.VISIBLE
                                view?.progress_loading_bar?.visibility = View.GONE
                                println("response1122334455_fulldetails:   ${response.reel_feed}")



                                if (response.reel_feed.items.size == 0) {
                                    iUtils.ShowToast(activity, getString(R.string.nostoryfound))
                                }

                                listAllStoriesOfUserAdapter = ListAllStoriesOfUserAdapter(
                                    activity,
                                    response.reel_feed.items
                                )
                                view?.rec_stories_list?.visibility = View.VISIBLE

                                val gridLayoutManager = GridLayoutManager(context, 3)


                                view?.rec_stories_list?.layoutManager = gridLayoutManager
                                view?.rec_stories_list?.isNestedScrollingEnabled = true
                                view?.rec_stories_list?.adapter = listAllStoriesOfUserAdapter
                                listAllStoriesOfUserAdapter.notifyDataSetChanged()
                            } catch (e: java.lang.Exception) {
                                view?.rec_stories_list?.visibility = View.GONE
                                e.printStackTrace()
                                view?.progress_loading_bar?.visibility = View.GONE
                                iUtils.ShowToast(activity, getString(R.string.nostoryfound))

                            }

                        }

                        override fun onError(anError: ANError) {
                            println("response1122334455:   " + "Failed2")
                            view?.progress_loading_bar?.visibility = View.GONE
                        }
                    })

    }

    override fun onclickUserStoryListeItem(position: Int, modelUsrTray: ModelUsrTray?) {

        println("response1122ff334455:   " + modelUsrTray + position)

        callStoriesDetailApi(modelUsrTray?.user?.pk.toString())

    }

//    class UiUpdate(chkAutoDownload: CheckBox) : BroadcastReceiver() {
//
//        override fun onReceive(context: Context?, intent: Intent?) {
//
//        }
//
//    }

}