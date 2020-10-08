package cn.surine.schedulex.ui.schedule_import_pro.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import cn.surine.schedulex.base.controller.BaseViewModel
import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.data.entity.Commons
import cn.surine.schedulex.ui.schedule_import_pro.model.RemoteUniversity
import cn.surine.schedulex.ui.schedule_import_pro.repository.ScheduleDataFetchRepository

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/16/20 16:10
 */
class ScheduleDataFetchViewModel(val repository: ScheduleDataFetchRepository) : BaseViewModel() {
    val mCommons: MutableLiveData<Commons> by lazy {
        MutableLiveData<Commons>()
    }

    val mUniversityInfo: MutableLiveData<RemoteUniversity> by lazy {
        MutableLiveData<RemoteUniversity>()
    }

    val loadUniversityStatus: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    /**
     * 获取公共配置参数
     * */
    fun getCommon() = request({
        val data = ScheduleDataFetchRepository.getCommon()
        mCommons.value = data
    })


    /**
     * 临时使用bmob查询数据
     * */
    fun getUniversityInfo(name: String, code: String) = request({
        loadUniversityStatus.value = START_LOAD
        val query = BmobQuery<RemoteUniversity>()
        query.apply {
            addWhereEqualTo("name", name)
            addWhereEqualTo("code", code)
        }
        query.setLimit(1)
        query.findObjects(object : FindListener<RemoteUniversity>() {
            override fun done(p0: MutableList<RemoteUniversity>?, p1: BmobException?) {
                if (p1 != null || p0 == null) {
                    loadUniversityStatus.value = LOAD_FAIL
                    return
                }
                mUniversityInfo.value = p0[0]
                loadUniversityStatus.value = LOAD_SUCCESS
            }
        })
    })


    fun uploadFetchSuccess(mUniversity: RemoteUniversity) {
        val remoteUniversity = mUniversity.copy()
        remoteUniversity.useTimes++
        remoteUniversity.update(mUniversity.objectId, object : UpdateListener() {
            override fun done(p0: BmobException?) {
            }
        })
    }
}