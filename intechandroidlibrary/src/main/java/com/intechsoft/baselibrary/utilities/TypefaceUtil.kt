package com.intechsoft.baselibrary.utilities

import android.content.Context
import android.graphics.Typeface


object TypefaceUtil {

    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     * @param context to work with assets
     * @param defaultFontNameToOverride for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     */
    fun overrideFont(
        context: Context,
        defaultFontNameToOverride: String,
        customFontFileNameInAssets: String
    ) {
        try {
            val customFontTypeface =
                Typeface.createFromAsset(context.assets, customFontFileNameInAssets)

            val defaultFontTypefaceField =
                Typeface::class.java.getDeclaredField(defaultFontNameToOverride)
            defaultFontTypefaceField.isAccessible = true
            defaultFontTypefaceField.set(null, customFontTypeface)
        } catch (e: Exception) {

        }

    }
}
