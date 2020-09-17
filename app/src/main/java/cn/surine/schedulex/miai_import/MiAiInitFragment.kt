package cn.surine.schedulex.miai_import

import android.view.View
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Others
import kotlinx.android.synthetic.main.fragment_miai_init.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/11/20 18:53
 */
class MiAiInitFragment :BaseFragment(){
    companion object{
        const val MI_AI_SHARE_URL = "MI_AI_SHARE_URL"
    }
    override fun onInit(parent: View?) {
        parse.setOnClickListener {
            if(message.text.toString().isNotEmpty()){
                Navigations.open(this,R.id.action_miAiInitFragment_to_miAiFetchFragment, arguments?.apply {
                    putString(MI_AI_SHARE_URL,message.text.toString())
                })
            }
        }
        importMiaiHelp.setOnClickListener {
            Others.openUrl("https://support.qq.com/products/282532/faqs/79786")
        }
    }

    override fun layoutId() = R.layout.fragment_miai_init
}