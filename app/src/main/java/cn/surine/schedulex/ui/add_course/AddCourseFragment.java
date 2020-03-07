package cn.surine.schedulex.ui.add_course;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Strs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentAddCourseBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs;
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-02-01 17:58
 */
public class AddCourseFragment extends BaseBindingFragment<FragmentAddCourseBinding> {


    private Schedule schedule;
    private Course course;
    private CourseViewModel courseViewModel;
    private FragmentAddCourseBinding globalT;
    private boolean hasCourseData;

    @Override
    public int layoutId() {
        return R.layout.fragment_add_course;
    }


    @Override
    protected void onInit(FragmentAddCourseBinding t) {
        globalT = t;
        Class[] classesForSchedule = new Class[]{ScheduleRepository.class};
        Object[] argsForSchedule = new Object[]{ScheduleRepository.abt.getInstance()};
        ScheduleViewModel scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForSchedule, argsForSchedule)).get(ScheduleViewModel.class);
        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForCourse, argsForCourse)).get(CourseViewModel.class);
        //获取当前课表
        schedule = scheduleViewModel.getCurSchedule();

        Bundle bundle = getArguments();
        if (bundle == null) {
            //bundle为空的情况
            course = new Course();
            course.scheduleId = schedule.roomId;
            course.color = Constants.COLOR_1[new Random(System.currentTimeMillis()).nextInt(Constants.COLOR_1.length)];
            course.id = course.scheduleId + "@" + System.currentTimeMillis();
        } else if (Strs.isNotEmpty(bundle.getString(BtmDialogs.COURSE_ID))) {
            //course id不为空的情况
            course = courseViewModel.getCourseById(getArguments().getString(BtmDialogs.COURSE_ID));
            if (course != null) {
                hasCourseData = true;
                initCourseUi(t);
            } else {
                Toasts.toast(getString(R.string.get_course_fail));
                return;
            }
        } else {
            Toasts.toast(getString(R.string.get_course_fail));
            return;
        }


        //编辑课表名
        t.editCourseName.setOnClickListener(v -> CommonDialogs.getEditDialog(activity(), hasCourseData ? course.coureName : t.courseNameSubTitle.getText().toString(), hasCourseData, s -> {
            course.coureName = s;
            t.courseNameSubTitle.setText(s);
        }, null));

        //编辑上课地点
        t.editCoursePosition.setOnClickListener(v -> CommonDialogs.getEditDialog(activity(), hasCourseData ? course.teachingBuildingName + course.classroomName : t.coursePositionSubtitle.getText().toString(), hasCourseData, s -> {
            course.teachingBuildingName = s;
            //如果是修改，直接保存building就可
            if (hasCourseData) {
                course.classroomName = "";
            }
            t.coursePositionSubtitle.setText(s);
        }, null));


        //编辑教师姓名
        t.editCourseTeacher.setOnClickListener(v -> CommonDialogs.getEditDialog(activity(), hasCourseData ? course.teacherName : t.courseTeacherSubtitle.getText().toString(), hasCourseData, s -> {
            course.teacherName = s;
            t.courseTeacherSubtitle.setText(s);
        }, null));


        //编辑学分
        t.editCourseScore.setOnClickListener(v -> CommonDialogs.getEditDialog(activity(), hasCourseData ? course.xf : t.courseScoreSubtitle.getText().toString(), hasCourseData, s -> {
            course.xf = s;
            t.courseScoreSubtitle.setText(getString(R.string.score_2_0, s));
        }, null));

        //编辑上课时间
        t.editCoursePlan.setOnClickListener(v -> showPlanTime());


        //保存课程
        t.addCourse.setOnClickListener(v -> {
            if (course != null) {
                if (Strs.hasEmpty(course.coureName, course.classWeek, course.classDay, course.classSessions, course.continuingSession)) {
                    Toasts.toast(getString(R.string.param_empty));
                } else {
                    //分更新和升级
                    if (hasCourseData) {
                        courseViewModel.update(course);
                    } else {
                        courseViewModel.insert(course);
                    }
                    Toasts.toast(getString(R.string.handle_success));
                    NavHostFragment.findNavController(AddCourseFragment.this).navigateUp();
                }
            } else {
                Toasts.toast(getString(R.string.course_null));
            }
        });

    }

    /**
     * 加载初始UI
     *
     * @param t
     */
    private void initCourseUi(FragmentAddCourseBinding t) {
        t.courseNameSubTitle.setText(course.coureName);
        t.courseTimeSubtitle.setText(course.getWeekDescription() + "\n" + course.getClassDayDescription() + " " + course.getSessionDescription());
        t.coursePositionSubtitle.setText(course.teachingBuildingName + course.classroomName);
        t.courseTeacherSubtitle.setText(course.teacherName);
        t.courseScoreSubtitle.setText(course.xf);
        t.delete.setVisibility(View.VISIBLE);
        t.delete.setOnClickListener(v -> deleteCourse());
    }


    /**
     * 删除课表
     */
    private void deleteCourse() {
        CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.delete_schedule_dialog_msg)
                , () -> {
                    courseViewModel.deleteByCourseId(course.id);
                    Toasts.toast(getString(R.string.handle_success));
                    NavHostFragment.findNavController(AddCourseFragment.this).navigateUp();
                }, null).show();
    }


    private void showPlanTime() {
        BottomSheetDialog bt = new BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme);
        bt.setDismissWithAnimation(true);
        View view;
        bt.setContentView(view = Uis.inflate(activity(), R.layout.view_course_time_plan));
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.show();
        view.animate().translationY(50);
        FrameLayout frameLayout = view.findViewById(R.id.view_course_time_plan);
        frameLayout.addView(getWeekView(frameLayout, bt));
    }


    /**
     * 添加周View
     *
     * @param frameLayout
     * @param bt
     */
    @SuppressLint("SetTextI18n")
    private View getWeekView(FrameLayout frameLayout, BottomSheetDialog bt) {
        View weekView = Uis.inflate(activity(), R.layout.view_week_choose);
        //周选择
        ChipGroup chipGroup = weekView.findViewById(R.id.chipGroup);
        chipGroup.removeAllViews();
        for (int i = 0; i < schedule.totalWeek; i++) {
            Chip chip = new Chip(activity());
            chip.setText("" + (i + 1));
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);
            if (hasCourseData && Strs.isNotEmpty(course.classWeek) && course.classWeek.charAt(i) == '1') {
                chip.setChecked(true);
            }
            chipGroup.addView(chip);
        }

        Button single = weekView.findViewById(R.id.single);
        Button doubleWeek = weekView.findViewById(R.id.doubleWeek);
        Button all = weekView.findViewById(R.id.all);

        single.setOnClickListener(v -> {
            chipGroup.clearCheck();
            for (int i = 0; i < schedule.totalWeek; i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (Integer.parseInt(chip.getText().toString()) % 2 != 0) {
                    chip.setChecked(true);
                }
            }
        });

        doubleWeek.setOnClickListener(v -> {
            chipGroup.clearCheck();
            for (int i = 0; i < schedule.totalWeek; i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (Integer.parseInt(chip.getText().toString()) % 2 == 0) {
                    chip.setChecked(true);
                }
            }
        });


        all.setOnClickListener(v -> {
            chipGroup.clearCheck();
            for (int i = 0; i < schedule.totalWeek; i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                chip.setChecked(true);
            }
        });


        Button button = weekView.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < schedule.totalWeek; i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    sb.append(1);
                } else {
                    sb.append(0);
                }
            }

            if (sb.toString().contains("1")) {
                course.classWeek = sb.toString();
                frameLayout.removeAllViews();
                frameLayout.addView(getDayView(frameLayout, bt));
            }

        });
        return weekView;
    }


    /**
     * 获取周view
     */
    private View getDayView(FrameLayout frameLayout, BottomSheetDialog bt) {
        View dayView = Uis.inflate(activity(), R.layout.view_day_choose);
        NumberPicker numberPicker = dayView.findViewById(R.id.number_picker);
        String[] city = {"星期一", "星期二", "星期三", "星期四", "星期五"};
        numberPicker.setDisplayedValues(city);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(city.length - 1);
        if (hasCourseData && Strs.isNotEmpty(course.classDay)) {
            numberPicker.setValue(Integer.parseInt(course.classDay) - 1);
        }
        numberPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        Button ok = dayView.findViewById(R.id.button);
        ok.setOnClickListener(v -> {
            course.classDay = String.valueOf(numberPicker.getValue() + 1);
            frameLayout.removeAllViews();
            frameLayout.addView(getSessionView(frameLayout, bt));
        });
        return dayView;
    }

    /**
     * 获取节次view
     */
    @SuppressLint("SetTextI18n")
    private View getSessionView(FrameLayout frameLayout, BottomSheetDialog bt) {
        View sessionView = Uis.inflate(activity(), R.layout.view_session_choose);
        NumberPicker npForStartSession = sessionView.findViewById(R.id.number_picker);
        String[] s1 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        npForStartSession.setDisplayedValues(s1);
        npForStartSession.setMinValue(0);
        npForStartSession.setMaxValue(s1.length - 1);
        npForStartSession.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        if (hasCourseData && Strs.isNotEmpty(course.classSessions)) {
            npForStartSession.setValue(getCursor(s1, course.classSessions));
        }

        NumberPicker npForContinueSession = sessionView.findViewById(R.id.numberPicker2);
        String[] s2 = s1;
        npForContinueSession.setDisplayedValues(s2);
        npForContinueSession.setMinValue(0);
        npForContinueSession.setMaxValue(s2.length - 1);
        npForContinueSession.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        if (hasCourseData && Strs.isNotEmpty(course.continuingSession)) {
            npForContinueSession.setValue(getCursor(s2, course.continuingSession));
        }

        Button sessionOk = sessionView.findViewById(R.id.button);
        sessionOk.setOnClickListener(v -> {
            String result_1 = s1[npForStartSession.getValue()];
            String result_2 = s2[npForContinueSession.getValue()];
            if (Integer.parseInt(result_1) + Integer.parseInt(result_2) - 1 <= Constants.MAX_SESSION) {
                course.classSessions = result_1;
                course.continuingSession = result_2;
                bt.dismiss();
                globalT.courseTimeSubtitle.setText(course.getWeekDescription() + "\n" + course.getClassDayDescription() + " " + course.getSessionDescription());
            } else {
                Toasts.toast(getString(R.string.overlimit));
            }
        });
        return sessionView;
    }

    /**
     * 获取某个值的数组下标
     */
    private int getCursor(String[] values, String target) {
        for (int i = 0; i < values.length; i++) {
            if (Strs.equals(values[i], target)) {
                return i;
            }
        }
        return 0;
    }


    @Override
    public void onBackPressed() {
        boolean condition = Strs.isNotEmpty(course.coureName) || Strs.isNotEmpty(course.classWeek) || Strs.isNotEmpty(course.classDay) || Strs.isNotEmpty(course.classSessions) || Strs.isNotEmpty(course.continuingSession);
        if (condition) {
            Snackbar.make(getView(), getString(R.string.snack_course_edit), Snackbar.LENGTH_LONG).setAction(R.string.btn_ok, v -> NavHostFragment.findNavController(AddCourseFragment.this).navigateUp()).show();
        } else {
            super.onBackPressed();
        }
    }
}
