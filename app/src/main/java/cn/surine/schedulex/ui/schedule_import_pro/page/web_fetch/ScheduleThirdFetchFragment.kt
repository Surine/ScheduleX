package cn.surine.schedulex.ui.schedule_import_pro.page.web_fetch

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.*
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.IMPORT_FAIL
import cn.surine.schedulex.app_base.UNIVERSITY_IMPORT_SUCCESS
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.app_base.hit
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Files
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Others
import cn.surine.schedulex.base.utils.Prefs
import cn.surine.schedulex.base.utils.Toasts.toast
import cn.surine.schedulex.base.utils.Toasts.toastLong
import cn.surine.schedulex.data.helper.ParserManager
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_import_pro.core.JwParserDispatcher
import cn.surine.schedulex.ui.schedule_import_pro.core.ParseDispatcher.IS_HTML
import cn.surine.schedulex.ui.schedule_import_pro.core.ParseDispatcher.UNIVERSITY
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.model.RemoteUniversity
import cn.surine.schedulex.ui.schedule_import_pro.page.change_school.SelectSchoolFragment
import cn.surine.schedulex.ui.schedule_import_pro.viewmodel.ScheduleDataFetchViewModel
import cn.surine.ui_lib.setting
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.peanut.sdk.miuidialog.MIUIDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.fragment_third_fetch.*
import kotlinx.android.synthetic.main.view_dsl_setting.*
import java.net.URLDecoder

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/21 22:25
 */
class ScheduleThirdFetchFragment : BaseFragment() {
    companion object {
        const val FOR_HTML = 1001
    }

    private var localHtml: String? = null
    private var isHtml: Boolean = false
    private lateinit var mUniversity: RemoteUniversity
    private lateinit var opInfo: String
    lateinit var scheduleViewModel: ScheduleViewModel
    lateinit var courseViewModel: CourseViewModel
    lateinit var dataFetchViewModel: ScheduleDataFetchViewModel
    var helperUrl = ""
    var helperType = ""
    lateinit var webviewBackup: WebView
    override fun layoutId(): Int = R.layout.fragment_third_fetch
    @SuppressLint("SetTextI18n")
    override fun onInit(parent: View?) {
        VmManager(this).apply {
            scheduleViewModel = vmSchedule
            courseViewModel = vmCourse
            dataFetchViewModel = vmScheduleFetch
        }

        mUniversity = arguments?.get(UNIVERSITY) as RemoteUniversity
        val url = mUniversity.jwUrl
        val type = mUniversity.jwSystem
        opInfo = mUniversity.opInfo
        isHtml = arguments?.getBoolean(IS_HTML) ?: false
        helperUrl = url
        helperType = type
        webviewBackup = thirdPageWebView
        loadWebViewConfig()
        thirdPageWebView.postDelayed({ loadTip() }, 500)
        thirdPageWebView.loadUrl(URLDecoder.decode(url))
        addressBox.setText(URLDecoder.decode(url))
        imgGo.setOnClickListener {
            if (addressBox.text.toString().isNotEmpty()) {
                thirdPageWebView.loadUrl(addressBox.text.toString())
            }
        }
        parseName.text = "${mUniversity.jwSystemName}解析器"
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
            toast("开始解析，请稍后~")
            thirdPageWebView.loadUrl(js)
        }

        tool.setOnClickListener {
            MaterialDialog(activity(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                customView(R.layout.view_dsl_setting)
                setting(rootView) {
                    group {
                        switchItem("设置UA", openSubTitle = "Android UA", closeSubTitle = "桌面 UA", initValue = false, tag = "ua") { _, isChecked ->
                            if (isChecked) {
                                webviewBackup.settings.userAgentString = "Mozilla/5.0 (Linux; Android 7.0; Nexus 5X Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/54.0.2840.85 Mobile Safari/537.36"
                            } else {
                                webviewBackup.settings.userAgentString = "Mozilla/5.0 (Linux; Windows 10; Nexus 5X Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/54.0.2840.85 Mobile Safari/537.36"
                            }
                            webviewBackup.reload()
                        }
                        item("显示提示弹窗", "可以多次查看导入教程") {
                            showTipDialog()
                        }
                    }
                }
            }
        }
    }

    private fun loadTip() {
        if (isHtml) {
            MIUIDialog(activity()).show {
                title(text = "教务导入")
                message(text = "<html>非常抱歉，当前浏览器无法兼容您的教务系统访问。因此您无法继续使用教务自动导入。不过您仍可以通过以下几种方式尝试：<br>" +
                        "<br>1.使用超级课程表导入<br> " +
                        "<br>2.使用网页源码（html）导入，可参照<a href='#'>适配教程</a>中第一步获取源代码文件</html>，然后重新从本页面选择文件，系统会加载本文件，您不必在意显示的内容，直接点击导入按钮即可~") {
                    html {
                        Others.openUrl("https://support.qq.com/products/282532/faqs/79948")
                    }
                }
                positiveButton(text = "选择文件") {
                    RxPermissions(activity()).request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe {
                        if (it) {
                            hit("html_jw_import")
                            importFile()
                        } else {
                            toast("请授予读写文件权限")
                        }
                    }
                }
                negativeButton(text = "取消") { this.cancel() }
            }
        } else {
            showTipDialog()
        }
    }

