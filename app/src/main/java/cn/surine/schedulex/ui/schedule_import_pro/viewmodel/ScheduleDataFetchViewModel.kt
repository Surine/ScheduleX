package cn.surine.schedulex.ui.schedule_import_pro.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import cn.surine.schedulex.base.controller.BaseViewModel
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

    companion object {
        //数据库status = -1为维护
        const val LOAD_FAIL_VERSION_OLD = -1 //版本太旧
        const val LOAD_FAIL_MAINTENANCE = -2 //维护
        const val LOAD_FAIL_NULL = -3  //未适配
    }

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
        val data = repository.getCommon()
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
                //未适配
                if (p1 != null || p0 == null) {
                    loadUniversityStatus.value = LOAD_FAIL_NULL
                    return
                }
                //维护中
                if (p0[0].status == -1) {
                    mUniversityInfo.value = p0[0]
                    loadUniversityStatus.value = LOAD_FAIL_MAINTENANCE
                    return
                }
                //版本太旧
                if (p0[0].version > cn.surine.schedulex.BuildConfig.VERSION_CODE) {
                    mUniversityInfo.value = p0[0]
                    loadUniversityStatus.value = LOAD_FAIL_VERSION_OLD
                    return
                }
                mUniversityInfo.value = p0[0]
                loadUniversityStatus.value = LOAD_SUCCESS
            }
        })
    })


    fun uploadFetchSuccess(mUniversity: RemoteUniversity) {
        //通用教务不上报
        if (mUniversity.code.contains("-")) return
        val remoteUniversity = mUniversity.copy()
        remoteUniversity.useTimes++
        remoteUniversity.update(mUniversity.objectId, object : UpdateListener() {
            override fun done(p0: BmobException?) {
            }
        })
    }
}