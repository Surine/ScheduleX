package cn.surine.schedulex.miai_import

import android.os.Bundle
import android.view.View
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Navigations
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
                Navigations.open(this,R.id.action_miAiInitFragment_to_miAiFetchFragment, Bundle().apply {
                    putString(MI_AI_SHARE_URL,message.text.toString())
                })
            }
        }
    }

    override fun layoutId() = R.layout.fragment_miai_init
}