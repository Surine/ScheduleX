package cn.surine.schedulex.ui.schedule_data_fetch

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.*
import androidx.lifecycle.ViewModelProviders
import cn.surine.schedulex.R
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.InstanceFactory
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Prefs
import cn.surine.schedulex.base.utils.Toasts.toast
import cn.surine.schedulex.base.utils.bitCount
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.ui.course.CourseRepository
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleRepository
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_import_pro.core.ParseDispatcher.IS_HTML
import cn.surine.schedulex.ui.schedule_import_pro.core.ParseDispatcher.JW_SYSTEM
import cn.surine.schedulex.ui.schedule_import_pro.core.ParseDispatcher.JW_URL
import cn.surine.schedulex.ui.schedule_import_pro.core.ParseDispatcher.OP_INFO
import cn.surine.schedulex.ui.schedule_import_pro.data.CourseWrapper
import cn.surine.schedulex.ui.schedule_init.ScheduleInitFragment
import com.peanut.sdk.miuidialog.MIUIDialog
import kotlinx.android.synthetic.main.fragment_third_fetch.*
import java.net.URLDecoder
import java.util.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/21 22:25
 */
class ScheduleThirdFetchFragment : BaseFragment() {
    private lateinit var opInfo: String
    lateinit var scheduleViewModel: ScheduleViewModel
    lateinit var courseViewModel: CourseViewModel
    var helperUrl = ""
    var helperType = ""
    override fun layoutId(): Int = R.layout.fragment_third_fetch
    override fun onInit(parent: View?) {
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(arrayOf<Class<*>>(ScheduleRepository::class.java), arrayOf<Any>(ScheduleRepository.abt.instance)))[ScheduleViewModel::class.java]
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(arrayOf<Class<*>>(CourseRepository::class.java), arrayOf<Any>(CourseRepository.abt.instance)))[CourseViewModel::class.java]
        val url = arguments?.get(JW_URL).toString()
        val type = arguments?.get(JW_SYSTEM).toString()
        opInfo = arguments?.get(OP_INFO).toString()
        var isHtml = arguments?.getBoolean(IS_HTML) ?: false
        helperUrl = url
        helperType = type
        loadWebViewConfig()
        loadTip()
        thirdPageWebView.loadUrl(URLDecoder.decode(url))
        addressBox.setText(URLDecoder.decode(url))
        imgGo.setOnClickListener {
            if (addressBox.text.toString().isNotEmpty()) {
                thirdPageWebView.loadUrl(addressBox.text.toString())
            }
        }
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
    }

    private fun loadTip() {
        MIUIDialog(activity()).show {
            title(text = "教务导入")
            message(text = if (opInfo.isEmpty()) "暂无说明" else opInfo)
            positiveButton(text = "确定") { this.cancel() }
            negativeButton(text = "取消") { this.cancel() }
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
//        thirdPageWebView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 7.0; Nexus 5X Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/54.0.2840.85 Mobile Safari/537.36"
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
//            val engineFunction = when (system) {
////                JwInfo.NEW_ZF -> ::newZenFang
//                PKU -> ::PKU
////                NCUT -> ::NCUT
////                ZF -> ::ZF
////                SW -> ::SW
////                OLD_QZ -> ::OLD_QZ
////                SWUST -> ::SWUST
//                else -> ::PKU
//            }
//            Parser().parse(engine = engineFunction, html = if (testHtml.isEmpty()) html else testHtml) { list, e ->
//                activity().runOnUiThread {
//                    if (list != null) {
//                        parseData(list)
//                    }
//                    if (e != null) {
//                        MIUIDialog(activity()).show {
//                            title(text = "解析失败")
//                            message(text = "<html>请尝试定位到课表页面再进行解析，如果还是无法导入，请您加QQ群<a href='https://www.baidu.com'>686976115</a>进行反馈；同时，如果您有适配您的学校的想法也可以直接在群里联系开发者。<br><b>如果学校已经适配，但依然解析失败，可能是源码获取异常，请尝试反馈</b></html>") {
//                                html {
//                                    Others.openUrl("https://jq.qq.com/?_wv=1027&k=SmyNDbv6")
//                                }
//                            }
//                            positiveButton(text = "确定") { this.cancel() }
//                            negativeButton(text = "反馈源码") {
//                                RxPermissions(activity()).apply {
//                                    request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe {
//                                        if (it) {
//                                            val name = "${system}系统源码-${System.currentTimeMillis()}"
//                                            if (Files.save(name, html, "html")) {
//                                                toastLong("源码保存成功,路径 /Download/${name}，请尝试发送给开发者进行反馈~")
//                                            } else {
//                                                toast("保存失败！")
//                                            }
//                                        } else {
//                                            toast(getString(R.string.permission_is_denied))
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

    //解析coursewrapper列表
    private fun parseData(list: List<CourseWrapper>?) {
        if (list == null) {
            toast("未检测到课程！")
            return
        } else {
            val scheduleId = scheduleViewModel.addSchedule(arguments?.getString(ScheduleInitFragment.SCHEDULE_NAME)
                    ?: "UnKnow", 24, 1, Schedule.IMPORT_WAY.JW)
            val targetList = mutableListOf<Course>()
            list.forEach {
                val course = Course()
                course.scheduleId = scheduleId
                course.id = StringBuilder().run {
                    append(course.scheduleId).append("@").append(UUID.randomUUID()).append(System.currentTimeMillis())
                    toString()
                }
                course.coureName = it.name
                course.teacherName = it.teacher
                course.teachingBuildingName = it.position
                course.classDay = it.day.toString()
                course.color = Constants.COLOR_1[Random().nextInt(Constants.COLOR_1.size)]
                course.classSessions = it.sectionStart.toString()
                var maxSession = it.sectionContinue
                if (maxSession > Constants.MAX_SESSION) {
                    maxSession = Constants.MAX_SESSION
                }
                course.continuingSession = maxSession.toString()
                course.classWeek = it.week.bitCount(Constants.STAND_WEEK)
                targetList.add(course)
            }
            courseViewModel.saveCourseByDb(targetList, scheduleId)
            toast("导入成功")
            Prefs.save(Constants.CUR_SCHEDULE, scheduleId)
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

    val testHtml = """"""
}