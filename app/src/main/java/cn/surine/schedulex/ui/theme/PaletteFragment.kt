package cn.surine.schedulex.ui.theme

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.InstanceFactory
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.base.utils.load
import cn.surine.schedulex.data.entity.Palette
import cn.surine.schedulex.ui.course.CourseRepository
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleRepository
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment.Companion.SCHEDULE_ID
import kotlinx.android.synthetic.main.fragment_palette.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 14:19
 */
class PaletteFragment : BaseFragment() {

    lateinit var scheduleViewModel: ScheduleViewModel
    lateinit var courseViewModel: CourseViewModel
    var scheduleId: Int? = -1
    val mDatas = mutableListOf<Palette>()
    override fun layoutId(): Int = R.layout.fragment_palette

    override fun onInit(parent: View?) {
        if (!::scheduleViewModel.isInitialized) {
            scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(arrayOf<Class<*>>(ScheduleRepository::class.java), arrayOf<Any>(ScheduleRepository.abt.instance)))[ScheduleViewModel::class.java]
        }
        if (!::courseViewModel.isInitialized) {
            courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(arrayOf<Class<*>>(CourseRepository::class.java), arrayOf<Any>(CourseRepository.abt.instance)))[CourseViewModel::class.java]
        }

        scheduleId = arguments?.getInt(SCHEDULE_ID)
        if (scheduleId == -1) {
            Toasts.toast("无此课表");
            Navigations.close(this)
            return
        }

        recyclerview.load(LinearLayoutManager(activity()), BaseAdapter(mDatas, R.layout.item_palette, BR.palette)) {
            it.setOnItemClickListener { position ->
                val schedule = scheduleViewModel.getScheduleById(scheduleId!!.toLong())
                schedule.courseThemeId = mDatas[position].id
                scheduleViewModel.updateSchedule(schedule)
                updateCourses(mDatas[position])
                Toasts.toast("更新成功！")
                Navigations.close(this)
            }
        }
        loadData()
    }

    private fun loadData() {
        mDatas.clear()
        mDatas.add(Constants.p1)
        mDatas.add(Constants.p2)
        mDatas.add(Constants.p3)
        mDatas.add(Constants.p4)
        mDatas.add(Constants.p5)
        mDatas.add(Constants.p6)
        mDatas.add(Constants.p7)
        mDatas.add(Constants.p8)
        recyclerview.adapter?.notifyDataSetChanged()
    }

    private fun updateCourses(palette: Palette) {
        val set = palette.colors
        val courseList = courseViewModel.getCourseByScheduleId(scheduleId!!)
        for (i in courseList.indices) {
            val course = courseList[i]
            course.color = set[i % set.size]
            courseViewModel.update(course)
        }
    }
}

