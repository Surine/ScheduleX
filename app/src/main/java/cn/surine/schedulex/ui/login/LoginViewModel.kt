package cn.surine.schedulex.ui.login

import android.text.Editable
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseViewModel
import cn.surine.schedulex.base.http.BaseHttpSubscriber
import cn.surine.schedulex.base.utils.SimpleTextWatcher
import cn.surine.schedulex.data.entity.VmResultString

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 11:18
 */
class LoginViewModel(val mLoginRepository: LoginRepository) : BaseViewModel() {

    private val account = MutableLiveData<String>()
    private val password = MutableLiveData<String>()


    /**
     * 登录状态
     */
    var loginStatus = MutableLiveData<Int>()


    companion object {
        val START_LOGIN = 0
        val LOGIN_SUCCESS = 1
        val LOGIN_FAIL = 2
    }


    var accountWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            account.value = s.toString()
        }
    }
    var passwordWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            password.value = s.toString()
        }
    }


    /**
     * 登录操作
     */
    fun login() {
        if (TextUtils.isEmpty(account.value) || TextUtils.isEmpty(password.value)) {
            return
        }
        loginStatus.value = START_LOGIN
        mLoginRepository.login(account.value!!, password.value!!).subscribe(object : BaseHttpSubscriber<VmResultString?>() {
            override fun onSuccess(vm: MutableLiveData<VmResultString?>?) {
                loginStatus.value = if (Constants.LOGIN_SUCCESS == vm?.value!!.result) LOGIN_SUCCESS else LOGIN_FAIL
            }
        })
    }
}