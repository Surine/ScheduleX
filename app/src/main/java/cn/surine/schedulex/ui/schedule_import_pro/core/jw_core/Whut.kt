package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser.TableParser
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.WeekUtilsV2
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * 武汉理工大学
 * */
class Whut : TableParser(){
    override fun itemParse(element: Element?, trIndex: Int, tdIndex: Int): List<CourseWrapper> {
        element?:return emptyList()
        if(element.children().isEmpty())return emptyList()
        val tags = element.getElementsByTag("a").text().split(" ")
        //这几个tr，有早中晚的标识
        val weekDay = if(intArrayOf(0,2,4).contains(trIndex)) tdIndex - 1 else tdIndex
        val courseList = mutableListOf<CourseWrapper>()
        courseList.add(CourseWrapper().apply {
            name = tags[0]
            position = tags[1]
            teacher = ""
            day = weekDay
            week = WeekUtilsV2.parse(tags[2].substringBefore("("))
            val sectionStr  = tags[2].substringAfter("(").substringBefore("节").split("-")
            sectionStart = sectionStr[0].toInt()
            sectionContinue = sectionStr[1].toInt() - sectionStr[0].toInt() + 1
        })
        return courseList
    }

    override fun getTargetInitFunc(html: String): Element = Jsoup.parse(html).getElementsByClass("table-class-even")[0]

    override fun skipRow() = 0

    override fun skipCol() = 0
}

//fun main(){
//    var html = """
//        <html>
//         <head></head>
//         <body>
//          <table>
//           <thead>
//            <tr>
//             <th class="table-class-num"></th>
//             <th class="table-class-num"></th>
//             <th> 星期一 <br /> <span>2021-02-15</span> </th>
//             <th> 星期二 <br /> <span>2021-02-16</span> </th>
//             <th> 星期三 <br /> <span>2021-02-17</span> </th>
//             <th> 星期四 <br /> <span>2021-02-18</span> </th>
//             <th> 星期五 <br /> <span>2021-02-19</span> </th>
//             <th> 星期六 <br /> <span>2021-02-20</span> </th>
//             <th> 星期天<br /> <span>2021-02-21</span> </th>
//            </tr>
//           </thead>
//           <tbody>
//            <tr class="table-kk">
//             <td>空白</td>
//            </tr>
//           </tbody>
//           <tbody class="table-class-even">
//            <tr>
//             <td rowspan="2">上午</td>
//             <td>第一节</td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">分析化学A <p>@博学主楼(原新1)-510</p> <p>◇第01-15周(1-2节)</p> </a>
//              </div> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">高等数学A下 <p>@博学主楼(原新1)-203</p> <p>◇第01-16周(1-2节)</p> </a>
//              </div> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">军事理论 <p>@博学主楼(原新1)-401</p> <p>◇第01-17周(1-2节)</p> </a>
//              </div> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">高等数学A下 <p>@博学主楼(原新1)-203</p> <p>◇第01-15周(1-2节)</p> </a>
//              </div> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">大学英语D2 <p>@博学北楼(原新4)-102</p> <p>◇第01-17周(1-2节)</p> </a>
//              </div> </td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center"> </td>
//            </tr>
//            <tr>
//             <td>第二节</td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">高等数学A下 <p>@博学主楼(原新1)-203</p> <p>◇第01-16周(3-4节)</p> </a>
//              </div> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">C程序设计基础 <p>@博学东楼(原新2)-107</p> <p>◇第01-17周(3-4节)</p> </a>
//              </div> </td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">分析化学A <p>@博学主楼(原新1)-303</p> <p>◇第01-15周(3-4节)</p> </a>
//              </div> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">无机化学A2 <p>@博学主楼(原新1)-403</p> <p>◇第01-13周(3-4节)</p> </a>
//              </div> </td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center"> </td>
//            </tr>
//            <tr>
//             <td rowspan="2">下午</td>
//             <td>第三节</td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">计算机基础与C程序设计综合实验 <p>@新2-507</p> <p>◇第05-16周(6-8节)</p> </a>
//              </div> </td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">思想道德修养与法律基础 <p>@博学东楼(原新2)-204</p> <p>◇第01-15周(6-8节)</p> </a>
//              </div> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">分析化学实验A <p>@fx实验2</p> <p>◇第01-17周(6-7节)</p> </a>
//              </div> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">无机化学实验A2 <p>@wj实验四</p> <p>◇第03-15周(6-7节)</p> </a>
//              </div> </td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center"> </td>
//            </tr>
//            <tr>
//             <td>第四节</td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">心理健康教育 <p>@博学主楼(原新1)-209</p> <p>◇第01-13周(9-10节)</p> </a>
//              </div> </td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">无机化学A2 <p>@博学主楼(原新1)-403</p> <p>◇第01-13周(9-10节)</p> </a>
//              </div> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">分析化学实验A <p>@fx实验2</p> <p>◇第01-17周(9-10节)</p> </a>
//              </div> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">无机化学实验A2 <p>@wj实验四</p> <p>◇第03-15周(9-10节)</p> </a>
//              </div> </td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center"> </td>
//            </tr>
//            <tr>
//             <td>晚上</td>
//             <td>第五节</td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center">
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">形势与政策 <p>@博学主楼(原新1)-407</p> <p>◇第10-10周(11-12节)</p> </a>
//              </div>
//              <div style="margin-top: 2px; font-size: 10px;">
//               <a style="color: black" href="http://whut.fysso.chaoxing.com/sso/whutlogin" target="_blank">形势与政策 <p>@博学主楼(原新1)-407</p> <p>◇第11-12周(11-13节)</p> </a>
//              </div> </td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center"> </td>
//             <td style="text-align: center"> </td>
//            </tr>
//           </tbody>
//          </table>
//         </body>
//        </html>
//    """.trimIndent()
//    println(Whut().parse(html))
//}