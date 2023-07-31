package com.intellicoder.vydeondownloader

import android.os.Bundle
import android.view.Window
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.intellicoder.vydeondownloader.fragments.AudiogalleryGallery
import com.intellicoder.vydeondownloader.fragments.GalleryFragmentMainGallery
import com.intellicoder.vydeondownloader.fragments.InstagalleryImagesGallery
import com.intellicoder.vydeondownloader.fragments.StatusSaverGallery
import java.util.*

@Suppress("DEPRECATION")
class GalleryActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_gallery)




        setlayout()

    }


    fun setlayout() {
        viewPager = findViewById<ViewPager>(R.id.viewpagergallery)
        setupViewPager(viewPager!!)

        tabLayout = findViewById<TabLayout>(R.id.tabsgallery)
        tabLayout!!.setupWithViewPager(viewPager)
        setupTabIcons()


    }

    fun setupTabIcons() {

        tabLayout?.getTabAt(0)?.setIcon(R.drawable.ic_gallery_color_24dp)
        tabLayout?.getTabAt(1)?.setIcon(R.drawable.statuspic)
        tabLayout?.getTabAt(2)?.setIcon(R.drawable.ic_image)
        tabLayout?.getTabAt(3)?.setIcon(R.drawable.ic_music_note_24dp)


    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(
            GalleryFragmentMainGallery(),
            getString(R.string.gallery_fragment_statussaver)
        )
        adapter.addFragment(StatusSaverGallery(), getString(R.string.StatusSaver_gallery))
        adapter.addFragment(InstagalleryImagesGallery(), getString(R.string.instaimage))
        adapter.addFragment(AudiogalleryGallery(), getString(R.string.audios))

        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {

            return mFragmentList[position]

        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }


//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//
//            R.id.action_privacy -> {
//
//                AlertDialog.Builder(this)
//                    .setTitle(getString(R.string.privacy))
//                    .setMessage(R.string.privacy_message)
//                    .setPositiveButton(
//                        android.R.string.yes
//                    ) { dialog, _ -> dialog.dismiss() }
//                    .setIcon(R.drawable.ic_info_black_24dp)
//                    .show()
//
//
//
//                true
//            }
//
//
//            R.id.action_rate -> {
//
//
//                AlertDialog.Builder(this)
//                    .setTitle(getString(R.string.RateAppTitle))
//                    .setMessage(getString(R.string.RateApp))
//                    .setCancelable(false)
//                    .setPositiveButton(
//                        getString(R.string.rate_dialog),
//                        { _, _ ->
//                            val appPackageName = packageName
//                            try {
//                                startActivity(
//                                    Intent(
//                                        Intent.ACTION_VIEW,
//                                        Uri.parse("market://details?id=$appPackageName")
//                                    )
//                                )
//                            } catch (anfe: android.content.ActivityNotFoundException) {
//                                startActivity(
//                                    Intent(
//                                        Intent.ACTION_VIEW,
//                                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
//                                    )
//                                )
//                            }
//                        })
//                    .setNegativeButton(getString(R.string.later_btn), null).show()
//
//                true
//            }
//
//
//            R.id.ic_whatapp -> {
//
//                val launchIntent = packageManager.getLaunchIntentForPackage("com.whatsapp")
//                if (launchIntent != null) {
//
//                    startActivity(launchIntent)
//                    finish()
//                } else {
//
//                    iUtils.ShowToast(
//                        this,
//                        this.resources.getString(R.string.appnotinstalled)
//                    )
//                }
//                true
//            }
//
//
////            R.id.about_rate -> {
////
////                val dialog = Dialog(this)
////                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
////                dialog.setCancelable(true)
////                dialog.setContentView(R.layout.dialog_change_language)
////
////                val l_english = dialog.findViewById(R.id.l_english) as TextView
////                l_english.setOnClickListener {
////
////                    LocaleHelper.setLocale(application, "en")
////                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
////
////
////                    val editor: SharedPreferences.Editor = getSharedPreferences(
////                        "lang_pref",
////                        Context.MODE_PRIVATE
////                    ).edit()
////                    editor.putString("lang", "en")
////
////                    editor.apply()
////
////
////                    recreate()
////                    dialog.dismiss()
////                }
////
////                val l_arabic = dialog.findViewById(R.id.l_arabic) as TextView
////                l_arabic.setOnClickListener {
////                    LocaleHelper.setLocale(application, "ar")
////                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
////
////                    val editor: SharedPreferences.Editor = getSharedPreferences(
////                        "lang_pref",
////                        Context.MODE_PRIVATE
////                    ).edit()
////                    editor.putString("lang", "ar")
////
////                    editor.apply()
////
////
////                    recreate()
////                    dialog.dismiss()
////
////                }
////                val l_urdu = dialog.findViewById(R.id.l_urdu) as TextView
////                l_urdu.setOnClickListener {
////                    LocaleHelper.setLocale(application, "ur")
////                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
////
////
////                    val editor: SharedPreferences.Editor = getSharedPreferences(
////                        "lang_pref",
////                        Context.MODE_PRIVATE
////                    ).edit()
////                    editor.putString("lang", "ur")
////
////                    editor.apply()
////
////
////                    recreate()
////                    dialog.dismiss()
////                }
////
////
////                val l_turkey = dialog.findViewById(R.id.l_turkey) as TextView
////                l_turkey.setOnClickListener {
////                    LocaleHelper.setLocale(application, "tr")
////                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
////                    val editor: SharedPreferences.Editor = getSharedPreferences(
////                        "lang_pref",
////                        Context.MODE_PRIVATE
////                    ).edit()
////                    editor.putString("lang", "tr")
////
////                    editor.apply()
////
////
////                    recreate()
////                    dialog.dismiss()
////                }
////
////
////                val l_portougese = dialog.findViewById(R.id.l_portougese) as TextView
////                l_portougese.setOnClickListener {
////                    LocaleHelper.setLocale(application, "pt")
////                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
////                    val editor: SharedPreferences.Editor = getSharedPreferences(
////                        "lang_pref",
////                        Context.MODE_PRIVATE
////                    ).edit()
////                    editor.putString("lang", "pt")
////
////                    editor.apply()
////
////
////                    recreate()
////                    dialog.dismiss()
////                }
////
////
////                val l_chinese = dialog.findViewById(R.id.l_chinese) as TextView
////                l_chinese.setOnClickListener {
////                    LocaleHelper.setLocale(application, "zh")
////                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
////                    val editor: SharedPreferences.Editor = getSharedPreferences(
////                        "lang_pref",
////                        Context.MODE_PRIVATE
////                    ).edit()
////                    editor.putString("lang", "zh")
////
////                    editor.apply()
////
////
////                    recreate()
////                    dialog.dismiss()
////                }
////
////
////                val l_hindi = dialog.findViewById(R.id.l_hindi) as TextView
////                l_hindi.setOnClickListener {
////                    LocaleHelper.setLocale(application, "hi")
////                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
////                    val editor: SharedPreferences.Editor = getSharedPreferences(
////                        "lang_pref",
////                        Context.MODE_PRIVATE
////                    ).edit()
////                    editor.putString("lang", "hi")
////
////                    editor.apply()
////
////
////                    recreate()
////                    dialog.dismiss()
////                }
////
////
////
////
////                dialog.show()
////
////                true
////            }
////
////            else -> super.onOptionsItemSelected(item)
////        }
////    }
//
//
////    override fun attachBaseContext(newBase: Context?) {
////        super.attachBaseContext(LocaleHelper.onAttach(newBase))
////    }


}