    private fun showTipDialog() {
        MIUIDialog(activity()).show {
            title(text = "教务导入")
            message(text = if (opInfo.isEmpty()) "1.首先登录账号并定位到课表页面\n2.然后点击右下角导入按钮导入课程" else opInfo)
            positiveButton(text = "确定") { this.cancel() }
            negativeButton(text = "取消") { this.cancel() }
        }
    }


    /**
     * 导入
     */
    private fun importFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        this.startActivityForResult(intent, FOR_HTML)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data ?: return
        if (requestCode == FOR_HTML) {
            try {
                val uri = data.data
                localHtml = Files.getFileContent(Files.getFilePath(activity(), uri))
                thirdPageWebView.loadData(localHtml, "text/html; charset=UTF-8", null)
                toast("加载成功，您可以直接点击导入按钮。")
            } catch (e: Exception) {
                toast("加载失败:${e.message}")
                CrashReport.postCatchedException(e)
            }
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
        thirdPageWebView.settings.textZoom = 100
        thirdPageWebView.addJavascriptInterface(InJavaScriptLocalObj(), "local_obj")
        thirdPageWebView.settings.javaScriptCanOpenWindowsAutomatically = true
        thirdPageWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        if (Prefs.getBoolean("ua", false)) {
            thirdPageWebView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 7.0; Nexus 5X Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/54.0.2840.85 Mobile Safari/537.36"
        } else {
            thirdPageWebView.settings.userAgentString = "Mozilla/5.0 (Linux; Windows 10; Nexus 5X Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/54.0.2840.85 Mobile Safari/537.36"
        }
        thirdPageWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val text = request?.url.toString()
                addressBox.setText(text)
                helperUrl = text
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        thirdPageWebView.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && thirdPageWebView.canGoBack()) {
                    thirdPageWebView.goBack()
                    return@setOnKeyListener true
                }
            }
            false
        }
        thirdPageWebView.webChromeClient = object : WebChromeClient() {}
        imgBack.setOnClickListener {
            if (thirdPageWebView.canGoBack()) {
                thirdPageWebView.goBack()
            }
        }
        addressBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (addressBox.text.toString().isNotEmpty()) {
                    thirdPageWebView.loadUrl(addressBox.text.toString())
                }
            }
            false
        }
    }

    internal inner class InJavaScriptLocalObj {
        @JavascriptInterface
        fun showSource(html: String, system: String) {
            JwParserDispatcher.parse(system, if (localHtml != null) localHtml!! else html) { list, e ->
                activity().runOnUiThread {
                    if (list != null) {
                        parseData(list)
                    }
                    if (e != null) {
                        hit(IMPORT_FAIL, "data" to "name:${mUniversity.name},jwSystemName:${mUniversity.jwSystemName},jwSystem:${mUniversity.jwSystem},inputUrl:${mUniversity.jwUrl}")
                        MIUIDialog(activity()).show {
                            title(text = "解析失败")
                            message(text = "<html>请尝试定位到课表页面再进行解析，如果还是无法导入，请您加QQ群<a href='https://www.baidu.com'>686976115</a>进行反馈；同时，如果您有适配您的学校的想法也可以直接在群里联系开发者。<br><b>如果学校已经适配，但依然解析失败，可能是源码获取异常，请尝试反馈</b></html>") {
                                html {
                                    Others.openUrl("https://jq.qq.com/?_wv=1027&k=SmyNDbv6")
                                }
                            }
                            positiveButton(text = "确定") { this.cancel() }
                            negativeButton(text = "反馈源码") {
                                RxPermissions(activity()).apply {
                                    request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe {
                                        if (it) {
                                            val name = "${system}系统源码-${System.currentTimeMillis()}"
                                            if (Files.save(name, html, "html")) {
                                                toastLong("源码保存成功,路径 /Download/${name}，请尝试发送给开发者进行反馈~")
                                            } else {
                                                toast("保存失败！")
                                            }
                                        } else {
                                            toast(getString(R.string.permission_is_denied))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //解析coursewrapper列表
    private fun parseData(list: List<CourseWrapper>?) {
        if (list == null) {
            toast("未检测到课程！")
            return
        } else {
            val scheduleId = scheduleViewModel.addSchedule("我的课程表", 24, 1, 1)
            val targetList = ParserManager.wrapper2course(list, scheduleId)
            courseViewModel.saveCourseByDb(targetList, scheduleId)
            dataFetchViewModel.uploadFetchSuccess(mUniversity)
            toast("导入成功")
            Prefs.save(Constants.CUR_SCHEDULE, scheduleId)
            if (list.isNotEmpty()) {
                hit(UNIVERSITY_IMPORT_SUCCESS, "data" to "name:${Prefs.getString(SelectSchoolFragment.CUR_SCHOOL_NAME, "*" + mUniversity.name)},jw:${mUniversity.jwSystem},url:${addressBox.text.toString()}")
            }
            Navigations.open(this, R.id.action_scheduleThirdFetchFragment_to_scheduleFragment)
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
