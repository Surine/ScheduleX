package cn.surine.schedulex.data.network

import cn.surine.schedulex.base.http.Retrofits.getService

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/25 15:14
 */
object Loader {
    val mService: Api = getService(Api::class.java)
}