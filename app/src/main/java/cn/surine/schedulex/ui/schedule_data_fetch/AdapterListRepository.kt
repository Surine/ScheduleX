package cn.surine.schedulex.ui.schedule_data_fetch

import cn.surine.schedulex.base.controller.BaseRepository
import cn.surine.schedulex.data.network.Loader
import cn.surine.schedulex.third_parse.JwInfo
import kotlinx.coroutines.Deferred

/**
 * Intro：
 * 适配列表仓库
 * @author sunliwei
 * @date 2020/6/23 18:14
 */
object AdapterListRepository : BaseRepository() {

    /**
     * 获取适配列表
     * */
    fun getAdapterListAsync(): Deferred<MutableList<JwInfo>>? {
        return Loader.mService.adapterList
    }
}