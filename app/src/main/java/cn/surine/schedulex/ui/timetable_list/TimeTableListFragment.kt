package cn.surine.schedulex.ui.timetable_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.InstanceFactory
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.base.utils.load
import cn.surine.schedulex.data.entity.TimeTable
import cn.surine.schedulex.ui.schedule.ScheduleRepository
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_config.ScheduleConfigFragment
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment.Companion.SCHEDULE_ID
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs
import kotlinx.android.synthetic.main.fragment_timetable_list.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 10:24
 */
class TimeTableListFragment : BaseFragment() {
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var timetableViewModel: TimeTableViewModel
    val data = ArrayList<TimeTable>()
    override fun layoutId(): Int = R.layout.fragment_timetable_list

    companion object {
        const val TIMETABLE_ID = "TIMETABLE_ID"
    }

    @SuppressLint("StringFormatMatches")
    override fun onInit(parent: View?) {
        if (!::scheduleViewModel.isInitialized) {
            scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(arrayOf<Class<*>>(ScheduleRepository::class.java), arrayOf<Any>(ScheduleRepository.abt.instance)))[ScheduleViewModel::class.java]
        }
        if (!::timetableViewModel.isInitialized) {
            timetableViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(arrayOf<Class<*>>(TimeTableRepository::class.java), arrayOf<Any>(TimeTableRepository)))[TimeTableViewModel::class.java]
        }
        if (arguments == null || requireArguments().getLong(SCHEDULE_ID, -1) == -1L) {
            Toasts.toast(getString(R.string.app_error))
            Navigations.close(this)
        }
        data.apply {
            clear()
            addAll(timetableViewModel.allTimeTables)
        }
        viewRecycler.load(LinearLayoutManager(activity()), BaseAdapter<TimeTable>(data, R.layout.item_time_table_list, BR.timetable)) {
            it.setOnItemClickListener { position ->
                val timeTable = data[position]
                val scheduleId = requireArguments().getLong(SCHEDULE_ID, -1)
                val schedule = scheduleViewModel.getScheduleById(scheduleId)
                if (schedule != null) {
                    if (schedule.maxSession > timeTable.sessionNum) {
                        Toasts.toast(getString(R.string.session_is_not_match, timeTable.sessionNum, schedule.maxSession))
                    } else {
                        schedule.timeTableId = timeTable.roomId.toLong()
                        scheduleViewModel.updateSchedule(schedule)
                        Toasts.toast(getString(R.string.timetable_is_selected))
                        Navigations.close(this)
                    }
                }
            }
            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener(R.id.more_function) {
                override fun onClick(v: View?, position: Int) {
                    PopupMenu(context, v).apply {
                        menuInflater.inflate(R.menu.popup_menu_timetable, menu)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.popup_menu_action_1 -> Navigations.open(this@TimeTableListFragment, R.id.action_timeTableListFragment_to_addTimeTableFragment, Bundle().apply { putInt(TIMETABLE_ID, data.get(position).roomId) })
                                R.id.popup_menu_action_2 -> deleteTimeTable(position)
                            }
                            false
                        }
                        show()
                    }
                }
            })
        }
        viewRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                addTimeTable.apply {
                    if (dy > 0) shrink() else extend()
                }
            }
        })
        addTimeTable.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_timeTableListFragment_to_addTimeTableFragment))
    }

    private fun deleteTimeTable(position: Int) {
        if (data[position].roomId == 0) {
            Toasts.toast(getString(R.string.is_ban_to_delete))
            return
        }
        CommonDialogs.miuiDialog(activity(),getString(R.string.warning), getString(R.string.are_you_sure_to_delete_timetable),okCall = {
            timetableViewModel.deleteTimeTableById(data[position].roomId)
            Toasts.toast(getString(R.string.time_table_has_been_deleted))
            data.removeAt(position)
            viewRecycler.adapter!!.notifyDataSetChanged()
        })
//        CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.are_you_sure_to_delete_timetable), okCall = {
//
//        }).show()
    }
}
