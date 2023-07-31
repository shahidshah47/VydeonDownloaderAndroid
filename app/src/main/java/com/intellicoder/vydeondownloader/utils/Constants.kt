@file:Suppress("DEPRECATION")

package com.intellicoder.vydeondownloader.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.gms.measurement.api.AppMeasurementSdk
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellicoder.vydeondownloader.PlayActivity
import com.intellicoder.vydeondownloader.models.storymodels.ModelEdNode
import com.intellicoder.vydeondownloader.models.storymodels.ModelGetEdgetoNode
import com.intellicoder.vydeondownloader.models.storymodels.ModelInstagramResponse
import com.intellicoder.vydeondownloader.tasks.downloadFile
import org.json.JSONObject
import java.lang.reflect.Type


object Constants {


    //  const val TiktokApi: String = "https://api2-16-h2.musical.ly/aweme/v1/aweme/detail/"
    const val TiktokApi: String = "https://api2.musical.ly/aweme/v1/playwm/detail/"

    //  const val TiktokApiNowatermark: String = "https://nodejsapidownloader.herokuapp.com/api"
    const val TiktokApiNowatermark: String = "http://localhost/img/test.php?url="

    //http://localhost/img/test.php?url=https://vm.tiktok.com/ZSQTnNWu/
    const val DlApisUrl: String = "https://dlphpapis.herokuapp.com/api/info?url="
    //https://dlphpapis.herokuapp.com/api/info?url=

    const val DOWNLOAD_DIRECTORY: String = "AIO_Video_Downloader"
    const val FacebookApi: String = "https://m.facebook.com/watch/"
    const val STARTFOREGROUND_ACTION =
        "com.infusiblecoder.allinonevideodownloader.action.startforeground"
    const val STOPFOREGROUND_ACTION =
        "com.infusiblecoder.allinonevideodownloader.action.stopforeground"
    const val PREF_APPNAME: String = "aiovidedownloader"
    const val PREF_CLIP: String = "tikVideoDownloader"
    const val FOLDER_NAME = "/WhatsApp/"
    const val FOLDER_NAME_Whatsappbusiness = "/WhatsApp Business/"
    const val SAVE_FOLDER_NAME = "/AIO_Status_Saver/"
    const val MY_ANDROID_10_IDENTIFIER_OF_FILE = "All_Video_Downloader_"
    const val MyPREFERENCES = "PREFS"

    const val videoDownloaderFolderName = "AIO_Video_Downloader"
    const val videowloaderSubfolder = "AIO_Videos"

    const val tiktokWebviewUrl = "https://www.tiktok.com/?lang=en"
    const val tiktokDownloadWebviewUrl = "https://ssstiktok.io/"

    const val directoryInstaShoryDirectory_videos = "/Download/InstaStory"
    const val directoryInstaShoryDirectorydownload_videos = "/InstaStory/videos/"
    const val directoryInstaShoryDirectorydownload_images = "/InstaStory/images/"


    const val showyoutube = false

    //TODO NOTE: if you enable this admobe should be disabled
    const val show_facebookads = false
    const val show_admobads = true
    const val show_startappads = true
    const val show_earning_card_in_extrafragment = true


    var myVideoUrlIs: String? = ""
    var myPhotoUrlIs: String? = ""
    lateinit var myprogressDD: ProgressDialog


    fun startInstaDownload(context: Context, Url: String, islinkextractor: Boolean) {


        try {
            System.err.println("workkkkkkkkk 4")


            val sharedPrefsFor = SharedPrefsForInstagram(context)
            val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
            if (map != null) {

                downloadInstagramImageOrVideodata(
                    context,
                    Url,
                    "ds_user_id=" + map.get(
                        SharedPrefsForInstagram.PREFERENCE_USERID
                    )
                            + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID),
                    islinkextractor
                )
            } else {
                downloadInstagramImageOrVideodata(context, Url, "", islinkextractor)
            }


        } catch (e: java.lang.Exception) {
            System.err.println("workkkkkkkkk 5")
            e.printStackTrace()
        }
    }


    fun downloadInstagramImageOrVideodata(
        context: Context,
        URL: String?,
        Cookie: String?,
        islinkextractor: Boolean
    ) {

        myprogressDD = ProgressDialog(context)
        myprogressDD.setMessage("Loading....")
        myprogressDD.setCancelable(false)
        myprogressDD.show()

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
                    myprogressDD.dismiss()

                    println("response1122334455_jsomobj:   ${response}")
                    try {
                        val listType: Type =
                            object : TypeToken<ModelInstagramResponse?>() {}.getType()
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
                                        context,
                                        myVideoUrlIs,
                                        iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                        ".mp4"
                                    )

                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                    downloadFile.DownloadingInsta(
                                        context,
                                        myPhotoUrlIs,
                                        iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                        ".png"
                                    )
                                    myPhotoUrlIs = ""

                                }
                            }
                        } else {

                            if (!islinkextractor) {
                                val isVideo =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                                if (isVideo) {
                                    myVideoUrlIs =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                    downloadFile.DownloadingInsta(
                                        context,
                                        myVideoUrlIs,
                                        iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                        ".mp4"
                                    )

                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                    downloadFile.DownloadingInsta(
                                        context,
                                        myPhotoUrlIs,
                                        iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                        ".png"
                                    )

                                    myPhotoUrlIs = ""
                                }

                            } else {

                                myVideoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                context.startActivity(
                                    Intent(
                                        context,
                                        PlayActivity::class.java
                                    ).putExtra(
                                        "videourl",
                                        myVideoUrlIs
                                    ).putExtra(
                                        AppMeasurementSdk.ConditionalUserProperty.NAME,
                                        "allvideo" + System.currentTimeMillis() + ".mp4"
                                    )
                                )


                            }


                        }
                    } catch (e: java.lang.Exception) {
                        myprogressDD.dismiss()

                        e.printStackTrace()

                    }


                }

                override fun onError(error: ANError) {
                    myprogressDD.dismiss()

                    println("response1122334455:   " + "Failed1")
                }
            })


    }


}