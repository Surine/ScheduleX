package cn.surine.schedulex.app_widget.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import cn.surine.schedulex.base.utils.Logs;
import cn.surine.schedulex.base.utils.Toasts;

public class KeepService extends Service {
    public static final String ACTION_NAME = "ACTION_NAME";
    public static final String EXTRA_STR = "EXTRA_STR";

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Logs.d("onCreate KeepService");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        Logs.d("onStartCommand KeepService");
        String stringExtra = intent.getStringExtra(ACTION_NAME);
        StringBuilder sb = new StringBuilder();
        sb.append("启动辅助服务！");
        sb.append(stringExtra);
        Toasts.toast(sb.toString());
        Intent intent2 = new Intent(stringExtra);
        String str = EXTRA_STR;
        intent2.putExtra(str, intent.getStringExtra(str));
        BoardCastSender.send(this, intent2);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Logs.d("onDestroy KeepService");
        super.onDestroy();
    }
}
