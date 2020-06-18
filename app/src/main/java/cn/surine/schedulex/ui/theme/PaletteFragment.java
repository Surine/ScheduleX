package cn.surine.schedulex.ui.theme;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.BR;
import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseAdapter;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Palette;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentPaletteBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;

import static cn.surine.schedulex.ui.schedule_config.ScheduleConfigFragment.SCHEDULE_ID;

public class PaletteFragment extends BaseBindingFragment<FragmentPaletteBinding> {
    private List<Palette> mDatas = new ArrayList<>();
    private int scheduleId;
    private ScheduleViewModel scheduleViewModel;
    private CourseViewModel courseViewModel;


    @Override
    public int layoutId() {
        return R.layout.fragment_palette;
    }


    @Override
    protected void onInit(FragmentPaletteBinding t) {

        Class[] classes = new Class[]{ScheduleRepository.class};
        Object[] args = new Object[]{ScheduleRepository.abt.getInstance()};
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classes, args)).get(ScheduleViewModel.class);


        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForCourse, argsForCourse)).get(CourseViewModel.class);

        scheduleId = getArguments() == null ? -1 : getArguments().getInt(SCHEDULE_ID);
        if (scheduleId == -1) {
            Toasts.toast("无此课表");
            Navigations.close(PaletteFragment.this);
        }

        loadData();

        BaseAdapter<Palette> paletteBaseAdapter = new BaseAdapter<>(mDatas, R.layout.item_palette, BR.palette);
        LinearLayoutManager llm = new LinearLayoutManager(activity());
        t.recyclerview.setLayoutManager(llm);
        t.recyclerview.setAdapter(paletteBaseAdapter);
        paletteBaseAdapter.setOnItemClickListener(position -> {
            Schedule schedule = scheduleViewModel.getScheduleById(scheduleId);
            schedule.courseThemeId = mDatas.get(position).id;
            scheduleViewModel.updateSchedule(schedule);
            updateCourses(mDatas.get(position));
            Toasts.toast("更新成功！");
            Navigations.close(PaletteFragment.this);
        });
    }

    private void loadData() {
        mDatas.add(Constants.p1);
        mDatas.add(Constants.p2);
        mDatas.add(Constants.p3);
        mDatas.add(Constants.p4);
        mDatas.add(Constants.p5);
        mDatas.add(Constants.p6);
        mDatas.add(Constants.p7);
        mDatas.add(Constants.p8);
    }

    private void updateCourses(Palette palette) {
        String[] set = palette.colors;
        List<Course> courseList = courseViewModel.getCourseByScheduleId(scheduleId);
        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            course.color = set[i % set.length];
            courseViewModel.update(course);
        }
    }
}
