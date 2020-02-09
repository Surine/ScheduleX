package cn.surine.schedulex.ui.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.text.TextUtils;

import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.databinding.FragmentLoginBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_init.ScheduleInitFragment;

public class LoginFragment extends BaseBindingFragment<FragmentLoginBinding> {

    private CourseViewModel courseViewModel;
    private ScheduleViewModel scheduleViewModel;
    private AlertDialog dialog;
    private long scheduleId;

    @Override
    public int layoutId() {
        return R.layout.fragment_login;
    }


    @Override
    protected void onInit(FragmentLoginBinding binding) {

        Class[] classes = new Class[]{LoginRepository.class};
        Object[] args = new Object[]{LoginRepository.abt.getInstance()};
        LoginViewModel loginViewModel;
        binding.setData(loginViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classes,args)).get(LoginViewModel.class));

        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        courseViewModel = ViewModelProviders.of(this,InstanceFactory.getInstance(classesForCourse,argsForCourse)).get(CourseViewModel.class);

        Class[] classesForSchedule = new Class[]{ScheduleRepository.class};
        Object[] argsForSchedule = new Object[]{ScheduleRepository.abt.getInstance()};
        scheduleViewModel = ViewModelProviders.of(this,InstanceFactory.getInstance(classesForSchedule,argsForSchedule)).get(ScheduleViewModel.class);

        dialog = new ProgressDialog(activity());
        dialog.setCancelable(false);

        //网络进度
        loginViewModel.loginStatus.observe(getViewLifecycleOwner(), status -> {
            if(status == LoginViewModel.START_LOGIN){
                dialog.setTitle(getString(R.string.warning));
                dialog.setMessage(getString(R.string.ready_to_login_tust));
                dialog.show();
            }
            if(status == LoginViewModel.LOGIN_SUCCESS){
                loginViewModel.saveAccountAndPassword();
                courseViewModel.getCourseByNet();
            }
            if(status == LoginViewModel.LOGIN_FAIL){
                dialog.dismiss();
                Toasts.toast(getString(R.string.login_fail));
            }
        });

        //获取课表进度
        courseViewModel.getCourseStatus.observe(getViewLifecycleOwner(), status-> {
            if(status == CourseViewModel.START){
                dialog.setMessage(getString(R.string.ready_to_get_schedule_list));
            }
            if(status == CourseViewModel.SUCCESS){
                dialog.dismiss();
                //保存课表
                if(getArguments() == null || TextUtils.isEmpty(getArguments().getString(ScheduleInitFragment.SCHEDULE_NAME))){
                    Toasts.toast(getString(R.string.arg_exception));
                    return;
                }
                scheduleId = scheduleViewModel.addSchedule(getArguments().getString(ScheduleInitFragment.SCHEDULE_NAME),courseViewModel.totalWeek.getValue(),courseViewModel.nowWeek.getValue());
                //保存当前选中课表
                Prefs.save(Constants.CUR_SCHEDULE,scheduleId);
                //对课程所属课表进行赋值
                List<Course> courseList = courseViewModel.mMutableCourses.getValue();
                for (Course course:courseList) {
                    course.scheduleId = scheduleId;
                    course.id = course.scheduleId +"@"+ course.id;
                }
                courseViewModel.saveCourseByDb(courseList,scheduleId);
                Navigations.open(this,R.id.scheduleFragment);
                Toasts.toast(getString(R.string.handle_success));
            }
            if(status == CourseViewModel.FAIL){
                dialog.dismiss();
                //清理所有课程数据
                courseViewModel.deleteCourseByScheduleId(scheduleId);
                //删除保存的课表
                scheduleViewModel.deleteScheduleById(scheduleId);
                Toasts.toast(getString(R.string.get_fail));
            }
        });
    }
}
