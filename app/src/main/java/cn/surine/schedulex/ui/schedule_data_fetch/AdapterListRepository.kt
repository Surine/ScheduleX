package cn.surine.schedulex.ui.schedule_data_fetch

import cn.surine.schedulex.BuildConfig
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
        val url: String = if(BuildConfig.DEBUG){
            "https://surinex.coding.net/p/schedulex/d/schedulex/git/raw/dev/schools.json"
        }else{
            "https://surinex.coding.net/p/schedulex/d/schedulex/git/raw/master/schools.json"
        }
        return Loader.mService.getAdapterList(url)
    }
}