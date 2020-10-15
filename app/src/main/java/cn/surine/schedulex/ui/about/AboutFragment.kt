package cn.surine.schedulex.ui.about


import android.view.View
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Others
import cn.surine.schedulex.base.utils.Toasts.toast
import cn.surine.schedulex.base.utils.Toasts.toastLong
import cn.surine.schedulex.base.utils.click
import cn.surine.schedulex.base.utils.init
import cn.surine.schedulex.base.utils.ui
import com.peanut.sdk.miuidialog.MIUIDialog
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
        init {
            loadAnimation()
        }

        ui {
            versionSlogan.text = getString(R.string.version_slogan, Others.getAppVersion())
        }

        click {
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
            wechat.setOnClickListener {
                toastLong("感谢捐赠，请在扫码界面，选择相册里的微信收款码（**伟）进行识别。")
                Others.donateWeixin(activity)
            }

            aboutItemCoolApk.setOnClickListener {
                Others.startCoolApk("667393")
            }
            settingUpdate.setOnClickListener {
                Beta.checkUpgrade(true, false)
            }
            aboutItemFeedBack.setOnClickListener {
                Others.openUrl("https://support.qq.com/product/282532?d-wx-push=1")
            }
            settingShare.setOnClickListener {
                Others.share(activity, "嗨，这位小伙伴。欢迎使用Schedulex课程表，点击链接 https://www.coolapk.com/apk/cn.surine.schedulex 查看详细信息~")
            }
            opensourse.setOnClickListener {
                MIUIDialog(activity()).show {
                    message(res = R.string.open_source_license)
                }
            }
        }
    }


    private fun loadAnimation() {
        SpringAnimation(jetpack, SpringAnimation.ROTATION_Y).apply {
            spring = SpringForce(if (rotate == 0F) 360F else 0F).apply {
                dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
                stiffness = SpringForce.STIFFNESS_VERY_LOW
            }
            start()
        }
    }
}