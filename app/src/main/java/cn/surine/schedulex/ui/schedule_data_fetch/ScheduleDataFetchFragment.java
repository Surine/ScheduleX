package cn.surine.schedulex.ui.schedule_data_fetch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.Files;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Jsons;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Objs;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentDataFetchBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_init.ScheduleInitFragment;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-23 20:54
 */
public class ScheduleDataFetchFragment extends BaseBindingFragment<FragmentDataFetchBinding> {

    private static final int JSON_REQUEST_CODE = 1001;
    ScheduleViewModel scheduleViewModel;
    private CourseViewModel courseViewModel;
    private String scheduleName;

    @Override
    public int layoutId() {
        return R.layout.fragment_data_fetch;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onInit(FragmentDataFetchBinding t) {
        Class[] classes = new Class[]{ScheduleRepository.class};
        Object[] args = new Object[]{ScheduleRepository.abt.getInstance()};
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classes, args)).get(ScheduleViewModel.class);


        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForCourse, argsForCourse)).get(CourseViewModel.class);

        scheduleName = Objs.notNull(getArguments()) ? getArguments().getString(ScheduleInitFragment.SCHEDULE_NAME) : "UNKNOWN";
        t.scheduleTitle.setText(scheduleName);

        Bundle bundle = new Bundle();
        bundle.putString(ScheduleInitFragment.SCHEDULE_NAME, scheduleName);
        t.loginJw.setOnClickListener(v -> Navigations.open(ScheduleDataFetchFragment.this, R.id.action_dataFetchFragment_to_loginFragment, bundle));
        t.importJson.setOnClickListener(v -> {
            RxPermissions rxPermissions = new RxPermissions(activity());
            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                if (aBoolean) {
                    importByJson();
                } else {
                    Toasts.toast(getString(R.string.permission_is_denied));
                }
            });
        });
        t.superClass.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_dataFetchFragment_to_superLoginFragment, bundle));
        t.importExcel.setOnClickListener(v -> Toasts.toast("暂不支持"));
        t.other.setOnClickListener(v -> Toasts.toast("欢迎加群反馈！"));
        t.skip.setOnClickListener(v -> {
            Prefs.save(Constants.CUR_SCHEDULE, scheduleViewModel.addSchedule(scheduleName, 24, 1, Schedule.IMPORT_WAY.ADD));
            Navigations.open(ScheduleDataFetchFragment.this, R.id.action_dataFetchFragment_to_scheduleFragment);
        });

    }


    /**
     * 导入json
     */
    private void importByJson() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        this.startActivityForResult(intent, JSON_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            // 用户未选择任何文件，直接返回
            return;
        }
        if (requestCode == JSON_REQUEST_CODE) {
            try {
                Uri uri = data.getData();
                loadData(Files.getFilePath(activity(), uri));
            }catch (Exception e){
                CrashReport.postCatchedException(e);
            }
        }

    }


    /**
     * 加载数据
     *
     * @param jsonPath
     */
    private void loadData(String jsonPath) {
        String jsonContent = Files.getFileContent(jsonPath);
        long id;
        List<Course> list;
        try {
            list = Jsons.parseJsonWithGsonToList(jsonContent, Course.class);
            Course[] courses = new Course[list.size()];
            Prefs.save(Constants.CUR_SCHEDULE, (id = scheduleViewModel.addSchedule(scheduleName, 24, 1,Schedule.IMPORT_WAY.JSON)));
            for (int i = 0; i < list.size(); i++) {
                Course course = list.get(i);
                course.scheduleId = id;
                course.id = course.scheduleId + course.id.split("@")[1];
                courses[i] = course;
            }
            courseViewModel.insert(courses);
            Toasts.toast(getString(R.string.handle_success));
            Navigations.open(ScheduleDataFetchFragment.this, R.id.action_dataFetchFragment_to_scheduleFragment);
        } catch (Exception e) {
            Toasts.toast(getString(R.string.parse_fail));
            e.printStackTrace();
        }


    }
}
