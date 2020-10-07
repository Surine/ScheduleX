package cn.surine.schedulex.ui.schedule_import_pro.page.fetch_init

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.App
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.*
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_data_fetch.file.FileParserFactory
import cn.surine.schedulex.ui.schedule_import_pro.core.ParseDispatcher
import cn.surine.schedulex.ui.schedule_import_pro.data.RemoteUniversity
import cn.surine.schedulex.ui.schedule_import_pro.page.change_school.SelectSchoolFragment
import cn.surine.schedulex.ui.schedule_import_pro.viewmodel.ScheduleDataFetchViewModel
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.fragment_data_fetch_v3.*
import kotlinx.android.synthetic.main.view_file_import.*
import kotlinx.android.synthetic.main.view_jw_import.*
import kotlinx.android.synthetic.main.view_miai_import.*
import kotlinx.android.synthetic.main.view_super_import.*
import java.util.*


/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 13:32
 */
class ScheduleDataFetchFragment : BaseFragment() {

    lateinit var scheduleViewModel: ScheduleViewModel
    lateinit var courseViewModel: CourseViewModel
    lateinit var scheduleDataFetchViewModel: ScheduleDataFetchViewModel
    var scheduleName = ""

    var mRemoteUniversity: RemoteUniversity? = null

    companion object {
        const val JSON_REQUEST_CODE = 1001
    }

    override fun layoutId(): Int = R.layout.fragment_data_fetch_v3

    @SuppressLint("SetTextI18n")
    override fun onInit(parent: View?) {
        VmManager(this).apply {
            scheduleViewModel = vmSchedule
            courseViewModel = vmCourse
            scheduleDataFetchViewModel = vmScheduleFetch
        }
        changeSchool.setOnClickListener {
            Navigations.open(this, R.id.action_dataFetchFragment_to_selectSchoolFragment, arguments)
        }

        val curSchoolNameStr = Prefs.getString(SelectSchoolFragment.CUR_SCHOOL_NAME, "教务导入")
        val curSchoolCode = Prefs.getString(SelectSchoolFragment.CUR_SCHOOL_CODE, "")

        curSchoolName.text = curSchoolNameStr
        curSchoolNameStr?.let {
            scheduleDataFetchViewModel.getUniversityInfo(curSchoolNameStr, curSchoolCode ?: "")
            scheduleDataFetchViewModel.mUniversityInfo.observe(this, Observer {
                mRemoteUniversity = it
                curSchoolInfo.text = "${it.jwSystemName}教务/${it.author}\n已成功导入${it.useTimes}次"
            })
        }


        loginJw.setOnClickListener {
            if (mRemoteUniversity != null) {
                ParseDispatcher.dispatch(this, mRemoteUniversity!!)
            }else{
                Toasts.toast("着呢")
            }
        }


        //超表导入
        fromSuperCn.setOnClickListener {
            // 添加共享元素动画
            val extras = FragmentNavigator.Extras.Builder()
                    .addSharedElement(fromSuperCn, getString(R.string.transition_super))
                    .addSharedElement(super_logo, getString(R.string.transition_super3))
                    .build()
            val directions = ScheduleDataFetchFragmentDirections.actionDataFetchFragmentToSuperLoginFragment()
            Navigation.findNavController(it).navigate(directions, extras)
        }
        skip.setOnClickListener {
            Prefs.save(Constants.CUR_SCHEDULE, scheduleViewModel.addSchedule(scheduleName, 24, 1, Schedule.IMPORT_WAY.ADD))
            Navigations.open(this, R.id.action_dataFetchFragment_to_scheduleFragment)
        }
        help.setOnClickListener {
            Others.openUrl("https://support.qq.com/products/282532")
        }

        fromFile.setOnClickListener {
            RxPermissions(activity()).request((Manifest.permission.READ_EXTERNAL_STORAGE)).subscribe {
                if (it) {
                    showImportDialog()
                } else {
                    Toasts.toast(getString(R.string.permission_is_denied))
                }
            }
        }

        scheduleDataFetchViewModel.getCommon()
        scheduleDataFetchViewModel.mCommons.observe(this, Observer {
            if (it.isShowMiAi) {
                fromMiai.setOnClickListener {
                    val extras = FragmentNavigator.Extras.Builder()
                            .addSharedElement(fromMiai, getString(R.string.transition_mi))
                            .addSharedElement(miai_logo, getString(R.string.transition_mi3))
                            .build()
                    val directions = ScheduleDataFetchFragmentDirections.actionDataFetchFragmentToMiAiInitFragment()
                    Navigation.findNavController(fromMiai).navigate(directions, extras)
                }
            } else {
                Toasts.toast("因官方限制，小爱导入已不可用，感谢支持")
            }
        })

    }

