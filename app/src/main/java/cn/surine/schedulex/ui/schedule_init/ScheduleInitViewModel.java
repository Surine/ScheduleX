package cn.surine.schedulex.ui.schedule_init;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import cn.surine.schedulex.base.utils.SimpleTextWatcher;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-22 19:09
 */
public class ScheduleInitViewModel extends ViewModel {
    public MutableLiveData<String> scheduleName = new MutableLiveData<>();

    public TextWatcher scheduleWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            scheduleName.setValue(s.toString());
        }
    };

}
