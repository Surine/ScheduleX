package cn.surine.schedulex.ui.schedule_data_fetch

import cn.surine.schedulex.BuildConfig
import cn.surine.schedulex.base.controller.BaseRepository
import cn.surine.schedulex.data.entity.Commons
import cn.surine.schedulex.data.network.Loader

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/16/20 16:10
 */
object ScheduleDataFetchRepository :BaseRepository(){
    suspend fun getCommon(): Commons = remote {
        val url: String = if (BuildConfig.DEBUG) {
            "https://surinex.coding.net/p/schedulex/d/schedulex/git/raw/dev/common.json"
        } else {
            "https://surinex.coding.net/p/schedulex/d/schedulex/git/raw/master/common.json"
        }
        Loader.mService.getCommon(url).await()
    }
}