package jp.co.cyberagent.dojo2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val myWebView = findViewById<WebView>(R.id.webViewId)
        myWebView.setWebViewClient(WebViewClient())

        val url = intent.getStringExtra("Url")//QRcodeActivityで読み込んだQRのデータを受け取る

        //val url = "https://www.google.com"//これをgitとtwitterにする
        myWebView.loadUrl(url)

    }
}
