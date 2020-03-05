package cn.surine.schedulex.ui.view.custom.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.bottomsheet.BottomSheetDialog;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.App;
import cn.surine.schedulex.base.controller.BaseFragment;
import cn.surine.schedulex.base.interfaces.DCall;
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


    /**
     * 获取一个底部弹窗的基础UI
     * */
    public static void getBaseConfig(Context context,View view,DCall<View> showCall){
        BottomSheetDialog bt = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.setDismissWithAnimation(true);
        bt.setContentView(view);
        view.animate().translationY(50);
        if(showCall != null){
            showCall.back(view);
        }
        bt.show();
    }




    /**
     * 显示一个编辑底部框
     *
     * @param context 上下文
     * @param dcall   数据回调
     */
    public static void showEditBtmDialog(Context context, String txt,boolean isText, DCall<String> dcall) {
        BottomSheetDialog bt = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        View view;
        bt.setContentView(view = Uis.inflate(context, R.layout.view_edit_layout));
        bt.setDismissWithAnimation(true);
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.show();
        EditText editText = view.findViewById(R.id.editText);
        if(isText){
            editText.setText(txt);
        }else{
            editText.setHint(txt);
        }
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
     * @param baseFragment 上下文
     * @param course  课程
     */
    public static void showCourseInfoBtmDialog(BaseFragment baseFragment, Course course) {
        BottomSheetDialog bt = new BottomSheetDialog(baseFragment.activity(),R.style.BottomSheetDialogTheme);
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
        courseSession.setBackground(Drawables.getDrawable(Color.parseColor(course.color),180,0,0));
        courseClassDay.setBackground(Drawables.getDrawable(App.context.getResources().getColor(R.color.colorPrimary),180,0,0));
        Bundle bundle = new Bundle();
        bundle.putString(COURSE_ID,course.id);
        edit.setOnClickListener(v -> {
            Navigations.open(baseFragment,R.id.action_scheduleFragment_to_addCourseFragment,bundle);
            bt.dismiss();
        });
    }

}
