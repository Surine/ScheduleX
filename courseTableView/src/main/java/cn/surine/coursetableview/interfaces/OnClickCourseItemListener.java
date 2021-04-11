package cn.surine.coursetableview.interfaces;

import android.view.View;

import java.util.List;

import cn.surine.coursetableview.entity.BCourse;

/**
 * Created by Surine on 2019/2/26.
 */

public interface OnClickCourseItemListener {
//    void onClickItem(View v, List<BCourse> list, int itemPosition, boolean isThisWeek);
    //section ,day 起始index为0
    void onClickItemV2(View v,List<BCourse> clickList,List<Boolean> isThisWeek,int section,int day);
}
