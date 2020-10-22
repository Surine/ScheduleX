package cn.surine.schedulex.ui.schedule_import_pro.core

import android.os.Bundle
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.ui.schedule_import_pro.model.RemoteUniversity
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseData

/**
 * Intro：
 *  负责分发场景
 * @author sunliwei
 * @date 10/7/20 21:41
 */
object ParseDispatcher {
    const val OTHER = -1
    const val JSOUP = 1
    const val API = 2
    const val HTML = 3

    //是否html解析
    const val IS_HTML = "IS_HTML"
    const val UNIVERSITY = "UNIVERSITY"

    fun dispatch(fragment: BaseFragment, mRemoteUniversity: RemoteUniversity) {
        when (mRemoteUniversity.importType) {
            JSOUP -> {
                Navigations.open(fragment, R.id.action_dataFetchFragment_to_scheduleThirdFetchFragment, Bundle().apply {
                    putSerializable(UNIVERSITY, mRemoteUniversity)
                    putBoolean(IS_HTML, false)
                })
            }
            HTML -> Navigations.open(fragment, R.id.action_dataFetchFragment_to_scheduleThirdFetchFragment, Bundle().apply {
                putSerializable(UNIVERSITY, mRemoteUniversity)
                putBoolean(IS_HTML, true)
            })
            API -> {
                when (mRemoteUniversity.jwSystem) {
                    ParseData.tust -> Navigations.open(fragment, R.id.action_dataFetchFragment_to_loginFragment,Bundle().apply {
                        putSerializable(UNIVERSITY, mRemoteUniversity)
                    })
                }
            }
            OTHER -> Toasts.toast("暂不支持")
        }
    }

}