    /**
     * 导入
     */
    private fun importFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        this.startActivityForResult(intent, JSON_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data ?: return
        if (requestCode == JSON_REQUEST_CODE) {
            try {
                val uri = data.data
                loadData(Files.getFilePath(activity(), uri))
            } catch (e: Exception) {
                CrashReport.postCatchedException(e)
            }
        }
    }

    private fun loadData(path: String?) {
        val data = path?.split("\\.") ?: return
        val parser = FileParserFactory.abt.instance.get(data[data.size - 1])
        val list = parser.parse(path)
        if (list.isNullOrEmpty()) {
            Toasts.toast("无数据，请检查资源格式是否正确")
            return
        }
        //开始生成列表
        val id: Long
        val courses = arrayOfNulls<Course>(list.size)
        Prefs.save(Constants.CUR_SCHEDULE, (scheduleViewModel.addSchedule(scheduleName, 24, 1, Schedule.IMPORT_WAY.JSON)).apply {
            id = this
        })
        list.indices.forEach { i ->
            val course = list[i]
            course.scheduleId = id
            course.id = "${course.scheduleId}@${UUID.randomUUID()}${System.currentTimeMillis()}"
            courses[i] = course
        }
        courses.forEach { courseViewModel.insert(it) }
        Toasts.toast(getString(R.string.handle_success))
        Navigations.open(this, R.id.action_dataFetchFragment_to_scheduleFragment)
    }

    private fun showImportDialog() {
        if (Prefs.getBoolean(Constants.FILE_SELECTOR_DONT_TIP, false)) {
            importFile()
            return
        }
        CommonDialogs.getBaseConfig(activity(), Uis.inflate(activity(), R.layout.view_import_file), dialogCall = { view, dialog ->
            val tJson = view.findViewById<TextView>(R.id.t_json)
            val tExcel = view.findViewById<TextView>(R.id.t_excel)
            val tCsv = view.findViewById<TextView>(R.id.t_csv)
            val vBok = view.findViewById<Button>(R.id.b_ok)
            val vDontTip = view.findViewById<CheckBox>(R.id.c_dont_tip)

            val sJson = "${tJson.text}\n点击下载模板"
            tJson.text = Spans.with(sJson).size(13, tJson.text.toString().length, sJson.length).color(App.context.resources.getColor(R.color.blue), tJson.getText().toString().length, sJson.length).toSpannable()

            val sExcel = "${tExcel.text}\n点击下载模板"
            tExcel.text = Spans.with(sExcel).size(13, tExcel.text.toString().length, sExcel.length).color(App.context.resources.getColor(R.color.blue), tExcel.getText().toString().length, sExcel.length).toSpannable()

            val sCsv = "${tCsv.text}\n点击下载模板"
            tCsv.text = Spans.with(sCsv).size(13, tCsv.text.toString().length, sCsv.length).color(App.context.resources.getColor(R.color.blue), tCsv.getText().toString().length, sCsv.length).toSpannable()

            tJson.setOnClickListener {
                Toasts.toast("请在本页面查看Json格式")
                Others.openUrl("https://github.com/surine/ScheduleX")
            }

            tCsv.setOnClickListener {
                Toasts.toast("请在本页面查看Csv格式")
                Others.openUrl("https://github.com/surine/ScheduleX")
            }

            tExcel.setOnClickListener {
                Toasts.toast("难产啦！再等等吧。")
            }

            vBok.setOnClickListener {
                if (vDontTip.isChecked) {
                    Prefs.save(Constants.FILE_SELECTOR_DONT_TIP, true)
                }
                importFile()
                dialog.dismiss()
            }
        })
    }
}