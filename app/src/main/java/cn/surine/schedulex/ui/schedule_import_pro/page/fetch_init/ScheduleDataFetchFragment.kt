package cn.surine.schedulex.ui.schedule_import_pro.page.fetch_init

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.LinearLayoutManager
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.DATA
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.app_base.hit
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.App
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.controller.BaseViewModel
import cn.surine.schedulex.base.utils.*
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.data.helper.ParserManager
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_import_pro.core.FileParserDispatcher
import cn.surine.schedulex.ui.schedule_import_pro.core.ParseDispatcher
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.model.RemoteUniversity
import cn.surine.schedulex.ui.schedule_import_pro.page.change_school.SelectSchoolFragment
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseData
import cn.surine.schedulex.ui.schedule_import_pro.viewmodel.ScheduleDataFetchViewModel
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs
import com.google.android.material.snackbar.Snackbar
import com.peanut.sdk.miuidialog.MIUIDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.fragment_data_fetch_v3.*
import kotlinx.android.synthetic.main.view_common_jw_system.view.*
import kotlinx.android.synthetic.main.view_file_import.*
import kotlinx.android.synthetic.main.view_jw_import.*
import kotlinx.android.synthetic.main.view_miai_import.*
import kotlinx.android.synthetic.main.view_super_import.*
import java.io.File


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

        textView20.setOnClickListener {

        }

        //更换学校
        changeSchool.setOnClickListener {
            Navigations.open(this, R.id.action_dataFetchFragment_to_selectSchoolFragment)
        }

        //加载数据
        val curSchoolNameStr = Prefs.getString(SelectSchoolFragment.CUR_SCHOOL_NAME, "")!!
        val curSchoolCode = Prefs.getString(SelectSchoolFragment.CUR_SCHOOL_CODE, "")!!
        if (curSchoolNameStr.isNotEmpty()) {
            curSchoolName.text = curSchoolNameStr
            curSchoolNameStr.let {
                scheduleDataFetchViewModel.getUniversityInfo(curSchoolNameStr, curSchoolCode)
                scheduleDataFetchViewModel.mUniversityInfo.observe(this, Observer {
                    mRemoteUniversity = it
                    curSchoolInfo.text = "${it.jwSystemName}教务/${it.author}\n已成功导入${it.useTimes}次"
                    tryCommonJw.hide()
                })
            }
            //元素的控制
            scheduleDataFetchViewModel.loadUniversityStatus.observe(this, Observer {
                when (it) {
                    BaseViewModel.START_LOAD, BaseViewModel.LOADING -> {
                        loginJw.setOnClickListener {
                            Snackbar.make(parent!!, "没有啥数据哦,客官请稍后~", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    ScheduleDataFetchViewModel.LOAD_FAIL_NULL -> {
                        val str = "咱还没适配您学校哦！ 如果您愿意帮助适配，点击此卡片查看教程~"
                        curSchoolInfo.text = str
                        tryCommonJw.show()
                        tryCommonJw.setOnClickListener {
                            showCommonJwDialog()
                        }
                        loginJw.setOnClickListener {
                            hit("adapter")
                            Others.openUrl("https://support.qq.com/products/282532/faqs/79948")
                        }
                    }
                    ScheduleDataFetchViewModel.LOAD_FAIL_MAINTENANCE -> {
                        val str = "您的学校解析器正在维护中哦，请过一段时间再试~"
                        curSchoolInfo.text = str
                        tryCommonJw.hide()
                        loginJw.setOnLongClickListener {
                            ParseDispatcher.dispatch(this, mRemoteUniversity!!)
                            true
                        }
                    }
                    ScheduleDataFetchViewModel.LOAD_FAIL_VERSION_OLD -> {
                        val str = "新版本已经适配了您的学校~ 请升级或者关注未来发版消息"
                        curSchoolInfo.text = str
                        tryCommonJw.hide()
                    }
                    BaseViewModel.LOAD_SUCCESS -> {
                        loginJw.setOnClickListener {
                            ParseDispatcher.dispatch(this, mRemoteUniversity!!)
                        }
                    }
                }
            })
        } else {
            loginJw.setOnClickListener {
                Snackbar.make(it, "请先点击卡片右上角选择您的学校哦~", Snackbar.LENGTH_SHORT).show()
            }
        }

        //超表导入
        fromSuperCn.setOnClickListener {
            hit("from_super_cn")
            // 添加共享元素动画
            val extras = FragmentNavigator.Extras.Builder()
                    .addSharedElement(fromSuperCn, getString(R.string.transition_super))
                    .addSharedElement(super_logo, getString(R.string.transition_super3))
                    .build()
            val directions = ScheduleDataFetchFragmentDirections.actionDataFetchFragmentToSuperLoginFragment()
            Navigation.findNavController(it).navigate(directions, extras)
        }
        skip.setOnClickListener {
            hit("skip")
            Prefs.save(Constants.CUR_SCHEDULE, scheduleViewModel.addSchedule(scheduleName, 24, 1, Schedule.IMPORT_WAY.ADD))
            Navigations.open(this, R.id.action_dataFetchFragment_to_scheduleFragment)
        }
        help.setOnClickListener {
            hit("help")
            Others.openUrl("https://support.qq.com/products/282532")
        }

        fromFile.setOnClickListener {
            hit("from_file")
            showImportDialog()
        }

        scheduleDataFetchViewModel.getCommon()
        scheduleDataFetchViewModel.mCommons.observe(this, Observer {
            if (it.isShowMiAi) {
                fromMiai.setOnClickListener {
                    hit("from_mi")
                    val extras = FragmentNavigator.Extras.Builder()
//                            .addSharedElement(fromMiai, getString(R.string.transition_mi))
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

    private fun showCommonJwDialog() {
        val data = ParseData.commonJwData
        MIUIDialog(activity()).show {
            title(text = "通用系统")
            customView(R.layout.view_common_jw_system) { view ->
                view.commonJwList.load(LinearLayoutManager(activity), BaseAdapter(data, R.layout.item_common_jw, BR.university)) {
                    it.setOnItemClickListener { position ->
                        val curSelectSystem = data[position]
                        hit("common_system", func = DATA, map = hashMapOf("system" to curSelectSystem.name))
                        val mRemoteUniversity = RemoteUniversity(
                                code = curSelectSystem.code,
                                name = curSelectSystem.name,
                                importType = curSelectSystem.isHtmlSystem(),
                                jwSystemName = curSelectSystem.name,
                                jwSystem = curSelectSystem.jwSystem
                        )
                        ParseDispatcher.dispatch(fragment(), mRemoteUniversity)
                        cancel()
                    }
                }
            }
        }
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
        path ?: return
        val list = FileParserDispatcher.get(path).parse(path)
        if (list.isNullOrEmpty()) {
            Toasts.toast("无数据，请检查资源格式是否正确")
            return
        }
        //开始生成列表
        val id: Long
        Prefs.save(Constants.CUR_SCHEDULE, (scheduleViewModel.addSchedule(scheduleName, 24, 1, Schedule.IMPORT_WAY.JSON)).apply {
            id = this
        })
        ParserManager.wrapper2course(list, id).forEach {
            courseViewModel.insert(it)
        }
        hit("json_success", func = DATA)
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
                RxPermissions(activity()).request((Manifest.permission.READ_EXTERNAL_STORAGE)).subscribe { status ->
                    if (status) {
                        hit("download_json_demo")
                        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/json模板.json")
                        file.writeText(Jsons.entityToJson(mutableListOf(CourseWrapper().apply {
                            name = "课程名"
                            teacher = "教师名"
                            position = "上课地点"
                            sectionStart = 1
                            sectionContinue = 2
                            day = 1
                            week = mutableListOf(1, 3, 5, 7, 9)
                        })))
                        Snackbar.make(it, "保存成功,路径 /Download/Json模板.json", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Toasts.toast(getString(R.string.permission_is_denied))
                    }
                }
            }

            tCsv.setOnClickListener {
                RxPermissions(activity()).request((Manifest.permission.READ_EXTERNAL_STORAGE)).subscribe { status ->
                    if (status) {
                        hit("download_csv_demo")
                        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/csv模板备份.csv")
                        file.writeText(StringBuilder("name,teacher,position,sectionStart,sectionContinue,day,week").append("\n").append("课程名,教师,位置,1,2,1,1 2 3 4 5").toString())
                        Snackbar.make(it, "保存成功,路径 /Download/csv模板备份.csv", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Toasts.toast(getString(R.string.permission_is_denied))
                    }
                }
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

    override fun onBackPressed() {
        if (arguments != null) {
            activity().finish()
        } else {
            super.onBackPressed()
        }
    }
}