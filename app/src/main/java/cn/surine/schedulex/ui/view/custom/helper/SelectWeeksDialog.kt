package cn.surine.schedulex.ui.view.custom.helper

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.utils.load
import com.peanut.sdk.miuidialog.MIUIDialog


data class SelectInt(
        val number:Int,
        var select:Boolean = false
){
    fun clear(){
        select = false
    }
}

class SelectWeeksDialog {
    fun show(context: Context, totalWeek:Int = Constants.MAX_WEEK, function:(List<Int>)->Unit){
        val mDatas = List(totalWeek){ SelectInt(it + 1) }
        MIUIDialog(context).show {
            customView(viewRes = R.layout.view_select_weeks) {
                val weekRecyclerView = it.findViewById<SelectWeekRecyclerView>(R.id.recyclerview)
                val single = it.findViewById<Button>(R.id.single)
                val doubleWeek = it.findViewById<Button>(R.id.doubleWeek)
                val all = it.findViewById<Button>(R.id.all)
                single.setOnClickListener {
                    mDatas.map { m-> m.clear() }
                    mDatas.filter { f->
                        f.number % 2 == 1
                    }.map { m->
                        m.select = !m.select
                    }
                    weekRecyclerView.adapter?.notifyDataSetChanged()
                }
                doubleWeek.setOnClickListener {
                    mDatas.map { m-> m.clear() }
                    mDatas.filter { f->
                        f.number % 2 == 0
                    }.map { m->
                        m.select = !m.select
                    }
                    weekRecyclerView.adapter?.notifyDataSetChanged()
                }
                all.setOnClickListener {
                    mDatas.map { m-> m.clear() }
                    mDatas.map { m->
                        m.select = !m.select
                    }
                    weekRecyclerView.adapter?.notifyDataSetChanged()
                }

                weekRecyclerView.load(
                        mLayoutManager = GridLayoutManager(context,5),
                        mAdapter = BaseAdapter(mDatas,R.layout.item_week_number_item,BR.selectInt)
                ){ ad->
                    ad.setOnItemClickListener {pos->
                       mDatas[pos].select = !mDatas[pos].select
                       ad.notifyItemChanged(pos)
                    }
                }
                var prePosition = -1
                weekRecyclerView.eventListener = {
                    position,eventDown ->
                    if(position >= 0 && position < mDatas.size){
                        if(prePosition != position || eventDown){
                            mDatas[position].select = !mDatas[position].select
                            weekRecyclerView.adapter?.notifyItemChanged(position)
                            Log.d("slw", "show: $position - $eventDown")
                            if(prePosition != position){
                                prePosition = position
                            }
                        }
                    }
                }
            }
            positiveButton(text = "确定") {
                function(mDatas.filter { m-> m.select }.map { m -> m.number })
            }
        }
    }
}


class SelectWeekRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : RecyclerView(context, attrs, defStyleAttr) {

    private fun outOfBounds(x: Float, y: Float) = x > width || x < 0 || y > height || y < 0

    var eventListener :(Int,Boolean)->Unit = {a,b -> }

    private fun getPosition(x:Int, y: Int) = x / (width / 5) + y / (width / 5) * 5

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        e?:return true
        if(outOfBounds(e.x,e.y)) return true
        when(e.action){
            MotionEvent.ACTION_DOWN -> {
                eventListener(getPosition(e.x.toInt(),e.y.toInt()),true)
            }
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_UP-> {
                eventListener(getPosition(e.x.toInt(),e.y.toInt()),false)
            }
        }
        return true
    }
}