package cn.surine.schedulex.ui.timer;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.App;

/**
 * Intro：用作app里所有关于时间的操作
 *
 * @author sunliwei
 * @date 2020-01-22 16:11
 */
public class TimerViewModel extends ViewModel {

    private TimerRepository mTimerRepository;

    public MutableLiveData<String> curWeekStr = new MutableLiveData<>();

    public TimerViewModel(TimerRepository timerRepository) {
        this.mTimerRepository = timerRepository;
    }

    public String getIndexTitle() {
        return mTimerRepository.getIndexTitle();
    }


    /**
     * 首页当前日背景
     */
    public Drawable getWeekDayBackground(int i) {
        return mTimerRepository.getWeekDay() - 1 != i ? null : App.context.getResources().getDrawable(R.drawable.primary_color_10r_rect);
    }


    /**
     * 首页当前日文本颜色
     */
    public int getWeekDayTextColor(int i) {
        return mTimerRepository.getWeekDay() - 1 != i ? Color.BLACK : Color.WHITE;
    }

}
