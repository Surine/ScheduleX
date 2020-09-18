package cn.surine.schedulex.ui.schedule_data_fetch

import cn.surine.schedulex.BuildConfig
import cn.surine.schedulex.base.controller.BaseRepository
import cn.surine.schedulex.data.network.Loader
import cn.surine.schedulex.third_parse.JwInfo

/**
 * Intro：
 * 适配列表仓库
 * @author sunliwei
 * @date 2020/6/23 18:14
 */
object AdapterListRepository : BaseRepository() {


    /**
     * 优化版适配列表
     * */
    suspend fun getAdapterListAsync(): MutableList<JwInfo> = remote {
        val url: String = if (BuildConfig.DEBUG) {
            "https://surinex.coding.net/p/schedulex/d/schedulex/git/raw/dev/schools.json"
        } else {
            "https://surinex.coding.net/p/schedulex/d/schedulex/git/raw/master/schools_v2.json"
        }
        Loader.mService.getAdapterList(url).await()
    }
}