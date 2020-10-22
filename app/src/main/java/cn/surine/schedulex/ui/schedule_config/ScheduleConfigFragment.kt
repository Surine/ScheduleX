package cn.surine.schedulex.ui.schedule_config

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseBindingFragment
import cn.surine.schedulex.base.utils.*
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.databinding.FragmentScheduleConfigBinding
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment.Companion.CHANGE_BACKGROUND
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment.Companion.CHANGE_COURSE_ITEM_HEIGHT
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment.Companion.CHANGE_SCHEDULE_NAME
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment.Companion.CHANGE_WEEK_INFO
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment.Companion.PICK_PHOTO
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment.Companion.SCHEDULE_ID
import cn.surine.schedulex.ui.timetable_list.TimeTableViewModel
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.peanut.sdk.miuidialog.MIUIDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_schedule_config.*
import kotlinx.android.synthetic.main.view_single_slider_ui.view.*
import java.io.File

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 16:28
 */
class ScheduleConfigFragment : BaseBindingFragment<FragmentScheduleConfigBinding>() {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var courseViewModel: CourseViewModel
    private lateinit var timetableViewModel: TimeTableViewModel

    private var scheduleId: Int = -1
    private lateinit var schedule: Schedule
    private var mSettingItemTag: Int = -1

