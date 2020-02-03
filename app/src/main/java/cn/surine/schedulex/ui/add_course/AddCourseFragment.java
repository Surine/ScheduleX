package cn.surine.schedulex.ui.add_course;

import android.graphics.Color;

import androidx.core.graphics.ColorUtils;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.databinding.FragmentAddCourseBinding;
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-02-01 17:58
 */
public class AddCourseFragment extends BaseBindingFragment<FragmentAddCourseBinding> {

    @Override
    public int layoutId() {
        return R.layout.fragment_add_course;
    }


    @Override
    protected void onInit(FragmentAddCourseBinding t) {
        Course course = new Course();

        //编辑课表名
        t.editCourseName.setOnClickListener(v -> BtmDialogs.showEditBtmDialog(activity(), t.courseNameSubTitle.getText().toString(), s -> {
            course.coureName = s;
            t.courseNameSubTitle.setText(s);
        }));

        //编辑上课地点
        t.editCoursePosition.setOnClickListener(v -> BtmDialogs.showEditBtmDialog(activity(), t.coursePositionSubtitle.getText().toString(), s -> {
            course.teachingBuildingName = s;
            t.coursePositionSubtitle.setText(s);
        }));

    }
}
