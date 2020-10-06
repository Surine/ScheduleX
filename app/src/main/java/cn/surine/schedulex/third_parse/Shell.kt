package cn.surine.schedulex.third_parse

import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.third_parse.bean.Course
import cn.surine.schedulex.ui.schedule_import_pro.data.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup

object Shell {

    fun maintaining(html: String): List<Course> {
        Toasts.toast("解析器维修中~")
        return emptyList()
    }

    fun NewZFParser(html: String): List<Course> {
        val courseList = arrayListOf<Course>()
        val doc = Jsoup.parse(html)
        val table1 = doc.getElementById("table1")
        val trs = table1.getElementsByTag("tr")
        var node = 0
        var day = 0
        var teacher = ""
        var room = ""
        var step = 0
        var startWeek = 0
        var endWeek = 0
        var type = 0
        var timeStr = ""
        val nodeRegex = Regex("""\(.*节\)""")
        for (tr in trs) {
            val nodeStr = tr.getElementsByClass("festival").text()
            if (nodeStr.isEmpty()) {
                continue
            }
            node = nodeStr.toInt()

            val tds = tr.getElementsByTag("td")
            for (td in tds) {
                val divs = td.getElementsByTag("div")
                for (div in divs) {
                    val courseValue = div.text().trim()

                    if (courseValue.length <= 1) {
                        continue
                    }

                    val courseName = div.getElementsByClass("title").text()
                    if (courseName.isEmpty()) {
                        continue
                    }

                    day = td.attr("id")[0].toString().toInt()

                    val pList = div.getElementsByTag("p")
                    val weekList = arrayListOf<String>()
                    pList.forEach { e ->
                        when (e.getElementsByAttribute("title").attr("title")) {
                            "教师" -> teacher = e.text().trim()
                            "上课地点" -> room = e.text().trim()
                            "节/周", "周/节" -> {
                                timeStr = e.text().trim()
                                val result = Common.nodePattern.find(timeStr)
                                if (result != null) {
                                    val nodeInfo = result.value
                                    val nodes = nodeInfo.substring(1, nodeInfo.length - 1).split("-".toRegex())
                                            .dropLastWhile { it.isEmpty() }

                                    if (nodes.isNotEmpty()) {
                                        node = nodes[0].toInt()
                                    }
                                    if (nodes.size > 1) {
                                        val endNode = nodes[1].toInt()
                                        step = endNode - node + 1
                                    }
                                }
                                weekList.clear()
                                weekList.addAll(nodeRegex.replace(timeStr, "").split(','))
                            }
                        }

                    }

                    weekList.forEach {
                        if (it.contains('-')) {
                            val weeks = it.substring(0, it.indexOf('周')).split('-')
                            if (weeks.isNotEmpty()) {
                                startWeek = weeks[0].toInt()
                            }
                            if (weeks.size > 1) {
                                endWeek = weeks[1].toInt()
                            }

                            type = when {
                                it.contains('单') -> 1
                                it.contains('双') -> 2
                                else -> 0
                            }
                        } else {
                            try {
                                startWeek = it.substring(0, it.indexOf('周')).toInt()
                                endWeek = it.substring(0, it.indexOf('周')).toInt()
                            } catch (e: Exception) {
                                startWeek = 1
                                endWeek = 20
                            }
                        }

                        courseList.add(
                                Course(
                                        name = courseName, room = room,
                                        teacher = teacher, day = day,
                                        startNode = node, endNode = node + step - 1,
                                        startWeek = startWeek, endWeek = endWeek,
                                        type = type
                                )
                        )
                    }
                }
            }
        }
        val table2 = doc.getElementById("sycjlrtabGrid")
        if (table2 != null) {
            val trs2 = table2.getElementsByTag("tr")
            for (tr in trs2) {
                val tds = tr.getElementsByTag("td")
                for (td in tds) {
                    val kcmc = td.attr("aria-describedby", "sycjlrtabGrid_kcmc")
                    val timeAndPlace = td.attr("aria-describedby", "sycjlrtabGrid_sksjdd")
                    if (kcmc == null || timeAndPlace == null) continue
                    val title = td.attr("aria-describedby", "sycjlrtabGrid_xmmc")
                }
            }
        }
        return courseList
    }

