package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.WeekUtilsV2
import org.jsoup.Jsoup

//武汉学院
class Whxy :IJWParse{
    override fun parse(html: String): List<CourseWrapper> {
        val target = Jsoup.parse(html).getElementById("manualArrangeCourseTable").getElementsByTag("tbody")[0]
        val trs = target.getElementsByTag("tr")
        val courseList = mutableListOf<CourseWrapper>()
        for (trIndex in trs.indices) {
            val tr = trs[trIndex]
            val tds = tr.getElementsByTag("td")
            for (tdIndex in tds.indices) {
                if(tdIndex == 0)continue
                val info = tds[tdIndex].attr("title").split(";").filter {
                    it.isNotEmpty()
                }
                info.windowed(2,2).forEach {
                    courseList.add(CourseWrapper().apply {
                        name = it[0].substringBeforeLast("(")
                        teacher = it[0].substringAfterLast("(").substringBefore(")")
                        val weekStr = it[1].split(",")[0].replace(" ",",").substringAfter("(")
                        week = WeekUtilsV2.parse(weekStr)
                        position = it[1].split(",")[1].removeSuffix(")")
                        day = tdIndex
                        sectionStart = trIndex + 1
                        sectionContinue = 2  //可能有问题
                    })
                }
            }
        }
        return courseList
    }
}

