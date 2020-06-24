package cn.surine.schedulex.ui.schedule_data_fetch

import androidx.lifecycle.MutableLiveData
import cn.surine.schedulex.base.controller.BaseViewModel
import cn.surine.schedulex.third_parse.JwInfo

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/23 18:19
 */
class AdapterListViewModel(private val adapterListRepository: AdapterListRepository) : BaseViewModel() {
    val adapterListData: MutableLiveData<List<JwInfo>> by lazy {
        MutableLiveData<List<JwInfo>>()
    }

    //初版
    fun getAdapterList() = request({
        adapterListData.value = adapterListRepository.getAdapterListAsync()?.await()
    })

}