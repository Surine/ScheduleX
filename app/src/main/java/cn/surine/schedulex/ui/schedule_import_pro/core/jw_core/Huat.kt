package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser.TableParser
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.WeekUtilsV2
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


class Huat:TableParser(){
    override fun itemParse(element: Element?, trIndex: Int, tdIndex: Int): List<CourseWrapper> {
        element?:return emptyList()
        //如果父亲的孩子是9个，说明有 上午/下午/晚上的一列，则跳过~
        if(element.parent().childrenSize() == 9 && tdIndex == 1)return emptyList()
        val courseList = mutableListOf<CourseWrapper>()
        if(element.childrenSize() == 0)return emptyList()

        val curTable = element.getElementsByTag("table")
        if(curTable.isEmpty())return emptyList()

        val curBody = curTable[0].getElementsByTag("tbody")
        if(curBody.isEmpty())return emptyList()

        val curTd = curBody[0].getElementsByTag("td")
        val nodes = curTd.textNodes()

        val teacherName = curTd[0].getElementsByTag("a").text()
        courseList.add(CourseWrapper().apply {
            name = nodes[0].text()
            teacher = teacherName
            day =  if(element.parent().childrenSize() == 9) tdIndex - 1 else tdIndex
            position = nodes[3].text().split(" ")[0]
            week = WeekUtilsV2.parse(nodes[3].text().split(" ")[1])
            sectionStart = trIndex * 2 - 1
            //最后一个是9-11，是3节，其余是2节
            sectionContinue = if(trIndex * 2 - 1 == 9) 3 else 2
        })
        return courseList
    }

    override fun getTargetInitFunc(html: String) =  Jsoup.parse(html).getElementById("ctl00_ContentPlaceHolder1_CourseTable")!!


    override fun skipRow() = 1

    override fun skipCol() = 1

}

fun main(){
    val html = """
        <table id="ctl00_ContentPlaceHolder1_CourseTable" cellspacing="0" cellpadding="0" rules="all" border="1" style="border-color:Black;border-width:1px;border-style:Solid;border-collapse:collapse;"> 
    <tbody>
     <tr align="center" style="font-weight:bold;height:30px;"> 
      <td align="center" colspan="1" style="width:30px;">时间</td>
      <td style="width:50px;">节次</td>
      <td style="width:126px;">星期一</td>
      <td style="width:126px;">星期二</td>
      <td style="width:126px;">星期三</td>
      <td style="width:126px;">星期四</td>
      <td style="width:126px;">星期五</td>
      <td style="width:126px;">星期六</td>
      <td style="width:124px;">星期日</td> 
     </tr>
     <tr align="center" valign="middle"> 
      <td align="center" rowspan="2">上午</td>
      <td align="center">1－2节<br />8:10--</td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="RoyalBlue"> 
          <td>信息分析（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20000023);return false;">陈延寿</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />6217 1-8周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Aqua"> 
          <td>大数据开发（hadoop）（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20070021);return false;">杨林</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20070028);return false;">张伟丰</a> 2H<br />6214 1-10周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Maroon"> 
          <td>企业竞争情报（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20000023);return false;">陈延寿</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />6305 1-8周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Green"> 
          <td>专业英语（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20140028);return false;">郑珊珊</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />5303 1-8周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td></td>
      <td></td>
      <td></td> 
     </tr>
     <tr align="center" valign="middle"> 
      <td align="center">3－4节<br />10:05-</td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Purple"> 
          <td>信息系统分析与设计（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(19970021);return false;">薛昌春</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />5301 1-10周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Magenta"> 
          <td>项目管理（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20030046);return false;">王红英</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />6308 1-8周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Red"> 
          <td>马克思主义基本原理（5）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20132003);return false;">马保青</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />5103 3-11周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Maroon"> 
          <td>企业竞争情报（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20000023);return false;">陈延寿</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />6305 1-8周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td></td>
      <td></td> 
     </tr>
     <tr align="center" valign="middle"> 
      <td align="center" rowspan="2">下午</td>
      <td align="center">5－6节<br />夏14:30<br />秋14:00</td>
      <td></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Green"> 
          <td>专业英语（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20140028);return false;">郑珊珊</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />5303 1-8周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="RoyalBlue"> 
          <td>信息分析（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20000023);return false;">陈延寿</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />6217 1-8周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Aqua"> 
          <td>大数据开发（hadoop）（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20070021);return false;">杨林</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20070028);return false;">张伟丰</a> 2H<br />6214 1-10周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Yellow"> 
          <td>网页设计与网站建设（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20040005);return false;">蔡亮</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br /> 1-12周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td></td>
      <td></td> 
     </tr>
     <tr align="center" valign="middle"> 
      <td align="center">7－8节<br />夏16:25<br />秋15:55</td>
      <td></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Red"> 
          <td>马克思主义基本原理（5）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20132003);return false;">马保青</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />5103 3-11周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Purple"> 
          <td>信息系统分析与设计（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(19970021);return false;">薛昌春</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />5301 1-10周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Magenta"> 
          <td>项目管理（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20030046);return false;">王红英</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br />6308 1-8周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="Yellow"> 
          <td>网页设计与网站建设（1）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame(20040005);return false;">蔡亮</a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br /> 1-12周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td>
       <table>
        <tbody>
         <tr bgcolor="DeepSkyBlue"> 
          <td>大学英语四级（25）<br /><a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> &nbsp;<a class="strong" href="#" title="点击查看教师详细信息" onclick="openInfoiFame();return false;"></a> 2H<br /> 4-10周（全周）<br /></td>
         </tr>
        </tbody>
       </table></td>
      <td></td> 
     </tr>
     <tr align="center" valign="middle"> 
      <td align="center">晚上</td>
      <td align="center">9－11节<br />夏18:45<br />秋18:15</td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td> 
     </tr> 
    </tbody>
   </table> 
    """.trimIndent()
    Huat().parse(html).forEach {
        println(it)
    }
}