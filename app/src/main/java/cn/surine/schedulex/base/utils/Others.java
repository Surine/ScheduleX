package cn.surine.schedulex.base.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.didikee.donate.AlipayDonate;
import android.net.Uri;

import cn.surine.schedulex.BuildConfig;
import cn.surine.schedulex.R;

import static android.content.Context.CLIPBOARD_SERVICE;
import static cn.surine.schedulex.base.controller.App.context;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-02-02 18:00
 */
public class Others {


    /**
     * 使用支付宝捐助
     *
     * @param activity
     * @param payCode  支付码
     */
    public static void donateAlipay(Activity activity, String payCode) {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(activity);
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(activity, payCode);
        }
    }


    /**
     * 打开链接
     *
     * @param url
     */
    public static void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


    /**
     * 启动QQ
     *
     * @param context
     * @param qqNumber qq号
     */
    public static void startQQ(Context context, String qqNumber) {
        String qq = "com.tencent.mobileqq";
        String tim = "com.tencent.tim";
        Intent intent = new Intent();
        PackageManager packageManager = context.getPackageManager();
        try {
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            cmb.setText(qqNumber);
            intent = packageManager.getLaunchIntentForPackage(qq);
        } catch (Exception e) {
            intent = packageManager.getLaunchIntentForPackage(tim);
        }
        context.startActivity(intent);
    }


    /**
     * 打开coolapk个人主页
     *
     * @param str
     */
    public static void startCoolApk(String str) {
        Intent intent = new Intent();
        try {
            intent.setClassName("com.coolapk.market", "com.coolapk.market.view.AppLinkActivity");
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse("coolmarket://u/" + str));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Toasts.toast(context.getString(R.string.no_coolapk));
            e.printStackTrace();
        }
    }


    /**
     * 获取版本号
     */
    public static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }


    /**
     * 发邮件
     * */
    public static void sendEmail(String msg) {
        // 必须明确使用mailto前缀来修饰邮件地址
        Uri uri = Uri.parse("mailto:surinex@163.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_CC, "2234503567@qq.com"); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT,"解析错误，请输入您的学校"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, msg);// 正文
//        context.startActivity(Intent.createChooser(intent, "欢迎反馈~"));
        context.startActivity(intent);
    }

}
