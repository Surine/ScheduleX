package cn.surine.schedulex.ui.view.custom.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.interfaces.Call;

/**
 * Intro：
 * 生成普通对话框
 *
 * @author sunliwei
 * @date 2020-02-06 15:43
 */
public class CommonDialogs {

    /**
     * 获取一个公共对话框
     *
     * @param context    上下文
     * @param title      标题
     * @param msg        文本信息
     * @param okCall     确定回调
     * @param cancelCall 取消回调
     */
    public static Dialog getCommonDialog(Context context, String title, String msg, Call okCall, Call cancelCall) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_common,
                null);
        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimations);
        TextView titleTv = view.findViewById(R.id.title);
        TextView msgTv = view.findViewById(R.id.msg);
        Button okBtn = view.findViewById(R.id.ok);
        Button cancelBtn = view.findViewById(R.id.cancel);
        titleTv.setText(title);
        msgTv.setText(msg);
        okBtn.setOnClickListener(v -> {
            if (okCall != null) {
                okCall.back();
            }
            dialog.dismiss();
        });
        cancelBtn.setOnClickListener(v -> {
            if (cancelCall != null) {
                cancelCall.back();
            }
            dialog.dismiss();
        });
        return dialog;
    }
}