    override fun layoutId(): Int = R.layout.fragment_schedule_config
    override fun onInit(t: FragmentScheduleConfigBinding) {
        VmManager(this).apply {
            scheduleViewModel = vmSchedule
            courseViewModel = vmCourse
            timetableViewModel = vmTimetable
        }
        scheduleId = arguments?.getInt(SCHEDULE_ID) ?: -1
        if (scheduleId.toLong() == Prefs.getLong(Constants.CUR_SCHEDULE, -1)) {
            deleteSchedule.visibility = View.GONE
        }
        schedule = scheduleViewModel.getScheduleById(scheduleId.toLong())
        t.data = schedule
        //捷径
        mSettingItemTag = arguments?.getInt(ScheduleListFragment.FUNCTION_TAG, -1) ?: -1
        if (mSettingItemTag != -1) {
            when (mSettingItemTag) {
                CHANGE_WEEK_INFO -> showTimeConfigDialog()
                CHANGE_COURSE_ITEM_HEIGHT -> showItemHeightDialog()
                CHANGE_BACKGROUND -> chooseBackgroundPicture()
                CHANGE_SCHEDULE_NAME -> modifyScheduleName()
            }
            mSettingItemTag = -1
        }


        //删除
        deleteSchedule.setOnClickListener {
            CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.delete_schedule_dialog_msg), okCall = {
                scheduleViewModel.deleteScheduleById(scheduleId.toLong())
                courseViewModel.deleteCourseByScheduleId(scheduleId.toLong())
                Toasts.toast(getString(R.string.schedule_is_delete))
                Navigations.close(this@ScheduleConfigFragment)
            })
        }

        //课表名
        scheduleNameItem.setOnClickListener { modifyScheduleName() }
        //周信息
        scheduleWeekInfoItem.setOnClickListener { showTimeConfigDialog() }
        //时间表
        scheduleTimeTableItem.setOnClickListener {
            Navigations.open(this, R.id.action_scheduleConfigFragment_to_timeTableListFragment, Bundle().apply {
                putLong(SCHEDULE_ID, scheduleId.toLong())
            })
        }
        timeTableSubtitle.text = timetableViewModel.getTimTableById(schedule.timeTableId)?.name
                ?: "No Data"
        //背景图
        scheduleBackgroundItem.setOnClickListener { chooseBackgroundPicture() }
        scheduleBackgroundItem.setOnLongClickListener {
            scheduleViewModel.updateSchedule(schedule.apply { imageUrl = "" })
            true
        }
        if (schedule.imageUrl != null && schedule.imageUrl!!.isNotEmpty()) {
            Glide.with(activity()).load(File(schedule.imageUrl)).into(backgroundPic)
        }
        //颜色适配
        switchs.isChecked = schedule.lightText
        schedulePaletteItem.setOnClickListener {
            schedule.lightText = !schedule.lightText
            scheduleViewModel.updateSchedule(schedule)
            switchs.isChecked = schedule.lightText
            paletteColorSubTitle.setText(if (schedule.lightText) R.string.white_txt else R.string.black_txt)
        }
        //设置是否展示周末开关
        showWeekSwitchs.isChecked = schedule.isShowWeekend
        scheduleShowWeekItem.setOnClickListener {
            schedule.isShowWeekend = !schedule.isShowWeekend
            scheduleViewModel.updateSchedule(schedule)
            showWeekSwitchs.isChecked = schedule.isShowWeekend
            showWeekSubTitle.setText(if (schedule.isShowWeekend) R.string.show_weekend else R.string.not_show_weekend)
        }
        //打开透明度配置窗口
        scheduleCourseAlphaItem.setOnClickListener { showCourseItemAlphaDialog() }
        //配置最大节次
        scheduleMaxSessionItem.setOnClickListener { showMaxSessionDialog() }
        //配置课表最大高度
        scheduleCourseItemHeightItem.setOnClickListener { showItemHeightDialog() }
        //是否显示时间表
        showTimesSwitch.isChecked = schedule.isShowTime
        t.scheduleShowTimeItem.setOnClickListener {
            schedule.isShowTime = !schedule.isShowTime
            scheduleViewModel.updateSchedule(schedule)
            showTimesSwitch.isChecked = schedule.isShowTime
            scheduleShowTimeItemSubTitle.setText(if (schedule.isShowTime) R.string.show_time else R.string.not_show_timetable)
        }
        //课程卡片主题
        changeCourseCardUi.setOnClickListener {
            Navigations.open(this, R.id.action_scheduleConfigFragment_to_paletteFragment, Bundle().apply {
                putInt(SCHEDULE_ID, scheduleId)
            })
        }
        changeCharLimit.setOnClickListener {
            showChangeCharLimitDialog()
        }
        //导出
        export.setOnClickListener {
            Navigations.open(this, R.id.action_scheduleConfigFragment_to_scheduleDataExport, Bundle().apply {
                putInt(SCHEDULE_ID, scheduleId)
            })
        }
    }

    private fun showChangeCharLimitDialog() {
        MIUIDialog(activity()).show {
            customView(R.layout.view_single_slider_ui) {
                it.single_slider_view_title.text = "调整数值"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.singleSeekBar.min = 1
                }
                it.singleSeekBar.max = 20
                it.singleSeekBar.progress = schedule.maxHideCharLimit
                it.singleText.text = "${schedule.maxHideCharLimit}"
                it.singleSeekBar.setOnSeekBarChangeListener(object : MySeekBarChangeListener() {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        it.singleText.text = "$progress"
                    }
                })
                it.button.setOnClickListener { _->
                    schedule.maxHideCharLimit = it.singleSeekBar.progress
                    scheduleViewModel.updateSchedule(schedule)
                    dismiss()
                }
            }
        }
    }

    private fun modifyScheduleName() {
        MIUIDialog(activity()).show {
            title(text = "编辑课表名称")
            input(prefill = schedule.name) { text, _ ->
                schedule.name = text.toString()
                this@ScheduleConfigFragment.courseNameSubTitle.text = text
                Toasts.toast(getString(R.string.update_success))
                scheduleViewModel.updateSchedule(schedule)
            }
            positiveButton(text = "保存") { }
            negativeButton(text = "取消") { }
        }
    }

    @SuppressLint("CheckResult")
    private fun chooseBackgroundPicture() {
        RxPermissions(activity()).request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe {
            if (it) {
                startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), PICK_PHOTO)
            } else Toasts.toast(getString(R.string.permission_is_denied))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showItemHeightDialog() {
        BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme).apply {
            dismissWithAnimation = true
            val view: View = Uis.inflate(activity(), R.layout.view_schedule_time)
            setContentView(view)
            window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
            view.animate().translationY(50F)
            val tv = view.findViewById<TextView>(R.id.dialog_title)
            val s1 = view.findViewById<SeekBar>(R.id.seekBar)
            val s2 = view.findViewById<SeekBar>(R.id.seekBar2)
            val t1 = view.findViewById<TextView>(R.id.tvS1)
            val t2 = view.findViewById<TextView>(R.id.tvS2)
            val button = view.findViewById<Button>(R.id.button)
            tv.text = "配置课程格子高度"
            s2.visibility = View.GONE
            t2.visibility = View.GONE
            s1.max = 150
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                s1.min = 30
            }
            s1.progress = schedule.itemHeight
            t1.text = "${schedule.itemHeight} dp"
            s1.setOnSeekBarChangeListener(object : MySeekBarChangeListener() {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    t1.text = "$progress dp"
                }
            })
            button.setOnClickListener {
                if (s1.progress < 30) {
                    Toasts.toast(getString(R.string.cant_small_than_30))
                } else {
                    schedule.itemHeight = s1.progress
                    //6月24 @bug：在Dialog上下文中访问fragment的资源变量会产生空指针
                    this@ScheduleConfigFragment.scheduleCourseItemHeightSubtitle.text = "${s1.progress} px"
                    scheduleViewModel.updateSchedule(schedule)
                    dismiss()
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showMaxSessionDialog() {
        BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme).apply {
            dismissWithAnimation = true
            val view: View = Uis.inflate(activity(), R.layout.view_schedule_time)
            setContentView(view)
            window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
            view.animate().translationY(50F)
            val tv = view.findViewById<TextView>(R.id.dialog_title)
            val s1 = view.findViewById<SeekBar>(R.id.seekBar)
            val s2 = view.findViewById<SeekBar>(R.id.seekBar2)
            val t1 = view.findViewById<TextView>(R.id.tvS1)
            val t2 = view.findViewById<TextView>(R.id.tvS2)
            val button = view.findViewById<Button>(R.id.button)
            tv.text = "配置最大节次"
            s2.visibility = View.GONE
            t2.visibility = View.GONE
            s1.max = 20
            s1.progress = schedule.maxSession
            t1.text = "${schedule.maxSession} 节"
            s1.setOnSeekBarChangeListener(object : MySeekBarChangeListener() {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    t1.text = "$progress 节"
                }
            })
            button.setOnClickListener {
                if (s1.progress == 0) {
                    Toasts.toast(getString(R.string.param_is_illgal))
                } else {
                    schedule.maxSession = s1.progress
                    this@ScheduleConfigFragment.scheduleMaxSessionSubtitle.text = "${s1.progress} 节"
                    scheduleViewModel.updateSchedule(schedule)
                    dismiss()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showCourseItemAlphaDialog() {
        BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme).apply {
            dismissWithAnimation = true
            val view: View = Uis.inflate(activity(), R.layout.view_schedule_time)
            setContentView(view)
            window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
            view.animate().translationY(50F)
            val tv = view.findViewById<TextView>(R.id.dialog_title)
            val s1 = view.findViewById<SeekBar>(R.id.seekBar)
            val s2 = view.findViewById<SeekBar>(R.id.seekBar2)
            val t1 = view.findViewById<TextView>(R.id.tvS1)
            val t2 = view.findViewById<TextView>(R.id.tvS2)
            val button = view.findViewById<Button>(R.id.button)
            tv.text = "配置透明度"
            s2.visibility = View.GONE
            t2.visibility = View.GONE
            s1.max = 10
            s1.progress = schedule.alphaForCourseItem
            t1.text = "L ${schedule.alphaForCourseItem}"
            s1.setOnSeekBarChangeListener(object : MySeekBarChangeListener() {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    t1.text = "L $progress"
                }
            })
            button.setOnClickListener {
                schedule.alphaForCourseItem = s1.progress
                this@ScheduleConfigFragment.scheduleCourseAlphaSubTitle.text = "L ${s1.progress}"
                scheduleViewModel.updateSchedule(schedule)
                dismiss()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data ?: return
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                handleImage(data)
            }
        }
    }

    private fun handleImage(data: Intent) {
        try {
            val uri = data.data
            Toasts.toast(getString(R.string.update_success))
            schedule.imageUrl = Files.getFilePath(activity(), uri)
            scheduleViewModel.updateSchedule(schedule)
            Glide.with(activity()).load(File(schedule.imageUrl)).into(backgroundPic)
        } catch (e: Exception) {
            Toasts.toast(getString(R.string.pic_choose_fail))
        }
    }

    @SuppressLint("StringFormatMatches", "SetTextI18n")
    private fun showTimeConfigDialog() {
        BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme).apply {
            dismissWithAnimation = true
            val view: View = Uis.inflate(activity(), R.layout.view_schedule_time)
            setContentView(view)
            window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
            view.animate().translationY(50F)
            val s1 = view.findViewById<SeekBar>(R.id.seekBar)
            val s2 = view.findViewById<SeekBar>(R.id.seekBar2)
            val t1 = view.findViewById<TextView>(R.id.tvS1)
            val t2 = view.findViewById<TextView>(R.id.tvS2)
            val button = view.findViewById<Button>(R.id.button)

            var mTotalWeek: Int = schedule.totalWeek
            var mCurWeek = schedule.curWeek()
            s1.max = 30
            s1.progress = schedule.totalWeek
            s2.max = schedule.totalWeek
            s2.progress = schedule.curWeek()
            t1.text = getString(R.string.total_week, schedule.totalWeek)
            t2.text = getString(R.string.current_week, schedule.curWeek())

            s1.setOnSeekBarChangeListener(object : MySeekBarChangeListener() {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    s2.max = progress
                    t1.text = getString(R.string.total_week, progress.toString())
                    mTotalWeek = progress
                }
            })

            s2.setOnSeekBarChangeListener(object : MySeekBarChangeListener() {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    t2.text = getString(R.string.current_week, progress.toString())
                    mCurWeek = progress
                }
            })

            button.setOnClickListener {
                if (mTotalWeek == 0 || mCurWeek == 0) {
                    Toasts.toast(getString(R.string.param_is_illgal))
                } else {
                    schedule.totalWeek = mTotalWeek
                    schedule.termStartDate = Dates.getTermStartDate(mCurWeek)
                    this@ScheduleConfigFragment.scheduleWeekSubtitle.text = t1.text.toString() + t2.text.toString()
                    scheduleViewModel.updateSchedule(schedule)
                    dismiss()
                }
            }
        }
    }
}