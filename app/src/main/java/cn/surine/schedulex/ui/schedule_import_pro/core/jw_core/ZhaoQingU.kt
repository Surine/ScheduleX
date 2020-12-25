package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.base.utils.Jsons
import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.beans.ZhaoQingUBean
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper

/**
 * 肇庆学院教务处解析
 * */
class ZhaoQingU:IJWParse{
    override fun parse(html: String): List<CourseWrapper> {
        val after = html.substringAfter("var kbxx =")
        val before = after.substringBefore("""${'$'}.each(kbxx""")
        println(before)
        val uBeans:List<ZhaoQingUBean>? = Jsons.parseJsonWithGsonToList<ZhaoQingUBean>(before)
        uBeans?:return emptyList()
        val res = mutableListOf<CourseWrapper>()
        for (i in uBeans){
            val weeks = i.zcs.split(",").map {
                it.toInt()
            }.sorted()
            val session = i.jcdm2.split(",").map {
                it.toInt()
            }
            res.add(CourseWrapper(
               name = i.kcxm,teacher = i.teaxms,
                    position = i.jxcdmcs,
                    day = i.xq.toInt(),
                    week = weeks,
                    sectionStart = session[0],
                    sectionContinue = session.size
            ))
        }
        return res
    }
}

fun main(){
    val html = """
        <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <html>
        <head>
        <meta http-equiv="pragma" content="no-cache" /> 
        <meta http-equiv="cache-control" content="no-cache" /> 
        <meta http-equiv="expires" content="0" /> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
        <title>学生个人学期课表</title>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
        <meta name="renderer" content="webkit">
        <link rel="shortcut icon" href="/favicon.ico" /> 
        <link rel="stylesheet" type="text/css" href="/styles/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="/styles/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="/styles/themes/js_input.css">
        <link rel="stylesheet" href="/styles/js/poshytip-1.2/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
        <link rel="stylesheet" type="text/css" href="/styles/themes/main.css">
        <script type="text/javascript" src="/styles/js/jquery-1.8.0.min.js"></script>
        <script type="text/javascript" src="/styles/js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="/styles/js/jquery.parser.js"></script>
        <script type="text/javascript" src="/styles/js/easyui-lang-zh_CN.js"></script>
        <script type="text/javascript" src="/styles/layer/layer.min.js"></script>
        <script type="text/javascript" src="/styles/js/export/base64.js"></script>
        <script type="text/javascript" src="/styles/js/ntss.js?v=20191023"></script>
        <script type="text/javascript" src="/styles/js/js_input.js"></script>
        <script type="text/javascript" src="/styles/js/poshytip-1.2/jquery.poshytip.min.js"></script>
        <script type="text/javascript" src="/styles/js/entss.js"></script>
        <script type="text/javascript" src="/styles/js/underscore-min.js"></script>
        <script type="text/javascript" src="/styles/js/easyui-component.js?v=20180227"></script>

        <style type="text/css">
        body{padding:0;}
        a{text-decoration:none;color:#1f3a87;text-decoration:none;}
        </style>
        </head>
        <body>
        <table class="kb" cellspacing="0" cellpadding="0" border="0">
        	<tr ><th></th><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th>六</th><th>日</th></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第01节</td><td id="01-1"><div class="content"></div></td><td id="01-2"><div class="content"></div></td><td id="01-3"><div class="content"></div></td><td id="01-4"><div class="content"></div></td><td id="01-5"><div class="content"></div></td><td id="01-6"><div class="content"></div></td><td id="01-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第02节</td><td id="02-1"><div class="content"></div></td><td id="02-2"><div class="content"></div></td><td id="02-3"><div class="content"></div></td><td id="02-4"><div class="content"></div></td><td id="02-5"><div class="content"></div></td><td id="02-6"><div class="content"></div></td><td id="02-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第03节</td><td id="03-1"><div class="content"></div></td><td id="03-2"><div class="content"></div></td><td id="03-3"><div class="content"></div></td><td id="03-4"><div class="content"></div></td><td id="03-5"><div class="content"></div></td><td id="03-6"><div class="content"></div></td><td id="03-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第04节</td><td id="04-1"><div class="content"></div></td><td id="04-2"><div class="content"></div></td><td id="04-3"><div class="content"></div></td><td id="04-4"><div class="content"></div></td><td id="04-5"><div class="content"></div></td><td id="04-6"><div class="content"></div></td><td id="04-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第05节</td><td id="05-1"><div class="content"></div></td><td id="05-2"><div class="content"></div></td><td id="05-3"><div class="content"></div></td><td id="05-4"><div class="content"></div></td><td id="05-5"><div class="content"></div></td><td id="05-6"><div class="content"></div></td><td id="05-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第06节</td><td id="06-1"><div class="content"></div></td><td id="06-2"><div class="content"></div></td><td id="06-3"><div class="content"></div></td><td id="06-4"><div class="content"></div></td><td id="06-5"><div class="content"></div></td><td id="06-6"><div class="content"></div></td><td id="06-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第07节</td><td id="07-1"><div class="content"></div></td><td id="07-2"><div class="content"></div></td><td id="07-3"><div class="content"></div></td><td id="07-4"><div class="content"></div></td><td id="07-5"><div class="content"></div></td><td id="07-6"><div class="content"></div></td><td id="07-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第08节</td><td id="08-1"><div class="content"></div></td><td id="08-2"><div class="content"></div></td><td id="08-3"><div class="content"></div></td><td id="08-4"><div class="content"></div></td><td id="08-5"><div class="content"></div></td><td id="08-6"><div class="content"></div></td><td id="08-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第09节</td><td id="09-1"><div class="content"></div></td><td id="09-2"><div class="content"></div></td><td id="09-3"><div class="content"></div></td><td id="09-4"><div class="content"></div></td><td id="09-5"><div class="content"></div></td><td id="09-6"><div class="content"></div></td><td id="09-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第10节</td><td id="10-1"><div class="content"></div></td><td id="10-2"><div class="content"></div></td><td id="10-3"><div class="content"></div></td><td id="10-4"><div class="content"></div></td><td id="10-5"><div class="content"></div></td><td id="10-6"><div class="content"></div></td><td id="10-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第11节</td><td id="11-1"><div class="content"></div></td><td id="11-2"><div class="content"></div></td><td id="11-3"><div class="content"></div></td><td id="11-4"><div class="content"></div></td><td id="11-5"><div class="content"></div></td><td id="11-6"><div class="content"></div></td><td id="11-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第12节</td><td id="12-1"><div class="content"></div></td><td id="12-2"><div class="content"></div></td><td id="12-3"><div class="content"></div></td><td id="12-4"><div class="content"></div></td><td id="12-5"><div class="content"></div></td><td id="12-6"><div class="content"></div></td><td id="12-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第13节</td><td id="13-1"><div class="content"></div></td><td id="13-2"><div class="content"></div></td><td id="13-3"><div class="content"></div></td><td id="13-4"><div class="content"></div></td><td id="13-5"><div class="content"></div></td><td id="13-6"><div class="content"></div></td><td id="13-7"><div class="content"></div></td></tr>
        	
        	<tr><td style="width:60px;padding-left:10px;background-color: #CAE8EA;">第14节</td><td id="14-1"><div class="content"></div></td><td id="14-2"><div class="content"></div></td><td id="14-3"><div class="content"></div></td><td id="14-4"><div class="content"></div></td><td id="14-5"><div class="content"></div></td><td id="14-6"><div class="content"></div></td><td id="14-7"><div class="content"></div></td></tr>
        	
        </table>
        </body>
        <script type="text/javascript">
        ${'$'}(document).ready(function(){
        	showMask();
        	var kbxx = [{"kcmc":"光学","kcbh":"122114","jxbmc":"2018级物理学1班","kcrwdm":"1090809","jcdm2":"03,04,05","zcs":"16,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15","xq":"1","jxcdmcs":"1号教学楼504","teaxms":"李晓霞,王琳,于浩"},{"kcmc":"数学物理方法","kcbh":"122122","jxbmc":"2018级物理学1班","kcrwdm":"1090810","jcdm2":"01,02","zcs":"16,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15","xq":"1","jxcdmcs":"1号教学楼504","teaxms":"戴学斌"},{"kcmc":"数学物理方法","kcbh":"122122","jxbmc":"2018级物理学1班","kcrwdm":"1090810","jcdm2":"01,02","zcs":"16,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15","xq":"3","jxcdmcs":"1号教学楼504","teaxms":"戴学斌"},{"kcmc":"理论力学","kcbh":"122123","jxbmc":"2018级物理学1班","kcrwdm":"1090811","jcdm2":"03,04","zcs":"16,9,10,11,12,13,14,15","xq":"4","jxcdmcs":"1号教学楼504","teaxms":"孔德清"},{"kcmc":"理论力学","kcbh":"122123","jxbmc":"2018级物理学1班","kcrwdm":"1090811","jcdm2":"03,04,05","zcs":"8,6,7,1,2,3,4,5","xq":"4","jxcdmcs":"1号教学楼504","teaxms":"孔德清"},{"kcmc":"电工技术","kcbh":"122205","jxbmc":"2018级物理学1班","kcrwdm":"1090812","jcdm2":"06,07","zcs":"16,9,10,11,12,13,14,15","xq":"3","jxcdmcs":"金工楼203-204","teaxms":"许志勇"},{"kcmc":"电工技术","kcbh":"122205","jxbmc":"2018级物理学1班","kcrwdm":"1090812","jcdm2":"06,07","zcs":"18,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17","xq":"4","jxcdmcs":"1号教学楼504","teaxms":"许志勇"},{"kcmc":"创新创业教育","kcbh":"222003","jxbmc":"2018级物理学1班,2018级通信工程1班,2018级通信工程2班","kcrwdm":"1091224","jcdm2":"08,09","zcs":"15,9,10,11,12,13,14,16","xq":"1","jxcdmcs":"2号教学楼501","teaxms":"杨秀芷"},{"kcmc":"教育技术与应用","kcbh":"162004","jxbmc":"2018级物理学1班,2018级电气工程及其自动化1班,2018级电子信息科学与技术1班,2018级通信工程2班,2018级机械设计制造及其自动化1班[申请教师资格证B],2018级机械设计制造及其自动化3班[申请教师资格证B],2018级机械设计制造及其自动化4班[申请教师资格证B],2018级车辆工程1班[申请教师资格证B],2018级车辆工程2班[申请教师资格证B],2018级车辆工程3班[申请教师资格证B],2018级计算机科学与技术1班[申请教师资格证B],2018级软件工程1班[申请教师资格证B],2018级软件工程2班[申请教师资格证B],2018级软件工程3班[申请教师资格证B],2018级物联网工程1班[申请教师资格证B]","kcrwdm":"1091549","jcdm2":"06,07","zcs":"16,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15","xq":"1","jxcdmcs":"实验楼214","teaxms":"陈祝华"},{"kcmc":"马克思主义基本原理概论","kcbh":"152003","jxbmc":"2018级物理学1班,2018级通信工程1班,2018级通信工程2班","kcrwdm":"1094068","jcdm2":"03,04","zcs":"16,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15","xq":"2","jxcdmcs":"2号教学楼501","teaxms":"周春晓"},{"kcmc":"心理发展与健康（心理学）","kcbh":"1620011","jxbmc":"2018级物理学1班[申请教师资格证B],2018级机械设计制造及其自动化1班,2018级机械设计制造及其自动化3班,2018级机械设计制造及其自动化4班,2018级车辆工程1班,2018级车辆工程2班,2018级车辆工程3班,2018级物理学1班,2018级计算机科学与技术1班,2018级软件工程1班,2018级软件工程2班,2018级软件工程3班,2018级物联网工程1班","kcrwdm":"1096795","jcdm2":"06,07,08","zcs":"16,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15","xq":"2","jxcdmcs":"3号教学楼302","teaxms":"周彩虹"},{"kcmc":"大学英语4","kcbh":"062004","jxbmc":"18职场10","kcrwdm":"1098062","jcdm2":"01,02","zcs":"16,7,8,9,1,2,3,4,5,6,10,11,12,13,14,15","xq":"5","jxcdmcs":"2号教学楼614","teaxms":"庄国伟"},{"kcmc":"大学体育4","kcbh":"042101","jxbmc":"复制复制2018级（国贸+信息计算+音乐学1-6班+美术学1-8班+行政管理+书法学+物理学+工业设计）-12-1","kcrwdm":"1099846","jcdm2":"03,04","zcs":"9,10,11,12,13,14,15,16,1,2,3,4,5,6,7,8","xq":"3","jxcdmcs":"45号网球场（外语楼前网球场）","teaxms":"李国军"},{"kcmc":"普通物理实验(光学)","kcbh":"122143","jxbmc":"2018级物理学1班","kcrwdm":"1102124","jcdm2":"03,04","zcs":"17,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16","xq":"5","jxcdmcs":"实验楼315-316","teaxms":"辛建英"},{"kcmc":"普通物理实验(光学)","kcbh":"122143","jxbmc":"2018级物理学1班","kcrwdm":"1102124","jcdm2":"08,09","zcs":"16,4,5,6,7,8,9,10,11,13,14,15","xq":"5","jxcdmcs":"实验楼315-316","teaxms":"辛建英"}];
        	${'$'}.each(kbxx,function(index,item){
        		var kbrow = kbObj(item);
        		kbrow.appendSelf();
        	});
        	${'$'}.parser.parse(${'$'}("div"));
        	closeMask();
        });


        function kbObj(obj){
        	var _objkb  = new Object();
        	_objkb.jcArr = obj.jcdm2.split(",");
        	_objkb.startJc = _objkb.jcArr[0];
        	_objkb.zcSumary = getWeekSummary(obj.zcs);
        	_objkb.kcmc = obj.kcmc.length>7?obj.kcmc.substr(0,7)+"...":obj.kcmc;
        	_objkb.tips = "课程名称："+obj.kcmc+"&#10"
        				 +"课程编号："+obj.kcbh+"&#10"
        				 +"周次："+_objkb.zcSumary+"&#10"
        				 +"授课教师："+((obj.teaxms == '')?'未安排':obj.teaxms)+"&#10"
        				 +"教学场地："+obj.jxcdmcs+"&#10"
        				 +"教学班名称："+obj.jxbmc;
        	_objkb.appendSelf = function(){
        		for(var i=0;i<_objkb.jcArr.length;i++){
        			var _position = _objkb.jcArr[i]+"-"+obj.xq;
        			var _con = ${'$'}("#"+_position+" .content");
        			_con.append("<div title="+_objkb.tips+" class='kbdiv' style='float:left;background-color:#AEEEEE;width:132px;height:28px;margin-left:1px;'><a href='javascript:view(\""+obj.kcrwdm+"\",\""+obj.kcmc+"\");'>"+_objkb.kcmc+"</a>★"+_objkb.zcSumary+"<br>"+obj.jxcdmcs+"</div>");
        			var len = _con.find(".kbdiv").length;
        			if(len > 1){
        				_con.find(".kbdiv").css("width",Math.ceil((132-2*len)/len));
        			}
        		}
        	};
        	return _objkb;
        }

        function view(kcrwdm,kcmc){
        	${'$'}('<div id="dlg"><table id="skxxdatalist"></table></div>').dialog({
        		width:700,height:${'$'}(window).height()-100,
        		title:kcmc+'上课信息',
        		hrefMode:"iframe",modal:true,iconCls:"icon-edit",
        		onClose:function(){${'$'}(this).dialog("destroy");}
        	});
        	
        	//渲染datalist
        	${'$'}('#skxxdatalist').datagrid({
        		width: 684,
        		height: ${'$'}(window).height()-136,
        		striped: true,
        		singleSelect: false,
        		url:'xsgrkbcx!getSkxxDataList.action',
        		pagination: true,
        		rownumbers: false,
        		queryParams:{
        						kcrwdm:kcrwdm,
        						teadm :''
        					},
        		pageSize: '20',
        		pageList:['20','20'*2,'20'*3],
        		fitColumns: true,
        		sortName: 'kxh',
        		columns:[[
        			  {field:'kxh',title:'课序号',width:30,sortable:true},
        			  {field:'zc',title:'周次',width:30,sortable:true},
        		      {field:'xq',title:'星期',width:30,sortable:true},
        	          {field:'jcdm2',title:'节次',width:45,sortable:true},
        	          {field:'jxcdmc',title:'教学场地',width:50,sortable:true},
        		      {field:'jxbmc',title:'教学班',width:90,sortable:true},
        		      //{field:'kcmc',title:'课程名称',width:90,sortable:true},
        	          {field:'sknrjj',title:'授课内容',width:125,sortable:true}
        	    ]]
        	});
        }
        </script>
        </html>

    """.trimIndent()
    println(ZhaoQingU().parse(html))
}