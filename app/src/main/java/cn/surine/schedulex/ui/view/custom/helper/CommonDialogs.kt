package cn.surine.schedulex.ui.view.custom.helper

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import cn.surine.schedulex.R

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 11:51
 */
object CommonDialogs {

    /**
     * 获取一个公共对话框
     *
     * @param context    上下文
     * @param title      标题
     * @param msg        文本信息
     * @param okCall     确定回调
     * @param cancelCall 取消回调
     */
    @SuppressLint("InflateParams")
    fun getCommonDialog(context: Context, title: String, msg: String, okCall: () -> Unit = {}, cancelCall: () -> Unit = {}): Dialog {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.view_dialog_common,
                null)
        builder.setView(view)
        val dialog: Dialog = builder.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setWindowAnimations(R.style.DialogAnimations)
        val titleTv = view.findViewById<TextView>(R.id.title)
        val msgTv = view.findViewById<TextView>(R.id.msg)
        val okBtn = view.findViewById<Button>(R.id.ok)
        val cancelBtn = view.findViewById<Button>(R.id.cancel)
        titleTv.text = title
        msgTv.text = msg
        okBtn.setOnClickListener {
            okCall()
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            cancelCall()
            dialog.dismiss()
        }
        return dialog
    }


    /**
     * 修改框
     *
     * @param context
     * @param text       文本
     * @param dCall      确认call
     * @param cancelCall 取消call
     */
    @SuppressLint("InflateParams")
    fun getEditDialog(context: Context, text: String, isText: Boolean, okCall: (str: String) -> Unit, cancelCall: () -> Unit = {}): Dialog {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.view_edit_layout,
                null)
        builder.setView(view)
        val dialog: Dialog = builder.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setWindowAnimations(R.style.DialogAnimations)
        val editText = view.findViewById<EditText>(R.id.editText)
        if (!isText) {
            editText.hint = text
        } else {
            editText.setText(text)
        }
        val okBtn = view.findViewById<Button>(R.id.ok)
        val cancelBtn = view.findViewById<Button>(R.id.cancel)
        okBtn.setOnClickListener {
            okCall(editText.text.toString())
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            cancelCall()
            dialog.dismiss()
        }
        dialog.show()
        return dialog
    }

    /**
     * 时间选择框
     */
    fun timePickerDialog(context: Context, title: String, hour: Int, minute: Int, onTimeSetListener: (view: TimePicker, hourOfDay: Int, minute: Int) -> Unit) = TimePickerDialog(context, OnTimeSetListener { view, hourOfDay, minutes -> onTimeSetListener(view, hourOfDay, minutes) }, hour, minute, true).apply {
        setTitle(title)
    }


    /**
     * 基础自定义对话框
     */
    fun getBaseConfig(context: Context?, view: View, dialogCall: (view: View, dialog: Dialog) -> Unit): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        val dialog: Dialog = builder.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setWindowAnimations(R.style.DialogAnimations)
        dialogCall(view, dialog)
        dialog.show()
        return dialog
    }
}