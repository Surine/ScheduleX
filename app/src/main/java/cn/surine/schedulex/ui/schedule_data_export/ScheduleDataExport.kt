package cn.surine.schedulex.ui.schedule_data_export

import android.Manifest
import android.app.ProgressDialog
import android.view.View
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.app_base.hit
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.*
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment.Companion.SCHEDULE_ID
import cn.surine.schedulex.ui.timetable_list.TimeTableViewModel
import com.google.android.material.snackbar.Snackbar
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_date_export.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 10:47
 */
class ScheduleDataExport : BaseFragment() {

    lateinit var scheduleViewModel: ScheduleViewModel
    lateinit var courseViewModel: CourseViewModel
    private lateinit var timeTableViewModel: TimeTableViewModel
    lateinit var schedule: Schedule
    val calendarIds = ArrayList<Long>()
    override fun layoutId(): Int = R.layout.fragment_date_export

    override fun onInit(parent: View?) {
        arguments ?: return
        VmManager(this).apply {
            scheduleViewModel = vmSchedule
            courseViewModel = vmCourse
            timeTableViewModel = vmTimetable
        }
        val scheduleId = requireArguments().getInt(SCHEDULE_ID)
        schedule = scheduleViewModel.getScheduleById(scheduleId.toLong())
        scheduleTitle.text = schedule.name

        exportJson.setOnClickListener {
            RxPermissions(activity()).apply {
                request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe {
                    if (it) {
                        hit("save_json")
                        saveToJson(scheduleId)
                    } else {
                        Toasts.toast(getString(R.string.permission_is_denied))
                    }
                }
            }
        }

        other.setOnLongClickListener {
            RxPermissions(activity()).apply {
                request(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR).subscribe {
                    if (it) {
                        hit("delete_calendar")
                        CoroutineScope(Dispatchers.IO).launch {
                            deleteCourseTask()
                        }
                    } else {
                        Toasts.toast(getString(R.string.permission_is_denied))
                    }
                }
            }
            true
        }
        other.setOnClickListener {
            RxPermissions(activity()).apply {
                request(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR).subscribe {
                    if (it) {
                        hit("add_calendar")
                        val dialog = ProgressDialog(activity()).apply {
                            setMessage("正在导出至日历，请勿退出~ (如需删除，请长按导出按钮)")
                            setTitle("提醒")
                            setCancelable(false)
                            show()
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            addCourseTask(scheduleId, dialog)
                        }
                    } else {
                        Toasts.toast(getString(R.string.permission_is_denied))
                    }
                }
            }
        }

        exportExcel.setOnClickListener { Toasts.toast("暂不支持！") }
    }

    /**
     * 删除操作
     * */
    private suspend fun deleteCourseTask() {
        withContext(Dispatchers.Default) {
            Calendars.removeAllEvent(activity(), activity().packageName)
        }
        withContext(Dispatchers.Main) {
            Toasts.toast("删除成功！")
        }
    }

    /**
     * 添加日程
     * */
    private suspend fun addCourseTask(scheduleId: Int, dialog: ProgressDialog) {
        withContext(Dispatchers.Default) {
            val list: List<Course> = courseViewModel.getCourseByScheduleId(scheduleId)
            val c: Calendar = Calendar.getInstance()
            c.timeInMillis = System.currentTimeMillis()
            for (course in list) {
                //事件名称和提示信息
                val eventTitle = course.coureName
                val addrs = course.teachingBuildingName + course.classroomName
                val eventMsg = addrs + "/" + course.teacherName
                //当前周
                val curSchedule = scheduleViewModel.curSchedule
                val currentWeek = curSchedule.curWeek()
                val termStartTime = Dates.getDate(curSchedule.termStartDate, Dates.yyyyMMdd).time
                for ((index, week) in course.classWeek.withIndex()) {
                    //历史的周不会再添加
                    if (index + 1 < currentWeek || week == '0')
                        continue
                    val dateOffSet = termStartTime + Dates.ONE_DAY * (index * 7 + course.classDay.toInt() - 1)
                    val eventStartTime = dateOffSet + getCourseTime(course.classSessions.toInt(), true)
                    val eventEndTime = dateOffSet + getCourseTime(course.classSessions.toInt() + course.continuingSession.toInt() - 1, false)
                    //insert into calendar
                    val result = Calendars.addCalendarEvent(activity(), eventTitle, eventMsg, eventStartTime, eventEndTime, addr = addrs)
                    if (result != -1L) {
                        calendarIds.add(result)
                    }
                }
            }
        }
        withContext(Dispatchers.Main) {
            dialog.dismiss()
            Toasts.toast("添加成功！")
        }
    }


    /**
     * 获取上课时间
     * */
    private fun getCourseTime(classSessions: Int, start: Boolean): Long {
        val timeTable = timeTableViewModel.getTimTableById(scheduleViewModel.curSchedule.timeTableId)
        timeTable ?: return 0
        val bTimeTable = DataMaps.dataMappingTimeTableToBTimeTable(timeTable)
        if (classSessions > bTimeTable.timeInfoList.size) return 0
        val time = if (start) bTimeTable.timeInfoList[classSessions - 1].startTime else bTimeTable.timeInfoList[classSessions - 1].endTime
        return Dates.getTransformTimeString(time) * 60 * 1000
    }


    private fun saveToJson(scheduleId: Int) {
        val fileName = "${schedule.name}_$scheduleId"
        val courses = courseViewModel.getCourseByScheduleId(scheduleId)
        val targetList = mutableListOf<CourseWrapper>()
        for (i in courses) {
            targetList.add(DataMaps.dataMappingCourse2CourseWrapper(i))
        }
        if (Files.saveAsJson(fileName, Jsons.entityToJson(targetList))) {
            Snackbar.make(exportJson, "保存成功,路径 /Download/$fileName.json", Snackbar.LENGTH_SHORT).show();
        } else {
            Toasts.toast("保存失败！请稍后再试！");
        }
    }


}
