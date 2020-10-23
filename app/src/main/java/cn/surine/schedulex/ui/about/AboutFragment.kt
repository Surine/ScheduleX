package cn.surine.schedulex.ui.about


import android.view.View
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.hit
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Others
import cn.surine.schedulex.base.utils.Toasts.toast
import cn.surine.schedulex.base.utils.Toasts.toastLong
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
    override fun layoutId(): Int = R.layout.fragment_about

    override fun onInit(parent: View?) {
        versionSlogan.text = getString(R.string.version_slogan, Others.getAppVersion())
        aboutItemQQ.setOnClickListener {
            hit("qq")
            Others.startQQ(activity(), "686976115")
            toast(getString(R.string.qq_copy))
        }
        github.setOnClickListener {
            hit("github")
            toast("求个star，感谢啦！")
            Others.openUrl("https://github.com/surine/ScheduleX")
        }
        alipay.setOnClickListener {
            hit("alipay")
            toast("开心到飞起~~~")
            Others.donateAlipay(activity(), "fkx00798tue4qrncwkknh09")
        }
        wechat.setOnClickListener {
            hit("wechat")
            toastLong("感谢捐赠，请在扫码界面，选择相册里的微信收款码（**伟）进行识别。")
            Others.donateWeixin(activity)
        }

        aboutItemCoolApk.setOnClickListener {
            hit("coolapk")
            Others.startCoolApk("667393")
        }
        settingUpdate.setOnClickListener {
            hit("update")
            Beta.checkUpgrade(true, false)
        }
        aboutItemFeedBack.setOnClickListener {
            hit("feedback")
            Others.openUrl("https://support.qq.com/product/282532?d-wx-push=1")
        }
        settingShare.setOnClickListener {
            hit("share")
            Others.share(activity, "嗨，这位小伙伴。欢迎使用Schedulex课程表，点击链接 https://www.coolapk.com/apk/cn.surine.schedulex 查看详细信息~")
        }
        opensourse.setOnClickListener {
            hit("opensourse")
            MIUIDialog(activity()).show {
                message(res = R.string.open_source_license)
            }
        }
    }
}