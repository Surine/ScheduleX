package cn.surine.schedulex.ui.schedule

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.SeekBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import cn.surine.coursetableview.entity.BCourse
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.app_widget.core.BoardCastSender.notifyWidget
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.App
import cn.surine.schedulex.base.controller.BaseBindingFragment
import cn.surine.schedulex.base.interfaces.Call
import cn.surine.schedulex.base.utils.*
import cn.surine.schedulex.base.utils.Prefs.getBoolean
import cn.surine.schedulex.base.utils.Prefs.save
import cn.surine.schedulex.base.utils.Toasts.toast
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.data.entity.TimeTable
import cn.surine.schedulex.databinding.FragmentScheduleBinding
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.timer.TimerViewModel
import cn.surine.schedulex.ui.timetable_list.TimeTableViewModel
import cn.surine.schedulex.ui.view.custom.helper.ZoomOutPageTransformer
import co.mobiwise.materialintro.shape.Focus
import co.mobiwise.materialintro.shape.FocusGravity
import co.mobiwise.materialintro.view.MaterialIntroView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.peanut.sdk.miuidialog.MIUIDialog
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.io.File

/**
 * Intro：
 *
 * @author sunliwei
 * @date 8/15/20 11:00
 */
class ScheduleFragment : BaseBindingFragment<FragmentScheduleBinding>() {
    private lateinit var courseViewModel: CourseViewModel
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var timeTableViewModel: TimeTableViewModel
    private lateinit var curSchedule: Schedule
    private var curViewPagerPosition = 0
    private var currentWeek = 0
    private var handleCourseList: MutableList<MutableList<BCourse>> = ArrayList()
    private lateinit var scheduleViewPagerAdapter: ScheduleViewPagerAdapter

    override fun layoutId(): Int = R.layout.fragment_schedule

