package com.intechsoft.baselibrary.utilities

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.telephony.TelephonyManager
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.intechsoft.baselibrary.R
import com.intechsoft.baselibrary.baseclasses.BaseApplication
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.io.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


open class Utility {

    val deviceName: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else {
                capitalize(manufacturer) + " " + model
            }
        }

    fun openSite(site: String, activity: Activity) {
        var site = site

        try {
            if (!site.contains("http://") && !site.contains("https://")) {
                site = site + "http://" + site
            }
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(site)
            activity.startActivity(i)
        } catch (e: Exception) {

        }

    }

    fun alert(
        message: String,
        activity: Activity,
        onclick: android.content.DialogInterface.OnClickListener
    ) {
        val bld = AlertDialog.Builder(activity)
        bld.setMessage(message)
        bld.setNeutralButton("OK", onclick)
        bld.create().show()
    }

    fun readFromFile(fileName: String, context: Context?): String? {
        try {
            if (context != null) {
                val reader = BufferedReader(
                    InputStreamReader(
                        context.assets.open(fileName),
                        "UTF-8"
                    ) as Reader?
                )

                // do reading, usually loop until end of file reading
                var mLine = reader.readLine()
                var result = mLine
                while (mLine != null) {
                    // process line
                    mLine = reader.readLine()
                    if (mLine != null) {
                        result += mLine
                    }
                }

                reader.close()
                return result
            } else return ""
        } catch (e: IOException) {
            return null
        }

    }

    fun readFile(`is`: InputStream): String {
        Log.e(Globals.TAG, "Reading File")
        val reader = BufferedReader(InputStreamReader(`is`))
        var linecomp = StringBuffer()
        try {
            var line: String

            // line = new String(lineReader.readLine()); // read and ignore the
            line = reader.readLine()
            while (line != null) {
                linecomp = linecomp.append(line)
            }
        } catch (e: Exception) {

        }

        return linecomp.toString()

    }

    /**
     * Send Email
     */
    fun getStorageLocation(context: Activity): String {
        var dir: String?
        try {
            dir = context.filesDir.absolutePath
            var mExternalStorageAvailable = false
            var mExternalStorageWriteable = false
            val state = Environment.getExternalStorageState()

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // We can read and write the media
                mExternalStorageWriteable = true
                mExternalStorageAvailable = mExternalStorageWriteable
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                // We can only read the media
                mExternalStorageAvailable = true
                mExternalStorageWriteable = false
            } else {
                // Something else is wrong. It may be one of many other states,
                // but all we need
                // to know is we can neither read nor write
                mExternalStorageWriteable = false
                mExternalStorageAvailable = mExternalStorageWriteable
            }

            if (mExternalStorageWriteable) {
                dir = context.getExternalFilesDir(null)!!.absolutePath

            } else if (dir == null || dir.contains("null")) {
                dir = "/data/data/" + context.application.packageName + "/files"
            }

        } catch (e: Exception) {
            dir = "/data/data/com.kumait.q8dealapp/files"
            //            Log.e(C.TAG, "An exception was thrown", e);

        }

        if (dir == null || dir.contains("null")) {
            dir = "/data/data/com.kumait.q8dealapp/files"
        }

        //        Log.i("Directory Cor Caching::>", dir);
        return dir
    }

    fun decodeFile(f: File): Bitmap? {
        try {
            // Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(FileInputStream(f), null, o)

            // The new size we want to scale to
            val REQUIRED_SIZE = 200

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2
            }

            // Decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            return BitmapFactory.decodeStream(FileInputStream(f), null, o2)
        } catch (e: Exception) {
            //            Log.e(C.TAG, "An exception was thrown", e);
        }

        return null
    }

    fun getRealPathFromURI(contentUri: Uri, context: Activity): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    /*
     * Get Image Path
     */
    fun getPath(uri: Uri, context: Activity): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.managedQuery(uri, projection, null, null, null)
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else {
            return null
        }
    }

    fun stripWrongContent(str: String, pFirst: String, stuff: String): String {
        var str = str

        try {
            val p = Pattern.compile(pFirst)
            val m = p.matcher(str) // get a matcher object

            if (m.find()) {
                str = m.replaceAll(stuff)
            }
        } catch (e: Exception) {
            //            Log.e(C.TAG, "An exception was thrown", e);
            return str
        }

        return str
    }

    fun setNotify(act: Activity, notif: Boolean?) {
        val prefs = act.getSharedPreferences(Globals.APPNAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("Notify", notif!!)
        editor.commit()
    }

    fun getCachedNotify(act: Activity): Boolean? {
        val prefs = act.getSharedPreferences(Globals.APPNAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("Notify", true)
    }

    fun getCachedNotifyContext(cont: Context): Boolean? {
        val prefs =
            cont.applicationContext.getSharedPreferences(Globals.APPNAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("Notify", true)
    }

    private fun getOperator(context: Context?): String? {
        return try {
            if (context != null) {
                val m = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
                m.networkOperatorName
            } else ""
        } catch (e: Exception) {
            null
        }

    }

    private fun showToastMessage(context: Context?, errorMsg: String) {
        if (context != null) {
            var toast = Toast.makeText(context, errorMsg, Toast.LENGTH_LONG)
            val toastTV = (toast.view as LinearLayout).getChildAt(0) as TextView
            toastTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
            toast.show()
        }
    }

    fun isEmpty(valToCheck: String?, valToReturn: String): String {
        return if (valToCheck != null) {
            if (valToCheck.trim { it <= ' ' }.length > 0) {
                valToCheck
            } else {
                valToReturn
            }
        } else {
            valToReturn
        }
    }

    class getThisScreenShot(
        val act: FragmentActivity,
        val FolderName: String,
        val FileName: String,
        val Msg: String
    ) : AsyncTask<String, Void, Boolean>() {

        override fun doInBackground(vararg params: String): Boolean? {

            try {
                Thread.sleep(1000)
            } catch (e: Exception) {
                var i = 0
                i++
            }

            return true
        }

        override fun onPostExecute(result: Boolean?) {
            takeScreenshot(act, FolderName, FileName, Msg)
        }

        override fun onPreExecute() {}

        override fun onProgressUpdate(
            vararg values: Void
        ) {
        }
    }

    companion object {

        fun isConnected(context: Context?): Boolean {
            if (context != null) {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val ni = cm.activeNetworkInfo
                return ni != null && ni.isConnected
            }
            return false
        }

        fun showToast(context: Context?, msg: String) {
            if (context != null) {
                var toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                val toastTV = (toast.view as LinearLayout).getChildAt(0) as TextView
                toastTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
                toast.show()
            }
        }

        fun showToast(context: Context?, msg: String, editText: EditText) {
            if (context != null) {
                var toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                val toastTV = (toast.view as LinearLayout).getChildAt(0) as TextView
                toastTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
                toast.show()
                editText.error = msg
                editText.requestFocus()
            }
        }

        fun showToast(context: Context?, msg: String, autoCompleteTextView: AutoCompleteTextView) {
            if (context != null) {
                var toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                val toastTV = (toast.view as LinearLayout).getChildAt(0) as TextView
                toastTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
                toast.show()
                autoCompleteTextView.error = msg
                autoCompleteTextView.requestFocus()
            }
        }

        fun showToast(context: Context?, msg: Int) {
            try {
                if (context != null) {
                    var toast = Toast.makeText(context, context.getString(msg), Toast.LENGTH_SHORT)
                    val toastTV = (toast.view as LinearLayout).getChildAt(0) as TextView
                    toastTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
                    toast.show()
                }
            } catch (e: java.lang.Exception) {
            }
        }

        fun capitalize(text: String?): String {
            var retVal = ""
            if (text != null) {
                if (!text.isNullOrEmpty()) {
                    var textList: List<String> = listOf()
                    if (text.contains(" ")) {
                        textList = text.split(" ")
                    }
                    if (textList.isNotEmpty()) {
                        for (s in textList) {
                            val first = s[0]
                            retVal =
                                retVal + Character.toUpperCase(first) + s.substring(1).toLowerCase() + " "
                        }
                    } else {
                        val first = text[0]
                        retVal = Character.toUpperCase(first) + text.substring(1).toLowerCase()
                    }
                }
            }
            return retVal.trim()
        }

//        /**
//         * Convert a byte[] to hexadecimal.
//         *
//         * @param data
//         * @return
//         */
//        private fun convertToHex(data: ByteArray): String {
//            val buf = StringBuilder()
//            for (b in data) {
//                var halfbyte = b.ushr(4) and 0x0F
//                var two_halfs = 0
//                do {
//                    buf.append(if (0 <= halfbyte && halfbyte <= 9) ('0'.toInt() + halfbyte).toChar() else ('a'.toInt() + (halfbyte - 10)).toChar())
//                    halfbyte = b and 0x0F
//                } while (two_halfs++ < 1)
//            }
//            return buf.toString()
//        }

        fun showAlertDialog(alertMessageModel: AlertMessageModel): Dialog? {
            if (alertMessageModel.context != null) {
                val alertDialog = Dialog(alertMessageModel.context, R.style.CustomDialog)
//                alertDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                alertDialog.setCancelable(false)
//                alertDialog?.window?.setLayout(
//                        ConstraintLayout.LayoutParams.MATCH_PARENT,
//                        ConstraintLayout.LayoutParams.MATCH_PARENT
//                )
//                alertDialog?.setContentView(R.layout.warningmsg)
//                val txt_MsgHeader = alertDialog?.findViewById(R.id.txt_Header) as TextView
//                val txt_Msg = alertDialog?.findViewById(R.id.txt_WarningMsg) as TextView
//                val btn_Negative = alertDialog?.findViewById(R.id.btn_Negative) as Button
//                val btn_Positive = alertDialog?.findViewById(R.id.btn_Positive) as Button
//
//                txt_MsgHeader?.text = alertMessageModel.title
//                txt_Msg?.text = alertMessageModel.message
//
//                btn_Negative.setOnClickListener{
//                    alertDialog.dismiss()
//                }
//
//                btn_Positive.setOnClickListener{
//                    if (alertMessageModel.NeedToCloseApp) {
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                    } else {
//                        alertDialog.dismiss();
//                    }
//                }
//
//                alertDialog.show()
//                if (alertMessageModel.playMusic) {
//                    Utility.playsound(alertMessageModel.context, alertMessageModel.musicID)
//                }

                val builder =
                    AlertDialog.Builder(alertMessageModel.context, R.style.AlertDialogTheme)
                builder.setTitle(alertMessageModel.title)
                    .setMessage(alertMessageModel.message)
                    .setCancelable(false)
                    .setPositiveButton(alertMessageModel.PositiveButtonText)
                    { dialog, i ->
                        if (alertMessageModel.NeedToCloseApp) {
                            android.os.Process.killProcess(android.os.Process.myPid())
                        } else {
                            alertDialog.dismiss()
                        }
//                            if (alertMessageModel.title == alertMessageModel.context?.getString(R.string.no_connection)) {
//                                if (!HaveInternetConnection(alertMessageModel.context)) {
//                                    if (alertMessageModel.act != null) {
//                                        alertMessageModel.act?.startActivityForResult(Intent(android.provider.Settings.ACTION_SETTINGS), 101)
//                                    } else {
//                                        alertMessageModel.context?.startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
//                                    }
//                                }
//                            }
                    }
                builder.setNegativeButton(alertMessageModel.NegativeButtonText) { dialog, i ->
                    dialog.cancel()
                }
                val dialog: AlertDialog = builder.create()
                if (alertMessageModel.title == alertMessageModel.context.getString(R.string.no_connection)) {
                    if (!HaveInternetConnection(alertMessageModel.context)) {
                        dialog.show()
                    }
                } else {
                    dialog.show()
                }

                return alertDialog
            }
            return null
        }

        fun HaveInternetConnection(context: Context?): Boolean {
            try {
                if (context != null) {
                    val connectivity =
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    if (connectivity != null) {
                        val info = connectivity.allNetworkInfo
                        if (info != null) {
                            for (i in info.indices) {
                                if (info[i].state == NetworkInfo.State.CONNECTED) {
                                    return true
                                }
                            }
                        }

                    }
                }
                return false
            } catch (e: Exception) {
                return false
            }

        }

        fun toString(`val`: Any, format: String): String {
            try {
                val tempVal = `val`.toString()
                var tempReplace = `val`.toString()
                tempReplace = format.substring(0, format.length - tempReplace.length)
                tempReplace = tempReplace.replace("#", "")
                return tempReplace + tempVal
            } catch (e: Exception) {
                return `val`.toString()
            }

        }

        fun FormatAmountToString(Amount: Double, locale: Locale = Locale.getDefault()): String {
            var MyretVal = ""
            try {
                val nf = NumberFormat.getNumberInstance(locale)
                val AmtToString = nf as DecimalFormat
                AmtToString.applyPattern(("##0.000"))
                MyretVal = AmtToString.format(Amount)
                //            NumberFormat nf = NumberFormat.getNumberInstance(locale);
                //            MyretVal = nf.format(Amount);
            } catch (e: Exception) {
            }

            return MyretVal
        }

        fun FormatString(obj: Int, locale: Locale = Locale.getDefault()): String {
            var MyretVal = ""
            val nf = NumberFormat.getNumberInstance(locale)
            val AmtToString = nf as DecimalFormat
            AmtToString.applyPattern(("0000000000"))
            try {
                MyretVal = AmtToString.format(obj)
            } catch (e: Exception) {
                var i = 0
                i++
            }

            return MyretVal
        }

        fun FormatTime(
            DateTime: String,
            format: String,
            locale: Locale = Locale.getDefault()
        ): String {
            var MyretVal = ""
            if (DateTime.length == 0) {
                val c = Calendar.getInstance()
                val hh = toString(c.get(Calendar.HOUR), "00")
                val mm = toString(c.get(Calendar.MINUTE), "00")
                val ss = toString(c.get(Calendar.SECOND), "00")
                val aa = c.get(Calendar.AM_PM)
                MyretVal = hh + ":" + mm + ":" + ss + " " + if (aa == 1) "PM" else "AM"
            } else {
                try {
                    if (DateTime.contains("T")) {
                        MyretVal = DateTime.substring(11, DateTime.length)
                        val f = SimpleDateFormat("hh:mm:ss", locale)
                        var d: Date? = null
                        d = f.parse(MyretVal)
                        val time = SimpleDateFormat(format, locale)
                        MyretVal = time.format(d)
                    } else {
                        val f = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", locale)
                        var d: Date? = null
                        d = f.parse(DateTime)
                        val time = SimpleDateFormat(format, locale)
                        MyretVal = time.format(d)
                    }
                } catch (e: Exception) {
                    if (MyretVal.trim { it <= ' ' }.length <= 0) {
                        if (DateTime.contains("T")) {
                            MyretVal =
                                DateTime.split("T".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                        } else {
                            MyretVal =
                                DateTime.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                        }
                    }
                }

            }
            return MyretVal
        }

        fun shareMyApp(act: Activity) {
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody =
                "Download KFH Android App From Play Store \n https://play.google.com/store/apps/details?id=com.mpp.kfh"
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MABEN")
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            act.startActivityForResult(Intent.createChooser(sharingIntent, "Share MABEN"), 1)
        }

        fun playsound(mContext: Context?, resid: Int) {
            if (mContext != null) {
                val audio = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING)

                if (currentVolume > 0) {
                    val maxVolume = 50
                    val currVolume = 50

                    val log1 = (Math.log((maxVolume - currVolume).toDouble()) / Math
                        .log(maxVolume.toDouble())).toFloat()

                    val mediaPlayer = MediaPlayer.create(mContext, resid)
                    mediaPlayer.setVolume(0f, 1 - log1)
                    mediaPlayer.setOnCompletionListener { mp ->
                        // TODO Auto-generated method stub
                        mp.release()
                    }
                    mediaPlayer.start()
                }
            }
        }

        fun StartActivity(act: Activity, aIntent: Intent): Boolean {
            try {
                act.startActivity(aIntent)
                return true
            } catch (e: ActivityNotFoundException) {
                return false
            }

        }

        //    public static String FormatDateTime(String DateTime, String Formate) {
        //        String lFormatTemplate = "yyyy-MM-dd'T'hh:mm:ss'Z'";
        //        DateFormat lDateFormat = new SimpleDateFormat(lFormatTemplate, locale);
        //        String lDate = lDateFormat.format(itemValue);
        //
        //        return lDate;
        //    }
        fun FormatDate(format: String, locale: Locale = Locale.getDefault()): String {
            val c = Calendar.getInstance().time as Date
            val df = SimpleDateFormat(format, locale)
            val DateTime = df.format(c) //+ "T00:00:00.000Z"

            var MyretVal = ""
            if (DateTime.length == 0) {
                val c = Calendar.getInstance()
                val yyyy = c.get(Calendar.YEAR)
                val MM = toString(c.get(Calendar.MONTH) + 1, "00")
                val dd = toString(c.get(Calendar.DAY_OF_MONTH), "00")
                MyretVal = "$dd-$MM-$yyyy"
            } else {
                try {
//                        MyretVal = DateTime.substring(0, 10)
                    var d: Date? = DateTime(DateTime, DateTimeZone.forID("Asia/Kuwait")).toDate()
                    val date = SimpleDateFormat(format, locale)
                    MyretVal = date.format(d)
                } catch (e: Exception) {
                    if (MyretVal.trim { it <= ' ' }.length <= 0) {
                        if (DateTime.contains("T")) {
                            MyretVal =
                                DateTime.split("T".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                        } else {
                            MyretVal =
                                DateTime.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                        }
                    }
                }

            }
            return MyretVal
        }

        fun FormatDate(
            DateTime: String,
            format: String,
            locale: Locale = Locale.getDefault()
        ): String {
            var MyretVal = ""
            if (DateTime.length == 0) {
                val c = Calendar.getInstance()
                val yyyy = c.get(Calendar.YEAR)
                val MM = toString(c.get(Calendar.MONTH) + 1, "00")
                val dd = toString(c.get(Calendar.DAY_OF_MONTH), "00")
                MyretVal = "$dd-$MM-$yyyy"
            } else {
                try {
//                        MyretVal = DateTime.substring(0, 10)
                    var d: Date? = DateTime(DateTime, DateTimeZone.forID("Asia/Kuwait")).toDate()
                    val date = SimpleDateFormat(format, locale)
                    MyretVal = date.format(d)
                } catch (e: Exception) {
                    if (MyretVal.trim { it <= ' ' }.length <= 0) {
                        if (DateTime.contains("T")) {
                            MyretVal =
                                DateTime.split("T".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                        } else {
                            MyretVal =
                                DateTime.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                        }
                    }
                }

            }
            return MyretVal
        }

        fun FormatTodayDate(format: String, locale: Locale = Locale.getDefault()): String {
            val c = Calendar.getInstance().time as Date
            val df = SimpleDateFormat(format, locale)
            val DateTime = df.format(c) //+ "T00:00:00.000Z"

            var MyretVal = ""
            if (DateTime.length == 0) {
                val c = Calendar.getInstance()
                val yyyy = c.get(Calendar.YEAR)
                val MM = toString(c.get(Calendar.MONTH) + 1, "00")
                val dd = toString(c.get(Calendar.DAY_OF_MONTH), "00")
                MyretVal = "$dd-$MM-$yyyy"
            } else {
                try {
//                        MyretVal = DateTime.substring(0, 10)
                    var d: Date? = DateTime(DateTime, DateTimeZone.forID("Asia/Kuwait")).toDate()
                    val date = SimpleDateFormat(format, locale)
                    MyretVal = date.format(d)
                } catch (e: Exception) {
                    if (MyretVal.trim { it <= ' ' }.length <= 0) {
                        if (DateTime.contains("T")) {
                            MyretVal =
                                DateTime.split("T".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                        } else {
                            MyretVal =
                                DateTime.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                        }
                    }
                }

            }
            return MyretVal
        }

        fun getISOTime(
            dateTime: String,
            format: String,
            locale: Locale = Locale.getDefault()
        ): String {

            var d = DateTime(dateTime, DateTimeZone.forID("Asia/Kuwait"))
            val frmat: SimpleDateFormat = SimpleDateFormat(format, locale)
            val isoDate = frmat.format(d.toLocalDateTime().toDate())
            return isoDate
        }

        fun isNullOrEmpty(text: String): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(text, Html.FROM_HTML_OPTION_USE_CSS_COLORS).trim().toString().trim()
                    .isNullOrEmpty()
            } else {
                Html.fromHtml(text).trim().toString().trim().isNullOrEmpty()
            }
        }

        fun getTextFromHtml(text: String): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(text, Html.FROM_HTML_OPTION_USE_CSS_COLORS).trim().toString()
                    .trim()
            } else {
                Html.fromHtml(text).trim().toString().trim()
            }.replace("<br>", "\\n").trim()
        }

        fun EmailValidate(hex: String): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            pattern = Pattern.compile(EMAIL_PATTERN)
            matcher = pattern.matcher(hex)
            return matcher.matches()
        }

        private fun takeScreenshot(
            act: Activity,
            FolderName: String,
            FileName: String,
            Msg: String
        ) {
            var view: View? = null
            try {
                view = act.window.decorView
                view.isDrawingCacheEnabled = true
                view.buildDrawingCache(true)
                val Width = view.width
                val Height = view.height

                val rect = Rect()
                view.getWindowVisibleDisplayFrame(rect)
                val bitmap = Bitmap.createBitmap(view.drawingCache, 0, 0, Width, Height)
                val canvas = Canvas(bitmap)
                view.draw(canvas)
                //
                try {
                    //                    OutputStream fOut = null;
                    val fullPath =
                        Environment.getExternalStorageDirectory().absolutePath + "/" + FolderName
                    val Dir = File(fullPath)
                    if (!Dir.exists()) {
                        Dir.mkdir()
                    }
                    val file = File(
                        fullPath,
                        FileName.replace(" ", "_").replace("/", "_").trim({ it <= ' ' })
                    )
                    if (file.exists()) {
                        file.delete()
                    }

                    val fOut = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 95, fOut)
                    view.isDrawingCacheEnabled = false
                    Toast.makeText(act, Msg, Toast.LENGTH_LONG).show()
                    scanMedia(act, file)
                } catch (ex: Exception) {
                    //                Toast.makeText(act, ex.toString(), Toast.LENGTH_LONG).show();
                } finally {
                    view.destroyDrawingCache()
                    view.isDrawingCacheEnabled = false
                }
            } catch (e: Exception) {
                //            Toast.makeText(act, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

        fun scanMedia(context: Context, file: File) {
            try {
                val uri = Uri.fromFile(file)
                val scanFileIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
                context.sendBroadcast(scanFileIntent)
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }

        }

        fun deleteCache(context: Context?) {
            try {
                if (context != null) {
                    val dir = context.cacheDir
                    if (dir != null && dir.isDirectory) {
                        deleteDir(dir)
                    }
                }
            } catch (e: Exception) {
            }

        }

        fun deleteDir(dir: File?): Boolean {
            try {
                if (dir != null) {
                    if (dir.isDirectory) {
                        val children = dir.listFiles()
                        if (children != null && children.isNotEmpty()) {
                            for (i in children.indices) {
                                if (children[i].isDirectory) {
                                    deleteDir(children[i])
                                } else {
                                    val success = children[i].delete()
                                    if (!success) {
                                        return false
                                    }
                                }
                            }
                        }
                    } else {
                        return dir.delete()
                    }
                }
            } catch (e: Exception) {
            }
            return false
        }

        fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
            var width = image.width
            var height = image.height

            val bitmapRatio = width.toFloat() / height.toFloat()
            if (bitmapRatio > 0) {
                width = maxSize
                height = (width / bitmapRatio).toInt()
            } else {
                height = maxSize
                width = (height * bitmapRatio).toInt()
            }
            return Bitmap.createScaledBitmap(image, width, height, true)
        }

        private fun ClearHistory() {
            try {
//                if (Globals.DataList.Customer_info.size() > 0) {
//                    Globals.DataList.Customer_info.clear()
//                }
            } catch (e: Exception) {
            }

        }

        fun setDynamicHeight(mListView: ListView) {
            val mListAdapter = mListView.adapter
                ?: // when adapter is null
                return
            var height = 0
            val desiredWidth =
                View.MeasureSpec.makeMeasureSpec(mListView.width, View.MeasureSpec.UNSPECIFIED)
            for (i in 0 until mListAdapter.count) {
                val listItem = mListAdapter.getView(i, null, mListView)
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                height += listItem.measuredHeight
            }
            val params = mListView.layoutParams
            params.height = height + mListView.dividerHeight * mListAdapter.count
            mListView.layoutParams = params
            mListView.requestLayout()
        }

        fun getPixelValue(context: Context?, dimenId: Int): Int {
            return if (context != null) {
                val resources = context.resources
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dimenId.toFloat(),
                    resources.displayMetrics
                ).toInt()
            } else 0
        }

        fun responseMessage(x: Int): String {
            return when (x) {
                101 -> "Invalid Login"
                102 -> "Successful Login"
                103 -> "Invalid Header Request"
                104 -> "Unauthorized User Role"
                105 -> "Already Exists"
                106 -> "Login Session Expired"
                107 -> "Invalid Login Role"
                110 -> "Success"
                111 -> "Failed"
                else -> {
                    "Failed"
                }
            }
        }

        fun loadSettings(context: Context?) {
            if (context != null) {
                try {
                    val language = BaseApplication.prefs?.language as String
                    updateDisplayLocale(context, language)

                } catch (ex: Exception) {
                }
            }
        }

        fun changeLanguage(context: Context?): String {
            return if (context != null) {
                val locale = Locale.getDefault() as Locale
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
                context.resources.updateConfiguration(
                    config,
                    context.resources.displayMetrics
                )
                locale.toString()
            } else ""
        }

        private fun updateDisplayLocale(context: Context?, preferredLang: String) {
            if (context != null) {
                val locale = Locale(preferredLang)
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
            }
        }

        fun setLanguagePreference(context: Context?, preferredLang: String) {
            if (context != null) {
                BaseApplication.prefs?.language = preferredLang
                updateDisplayLocale(context, preferredLang)
            }
        }

    }

}