fun main(){
    val html = """
        <table width="100%" id="manualArrangeCourseTable" align="center" class="gridtable" style="text-align:center" border="1">
      <thead>
      <tr>
          <th style="background-color:#DEEDF7;" height="10px" width="80px">节次/周次
          </th><th style="background-color:#DEEDF7;">
              <font size="2px">星期一</font>
        </th>
          <th style="background-color:#DEEDF7;">
              <font size="2px">星期二</font>
        </th>
          <th style="background-color:#DEEDF7;">
              <font size="2px">星期三</font>
        </th>
          <th style="background-color:#DEEDF7;">
              <font size="2px">星期四</font>
        </th>
          <th style="background-color:#DEEDF7;">
              <font size="2px">星期五</font>
        </th>
          <th style="background-color:#DEEDF7;">
              <font size="2px">星期六</font>
        </th>
          <th style="background-color:#DEEDF7;">
              <font size="2px">星期日</font>
        </th>
      </tr>
      </thead>
      <tbody><tr>
          <td style="background-color:rgb(238, 255, 0)">
            <font size="2px"> 第一节</font>
        </td>
          <td id="TD0_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="面向对象程序设计 (刘胜艳);;;(1-15,学4205室)">面向对象程序设计 (刘胜艳)<br><br><br>(1-15,学4205室)</td>
          <td id="TD11_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="形势与政策（3） (黄德林);;;(9-12,中4103室)">形势与政策（3） (黄德林)<br><br><br>(9-12,中4103室)</td>
          <td id="TD22_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="中国近现代史纲要 (张源远);;;(8,学2104室)">中国近现代史纲要 (张源远)<br><br><br>(8,学2104室)</td>
          <td id="TD33_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="数据库原理与应用 (韩杰);(1-3,学4204室);数据库原理与应用 (韩杰);(5-16,中1301室（计算机基础实验室（4））)">数据库原理与应用 (韩杰)<br>(1-3,学4204室)<br>数据库原理与应用 (韩杰)<br>(5-16,中1301室（计算机基础实验室（4））)</td>
          <td id="TD44_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="面向对象程序设计 (刘胜艳);(1-2 15-16,学4305室);面向对象程序设计 (刘胜艳);(3 5-14,中1401室（计算机基础实验室（7））);面向对象程序设计 (刘胜艳);(4,中1301室（计算机基础实验室（4））)">面向对象程序设计 (刘胜艳)<br>(1-2 15-16,学4305室)<br>面向对象程序设计 (刘胜艳)<br>(3 5-14,中1401室（计算机基础实验室（7））)<br>面向对象程序设计 (刘胜艳)<br>(4,中1301室（计算机基础实验室（4））)</td>
          <td id="TD55_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD66_0" style="backGround-Color:#ffffff;font-size:12px"></td>
      </tr>
      <tr>
          <td style="background-color:rgb(238, 255, 0)">
            <font size="2px"> 第二节</font>
        </td>
          
          
          
          
          
          <td id="TD56_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD67_0" style="backGround-Color:#ffffff;font-size:12px"></td>
      </tr>
      <tr>
          <td style="background-color:rgb(238, 255, 0)">
            <font size="2px"> 第三节</font>
        </td>
          <td id="TD2_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="计算机网络 (陈建,鲁春怀);(1-4 6-16,学4404室);计算机网络 ;(5,学4404室)">计算机网络 (陈建,鲁春怀)<br>(1-4 6-16,学4404室)<br>计算机网络 <br>(5,学4404室)</td>
          <td id="TD13_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="线性代数 (王学敏);;;(1-16,学4305室)">线性代数 (王学敏)<br><br><br>(1-16,学4305室)</td>
          <td id="TD24_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="大学英语（3） (刘婕);;;(1-16,学3104室)">大学英语（3） (刘婕)<br><br><br>(1-16,学3104室)</td>
          <td id="TD35_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="中国近现代史纲要 (张源远);;;(1-3 5-16,学2403室)">中国近现代史纲要 (张源远)<br><br><br>(1-3 5-16,学2403室)</td>
          <td id="TD46_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="线性代数 (王学敏);;;(单1-15,学4205室)">线性代数 (王学敏)<br><br><br>(单1-15,学4205室)</td>
          <td id="TD57_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD68_0" style="backGround-Color:#ffffff;font-size:12px"></td>
      </tr>
      <tr>
          <td style="background-color:rgb(238, 255, 0)">
            <font size="2px"> 第四节</font>
        </td>
          
          
          
          
          
          <td id="TD58_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD69_0" style="backGround-Color:#ffffff;font-size:12px"></td>
      </tr>
      <tr>
          <td style="background-color:rgb(51, 187, 0)">
            <font size="2px"> 第五节</font>
        </td>
          <td id="TD4_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="数字逻辑 (陈铁红);;;(1-16,学4405室)">数字逻辑 (陈铁红)<br><br><br>(1-16,学4405室)</td>
          <td id="TD15_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="数据库原理与应用 (韩杰);;;(1-16,学4204室)">数据库原理与应用 (韩杰)<br><br><br>(1-16,学4204室)</td>
          <td id="TD26_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="数字逻辑 (陈铁红);(单1-7,学4505室);数字逻辑 (陈铁红);(双10-16,实1101（物联网综合实验室）)">数字逻辑 (陈铁红)<br>(单1-7,学4505室)<br>数字逻辑 (陈铁红)<br>(双10-16,实1101（物联网综合实验室）)</td>
          <td id="TD37_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD48_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="大学英语（3） (刘婕);;;(双2-16,学4205室)">大学英语（3） (刘婕)<br><br><br>(双2-16,学4205室)</td>
          <td id="TD59_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD70_0" style="backGround-Color:#ffffff;font-size:12px"></td>
      </tr>
      <tr>
          <td style="background-color:rgb(51, 187, 0)">
            <font size="2px"> 第六节</font>
        </td>
          
          
          
          <td id="TD38_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          
          <td id="TD60_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD71_0" style="backGround-Color:#ffffff;font-size:12px"></td>
      </tr>
      <tr>
          <td style="background-color:rgb(51, 187, 0)">
            <font size="2px"> 第七节</font>
        </td>
          <td id="TD6_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="大学体育（3） (黄正喜);;;(1-16,体育场1)">大学体育（3） (黄正喜)<br><br><br>(1-16,体育场1)</td>
          <td id="TD17_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD28_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="数据库原理与应用 (韩杰);(16,学4505室);计算机网络 (鲁春怀);(单1-15,学4305室)">数据库原理与应用 (韩杰)<br>(16,学4505室)<br>计算机网络 (鲁春怀)<br>(单1-15,学4305室)</td>
          <td id="TD39_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD50_0" style="background-color: rgb(148, 174, 243); font-size: 12px;" rowspan="2" class="infoTitle" title="面向对象程序设计 (刘胜艳);;;(14,学3104室)">面向对象程序设计 (刘胜艳)<br><br><br>(14,学3104室)</td>
          <td id="TD61_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD72_0" style="backGround-Color:#ffffff;font-size:12px"></td>
      </tr>
      <tr>
          <td style="background-color:rgb(51, 187, 0)">
            <font size="2px"> 第八节</font>
        </td>
          
          <td id="TD18_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          
          <td id="TD40_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          
          <td id="TD62_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD73_0" style="backGround-Color:#ffffff;font-size:12px"></td>
      </tr>
      <tr>
          <td style="background-color:rgb(241, 158, 194)">
            <font size="2px"> 第九节</font>
        </td>
          <td id="TD8_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD19_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD30_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD41_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD52_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD63_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD74_0" style="backGround-Color:#ffffff;font-size:12px"></td>
      </tr>
      <tr>
          <td style="background-color:rgb(241, 158, 194)">
            <font size="2px"> 第十节</font>
        </td>
          <td id="TD9_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD20_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD31_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD42_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD53_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD64_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD75_0" style="backGround-Color:#ffffff;font-size:12px"></td>
      </tr>
      <tr>
          <td style="background-color:rgb(241, 158, 194)">
            <font size="2px"> 第十一节</font>
        </td>
          <td id="TD10_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD21_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD32_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD43_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD54_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD65_0" style="backGround-Color:#ffffff;font-size:12px"></td>
          <td id="TD76_0" style="backGround-Color:#ffffff;font-size:12px"></td>
      </tr>
    </tbody></table> 
    """.trimIndent()
    Whxy().parse(html).forEach {
        println(it)
    }
}