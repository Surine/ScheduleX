package cn.surine.schedulex.ui.view.custom.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.App;
import cn.surine.schedulex.base.controller.BaseFragment;
import cn.surine.schedulex.base.utils.Drawables;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.Course;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-02-03 11:21
 */
public class BtmDialogs {

    public static final String COURSE_ID = "course_id";

    public interface DialogCall {
        void onDialogCall(View view, BottomSheetDialog bottomSheetDialog);
    }


    /**
     * 获取一个底部弹窗的基础UI
     *
     * @return
     */
    public static BottomSheetDialog getBaseConfig(Context context, View view, DialogCall dialogCall) {
        BottomSheetDialog bt = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        bt.setContentView(view);
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.setDismissWithAnimation(true);
        view.animate().translationY(50);
        if (dialogCall != null) {
            dialogCall.onDialogCall(view, bt);
        }
        bt.show();
        return bt;
    }


    /**
     * 显示课详情
     *
     * @param baseFragment 上下文
     * @param course       课程
     */
    public static void showCourseInfoBtmDialog(BaseFragment baseFragment, Course course) {
        BottomSheetDialog bt = new BottomSheetDialog(baseFragment.activity(), R.style.BottomSheetDialogTheme);
        View view;
        bt.setContentView(view = Uis.inflate(baseFragment.activity(), R.layout.view_course_info));
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.setDismissWithAnimation(true);
        bt.show();
        view.animate().translationY(50);
        TextView courseName = view.findViewById(R.id.courseName);
        TextView coursePosition = view.findViewById(R.id.coursePosition);
        TextView courseClassDay = view.findViewById(R.id.courseClassDay);
        TextView courseSession = view.findViewById(R.id.courseSession);
        TextView courseTeacher = view.findViewById(R.id.courseTeacher);
        TextView courseScore = view.findViewById(R.id.courseScore);
        TextView courseWeekInfo = view.findViewById(R.id.weekInfo);
        ImageView edit = view.findViewById(R.id.courseEdit);
        courseName.setText(course.coureName);
        coursePosition.setText(course.teachingBuildingName + course.classroomName);
        courseClassDay.setText("周" + course.classDay);
        courseSession.setText(course.classSessions + "-" + (Integer.parseInt(course.continuingSession) + Integer.parseInt(course.classSessions) - 1) + "节");
        courseTeacher.setText(course.teacherName == null ? App.context.getResources().getString(R.string.unknown) : course.teacherName);
        courseScore.setText(course.xf + "分");
        courseWeekInfo.setText(course.getWeekDescription());
        courseSession.setBackground(Drawables.getDrawable(Color.parseColor(course.color), 180, 0, 0));
        courseClassDay.setBackground(Drawables.getDrawable(App.context.getResources().getColor(R.color.colorPrimary), 180, 0, 0));
        Bundle bundle = new Bundle();
        bundle.putString(COURSE_ID, course.id);
        edit.setOnClickListener(v -> {
            Navigations.open(baseFragment, R.id.action_scheduleFragment_to_addCourseFragment, bundle);
            bt.dismiss();
        });
    }

}