    fun pku(html: String): List<Course> {
        val courseList = arrayListOf<Course>()
        val doc = Jsoup.parse(html)
        val kbtable = doc.select("table[class=datagrid]").first()
        val tBody = kbtable.getElementsByTag("tbody").first()
        var teacher = ""
        for (tr in tBody.getElementsByTag("tr")) {
            val tds = tr.getElementsByTag("td")
            if (tds.size >= 11) {
                if (tds[8].text().contains('未'))
                    continue
                val courseName = tds[0].text().trim()
                teacher = tds[4].text().trim()
                val timeInfos = tds[7].html().split("<br>")
                var startWeek = 1
                var endWeek = 16
                var startNode = 1
                var endNode = 2
                var type = 0
                var day = 7
                timeInfos.forEach {
                    val timeInfo = Jsoup.parse(it).text().trim().split(' ')
                    if (timeInfo.size >= 2) {
                        if (timeInfo[0].contains('~')) {
                            startWeek = timeInfo[0].substringBefore('~').toInt()
                            endWeek = timeInfo[0].substringAfter('~').substringBefore('周').toInt()
                        }
                        type = when {
                            timeInfo[1].contains('单') -> 1
                            timeInfo[1].contains('双') -> 2
                            else -> 0
                        }
                        Common.chineseWeekList.forEachIndexed { index, s ->
                            if (index != 0) {
                                if (timeInfo[1].contains(s)) {
                                    day = index
                                    return@forEachIndexed
                                }
                            }
                        }
                        val matchResult = Common.nodePattern1.find(timeInfo[1])
                        if (matchResult != null) {
                            val m = matchResult.value
                            startNode = m.substringBefore('~').toInt()
                            endNode = m.substringAfter('~').substringBefore('节').toInt()
                        }
                        val room = if (timeInfo.size >= 3) {
                            timeInfo[2]
                        } else {
                            timeInfo[1].substringAfter('(').substringBefore(')')
                        }
                        courseList.add(
                                Course(
                                        name = courseName, day = day, room = room,
                                        teacher = teacher, startNode = startNode,
                                        endNode = endNode, startWeek = startWeek,
                                        endWeek = endWeek, type = type
                                )
                        )
                    }
                }
            }
        }
        return courseList
    }


