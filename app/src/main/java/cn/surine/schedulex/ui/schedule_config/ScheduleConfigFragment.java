package cn.surine.schedulex.ui.schedule_config;

import android.app.AlertDialog;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentScheduleConfigBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-30 15:23
 */
public class ScheduleConfigFragment extends BaseBindingFragment<FragmentScheduleConfigBinding> {


    private ScheduleViewModel scheduleViewModel;
    int scheduleId;
    private CourseViewModel courseViewModel;
    private Schedule schedule;

    @Override
    public int layoutId() {
        return R.layout.fragment_schedule_config;
    }


    @Override
    protected void onInit(FragmentScheduleConfigBinding t) {
        Class[] classes = new Class[]{ScheduleRepository.class};
        Object[] args = new Object[]{ScheduleRepository.abt.getInstance()};
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classes, args)).get(ScheduleViewModel.class);

        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForCourse, argsForCourse)).get(CourseViewModel.class);

        scheduleId = getArguments() == null ? -1 : getArguments().getInt(ScheduleListFragment.SCHEDULE_ID);

        if(scheduleId == Prefs.getLong(Constants.CUR_SCHEDULE,-1L)){
            t.deleteSchedule.setVisibility(View.GONE);
        }

        schedule = scheduleViewModel.getScheduleById(scheduleId);
        t.setData(schedule);

        t.deleteSchedule.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity());
            builder.setTitle(R.string.warning);
            builder.setMessage(R.string.delete_schedule_dialog_msg);
            builder.setNegativeButton(R.string.btn_ok, (dialog, which) -> {
                //删除课表后需要删除所有相关课程
                scheduleViewModel.deleteScheduleById(scheduleId);
                courseViewModel.deleteCourseByScheduleId(scheduleId);
                Toasts.toast(getString(R.string.schedule_is_delete));
                NavHostFragment.findNavController(ScheduleConfigFragment.this).navigateUp();
            });
            builder.setPositiveButton(R.string.btn_cancel, null);
            builder.show();
        });


        t.scheduleNameItem.setOnClickListener(v -> Toasts.toast("测试"));
        t.scheduleWeekInfoItem.setOnClickListener(v -> Toasts.toast("测试"));

        t.scheduleBackgroundItem.setOnClickListener(v -> {

        });

        t.schedulePaletteItem.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_scheduleConfigFragment_to_themeListFragment));


    }
}
