package cn.surine.schedulex.base.utils

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-02-04 17:57
 */
open class MySeekBarChangeListener : OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}