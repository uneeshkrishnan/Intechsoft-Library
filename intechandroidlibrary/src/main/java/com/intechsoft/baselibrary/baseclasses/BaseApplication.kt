package com.intechsoft.baselibrary.baseclasses

//import com.intechsoft.baselibrary.interfaces.APIClient_RestInterface
import android.app.Application
import android.content.Context
import android.webkit.WebView
import androidx.multidex.MultiDex
import net.danlew.android.joda.JodaTimeAndroid

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        prefs = BaseSharedPreferences(applicationContext)
//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/kartikab.ttf") // font from assets: "assets/fonts/Roboto-Regular.ttf
        WebView(this).destroy()
        JodaTimeAndroid.init(applicationContext)
    }

    companion object {
//        const val GRAPH_BASE_URL = "https://graph.microsoft.com/v1.0/"
//        const val REST_BASE_URL = "https://accounts.accesscontrol.windows.net"
//
//        const val SITE_NAME = "kfhcom.sharepoint.com"
//        const val GRAPH_CLIENT_ID = "6108a4d9-c9c2-4e76-8a8d-ea04be9fc321" // "0bbd60f5-a01a-432b-abd4-d3f5bb89a87b" // uatbc = "6108a4d9-c9c2-4e76-8a8d-ea04be9fc321"
//        const val GRAPH_SECRET_KEY = "1eef34dd-5862-4fa2-9ba8-c29161bd1dde" // "51ac5e86-7f10-4da3-a720-c086d63773e3" // uatbc = "1eef34dd-5862-4fa2-9ba8-c29161bd1dde"
//
//        const val IDEA_GRAPH_CLIENT_ID = "2fc3186d-c2b0-40a8-b531-544175652ec2" // "0bbd60f5-a01a-432b-abd4-d3f5bb89a87b" // uatbc = "6108a4d9-c9c2-4e76-8a8d-ea04be9fc321"
//        const val IDEA_GRAPH_SECRET_KEY = "cc8e9727-6f2a-4a2e-892d-745d86519f9b" // "51ac5e86-7f10-4da3-a720-c086d63773e3" // uatbc = "1eef34dd-5862-4fa2-9ba8-c29161bd1dde"


        var prefs: BaseSharedPreferences? = null
//        var accessToken = ""
//        var restAccessToken = ""
        /**
         * Save the anonymous conversation.
         * @param conversation
         */

//        fun getGetAPIHeaders(): MutableMap<String, String> {
//            val headers = HashMap<String, String>()
//            headers["Authorization"] = "Bearer " + BaseApplication.accessToken
//            headers["Prefer"] = "HonorNonIndexedQueriesWarningMayFailRandomly"
//            return headers
//        }
//
//        fun getPostAPIHeaders(): MutableMap<String, String> {
//            val headers = HashMap<String, String>()
//            headers["Authorization"] = BaseApplication.accessToken
//            headers["Content-Type"] = "application/json"
//            return headers
//        }
//
//        val CLIENT_ID = "008373b1-db5c-4ccd-a088-c38341116706"
//        val SCOPES = arrayOf(
//                "https://graph.microsoft.com/User.Read",
//                "https://graph.microsoft.com/Sites.Read.All",
//                "https://graph.microsoft.com/Files.Read.All",
//                "https://graph.microsoft.com/Sites.ReadWrite.All")
//
//        var Authorization_Pre = "Bearer "
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

}
