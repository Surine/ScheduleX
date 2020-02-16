package cn.surine.schedulex.ui.timer;

import androidx.annotation.Keep;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Intro：用作app里所有关于时间的操作
 *
 * @author sunliwei
 * @date 2020-01-22 16:11
 */

@Keep
public class TimerViewModel extends ViewModel {

    private TimerRepository mTimerRepository;

    public MutableLiveData<String> curWeekStr = new MutableLiveData<>();

    public TimerViewModel(TimerRepository timerRepository) {
        this.mTimerRepository = timerRepository;
    }

    public String getIndexTitle() {
        return mTimerRepository.getIndexTitle();
    }

}
