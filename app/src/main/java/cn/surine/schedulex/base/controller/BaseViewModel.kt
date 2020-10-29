package cn.surine.schedulex.base.controller

import androidx.annotation.Keep
import androidx.databinding.BaseObservable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.surine.schedulex.base.utils.Toasts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Intro：
 * Base ViewModel
 * @author sunliwei
 * @date 2020/6/24 10:27
 */
@Keep
open class BaseViewModel : ViewModel(){

    companion object {
        const val START_LOAD = 1
        const val LOADING = 2
        const val LOAD_FAIL = 3
        const val LOAD_SUCCESS = 4
    }

     fun request(block: suspend CoroutineScope.() -> Unit, errorCall: (String?) -> Unit = {}) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                block()
            } catch (e: Exception) {
                Toasts.toast("请求出现异常：${e.localizedMessage}")
                errorCall(e.localizedMessage)
            }
        }
    }
}