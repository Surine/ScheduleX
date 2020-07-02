package cn.surine.ui_lib

import Setting
import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/7/2 14:54
 */

/**
 * 创建一个对话框
 * */
fun Fragment.dialog(title: String = "提示", message: String = "", cancel: Boolean = true, block: Dialog.() -> Unit) {
    Dialog(this.activity as? Context ?: return, title, message, cancel, block).dialog.show()
}

/**
 * 创建一个编辑对话框
 * */
fun Fragment.editDialog(title: String = "编辑", initValue: String = "", hint: String = "", cancel: Boolean = true, block: EditDialog.() -> Unit) {
    EditDialog(this.activity as? Context
            ?: return, title, initValue, hint, cancel, block).dialog.show()
}


/**
 * 创建一个设置项
 * */
fun Fragment.setting(view: ViewGroup, block: Setting.() -> Unit) {
    val setting = Setting(this.activity as? Context ?: return)
    setting.block()
    view.addView(setting.build())
}
