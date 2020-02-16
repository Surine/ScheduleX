package cn.surine.schedulex.ui.timer;

import android.os.Bundle;

import cn.surine.schedulex.base.controller.AbstractSingleTon;
import cn.surine.schedulex.base.controller.BaseRepository;
import cn.surine.schedulex.base.utils.Dates;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-22 16:12
 */
public class TimerRepository extends BaseRepository {

    public static AbstractSingleTon<TimerRepository> abt = new AbstractSingleTon<TimerRepository>() {
        @Override
        protected TimerRepository newObj(Bundle bundle) {
            return new TimerRepository();
        }
    };

    /**
     * 拼接首页Title
     */
    String getIndexTitle() {
        return Dates.getMonthInEng() + Dates.getDate("dd");
    }


    /**
     * 获取今日星期几
     */
    int getWeekDay() {
        return Dates.getWeekDay();
    }


    /**
     * 获取小部件日视图标题
     * */
    public String getWidgetDayClassTitle(){
        return Dates.getDate("MM月dd日");
    }
}
