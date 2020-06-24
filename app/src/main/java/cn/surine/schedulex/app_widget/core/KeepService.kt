package cn.surine.schedulex.app_widget.core

import android.app.Service
import android.content.Intent
import android.os.IBinder
import cn.surine.schedulex.app_widget.core.BoardCastSender.send
import cn.surine.schedulex.base.utils.Logs
import cn.surine.schedulex.base.utils.Toasts.toast

class KeepService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        Logs.d("onCreate KeepService")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, i: Int, i2: Int): Int {
        Logs.d("onStartCommand KeepService")
        val stringExtra = intent.getStringExtra(ACTION_NAME)
        val sb = StringBuilder()
        sb.append("启动辅助服务！")
        sb.append(stringExtra)
        toast(sb.toString())
        val intent2 = Intent(stringExtra)
        val str = EXTRA_STR
        intent2.putExtra(str, intent.getStringExtra(str))
        send(this, intent2)
        return START_STICKY
    }

    override fun onDestroy() {
        Logs.d("onDestroy KeepService")
        super.onDestroy()
    }

    companion object {
        const val ACTION_NAME = "ACTION_NAME"
        const val EXTRA_STR = "EXTRA_STR"
    }
}