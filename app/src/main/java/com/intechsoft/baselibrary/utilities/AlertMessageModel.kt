package com.intechsoft.baselibrary.utilities

import android.app.Activity
import android.content.Context

class AlertMessageModel(
    val act: Activity? = null,
    val context: Context?,
    val title: String,
    val message: String,
    val Icon: Int = android.R.drawable.ic_dialog_alert,
    val NeedToCloseApp: Boolean = false,
    val playMusic: Boolean = false,
    val musicID: Int = 0,
    val PositiveButtonText: String = "",
    val NegativeButtonText: String = ""
)