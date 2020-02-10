package cn.surine.coursetableview.interfaces;

import java.util.List;

import cn.surine.coursetableview.entity.BCourse;

/**
 * Created by Surine on 2019/2/26.
 */

public interface OnClickCourseItemListener {
    void onClickItem(List<BCourse> list, int itemPosition,boolean isThisWeek);
}
