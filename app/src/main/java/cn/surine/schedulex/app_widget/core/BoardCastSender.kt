package cn.surine.schedulex.app_widget.core

import android.content.Context
import android.content.Intent
import cn.surine.schedulex.app_widget.data.Actions

object BoardCastSender {

    fun send(context: Context, intent: Intent?) {
        context.sendBroadcast(intent)
    }

    fun notifyWidget(context: Context) {
        context.sendBroadcast(Intent(Actions.UPDATE))
    }
}