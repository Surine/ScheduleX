package cn.surine.schedulex.ui.schedule_list

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Prefs
import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.base.utils.load
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_schedule_manager.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 12:56
 */
class ScheduleListFragment : BaseFragment() {
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var courseViewModel: CourseViewModel
    private var data = mutableListOf<Schedule>()

    companion object {
        const val FUNCTION_TAG = "function_tag"
        const val CHANGE_SCHEDULE_NAME = 100
        const val CHANGE_WEEK_INFO = 101
        const val CHANGE_BACKGROUND = 102
        const val CHANGE_COURSE_ITEM_HEIGHT = 103
        const val PICK_PHOTO = 1
        const val SCHEDULE_ID = "SCHEDULE_ID"
    }

    override fun layoutId() = R.layout.fragment_schedule_manager

    override fun onInit(parent: View?) {

        VmManager(this).apply {
            scheduleViewModel = vmSchedule
            courseViewModel = vmCourse
        }
        data = scheduleViewModel.schedules

        viewRecycler.load(LinearLayoutManager(activity()), BaseAdapter(data, R.layout.item_schedule_list, BR.schedule)) {
            it.setOnItemClickListener { position ->
                val scheduleId = data[position].roomId.toLong()
                if (Prefs.getLong(Constants.CUR_SCHEDULE, -1L) != scheduleId) {
                    Prefs.save(Constants.CUR_SCHEDULE, scheduleId)
                    it.notifyDataSetChanged()
                    Snackbar.make(addSchedule, R.string.timetable_switched_successfully, Snackbar.LENGTH_SHORT).show()
                }
            }
            it.setOnItemLongClickListener { position ->
                openScheduleSetting(position)
                true
            }

            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener(R.id.more_function) {
                override fun onClick(v: View?, position: Int) {
                    with(PopupMenu(context, v)) {
                        menuInflater.inflate(R.menu.popup_menu_schedule, menu)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.popup_menu_action_1 -> openScheduleSetting(position)
                                R.id.popup_menu_action_2 -> deleteSchedule(position)
                                R.id.popup_menu_action_4 -> exportCourse(position)
                            }
                            false
                        }
                        show()
                    }
                }
            })

            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener((R.id.chip_config_name)) {
                override fun onClick(v: View?, position: Int) {
                    openScheduleSetting(position, CHANGE_SCHEDULE_NAME)
                }
            })

            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener((R.id.chip_config_week)) {
                override fun onClick(v: View?, position: Int) {
                    openScheduleSetting(position, CHANGE_WEEK_INFO)
                }
            })

            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener((R.id.chip_change_background)) {
                override fun onClick(v: View?, position: Int) {
                    openScheduleSetting(position, CHANGE_BACKGROUND)
                }
            })

            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener((R.id.chip_course_item_height)) {
                override fun onClick(v: View?, position: Int) {
                    openScheduleSetting(position, CHANGE_COURSE_ITEM_HEIGHT)
                }
            })

            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener((R.id.chip_more_setting)) {
                override fun onClick(v: View?, position: Int) {
                    openScheduleSetting(position)
                }
            })
        }
        topbar.setFunctionIcon(R.drawable.ic_settings_black_24dp)


        viewRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                addSchedule.apply {
                    if (dy > 0) shrink() else extend()
                }
            }
        })
        topbar.getFunctionView().setOnClickListener {
            Navigations.open(this, R.id.action_ScheduleListFragment_to_aboutFragment)
        }
        addSchedule.setOnClickListener {
            if (scheduleViewModel.schedulesNumber < Constants.MAX_SCHEDULE_LIMIT) {
                Navigations.open(this, R.id.action_ScheduleListFragment_to_dataFetchFragment)
            } else {
                Toasts.toast(getString(R.string.no_permission_to_add))
            }
        }
    }

    private fun openScheduleSetting(position: Int, tag: Int? = null) {
        val bundle = Bundle().apply {
            putInt(SCHEDULE_ID, data[position].roomId)
            tag?.let { putInt(FUNCTION_TAG, it) }
        }
        Navigations.open(this, R.id.action_ScheduleListFragment_to_scheduleConfigFragment, bundle)
    }


    private fun deleteSchedule(position: Int) {
        if (scheduleViewModel.curSchedule.roomId == data[position].roomId) {
            Toasts.toast("不允许删除正在使用的课表")
        } else {
            CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.delete_schedule_dialog_msg), okCall = {
                scheduleViewModel.deleteScheduleById(data[position].roomId.toLong())
                courseViewModel.deleteCourseByScheduleId(data[position].roomId.toLong())
                Toasts.toast(getString(R.string.schedule_is_delete))
                data.removeAt(position)
                viewRecycler.adapter?.notifyDataSetChanged()
            })
        }
    }


    private fun exportCourse(position: Int) {
        NavHostFragment.findNavController(this).navigate(R.id.action_ScheduleListFragment_to_scheduleDataExport, Bundle().apply {
            putInt(SCHEDULE_ID, data[position].roomId)
        })
    }
}