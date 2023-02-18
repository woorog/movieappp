package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewAssetLoader.AssetsPathHandler
import androidx.webkit.WebViewClientCompat

//webview 구현
class WebviewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val movieurl=intent.getStringExtra("movieurl")

        Log.d("app", movieurl.toString())

        // 추가

        var webView: WebView = findViewById(R.id.webview)

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", AssetsPathHandler(this))
            .build()

        //http https 사용을위해 WebViewAssetLoader사용

        webView.webViewClient = object : WebViewClientCompat() {
            @RequiresApi(21)
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(request.url)
            }

            // for API < 21
            override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(Uri.parse(url))
            }
        }

        val webViewSettings = webView.settings
// Setting this off for security. Off by default for SDK versions >= 16.
        webViewSettings.allowFileAccessFromFileURLs = false
// Off by default, deprecated for SDK versions >= 30.
        webViewSettings.allowUniversalAccessFromFileURLs = false

        webViewSettings.allowFileAccess = false
        webViewSettings.allowContentAccess = false

        webView.loadUrl(movieurl.toString())

    }


}