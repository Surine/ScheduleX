package cn.surine.schedulex.ui.timer

import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Intro：用作app里所有关于时间的操作
 *
 * @author sunliwei
 * @date 2020-01-22 16:11
 */
@Keep
class TimerViewModel(private val mTimerRepository: TimerRepository) : ViewModel() {
    @JvmField
    var curWeekStr = MutableLiveData<String>()
    val indexTitle: String
        get() = mTimerRepository.getIndexTitle()

}