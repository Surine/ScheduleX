package cn.surine.schedulex.ui.schedule_data_fetch

import android.annotation.SuppressLint
import android.util.Log
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
import cn.surine.schedulex.third_parse.CourseWrapper
import cn.surine.schedulex.third_parse.Parser
import cn.surine.schedulex.third_parse.ParserEngine.default
import cn.surine.schedulex.third_parse.ParserEngine.newZenFang
import cn.surine.schedulex.ui.course.CourseRepository
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleRepository
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_init.ScheduleInitFragment
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

    lateinit var scheduleViewModel: ScheduleViewModel
    lateinit var courseViewModel: CourseViewModel

    override fun layoutId(): Int = R.layout.fragment_third_fetch
    override fun onInit(parent: View?) {
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(arrayOf<Class<*>>(ScheduleRepository::class.java), arrayOf<Any>(ScheduleRepository.abt.instance)))[ScheduleViewModel::class.java]
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(arrayOf<Class<*>>(CourseRepository::class.java), arrayOf<Any>(CourseRepository.abt.instance)))[CourseViewModel::class.java]
        var url: String = arguments?.get(ScheduleSchoolListFragment.URL).toString()
        val type = arguments?.get(ScheduleSchoolListFragment.TYPE).toString()
        loadWebViewConfig()
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
        thirdPageWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val text = request?.url.toString()
                addressBox.setText(text)
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
        fun showSource(html: String,system: String) {
            val engineFunction = when(system){
                "newZenFang"-> ::newZenFang
                else -> ::default
            }
            Parser().parse(engine = engineFunction, html = html) { list ->
                parseData(list)
            }
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
                var maxSession = it.sectionStart + it.sectionContinue
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
}