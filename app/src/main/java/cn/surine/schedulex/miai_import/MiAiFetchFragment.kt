package cn.surine.schedulex.miai_import

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.webkit.*
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Jsons
import cn.surine.schedulex.base.utils.Navigations.open
import cn.surine.schedulex.base.utils.Prefs.save
import cn.surine.schedulex.base.utils.Toasts.toast
import cn.surine.schedulex.base.utils.bitCount
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import kotlinx.android.synthetic.main.view_webview.*
import okhttp3.*
import java.io.IOException
import java.net.URLDecoder
import java.util.*


/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/6/20 15:03
 */
class MiAiFetchFragment : BaseFragment() {
    lateinit var scheduleViewModel: ScheduleViewModel
    lateinit var courseViewModel: CourseViewModel
    override fun layoutId(): Int = R.layout.view_webview

    override fun onInit(parent: View?) {
        VmManager(this).apply {
            scheduleViewModel = this.vmSchedule
            courseViewModel = this.vmCourse
        }
        val url = "https://i.ai.mi.com/h5/precache/ai-schedule/#/import_schedule?token=9ee57bb5-43c8-4f71-8c05-7f4868b8272e&expireTime=1599483791000"
        web.loadUrl(url)
        webSetting()
    }

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    private fun webSetting() {
        web.settings.javaScriptEnabled = true
        web.settings.setSupportZoom(true)
        web.settings.builtInZoomControls = true
        web.settings.domStorageEnabled = true
        web.settings.useWideViewPort = true
        web.settings.setGeolocationEnabled(true)
        web.settings.loadWithOverviewMode = true
        web.settings.displayZoomControls = false
        web.settings.textZoom = 100
        web.settings.javaScriptCanOpenWindowsAutomatically = true
        web.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val text = request?.url.toString()
                if (text.startsWith("voiceassist:")) {
                    val parse = text.split("?")[1].removePrefix("url=")
                    val parseDecode = URLDecoder.decode(parse, "UTF-8")
                    web.loadUrl(parseDecode)
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                val text = request?.url.toString()
                if (text.startsWith("https://i.ai.mi.com/course/courseInfo?")) {
                    loadUrl(text)
                }
                return super.shouldInterceptRequest(view, request)
            }
        }
    }

    private fun loadUrl(url: String) {
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val text = response.body?.string() ?: ""
                if (text.isNotEmpty()) {
                    val data = Jsons.parseJsonWithGson(text, MiAiBean::class.java)
                    parseCourse("小爱", data.data)
                }
            }

        })
    }


    private fun parseCourse(str: String, miAiCourseInfo: MiAiCourseInfo) {
        val courseList = mutableListOf<Course>()
        val scheduleId = scheduleViewModel.addSchedule(str, miAiCourseInfo.totalWeek, miAiCourseInfo.presentWeek, Schedule.IMPORT_WAY.MI_AI)
        save(Constants.CUR_SCHEDULE, scheduleId)
        for (info in miAiCourseInfo.courseInfos) {
            val course = Course()
            course.scheduleId = scheduleId
            val sb = StringBuilder()
            sb.append(course.scheduleId)
            sb.append("@")
            sb.append(UUID.randomUUID())
            sb.append(System.currentTimeMillis())
            course.id = sb.toString()
            course.coureName = info.name
            course.teacherName = info.teacher
            course.teachingBuildingName = info.position
            course.classDay = info.day.toString()
            course.color = Constants.COLOR_1[Random().nextInt(Constants.COLOR_1.size)]
            //TODO 这里周处理还有问题
            course.classSessions = info.sections.first().section.toString()
            var i = info.sections.last().section - info.sections.first().section + 1
            if (i > Constants.STAND_SESSION) {
                i = Constants.STAND_SESSION
            }
            course.continuingSession = i.toString()
            course.classWeek = info.weeks.bitCount(miAiCourseInfo.totalWeek)
            courseList.add(course)
        }
        courseViewModel.saveCourseByDb(courseList, scheduleId)
        toast("导入成功")
        open(this, R.id.action_miAiFetchFragment_to_scheduleFragment)
    }

}