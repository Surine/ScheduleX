package cn.surine.schedulex.app_widget;

import android.content.Context;
import android.content.Intent;

import cn.surine.schedulex.app_widget.data.Actions;

public class BoardCastSender {
    public static void send(Context context, Intent intent) {
        context.sendBroadcast(intent);
    }

    public static void notifyWidget(Context context) {
        context.sendBroadcast(new Intent(Actions.UPDATE));
    }
}
