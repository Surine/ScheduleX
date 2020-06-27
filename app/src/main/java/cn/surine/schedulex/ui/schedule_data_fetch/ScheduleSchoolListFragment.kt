package cn.surine.schedulex.ui.schedule_data_fetch

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.load
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
    companion object {
        const val URL = "URL"
        const val TYPE = "TYPE"
    }

    private lateinit var adapterListViewModel: AdapterListViewModel
    private var mJwSystemDatas = ArrayList<JwInfo>()
    private var mSchoolDatas = ArrayList<JwInfo>()
    override fun layoutId(): Int = R.layout.fragment_schedule_school_list

    override fun onInit(parent: View?) {
        VmManager(this).apply {
            adapterListViewModel = vmAdapterList
        }
//        schoolTopbar.setFunctionIcon(R.drawable.ic_help_outline_black_24dp)
//        schoolTopbar.getFunctionView().setOnClickListener {
//            Toasts.toast("帮助")
//        }

        //教务列表
        systemList.load(LinearLayoutManager(activity(), LinearLayoutManager.HORIZONTAL, false), BaseAdapter(mJwSystemDatas, R.layout.item_jw_system, cn.surine.schedulex.BR.system)) {
            it.setOnItemClickListener {
                //注意 arguments?:Bundle() 写法的优先级
                Navigations.open(this, R.id.action_scheduleSchoolListFragment_to_scheduleThirdFetchFragment, (arguments
                        ?: Bundle()).apply {
                    putString(TYPE, mJwSystemDatas[it].system)
                })
            }
        }
        mJwSystemDatas.apply {
            clear()
            addAll(AdapterList.systemlist)
        }
        systemList.adapter?.notifyDataSetChanged()

        //学校列表
        schoolList.load(LinearLayoutManager(activity()), BaseAdapter(mSchoolDatas, R.layout.item_school_list, cn.surine.schedulex.BR.school)) {
            it.setOnItemClickListener { position ->
                when (mSchoolDatas[position].system) {
                    JwInfo.TUST -> Navigations.open(this, R.id.action_scheduleSchoolListFragment_to_loginFragment, (arguments
                            ?: Bundle()))
                    else -> {
                        Navigations.open(this, R.id.action_scheduleSchoolListFragment_to_scheduleThirdFetchFragment, (arguments
                                ?: Bundle()).apply {
                            val data = mSchoolDatas[position]
                            putString(TYPE, data.system)
                            putString(URL, data.url)
                        })
                    }
                }
            }
        }

        //请求
        adapterListViewModel.apply {
            getAdapterList()
            adapterListData.observe(this@ScheduleSchoolListFragment, Observer {
                mSchoolDatas.apply {
                    clear()
                    addAll(it)
                }
                schoolList.adapter?.notifyDataSetChanged()
            })
        }
    }
}