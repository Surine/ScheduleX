package cn.surine.schedulex.ui.splash

import androidx.lifecycle.MutableLiveData
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.surine.schedulex.base.controller.BaseViewModel

class SplashViewModel() : BaseViewModel() {
    val mCommonParam: MutableLiveData<CommonParam> by lazy {
        MutableLiveData<CommonParam>()
    }

    fun getCommon() = request({
        if(mCommonParam.value == null){
            val query = BmobQuery<CommonParam>()
            query.setLimit(1)
            query.findObjects(object : FindListener<CommonParam>() {
                override fun done(p0: MutableList<CommonParam>?, p1: BmobException?) {
                    if(p0 != null && p0.size != 0){
                        mCommonParam.value = p0[0]
                    }
                }
            })
        }
    })

}