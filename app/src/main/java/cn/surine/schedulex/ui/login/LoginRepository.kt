package cn.surine.schedulex.ui.login

import cn.surine.schedulex.base.controller.BaseRepository
import cn.surine.schedulex.data.entity.VmResultString
import cn.surine.schedulex.data.network.Loader.mService
import io.reactivex.Flowable

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 11:16
 */
object LoginRepository : BaseRepository() {
    /**
     * 登录操作
     */
    fun login(account: String, password: String): Flowable<VmResultString> {
        return mService.login(account, password).compose(schedulerHelper<VmResultString>())
    }

}