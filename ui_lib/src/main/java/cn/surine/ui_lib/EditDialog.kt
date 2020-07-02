package cn.surine.ui_lib

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/7/2 15:52
 */
class EditDialog(
        context: Context,
        title: String,
        initValue: String,
        hint: String,
        cancel: Boolean,
        block: EditDialog.() -> Unit
) {

    val dialog: android.app.Dialog
    private var okBtn: Button
    private var cancelBtn: Button
    private var msgEt: EditText

    init {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(
                R.layout.dialog_edit_normal,
                null
        )
        builder.setView(view)
        builder.setCancelable(cancel)
        dialog = builder.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setWindowAnimations(R.style.DialogAnimations)
        val titleTv = view.findViewById<TextView>(R.id.title)
        msgEt = view.findViewById(R.id.editText)
        okBtn = view.findViewById(R.id.positive)
        cancelBtn = view.findViewById(R.id.negative)
        okBtn.setOnClickListener { dialog.dismiss() }
        cancelBtn.setOnClickListener { dialog.dismiss() }
        titleTv.text = title
        msgEt.setText(initValue)
        msgEt.hint = hint
        block()
    }

    fun positive(title: String = "确定", clickEvent: (view: View?, value: String) -> Unit) {
        okBtn.text = title
        okBtn.setOnClickListener { v ->
            if (msgEt.text.toString().isNotEmpty()) {
                clickEvent(v, msgEt.text.toString())
            }
            dialog.dismiss()
        }
    }

    fun negative(title: String = "取消", clickEvent: (view: View?) -> Unit) {
        cancelBtn.text = title
        cancelBtn.setOnClickListener { v ->
            clickEvent(v)
            dialog.dismiss()
        }
    }
}