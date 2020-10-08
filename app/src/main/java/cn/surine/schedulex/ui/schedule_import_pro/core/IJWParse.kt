package cn.surine.schedulex.ui.schedule_import_pro.core

import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import org.jsoup.nodes.Element

/**
 * Intro：
 * 通用教务解析父类接口，所有解析器均需要实现此接口
 * @author sunliwei
 * @date 9/21/20 15:26
 */
interface IJWParse:ICommonParse {

    /**
     * 需实现方法
     * @param html 网页源文件
     * @return CourseWrapper课程数据
     * */
    fun parse(html: String): List<CourseWrapper>

    /**
     * 提供该方法签名为了应对细微不同的教务系统的个性化定制。
     * 比如正方教务可以有多个学校使用，但是每个学校根据自己的特点会有细微变化，为了
     * 避免写大量重复代码，可以直接继承主正方解析器，复写此方法来修改数据 @see Zf.kt
     *
     * @param element 节点信息
     * @param courseList 空课程集合（注意：当前集合为空）
     * @param trIndex 横向坐标
     * @param tdIndex 纵向坐标
     * */
    fun parseInfo(element: Element, courseList: MutableList<CourseWrapper>, trIndex: Int, tdIndex: Int) {
    }
}