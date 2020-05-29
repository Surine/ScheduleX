package cn.surine.schedulex.ui.view.custom.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.interfaces.Call;
import cn.surine.schedulex.base.interfaces.DCall;

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


    /**
     * 修改框
     *
     * @param context
     * @param text       文本
     * @param dCall      确认call
     * @param cancelCall 取消call
     */
    public static Dialog getEditDialog(Context context, String text, boolean isText, DCall<String> dCall, Call cancelCall) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_edit_layout,
                null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimations);

        EditText editText = view.findViewById(R.id.editText);
        if (!isText) {
            editText.setHint(text);
        } else {
            editText.setText(text);
        }

        Button okBtn = view.findViewById(R.id.ok);
        Button cancelBtn = view.findViewById(R.id.cancel);
        okBtn.setOnClickListener(v -> {
            if (dCall != null && !TextUtils.isEmpty(editText.getText().toString())) {
                dCall.back(editText.getText().toString());
            }
            dialog.dismiss();
        });
        cancelBtn.setOnClickListener(v -> {
            if (cancelCall != null) {
                cancelCall.back();
            }
            dialog.dismiss();
        });
        dialog.show();
        return dialog;
    }


    /**
     * 时间选择框
     */
    public static Dialog timePickerDialog(Context context, String title, int hour, int minute, TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        TimePickerDialog dialog = new TimePickerDialog(context, onTimeSetListener, hour, minute, true);
        dialog.setTitle(title);
        return dialog;
    }


    /**
     * 基础自定义对话框
     * */
    public static Dialog getBaseConfig(Context context, View view, DialogCall dialogCall) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimations);
        if (dialogCall != null) {
            dialogCall.onDialogCall(view, dialog);
        }
        dialog.show();
        return dialog;
    }


    public interface DialogCall {
        void onDialogCall(View view, Dialog dialog);
    }
}
