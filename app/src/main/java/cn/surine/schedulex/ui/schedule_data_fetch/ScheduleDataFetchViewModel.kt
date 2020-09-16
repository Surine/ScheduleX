package cn.surine.schedulex.ui.schedule_data_fetch

import androidx.lifecycle.MutableLiveData
import cn.surine.schedulex.base.controller.BaseViewModel
import cn.surine.schedulex.data.entity.Commons

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/16/20 16:10
 */
class ScheduleDataFetchViewModel(val repository: ScheduleDataFetchRepository) : BaseViewModel() {
    val mCommons: MutableLiveData<Commons> by lazy {
        MutableLiveData<Commons>()
    }


    /**
     * 获取公共配置参数
     * */
    fun getCommon() = request({
        val data = repository.getCommon()
        mCommons.value = data
    })
}