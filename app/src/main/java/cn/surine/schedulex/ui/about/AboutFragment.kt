package cn.surine.schedulex.ui.about


import android.view.View
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Others
import cn.surine.schedulex.base.utils.Toasts.toast
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/25 12:06
 */
class AboutFragment : BaseFragment() {
    private var rotate = 0F
    override fun layoutId(): Int = R.layout.fragment_about

    override fun onInit(parent: View?) {
        aboutItemQQ.setOnClickListener {
            Others.startQQ(activity(), "686976115")
            toast(getString(R.string.qq_copy))
        }
        github.setOnClickListener {
            toast("求个star，感谢啦！")
            Others.openUrl("https://github.com/surine/ScheduleX")
        }
        alipay.setOnClickListener {
            toast("开心到飞起~~~")
            Others.donateAlipay(activity(), "fkx00798tue4qrncwkknh09")
        }
        aboutItemCoolApk.setOnClickListener {
            Others.startCoolApk("667393")
        }
        versionSlogan.text = getString(R.string.version_slogan, Others.getAppVersion())
        loadAnimation()
        jetpack.setOnClickListener {
            Beta.checkUpgrade(true, false)
            loadAnimation()
            loadTest()
        }
    }

    private fun loadTest() {
//        dialog(title = "提示",message = "文本内容") {
//        }
//        editDialog(title = "消息"){
//            positive("记录"){
//                _, value -> toast(value)
//            }
//        }
    }

    private fun loadAnimation() {
        rotate = if (rotate == 0F) {
            360F
        } else {
            0F
        }
        val springForce = SpringForce(rotate)
        springForce.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
        springForce.stiffness = SpringForce.STIFFNESS_VERY_LOW
        val animation = SpringAnimation(jetpack, SpringAnimation.ROTATION_Y)
        animation.spring = springForce
        animation.start()
    }
}