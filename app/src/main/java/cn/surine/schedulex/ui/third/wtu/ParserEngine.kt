package cn.surine.schedulex.ui.third.wtu

import org.jsoup.Jsoup

object ParserEngine {
    /**
     * 新正方系统解析器
     */
    fun newZenFang(html:String):String{
        var count = 0
        val doc = Jsoup.parse(html)
        val trs = doc.select("div#table1 tr")
        for (tr in trs) {
            val courseWrapper = CourseWrapper()
            val nodeStr = tr.getElementsByClass("festival").text()
            if (nodeStr.isEmpty()) {
                continue
            }
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
                    count++
                    courseWrapper.name = courseName
                    courseWrapper.day = td.attr("id")[0].toString().toInt()
                    val pList = div.getElementsByTag("p")
                    pList.forEach { e ->
                        when (e.getElementsByAttribute("title").attr("title")) {
                            "教师" -> courseWrapper.teacher = e.text().trim()
                            "上课地点" -> courseWrapper.position = e.text().trim()
                            "节/周", "周/节" -> {
                                val timeStr = e.text().trim()//(1-2节)1-14周
                                val brace = timeStr.substring(timeStr.indexOf("("),timeStr.indexOf(")")+1)
                                val left = timeStr.replace(brace,"")
                                if (brace.contains("节")){
                                    val braceContent = brace.substring(1,brace.length-2)//1-2
                                    val sections = braceContent.split("-")
                                    courseWrapper.sectionStart = sections[0].toInt()
                                    courseWrapper.sectionContinue = sections[1].toInt()-sections[0].toInt()+1
                                    val leftContent = left.substring(0,left.length-1)
                                    val weeks = leftContent.split("-")
                                    courseWrapper.week = (weeks[0].toInt()..weeks[1].toInt()).toList()
                                }else if (brace.contains("周")){
                                    val braceContent = brace.substring(1,brace.length-2)//1-2
                                    val leftContent = left.substring(0,left.length-1)
                                    val sections = leftContent.split("-")
                                    courseWrapper.sectionStart = sections[0].toInt()
                                    courseWrapper.sectionContinue = sections[1].toInt()-sections[0].toInt()+1
                                    val weeks = braceContent.split("-")
                                    courseWrapper.week = (weeks[0].toInt()..weeks[1].toInt()).toList()
                                }
                            }
                        }
                    }
                }
            }
            //走到这里就有一个完整的wrapper类了，里面包含了你需要的东西
            //例如（你可以用来测试）
            //    name=数控技术与数控机床★
            //    position=阳光校区 YG03-110
            //    teacher=胡峰
            //    day=1
            //    sectionStart=1
            //    sectionContinue=2
            //    week=[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14]
        }
        return "成功导入${count}门课程"//按界面需要进行修改
    }
}