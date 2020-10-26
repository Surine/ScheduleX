package cn.surine.schedulex.ui.schedule_import_pro.page.change_school

import android.text.Editable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.SELECT_SCHOOL
import cn.surine.schedulex.app_base.hit
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Prefs
import cn.surine.schedulex.base.utils.SimpleTextWatcher
import cn.surine.schedulex.base.utils.load
import cn.surine.schedulex.ui.schedule_import_pro.model.LocalUniversity
import cn.surine.schedulex.ui.schedule_import_pro.util.LocalUniversityManager
import kotlinx.android.synthetic.main.fragment_select_school.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/7/20 17:39
 */
class SelectSchoolFragment : BaseFragment() {

    companion object {
        const val CUR_SCHOOL_CODE = "CUR_SCHOOL_CODE"
        const val CUR_SCHOOL_NAME = "CUR_SCHOOL_NAME"
    }

    val data: MutableList<LocalUniversity> = mutableListOf()
    override fun onInit(parent: View?) {
        searchResult.load(LinearLayoutManager(activity), BaseAdapter(data, R.layout.item_local_university, BR.university)) {
            it.setOnItemClickListener { pos ->
                hit(SELECT_SCHOOL,"school_name" to data[pos].name)
                Prefs.save(CUR_SCHOOL_CODE, data[pos].code)
                Prefs.save(CUR_SCHOOL_NAME, data[pos].name)
                Navigations.close(this)
            }
        }
        searchEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    data.clear()
                    searchResult.adapter?.notifyDataSetChanged()
                    return
                }
                data.clear()
                for (i in LocalUniversityManager.search(s.toString())) {
                    i ?: continue
                    data.add(i)
                }
                searchResult.adapter?.notifyDataSetChanged()
            }
        })

    }

    override fun layoutId() = R.layout.fragment_select_school

}