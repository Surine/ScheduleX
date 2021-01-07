package cn.surine.schedulex.app_base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cn.surine.schedulex.base.utils.InstanceFactory
import cn.surine.schedulex.ui.course.CourseRepository
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.login.LoginRepository
import cn.surine.schedulex.ui.login.LoginViewModel
import cn.surine.schedulex.ui.schedule.ScheduleRepository
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_data_fetch.AdapterListRepository
import cn.surine.schedulex.ui.schedule_data_fetch.AdapterListViewModel
import cn.surine.schedulex.ui.timer.TimerRepository
import cn.surine.schedulex.ui.timer.TimerViewModel
import cn.surine.schedulex.ui.timetable_list.TimeTableRepository
import cn.surine.schedulex.ui.timetable_list.TimeTableViewModel

/**
 * Intro：
 * 不想写这些样板代码 (ಥ﹏ಥ) 测试
 * @author sunliwei
 * @date 2020/6/26 10:50
 */
class VmManager(private val fragment: Fragment) {
    //课表vm
    val vmSchedule by lazy { ViewModelProviders.of(fragment, InstanceFactory.getInstance(arrayOf<Class<*>>(ScheduleRepository::class.java), arrayOf<Any>(ScheduleRepository.abt.instance)))[ScheduleViewModel::class.java] }
    //时间表vm
    val vmTimetable by lazy { ViewModelProviders.of(fragment, InstanceFactory.getInstance(arrayOf<Class<*>>(TimeTableRepository::class.java), arrayOf<Any>(TimeTableRepository)))[TimeTableViewModel::class.java] }
    //课程vm
    val vmCourse by lazy { ViewModelProviders.of(fragment, InstanceFactory.getInstance(arrayOf<Class<*>>(CourseRepository::class.java), arrayOf<Any>(CourseRepository.abt.instance)))[CourseViewModel::class.java] }
    //时间信息vm
    val vmTimer by lazy { ViewModelProviders.of(fragment, InstanceFactory.getInstance(arrayOf<Class<*>>(TimerRepository::class.java), arrayOf<Any>(TimerRepository)))[TimerViewModel::class.java] }
    //登录vm
    val vmLogin by lazy { ViewModelProviders.of(fragment, InstanceFactory.getInstance(arrayOf<Class<*>>(LoginRepository::class.java), arrayOf<Any>(LoginRepository)))[LoginViewModel::class.java] }
    //适配列表vm
    val vmAdapterList by lazy { ViewModelProviders.of(fragment, InstanceFactory.getInstance(arrayOf<Class<*>>(AdapterListRepository::class.java), arrayOf<Any>(AdapterListRepository)))[AdapterListViewModel::class.java] }
}