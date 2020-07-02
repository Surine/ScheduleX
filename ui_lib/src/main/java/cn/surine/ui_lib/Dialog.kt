package cn.surine.ui_lib

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/29 17:09
 */
class Dialog(
    context: Context,
    title: String,
    msg: String,
    cancel: Boolean,
    block: Dialog.() -> Unit
) {
    val dialog: android.app.Dialog
    private var okBtn:Button
    private var cancelBtn:Button

    init {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_normal,
            null
        )
        builder.setView(view)
        builder.setCancelable(cancel)
        dialog = builder.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setWindowAnimations(R.style.DialogAnimations)
        val titleTv = view.findViewById<TextView>(R.id.title)
        val msgTv = view.findViewById<TextView>(R.id.msg)
        okBtn = view.findViewById(R.id.positive)
        cancelBtn  = view.findViewById(R.id.negative)
        okBtn.setOnClickListener { dialog.dismiss() }
        cancelBtn.setOnClickListener { dialog.dismiss() }
        titleTv.text = title
        msgTv.text = msg
        block()
    }

    fun positive(title:String = "确定",clickEvent:(view: View?)->Unit){
        okBtn.text = title
        okBtn.setOnClickListener { v ->
            clickEvent(v)
            dialog.dismiss()
        }
    }

    fun negative(title:String = "取消",clickEvent:(view: View?)->Unit){
        cancelBtn.text = title
        cancelBtn.setOnClickListener { v ->
            clickEvent(v)
            dialog.dismiss()
        }
    }
}