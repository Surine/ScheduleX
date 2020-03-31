package cn.surine.coursetableview.interfaces;

import android.view.View;

import java.util.List;

import cn.surine.coursetableview.entity.BCourse;

/**
 * Created by Surine on 2019/2/26.
 */

public interface OnClickCourseItemListener {
    void onClickItem(View v, List<BCourse> list, int itemPosition, boolean isThisWeek);
}
