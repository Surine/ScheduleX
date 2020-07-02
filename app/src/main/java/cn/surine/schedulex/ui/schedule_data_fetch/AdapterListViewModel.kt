package cn.surine.schedulex.ui.schedule_data_fetch

import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import cn.surine.schedulex.base.controller.BaseViewModel
import cn.surine.schedulex.third_parse.JwInfo

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/23 18:19
 */
@Keep
class AdapterListViewModel(private val adapterListRepository: AdapterListRepository) : BaseViewModel() {
    val adapterListData: MutableLiveData<List<JwInfo>> by lazy {
        MutableLiveData<List<JwInfo>>()
    }


    //请求适配列表优化版
    fun getAdapterList() = request({
        //IO线程
        val data = adapterListRepository.getAdapterListAsync()
        //主线程
        adapterListData.value = data
    })

}