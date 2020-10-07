package cn.surine.schedulex.ui.schedule_import_pro.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.surine.schedulex.base.controller.BaseViewModel
import cn.surine.schedulex.data.entity.Commons
import cn.surine.schedulex.ui.schedule_import_pro.data.RemoteUniversity
import cn.surine.schedulex.ui.schedule_import_pro.repository.ScheduleDataFetchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

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

    /**
     * 获取公共配置参数
     * */
    fun getCommon() = request({
        val data = ScheduleDataFetchRepository.getCommon()
        mCommons.value = data
    })

    fun getUniversityInfo(name: String, code: String) = request({
//        val data = ScheduleDataFetchRepository.getUniversityInfo(name,code)
        withContext(Dispatchers.IO) {
            delay(1000)
        }
        mUniversityInfo.value = RemoteUniversity(
                code = "1111",
                name = "武汉纺织大学",
                eng = "WTU",
                importType = 1,
                opInfo = "登录至大厅后找到教务系统应用->进入后点击下方的学生课表查询->允许页面跳转->填写好想要导入的学年与学期，点击下方导入即可。",
                jwUrl =  "https://auth.wtu.edu.cn/authserver/login?service=http%3A%2F%2Fehall.wtu.edu.cn%2Flogin%3Fservice%3Dhttp%3A%2F%2Fehall.wtu.edu.cn%2Fnew%2Findex.html",
                author = "花生酱啊",
                useTimes = 30,
                jwSystemName = "新正方",
                jwSystem = "newZenFang"
        )

//        "school": "WTU",
//        "system": "newZenFang",
//        "name": "武汉纺织大学",
//        "url":
//        "author":
//        "belong":"",
//        "doc":
//        "version":104
    })
}