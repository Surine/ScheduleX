package cn.surine.schedulex.ui.third.wtu

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import cn.surine.schedulex.R
import kotlinx.android.synthetic.main.activity_wtu_course.*

class WtuCourseActivity : AppCompatActivity() {
    val web = "http://ehall.wtu.edu.cn/new/index.html"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wtu_course)
        webView.loadUrl(web)
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.domStorageEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.setGeolocationEnabled(true)
        webView.settings.javaScriptCanOpenWindowsAutomatically=true
//        webView.settings.setSupportMultipleWindows(true)
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // 拦截 url 跳转,在里边添加点击链接跳转或者操作
                Log.v("url",url)
                if (url.contains("http://jwglxt.wtu.edu.cn/kbcx/xskbcx_cxXskbcxIndex.html"))
                    ;
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                // 在结束加载网页时会回调

                // 获取页面内容
                view.loadUrl("javascript:window.java_obj.showSource("
                        + "document.getElementsByTagName('html')[0].innerHTML);")

                // 获取解析<meta name="share-description" content="获取到的值">
                view.loadUrl("javascript:window.java_obj.showDescription("
                        + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')"
                        + ");")
                super.onPageFinished(view, url)
            }

        }
    }
}