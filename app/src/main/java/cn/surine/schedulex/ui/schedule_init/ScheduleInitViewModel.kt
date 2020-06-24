package cn.surine.schedulex.ui.schedule_init

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import cn.surine.schedulex.base.controller.BaseViewModel
import cn.surine.schedulex.base.utils.SimpleTextWatcher

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 16:01
 */
class ScheduleInitViewModel :BaseViewModel(){
    val scheduleName = MutableLiveData<String>()
    val scheduleWatcher = object : SimpleTextWatcher(){
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            scheduleName.value = s.toString()
        }
    }
}