    override fun onInit(t: FragmentScheduleBinding?) {

        //初始化vm
        VmManager(this).apply {
            courseViewModel = this.vmCourse
            timerViewModel = this.vmTimer
            scheduleViewModel = this.vmSchedule
            timeTableViewModel = this.vmTimetable
        }

        //引导
        if (!getBoolean(Constants.IS_FIRST, false)) {
            MaterialIntroView.Builder(activity())
                    .enableDotAnimation(true)
                    .enableIcon(false)
                    .setFocusGravity(FocusGravity.CENTER)
                    .setFocusType(Focus.MINIMUM)
                    .setDelayMillis(500)
                    .enableFadeAnimation(true)
                    .performClick(true)
                    .setInfoText(getString(R.string.first_toast))
                    .setTarget(curWeekTv)
                    .show()
            save(Constants.IS_FIRST, true)

            MIUIDialog(activity()).show {
                title(text = "隐私政策")
                message(res = R.string.privacy_info)
                positiveButton(text = "接受") { }
                negativeButton(text = "拒绝") { activity?.finish() }
            }
        }

        //初始化的时候添加时间表，但是不会重复添加
        once(Constants.ADD_NORMAL_TIMETABLE) {
            timeTableViewModel.addTimeTable(TimeTable.tedaNormal())
        }

        //配置数据绑定
        curSchedule = scheduleViewModel.curSchedule
        t?.let {
            it.schedule = curSchedule
            it.timer = timerViewModel
        }

        //当前周
        currentWeek = curSchedule.curWeek()
        if (currentWeek > curSchedule.totalWeek) {
            alert("当前周: $currentWeek 而最大周: ${curSchedule.curWeek()} 数据不正确，请删除课表重新导入")
        }
        timerViewModel.curWeekStr.value = getString(R.string.week, currentWeek.toString())


        //初始化课程数据
        handleCourseList = ArrayList()
        initData(false)


        //设置viewpager
        scheduleViewPagerAdapter = ScheduleViewPagerAdapter(handleCourseList, DataMaps.dataMappingTimeTableToBTimeTable(timeTableViewModel.getTimTableById(curSchedule.timeTableId)), this@ScheduleFragment, curSchedule, currentWeek, courseViewModel).apply {
            dataSetUpdateCall = object : Call {
                override fun back() {
                    initData(true)
                }
            }
        }
        viewpager.apply {
            adapter = scheduleViewPagerAdapter
            offscreenPageLimit = 1
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            currentItem = currentWeek - 1
            setPageTransformer(ZoomOutPageTransformer())
        }
        //显示空视图
        if (TextUtils.isEmpty(curSchedule.imageUrl)) {
            if (currentWeek > 0 && currentWeek - 1 < handleCourseList.size) {
                emptyView.visibility = if (handleCourseList[currentWeek - 1].isNotEmpty()) View.GONE else View.VISIBLE
            }
        }
        //设置背景图
        if (!curSchedule.imageUrl.isNullOrEmpty()) {
            Glide.with(activity()).load(File(curSchedule.imageUrl)).apply {
                if (curSchedule.imageUrl.endsWith("gif")) {
                    asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                }
            }.crossFade().into(background)
        }

        programEntry.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_scheduleFragment_to_MemoFragment))
        funcBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_scheduleFragment_to_ScheduleListFragment2))
        addCourse.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_scheduleFragment_to_addCourseFragment))
        //设置彩蛋事件
        title.setOnClickListener {
            save(Constants.EGG, if (!getBoolean(Constants.EGG, false)) {
                toast("你发现了彩蛋，点击已添加的课程格子试试看，（要小点声哦~）")
                title.setTextColor(App.context.resources.getColor(R.color.blue))
                true
            } else {
                title.setTextColor(Color.BLACK)
                toast("彩蛋关闭，好好上课！")
                false
            })
        }
        //点击当前周文本的事件
        curWeekTv.setOnClickListener {
            val view = R.layout.view_change_week_quickly.parseUi()
            PopupWindow(view, Uis.dip2px(activity(), 200f), WindowManager.LayoutParams.WRAP_CONTENT).apply {
                //设置外面可触
                isOutsideTouchable = true
                //设置可触
                isFocusable = false
                setBackgroundDrawable(Drawables.getDrawable(Color.WHITE, 180, 0, Color.WHITE))
                isTouchable = true
                elevation = 8f
                showAsDropDown(curWeekTv, 20, 30)
            }
            val seekBar = view.findViewById<SeekBar>(R.id.seekBar)
            val weekTv = view.findViewById<TextView>(R.id.weekText)
            seekBar.max = curSchedule.totalWeek
            seekBar.progress = curViewPagerPosition
            weekTv.text = (curViewPagerPosition + 1).toString()
            seekBar.setOnSeekBarChangeListener(object : MySeekBarChangeListener() {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    super.onProgressChanged(seekBar, progress, fromUser)
                    weekTv.text = (progress + 1).toString()
                    viewpager.currentItem = progress
                }
            })
        }

        viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            @SuppressLint("StringFormatMatches")
            override fun onPageSelected(position: Int) {
                curViewPagerPosition = position
                timerViewModel.curWeekStr.value = getString(R.string.week, position + 1) + if (currentWeek == position + 1) "" else " [" + getString(R.string.not_cur_week) + "]"
                scheduleViewPagerAdapter.apply {
                    setWeek(position + 1)
                    //2020.9.18：滑动过程中，数据更新可能被打断，所以需要在绘制完成再更新数据
                    viewpager.post {
                        notifyItemChanged(position)
                    }
                }
                if (TextUtils.isEmpty(curSchedule.imageUrl) && position < handleCourseList.size) {
                    emptyView.visibility = if (handleCourseList[position].size != 0) View.GONE else View.VISIBLE
                }
                courseOp.hide()
            }
        })
        //通知小部件更新
        context?.let { notifyWidget(it) }
    }

    /**
     * 目前init参数全初始化为true，批量操作为false
     */
    private fun initData(init: Boolean) {
        handleCourseList.clear()
        for (i in 0 until curSchedule.totalWeek) {
            val dbData = courseViewModel.queryCourseByWeek(i + 1, curSchedule.roomId)
            val bCourseList: MutableList<BCourse> = ArrayList()
            for (course in dbData) {
                DataMaps.dataMappingByCourse(course).apply {
                    color = if (curSchedule.alphaForCourseItem != 0) "#" + Integer.toHexString(Uis.getColorWithAlpha(curSchedule.alphaForCourseItem / 10f, Color.parseColor(this.color))) else null
                    bCourseList.add(this)
                }
            }
            handleCourseList.add(bCourseList)
        }
        if (init) {
            scheduleViewPagerAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        activity().finish()
    }


    override fun onResume() {
        super.onResume()
        //回到当前周，有个小bug可以临时通过这个解决一下
        viewpager.setCurrentItem(curSchedule.curWeek() - 1, false)
    }


    override fun statusBarUi() {
        StatusBars.setStatusBarUI(activity(), !curSchedule.lightText)
    }

}