package cn.surine.schedulex.ui.schedule_data_fetch

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.third_parse.Parser
import cn.surine.schedulex.third_parse.ParserEngine.default
import cn.surine.schedulex.third_parse.ParserEngine.newZenFang
import kotlinx.android.synthetic.main.fragment_third_fetch.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/21 22:25
 */
class ScheduleThirdFetchFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_third_fetch
    override fun onInit(parent: View?) {
        val url = arguments?.get(ScheduleSchoolListFragment.URL).toString()
        val type = arguments?.get(ScheduleSchoolListFragment.TYPE).toString()
        loadWebViewConfig()
        thirdPageWebView.loadUrl(url)
        importThirdHtml.setOnClickListener {
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
                    "window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML + iframeContent + frameContent,'$type');"
            thirdPageWebView.loadUrl(js)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebViewConfig() {
        thirdPageWebView.settings.javaScriptEnabled = true
        thirdPageWebView.settings.setSupportZoom(true)
        thirdPageWebView.settings.builtInZoomControls = true
        thirdPageWebView.settings.domStorageEnabled = true
        thirdPageWebView.settings.useWideViewPort = true
        thirdPageWebView.settings.setGeolocationEnabled(true)
        thirdPageWebView.settings.loadWithOverviewMode = true
        thirdPageWebView.settings.displayZoomControls = false
        thirdPageWebView.addJavascriptInterface(InJavaScriptLocalObj(), "local_obj")
        thirdPageWebView.settings.javaScriptCanOpenWindowsAutomatically = true
        thirdPageWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        thirdPageWebView.settings.userAgentString = thirdPageWebView.settings.userAgentString.replace("Mobile", "Web").replace("Android", "MacOs")
        thirdPageWebView.webViewClient = object : WebViewClient() {}
        thirdPageWebView.webChromeClient = object : WebChromeClient() {}
    }

    internal inner class InJavaScriptLocalObj {
        @JavascriptInterface
        fun showSource(html: String,system: String) {
            val engineFunction = when(system){
                "newZenFang"-> ::newZenFang
                else -> ::default
            }
            val list = Parser().parse(engine = engineFunction,html = html)//list<CourseWrapper>
        }
    }

    override fun onDestroyView() {
        thirdPageWebView.webViewClient = null
        thirdPageWebView.webChromeClient = null
        thirdPageWebView.clearCache(true)
        thirdPageWebView.clearHistory()
        thirdPageWebView.removeAllViews()
        thirdPageWebView.destroy()
        super.onDestroyView()
    }
}