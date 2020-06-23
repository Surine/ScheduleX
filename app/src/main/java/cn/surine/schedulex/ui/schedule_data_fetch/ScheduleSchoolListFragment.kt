package cn.surine.schedulex.ui.schedule_data_fetch

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.third_parse.AdapterList
import cn.surine.schedulex.third_parse.JwInfo
import kotlinx.android.synthetic.main.fragment_schedule_school_list.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/22 11:41
 */
class ScheduleSchoolListFragment : BaseFragment() {
    companion object{
        const val URL = "URL"
        const val TYPE = "TYPE"
    }
    var mJwSystemDatas = ArrayList<JwInfo>()
    var mSchoolDatas =  ArrayList<JwInfo>()
    override fun onInit(parent: View?) {
        //教务列表
        systemList.layoutManager = LinearLayoutManager(activity(), LinearLayoutManager.HORIZONTAL, false)
        systemList.adapter = BaseAdapter<JwInfo>(mJwSystemDatas, R.layout.item_jw_system, cn.surine.schedulex.BR.system)
        mJwSystemDatas.apply {
            clear()
            addAll(AdapterList.systemlist)
        }
        systemList.adapter?.notifyDataSetChanged()
        val systemAdapter = systemList.adapter as BaseAdapter<*>
        systemAdapter.setOnItemClickListener {
            Navigations.open(this,R.id.action_scheduleSchoolListFragment_to_scheduleThirdFetchFragment, Bundle().apply {
                putString(TYPE,mJwSystemDatas[it].jwType)
            })
        }
        //学校列表
        schoolList.layoutManager = LinearLayoutManager(activity())
        schoolList.adapter = BaseAdapter<JwInfo>(mSchoolDatas, R.layout.item_school_list, cn.surine.schedulex.BR.school)
        mSchoolDatas.apply {
            clear()
            addAll(AdapterList.schoolList)
        }
        schoolList.adapter?.notifyDataSetChanged()
        val schoolAdapter = schoolList.adapter as BaseAdapter<*>
        schoolAdapter.setOnItemClickListener {
            when(mSchoolDatas[it].jwType){
                JwInfo.TUST -> Navigations.open(this,R.id.action_scheduleSchoolListFragment_to_loginFragment)
                else -> {
                    val bundle = Bundle().apply {
                        val data = mSchoolDatas[it]
                        putString(TYPE,data.jwType)
                        putString(URL,data.jwUrl)
                    }
                    Navigations.open(this,R.id.action_scheduleSchoolListFragment_to_scheduleThirdFetchFragment,bundle)
                }
            }
        }
    }


    override fun layoutId(): Int = R.layout.fragment_schedule_school_list

}