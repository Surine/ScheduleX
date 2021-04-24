package cn.surine.coursetableview.view

import cn.surine.coursetableview.entity.BCourse


object DataHandler {

    /**
     * 数据处理
     * @param data 初始数据
     * @param currentWeek 当前周
     */
    fun handle(data: MutableList<BCourse>, currentWeek: Int): MutableList<BCourse> {
        val lastList = mutableListOf<BCourse>()
        //按照天，起始节次，是否本周来排序
        data.sortWith(compareBy({ it.day }, { it.sectionStart }))
        //按照天分组
        val dayResult = data.groupBy { it.day }
        for ((_, value) in dayResult) {
            //分组后按照持续节次倒序排序，然后按照起始节次分组
            val sectionStartResult = value.sortedWith(compareBy { -1 * it.sectionContinue })
                    .groupBy { it.sectionStart }
            for ((_,v2) in sectionStartResult){
                //按照是否本周排列
                val sortResult = v2.sortedBy { !it.week.contains(currentWeek) }
                //重叠位置都合一
                val lastResult = sortResult[0].apply {
                    extraCourse = if(sortResult.size > 1) sortResult.subList(1,sortResult.size) else emptyList()
                }
                lastList.add(lastResult)
            }
        }
        return lastList
    }
}