package com.intechsoft.baselibrary.baseclasses

import android.content.Context
import android.content.SharedPreferences
import com.intechsoft.baselibrary.utilities.Globals

open class BaseSharedPreferences(context: Context) {
    private val PREFS_FILENAME = "com.mpp.prefs"
    private val LANG = "lang"
    private val LANG_API = "lang_api"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var language: String
        get() {
            return prefs.getString(LANG, Globals.English_Language).toString()
        }
        set(value) {
            if (value == Globals.English_Language) {
                prefs.edit().putString(LANG_API, Globals.English_API_Language).apply()
            } else {
                prefs.edit().putString(LANG_API, Globals.Arabic_API_Language).apply()
            }
            prefs.edit().putString(LANG, value).apply()
        }

    var api_language: String = Globals.English_API_Language
        get() {
            return prefs.getString(LANG_API, Globals.English_API_Language).toString()
        }

}