package cn.surine.schedulex.ui.view.custom.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.App;
import cn.surine.schedulex.base.interfaces.DCall;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.Course;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-02-03 11:21
 */
public class BtmDialogs {

    /**
     * 显示一个编辑底部框
     *
     * @param context 上下文
     * @param dcall   数据回调
     */
    public static void showEditBtmDialog(Context context, String hint, DCall<String> dcall) {
        BottomSheetDialog bt = new BottomSheetDialog(context);
        View view;
        bt.setContentView(view = Uis.inflate(context, R.layout.view_edit_layout));
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.show();
        EditText editText = view.findViewById(R.id.editText);
        editText.setHint(hint);
        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(v1 -> {
            String s = editText.getText().toString();
            if (!TextUtils.isEmpty(s)) {
                bt.dismiss();
                dcall.back(s);
            }
        });
    }


    /**
     * 显示课详情
     *
     * @param context 上下文
     * @param course  课程
     */
    public static void showCourseInfoBtmDialog(Context context, Course course) {
        BottomSheetDialog bt = new BottomSheetDialog(context);
        View view;
        bt.setContentView(view = Uis.inflate(context, R.layout.view_course_info));
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.show();
        view.animate().translationY(50);
        TextView courseName = view.findViewById(R.id.courseName);
        TextView coursePosition = view.findViewById(R.id.coursePosition);
        TextView courseClassDay = view.findViewById(R.id.courseClassDay);
        TextView courseSession = view.findViewById(R.id.courseSession);
        TextView courseTeacher = view.findViewById(R.id.courseTeacher);
        TextView courseScore = view.findViewById(R.id.courseScore);
        ImageView edit = view.findViewById(R.id.courseEdit);
        courseName.setText(course.coureName);
        coursePosition.setText(course.teachingBuildingName + course.classroomName);
        courseClassDay.setText("周" + course.classDay);
        courseSession.setText(course.classSessions + "-" + (Integer.parseInt(course.continuingSession) + Integer.parseInt(course.classSessions) - 1) + "节");
        courseTeacher.setText(course.teacherName == null ? App.context.getResources().getString(R.string.unknown) : course.teacherName);
        courseScore.setText(course.xf + "分");
        edit.setOnClickListener(v -> Toasts.toast("编辑"));
    }

}
