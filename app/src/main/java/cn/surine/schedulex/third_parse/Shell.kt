package cn.surine.schedulex.third_parse

import cn.surine.schedulex.third_parse.bean.Course
import org.jsoup.Jsoup

object Shell {

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

//    fun NewUrpParser(html: String): List<Course> {
//        val result = arrayListOf<Course>()
//        val gson = Gson()
//        val json = JsonParser.parseString(html)
//                .asJsonObject
//                .getAsJsonArray("dateList")[0].asJsonObject
//                .getAsJsonArray("selectCourseList").toString()
//        val list = gson.fromJson(json, Array<NewUrpCourseInfo>::class.java)
//        val weekIntList = arrayListOf<Int>()
//        list.forEach { info ->
//            info.timeAndPlaceList?.forEach { detail ->
//                weekIntList.clear()
//                detail.classWeek.forEachIndexed { index, c ->
//                    if (c == '1') {
//                        weekIntList.add(index + 1)
//                    }
//                }
//                Common.weekIntList2WeekBeanList(weekIntList).forEach { week ->
//                    result.add(
//                            Course(
//                                    name = info.courseName, day = detail.classDay,
//                                    room = (detail.campusName ?: "") + (detail.teachingBuildingName
//                                            ?: "") + detail.classroomName,
//                                    teacher = info.attendClassTeacher,
//                                    startNode = detail.classSessions,
//                                    endNode = detail.classSessions + detail.continuingSession - 1,
//                                    startWeek = week.start, endWeek = week.end,
//                                    type = week.type
//                            )
//                    )
//                }
//            }
//        }
//        return result
//    }
}