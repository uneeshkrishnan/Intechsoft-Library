package com.intechsoft.baselibrary.baseclasses

import androidx.annotation.StringRes

class BaseHeader(
    val enableDrawer: Boolean,
    @StringRes
    val screenName: Int,
    val enableLanguage: Boolean,
    val enableBack: Boolean = true
)