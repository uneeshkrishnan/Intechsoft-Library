package com.intechsoft.baselibrary.activities

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.intechsoft.baselibrary.R


open class StartActivity : AppCompatActivity() {

    var enableBack = false

    protected open fun loadFragmentContainerActivity() {

    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        this.setContentView(R.layout.startactivity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            CheckLatestVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "")
        } else {
            CheckLatestVersion().execute("")
        }
    }


    private inner class CheckLatestVersion : AsyncTask<String, Void, Boolean>() {

        override fun doInBackground(vararg params: String): Boolean? {

            var retVal = true
            try {
                ClearHistory()
                ReadAndSetPublicVariables()
//                retVal = Utility.HaveInternetConnection(baseContext)
//                val ws = WSFetchformobileapp(context)
//                retVal = ws.CheckLatestVersion()
            } catch (e: Exception) {
            }

            return retVal
        }

        override fun onPostExecute(result: Boolean) {
//            when {
//                result -> {
            loadFragmentContainerActivity()
//                }
//                Utility.HaveInternetConnection(baseContext) -> {
//                    //                val appPackageName = packageName // getPackageName() from Context or Activity object
//                    //                Utility.showAlertDialog(
//                    //                        baseContext, "Update Available", "Please update your app from Google Play Store.",
//                    //                        R.drawable.ic_dialog_alert, false, false, 0
//                    //                )
//                    //                try {
//                    //                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
//                    //                } catch (e: ActivityNotFoundException) {
//                    //                    startActivity(
//                    //                            Intent(
//                    //                                    Intent.ACTION_VIEW,
//                    //                                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
//                    //                            )
//                    //                    )
//                    //                }
//                    //
//                    //                finish()
//                }
//                else -> {
//                    //                Utility.showAlertDialog(context, Utility.getStringVal(context, R.string.noconnection), Utility.getStringVal(context, R.string.noconnectionmsg), R.drawable.ic_dialog_alert, true, false, 0)
//                }
//            }
        }

        override fun onPreExecute() {}

        override fun onProgressUpdate(vararg values: Void) {}

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (enableBack) {
                finish()
            } else {
                return enableBack
            }
        }
        return true
    }

    protected open fun ClearHistory() {}

    protected open fun ReadAndSetPublicVariables() {}

}