    //北方工业大学
    fun ncut(html: String): List<CourseWrapper> {
        val courseList = arrayListOf<CourseWrapper>()
        val element = Jsoup.parse(html).getElementById("Table6")
        //表格行
        val trs = element.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            //第一行为表头
            if (trIndex == 0) continue
            //每一行的列
            val tr = trs[trIndex]
            val tds = tr.getElementsByTag("td")
            for (tdIndex in tds.indices) {
                if (tdIndex == 0) continue
                val td = tds[tdIndex]
                val p = td.getElementsByTag("p").first()
                //跳过空白p标签
                if (p.html() == """&nbsp;""") continue
                //p标签中会包含多个a标签
                for (aInfo in p.getElementsByTag("a")) {
                    val courseInfo = aInfo.textNodes()
                    val weekInfo = aInfo.getElementsByTag("font").textNodes()[1].text()
                    val modifyWeekInfo = weekInfo.substring(1, weekInfo.length - 1)
                    val courseWrapper = CourseWrapper()
                    courseWrapper.name = courseInfo[0].text()
                    courseWrapper.position = courseInfo[1].text()
                    courseWrapper.teacher = courseInfo[2].text()
                    courseWrapper.day = tdIndex
                    courseWrapper.sectionStart = trIndex * 2 - 1
                    courseWrapper.sectionContinue = 2
                    val startWeek = modifyWeekInfo.substringBefore("-")
                    val endWeek = modifyWeekInfo.substringAfter("-")
                    val weekResult = when {
                        endWeek.contains("单") -> {
                            (startWeek.toInt()..endWeek.removeSuffix("单周").toInt()).toList().filter { it % 2 == 1 }
                        }
                        endWeek.contains("双") -> {
                            (startWeek.toInt()..endWeek.removeSuffix("双周").toInt()).toList().filter { it % 2 == 0 }
                        }
                        else -> {
                            (startWeek.toInt()..endWeek.removeSuffix("周").toInt()).toList()
                        }
                    }
                    courseWrapper.week = weekResult
                    courseList.add(courseWrapper)
                }
            }
        }
        return courseList
    }

    //正方教务
    fun zf(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementById("Table1")
        //表格行
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            if (trIndex <= 1) continue  //清除表头（包括早晨一行）
            val tr = trs[trIndex]
            val tds = tr.getElementsByTag("td")
            for (tdIndex in tds.indices) {
                var nodes = tds[tdIndex].textNodes()
                if (nodes.size % 4 == 0) {
                    while (nodes.size != 0) {
                        courseList.add(CourseWrapper().apply {
                            name = nodes[0].text()
                            position = nodes[3].text()
                            teacher = nodes[2].text()
                            day = ParseUtil.getDayInfoByStr(nodes[1].text().substring(0, 2))
                            week = ParseUtil.getWeekInfoByStr(nodes[1].text().substringAfter("{"),
                                    singleRules = "第%d-%d周|单周}",
                                    doubleRules = "第%d-%d周|双周}",
                                    commonRules = "第%d-%d周}"
                            )
                            val sessionInfo = nodes[1].text().substring(2, nodes[1].text().indexOf("{")).removePrefix("第").removeSuffix("节").split(",")
                            sectionStart = sessionInfo[0].toInt()
                            sectionContinue = sessionInfo.size
                        })
                        nodes = nodes.subList(4, nodes.size)
                    }
                }
            }
        }
        return courseList
    }

    //树维
    fun sw(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementById("manualArrangeCourseTable").getElementsByTag("tbody").first()
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            //每一行的列
            val tr = trs[trIndex]
            val tds = tr.getElementsByTag("td")
            for (tdIndex in tds.indices) {
                if (tdIndex == 0 || tds[tdIndex].text().isEmpty()) continue  //跳过时间列
                val tdData = tds[tdIndex]
                if (tdData.className().isNotEmpty()) {
                    var nodes = tdData.textNodes()
                    if (nodes.size % 2 == 0) {
                        while (nodes.size != 0) {
                            courseList.add(CourseWrapper().apply {
                                teacher = nodes[0].text().split(" ")[0]
                                sectionContinue = tdData.attr("rowspan").toInt()
                                //竖向排列
                                val idData = tdData.id().substringBefore("_").removePrefix("TD")
                                day = idData.toInt() / 12 + 1
                                sectionStart = idData.toInt() % 12 + 1
                                //包含逗号的才是有地点的课，否则略过
                                if (nodes[1].text().contains(",")) {
                                    position = nodes[1].text().split(",")[1].removeSuffix(")")
                                }
                                name = nodes[0].text().removePrefix(teacher).trim().substringBeforeLast("(")
                                //周信息
                                val tmpWeek = mutableListOf<Int>()
                                val weekInfo =
                                        if (nodes[1].text().contains(",")) {
                                            nodes[1].text().split(",")[0].removePrefix("(").split(" ")
                                        } else {
                                            nodes[1].text().removePrefix("(").removeSuffix(")").split(" ")
                                        }
                                for (i in weekInfo) {
                                    if (i.contains("-")) {
                                        tmpWeek.addAll(ParseUtil.getWeekInfoByStr(i,
                                                commonRules = "%d-%d"
                                        ))
                                    } else {
                                        tmpWeek.add(i.toInt())
                                    }
                                }
                                week = tmpWeek
                            })
                            nodes = nodes.subList(2, nodes.size)
                        }
                    }
                }
            }
        }
        return courseList
    }


    fun old_qz(html: String): MutableList<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementById("kbtable")
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            if (trIndex < 1 || trIndex == trs.size - 1) continue  //清除表头和表尾
            val tr = trs[trIndex]
            val tds = tr.getElementsByTag("td")
            for (tdIndex in tds.indices) {
                if (tdIndex < 1) continue  //清除时间列
                val targetDiv = tds[tdIndex].getElementsByTag("div")[1]
                if (targetDiv.textNodes().size == 1) continue
                val courseInfo = targetDiv.text().split(" ")
                courseList.add(CourseWrapper().apply {
                    name = courseInfo[0]
                    teacher = courseInfo[2]
                    position = courseInfo[4]
                    day = tdIndex
                    //session 信息
                    val sessionStr = courseInfo[3].substringAfter("[").substringBefore("节")
                    val sessionInfo = sessionStr.split("-")
                    sectionStart = sessionInfo[0].toInt()
                    sectionContinue = sessionInfo[1].toInt() - sessionInfo[0].toInt() + 1
                    //week 信息
                    val weekInfo = courseInfo[3].substringBefore("周").split(",")
                    val tmpWeek = mutableListOf<Int>()
                    for (i in weekInfo) {
                        if (i.contains("-")) {
                            tmpWeek.addAll(ParseUtil.getWeekInfoByStr(i, commonRules = "%d-%d"))
                        } else {
                            tmpWeek.add(i.toInt())
                        }
                    }
                    week = tmpWeek
                })
            }
        }
        return courseList
    }


    /**
     * 西南科技大学
     * */
    fun swust(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementsByClass("UICourseTable").first()
        val tbody = target.getElementsByTag("tbody").first()
        val trs = tbody.getElementsByTag("tr")
        for (i in trs.indices) {
            val tr = trs[i]
            val tds = tr.getElementsByTag("td")
            for (j in tds.indices) {
                val td = tds[j]
                if (td.text().isEmpty() || arrayOf("上午", "下午", "晚上").contains(td.text()) ||
                        (td.text().startsWith("第") && td.text().endsWith("讲"))) {
                    continue
                }
                val div = td.getElementsByClass("lecture").first()
                courseList.add(CourseWrapper().apply {
                    name = div.getElementsByClass("course").text()
                    teacher = div.getElementsByClass("teacher").text()
                    position = div.getElementsByClass("place").text()
                    sectionContinue = 2
                    day = if (tds.size == 9) j - 1 else j
                    sectionStart = (i + 1) * 2 - 1
                    val weekText = div.getElementsByClass("week").text()
                    week = if (weekText.contains("(1)")) {
                        val d1 = weekText.substringBefore("(").split("-").map { it.toInt() }
                        (d1[0]..d1[1]).filterIndexed { index, _ -> index % 2 == 0 }
                    } else {
                        val d1 = weekText.substringBefore("(").split("-").map { it.toInt() }
                        (d1[0]..d1[1]).toList()
                    }
                })
            }
        }
        return courseList
    }
}