package cn.surine.schedulex.ui.third.wtu

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import cn.surine.schedulex.R
import cn.surine.schedulex.ui.third.wtu.ParserEngine.newZenFang
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
        webView.settings.loadWithOverviewMode = true
        webView.settings.displayZoomControls = false
        webView.addJavascriptInterface(InJavaScriptLocalObj(), "local_obj")
        webView.settings.javaScriptCanOpenWindowsAutomatically=true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.settings.userAgentString = webView.settings.userAgentString.replace("Mobile", "eliboM").replace("Android", "diordnA")
        webView.webViewClient = object : WebViewClient() {}
        webView.webChromeClient = object:WebChromeClient(){}
        floatingActionButton.setOnClickListener {
            val js = "javascript:var ifrs=document.getElementsByTagName(\"iframe\");" +
                    "var iframeContent=\"\";" +
                    "for(var i=0;i<ifrs.length;i++){" +
                    "iframeContent=iframeContent+ifrs[i].contentDocument.body.parentElement.outerHTML;" +
                    "}\n" +
                    "var frs=document.getElementsByTagName(\"frame\");" +
                    "var frameContent=\"\";" +
                    "for(var i=0;i<frs.length;i++){" +
                    "frameContent=frameContent+frs[i].contentDocument.body.parentElement.outerHTML;" +
                    "}\n" +
                    "window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML + iframeContent + frameContent);"
            webView.loadUrl(js)
        }
    }

    internal inner class InJavaScriptLocalObj {

        @JavascriptInterface
        fun showSource(html: String) {
            Parser().parse(engine = ::newZenFang,html = html)
        }

    }

    override fun onDestroy() {
        webView.webViewClient = null
        webView.webChromeClient = null
        webView.clearCache(true)
        webView.clearHistory()
        webView.removeAllViews()
        webView.destroy()
        super.onDestroy()
    }
}