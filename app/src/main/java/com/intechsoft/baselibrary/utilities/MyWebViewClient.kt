package com.intechsoft.baselibrary.utilities

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient


class MyWebViewClient(internal var mDialog: ProgressDialog, internal var webview: WebView) :
    WebViewClient() {

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {

    }

    override fun onPageFinished(view: WebView, url: String) {
        mDialog.dismiss()
        webview.setBackgroundColor(0)
        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onReceivedError(
        view: WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        webview.loadUrl("file:///android_asset/www/myerrorpage.html")
    }
}
