package cn.surine.schedulex.ui.schedule_data_export

import android.Manifest
import android.view.View
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.*
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_config.ScheduleConfigFragment
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment.Companion.SCHEDULE_ID
import com.google.android.material.snackbar.Snackbar
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_date_export.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 10:47
 */
class ScheduleDataExport : BaseFragment() {

    lateinit var scheduleViewModel: ScheduleViewModel
    lateinit var courseViewModel: CourseViewModel
    lateinit var schedule: Schedule
    override fun layoutId(): Int = R.layout.fragment_date_export

    override fun onInit(parent: View?) {
        arguments ?: return
        VmManager(this).apply {
            scheduleViewModel = vmSchedule
            courseViewModel = vmCourse
        }
        val scheduleId = requireArguments().getInt(SCHEDULE_ID)
        schedule = scheduleViewModel.getScheduleById(scheduleId.toLong())
        scheduleTitle.text = schedule.name

        exportIcs.setOnClickListener {
            RxPermissions(activity()).apply {
                request(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR).subscribe {
                    if (it) {
                        exportIcs()
                    } else {
                        Toasts.toast(getString(R.string.permission_is_denied));
                    }
                }
            }
        }

        exportJson.setOnClickListener {
            RxPermissions(activity()).apply {
                request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe {
                    if (it) {
                        saveToJson(scheduleId);
                    } else {
                        Toasts.toast(getString(R.string.permission_is_denied));
                    }
                }
            }
        }

        exportExcel.setOnClickListener { Toasts.toast("暂不支持！") }
        other.setOnClickListener { Toasts.toast("欢迎加群提出意见！") }
    }


    private fun saveToJson(scheduleId: Int) {
        val fileName = "${schedule.name}_$scheduleId"
        if (Files.saveAsJson(fileName, Jsons.entityToJson(courseViewModel.getCourseByScheduleId(scheduleId)))) {
            Snackbar.make(exportJson, "保存成功,路径 /Download/$fileName.json", Snackbar.LENGTH_SHORT).show();
        } else {
            Toasts.toast("保存失败！请稍后再试！");
        }
    }

    private fun getCalendarTime(week:Int, classDay:String):Long {
        return Dates.getDate(schedule.termStartDate,Dates.yyyyMMdd).time + (7 * (week - 1) + Integer.parseInt(classDay) - 1) * Dates.ONE_DAY
    }

    private fun exportIcs(){
        val data = courseViewModel.getCourseByScheduleId(schedule.roomId)
        (data.indices).forEach {
            val course = data[it]
            val weeks= ArrayList<Int>()
            (course.classWeek.indices).forEach {
                j->
                if(course.classWeek[j] == '1'){
                    weeks.add(j + 1)
                }
            }
            (weeks.indices).forEach {
                //只记录比当前周大的数据
                k->
                if(weeks[k] >= schedule.curWeek()){
                    val startTime = getCalendarTime(weeks[k],course.classDay)
                    val endTime = startTime + 45 * 60 * 1000
                    CalendarProviders.addEvent(activity(),course.coureName,course.teachingBuildingName + course.classroomName,startTime,endTime)
                    CalendarProviders.deleteCalendarEvent(activity(),course.coureName)
                }
            }
        }
    }
}
