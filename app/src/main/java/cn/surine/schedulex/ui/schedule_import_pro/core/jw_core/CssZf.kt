package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser.NewUrpIntell

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/27/20 16:35
 */
fun main() {
//    Jsoup.parse(testHtml).select("table#Table6").first().select("td").asSequence().forEach {
//        //x,y索引
////        println("${it.parent().elementSiblingIndex()} | ${it.elementSiblingIndex()} | ${it.html()}")
//        println("${it.parent().elementSiblingIndex()} | ${it.elementSiblingIndex()} | ${it.textNodes().windowed(4)} ")
//    }
    NewUrpIntell().parse(testHtml)
}


val testHtml = """
    <div id="mycoursetable">
      <table class="table table-bordered" id="courseTable">
        <thead>
          <tr id="courseTableHead">
            <th colspan="3">节次/时间</th>
            <th>星期一</th>
            <th>星期二</th>
            <th>星期三</th>
            <th>星期四</th>
            <th>星期五</th>
            <th>星期六</th>
            <th>星期日</th></tr>
        </thead>
        <tbody id="courseTableBody">
          <tr style="height: 58.5px;">
            <th rowspan="4" style="vertical-align:middle;width:5px;background-color:rgba(207,255,228,0.7);">上
              <br>午</th>
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,255,228,0.7);text-align:center;" id="dj_0_1" rowspan="2">第一大节</th>
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,255,228,0.7);" id="0_1">第一节(00:00-00:01)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="1_1"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="2_1">
              <div class="class_div box_font div-kcb-12" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 87.1094px; left: 536px;">
                <p class="p-kcm-2">面向对象程序设计（C++）_01</p>
                <p class="kcb_p_gray">吴超*</p>
                <p class="kcb_p_gray">1-14周</p>
                <p class="kcb_p_gray">1-2节</p>
                <p class="p-jxl-2">泰达西院10-209(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100601135&quot;, &quot;01&quot;,&quot;97431736,吴超（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100601135&quot;, &quot;01&quot;, &quot;97431736,吴超（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="3_1"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="4_1">
              <div class="class_div box_font div-kcb-9" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 87.1094px; left: 922px;">
                <p class="p-kcm-4">四级英语_06</p>
                <p class="kcb_p_gray">薛红宏*</p>
                <p class="kcb_p_gray">2-16周双</p>
                <p class="kcb_p_gray">1-2节</p>
                <p class="p-jxl-4">泰达3-211(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K120204730&quot;, &quot;06&quot;,&quot;98121772,薛红宏（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K120204730&quot;, &quot;06&quot;, &quot;98121772,薛红宏（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="5_1">
              <div class="class_div box_font div-kcb-7" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 87.1094px; left: 1115px;">
                <p class="p-kcm-2">概率与统计B_03</p>
                <p class="kcb_p_gray">赵蕾*</p>
                <p class="kcb_p_gray">3-16周</p>
                <p class="kcb_p_gray">1-2节</p>
                <p class="p-jxl-2">泰达西院8-4阶梯(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K110600425&quot;, &quot;03&quot;,&quot;98423341,赵蕾（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K110600425&quot;, &quot;03&quot;, &quot;98423341,赵蕾&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="6_1"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="7_1"></td>
          </tr>
          <tr style="height: 58.5px;">
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,255,228,0.7);" id="0_2">第一节(00:00-00:01)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="1_2"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="2_2"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="3_2"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="4_2"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="5_2"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="6_2"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="7_2"></td>
          </tr>
          <tr style="height: 58.5px;">
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,255,228,0.7);text-align:center;" id="dj_0_3" rowspan="2">第二大节</th>
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,255,228,0.7);" id="0_3">第二节(00:00-00:01)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="1_3">
              <div class="class_div box_font div-kcb-7" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 205.109px; left: 343px;">
                <p class="p-kcm-2">概率与统计B_03</p>
                <p class="kcb_p_gray">赵蕾*</p>
                <p class="kcb_p_gray">4-16周双</p>
                <p class="kcb_p_gray">3-4节</p>
                <p class="p-jxl-2">泰达西院8-6阶梯(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K110600425&quot;, &quot;03&quot;,&quot;98423341,赵蕾（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K110600425&quot;, &quot;03&quot;, &quot;98423341,赵蕾&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="2_3">
              <div class="class_div box_font div-kcb-6" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 205.109px; left: 536px;">
                <p class="p-kcm-1">大学物理A-2_08</p>
                <p class="kcb_p_gray">1102外聘1*</p>
                <p class="kcb_p_gray">3-16周</p>
                <p class="kcb_p_gray">3-4节</p>
                <p class="p-jxl-1">泰达西院10-111(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K110200435&quot;, &quot;08&quot;,&quot;11911,1102外聘1（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K110200435&quot;, &quot;08&quot;, &quot;11911,1102外聘1（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="3_3">
              <div class="class_div box_font div-kcb-12" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 205.109px; left: 729px;">
                <p class="p-kcm-2">面向对象程序设计（C++）_01</p>
                <p class="kcb_p_gray">吴超*</p>
                <p class="kcb_p_gray">1-14周</p>
                <p class="kcb_p_gray">3-4节</p>
                <p class="p-jxl-2">泰达西院8-211(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100601135&quot;, &quot;01&quot;,&quot;97431736,吴超（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100601135&quot;, &quot;01&quot;, &quot;97431736,吴超（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="4_3">
              <div class="class_div box_font div-kcb-9" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 205.109px; left: 922px;">
                <p class="p-kcm-4">四级英语_06</p>
                <p class="kcb_p_gray">薛红宏*</p>
                <p class="kcb_p_gray">2-17周</p>
                <p class="kcb_p_gray">3-4节</p>
                <p class="p-jxl-4">泰达3-216(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K120204730&quot;, &quot;06&quot;,&quot;98121772,薛红宏（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K120204730&quot;, &quot;06&quot;, &quot;98121772,薛红宏（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="5_3"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="6_3"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="7_3"></td>
          </tr>
          <tr style="height: 58.5px;">
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,255,228,0.7);" id="0_4">第二节(00:00-00:01)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="1_4"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="2_4"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="3_4"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="4_4"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="5_4"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="6_4"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,255,228,0.7);" id="7_4"></td>
          </tr>
          <tr style="height: 58.5px;">
            <th rowspan="4" style="vertical-align:middle;width:5px;background-color:rgba(255,230,207,0.7);">下
              <br>午</th>
            <th style="vertical-align:middle;width:150px;background-color:rgba(255,230,207,0.7);text-align:center;" id="dj_0_5" rowspan="2">第三大节</th>
            <th style="vertical-align:middle;width:150px;background-color:rgba(255,230,207,0.7);" id="0_5">第三节(14:00-14:45)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="1_5">
              <div class="class_div box_font div-kcb-8" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 323.109px; left: 343px;">
                <p class="p-kcm-3">人工智能导论A_01</p>
                <p class="kcb_p_gray">侯琳*</p>
                <p class="kcb_p_gray">1-12周</p>
                <p class="kcb_p_gray">5-6节</p>
                <p class="p-jxl-3">泰达西院10-209(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100701130&quot;, &quot;01&quot;,&quot;98323081,侯琳（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100701130&quot;, &quot;01&quot;, &quot;98323081,侯琳（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="2_5">
              <div class="class_div box_font div-kcb-8" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 323.109px; left: 536px;">
                <p class="p-kcm-3">人工智能导论A_01</p>
                <p class="kcb_p_gray">侯琳*</p>
                <p class="kcb_p_gray">1-12周</p>
                <p class="kcb_p_gray">5-6节</p>
                <p class="p-jxl-3">泰达西院自主学习自主学习</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100701130&quot;, &quot;01&quot;,&quot;98323081,侯琳（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100701130&quot;, &quot;01&quot;, &quot;98323081,侯琳（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="3_5">
              <div class="class_div box_font div-kcb-4" classnum="4" style="position: absolute; width: 193px; height: 236px; top: 323.109px; left: 729px;">
                <p class="p-kcm-4">物理实验-1_09</p>
                <p class="kcb_p_gray">原凤英*</p>
                <p class="kcb_p_gray">3-17周单</p>
                <p class="kcb_p_gray">5-8节</p>
                <p class="p-jxl-4">泰达物理一实验室实验室1</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K110200310&quot;, &quot;09&quot;,&quot;97741179,原凤英（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K110200310&quot;, &quot;09&quot;, &quot;97741179,原凤英（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="4_5">
              <div class="class_div box_font div-kcb-11" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 323.109px; left: 922px;">
                <p class="p-kcm-1">体育3-足球_246</p>
                <p class="kcb_p_gray">林建军*</p>
                <p class="kcb_p_gray">5-18周</p>
                <p class="kcb_p_gray">5-6节</p>
                <p class="p-jxl-1">泰达运动场泰达1</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K130300110&quot;, &quot;246&quot;,&quot;97710485,林建军（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K130300110&quot;, &quot;246&quot;, &quot;97710485,林建军&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="5_5">
              <div class="class_div box_font div-kcb-5" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 323.109px; left: 1115px;">
                <p class="p-kcm-5">马克思主义基本原理_30</p>
                <p class="kcb_p_gray">王海霞*</p>
                <p class="kcb_p_gray">1-13周单</p>
                <p class="kcb_p_gray">5-6节</p>
                <p class="p-jxl-5">泰达西院10-211(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K160300125&quot;, &quot;30&quot;,&quot;97943644,王海霞（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K160300125&quot;, &quot;30&quot;, &quot;97943644,王海霞（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="6_5"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="7_5"></td>
          </tr>
          <tr style="height: 58.5px;">
            <th style="vertical-align:middle;width:150px;background-color:rgba(255,230,207,0.7);" id="0_6">第三节(14:55-15:40)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="1_6"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="2_6"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="3_6"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="4_6"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="5_6"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="6_6"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="7_6"></td>
          </tr>
          <tr style="height: 58.5px;">
            <th style="vertical-align:middle;width:150px;background-color:rgba(255,230,207,0.7);text-align:center;" id="dj_0_7" rowspan="2">第四大节</th>
            <th style="vertical-align:middle;width:150px;background-color:rgba(255,230,207,0.7);" id="0_7">第四节(15:55-16:40)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="1_7">
              <div class="class_div box_font div-kcb-5" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 441.109px; left: 343px;">
                <p class="p-kcm-5">马克思主义基本原理_30</p>
                <p class="kcb_p_gray">王海霞*</p>
                <p class="kcb_p_gray">1-13周</p>
                <p class="kcb_p_gray">7-8节</p>
                <p class="p-jxl-5">泰达西院10-209(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K160300125&quot;, &quot;30&quot;,&quot;97943644,王海霞（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K160300125&quot;, &quot;30&quot;, &quot;97943644,王海霞（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="2_7">
              <div class="class_div box_font div-kcb-10" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 441.109px; left: 536px;">
                <p class="p-kcm-5">数字逻辑B_02</p>
                <p class="kcb_p_gray">李宁*</p>
                <p class="kcb_p_gray">3-15周单</p>
                <p class="kcb_p_gray">7-8节</p>
                <p class="p-jxl-5">泰达西院10-111(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K020100525&quot;, &quot;02&quot;,&quot;97320412,李宁（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K020100525&quot;, &quot;02&quot;, &quot;97320412,李宁&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="3_7"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="4_7">
              <div class="class_div box_font div-kcb-6" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 441.109px; left: 922px;">
                <p class="p-kcm-1">大学物理A-2_08</p>
                <p class="kcb_p_gray">1102外聘1*</p>
                <p class="kcb_p_gray">3-16周</p>
                <p class="kcb_p_gray">7-8节</p>
                <p class="p-jxl-1">泰达西院10-110(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K110200435&quot;, &quot;08&quot;,&quot;11911,1102外聘1（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K110200435&quot;, &quot;08&quot;, &quot;11911,1102外聘1（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="5_7">
              <div class="class_div box_font div-kcb-10" classnum="2" style="position: absolute; width: 96.5px; height: 118px; top: 441.109px; left: 1115px;">
                <p class="p-kcm-5">数字逻辑B_02</p>
                <p class="kcb_p_gray">李宁*</p>
                <p class="kcb_p_gray">3-15周单</p>
                <p class="kcb_p_gray">7-8节</p>
                <p class="p-jxl-5">泰达西院10-111(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K020100525&quot;, &quot;02&quot;,&quot;97320412,李宁（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K020100525&quot;, &quot;02&quot;, &quot;97320412,李宁&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
              <div class="class_div box_font div-kcb-10" classnum="2" style="position: absolute; width: 96.5px; height: 118px; top: 441.109px; left: 1211.5px;">
                <p class="p-kcm-5">数字逻辑B_02</p>
                <p class="kcb_p_gray">李宁*</p>
                <p class="kcb_p_gray">4-14周双</p>
                <p class="kcb_p_gray">7-8节</p>
                <p class="p-jxl-5">泰达西院10-111(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K020100525&quot;, &quot;02&quot;,&quot;97320412,李宁（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K020100525&quot;, &quot;02&quot;, &quot;97320412,李宁&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="6_7"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="7_7"></td>
          </tr>
          <tr style="height: 58.5px;">
            <th style="vertical-align:middle;width:150px;background-color:rgba(255,230,207,0.7);" id="0_8">第四节(16:50-17:35)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="1_8"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="2_8"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="3_8"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="4_8"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="5_8"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="6_8"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(255,230,207,0.7);" id="7_8"></td>
          </tr>
          <tr style="height: 58.5px;">
            <th rowspan="4" style="vertical-align:middle;width:5px;background-color:rgba(207,228,255,0.7);">晚
              <br>上</th>
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,228,255,0.7);text-align:center;" id="dj_0_9" rowspan="2">第五大节</th>
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,228,255,0.7);" id="0_9">第五节(18:30-19:15)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="1_9">
              <div class="class_div box_font div-kcb-2" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 559.109px; left: 343px;">
                <p class="p-kcm-2">智能机器人_101</p>
                <p class="kcb_p_gray">彭一准*</p>
                <p class="kcb_p_gray">1-8周</p>
                <p class="kcb_p_gray">9-10节</p>
                <p class="p-jxl-2">泰达9-2阶梯(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;G900203410&quot;, &quot;101&quot;,&quot;97431660,彭一准（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;G900203410&quot;, &quot;101&quot;, &quot;97431660,彭一准（无）&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="2_9"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="3_9">
              <div class="class_div box_font div-kcb-1" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 559.109px; left: 729px;">
                <p class="p-kcm-1">日语_100</p>
                <p class="kcb_p_gray">管治国*</p>
                <p class="kcb_p_gray">1-16周</p>
                <p class="kcb_p_gray">9-10节</p>
                <p class="p-jxl-1">泰达9-12阶梯(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;G900102320&quot;, &quot;100&quot;,&quot;97652342,管治国（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;G900102320&quot;, &quot;100&quot;, &quot;97652342,管治国&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="4_9">
              <div class="class_div box_font div-kcb-3" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 559.109px; left: 922px;">
                <p class="p-kcm-3">离散数学_05</p>
                <p class="kcb_p_gray">刘颖*</p>
                <p class="kcb_p_gray">3-18周</p>
                <p class="kcb_p_gray">9-10节</p>
                <p class="p-jxl-3">泰达西院8-4阶梯(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100300540&quot;, &quot;05&quot;,&quot;98221925,刘颖（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100300540&quot;, &quot;05&quot;, &quot;98221925,刘颖&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="5_9">
              <div class="class_div box_font div-kcb-3" classnum="2" style="position: absolute; width: 193px; height: 118px; top: 559.109px; left: 1115px;">
                <p class="p-kcm-3">离散数学_05</p>
                <p class="kcb_p_gray">刘颖*</p>
                <p class="kcb_p_gray">3-18周</p>
                <p class="kcb_p_gray">9-10节</p>
                <p class="p-jxl-3">泰达西院8-4阶梯(d)</p>
                <div class="tools">
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100300540&quot;, &quot;05&quot;,&quot;98221925,刘颖（无）&quot;, &quot;rl&quot;);">
                    <i class="ace-icon fa fa-calendar"></i>
                  </a>
                  <a style="cursor:pointer;" onclick="toClickInfo(&quot;2020-2021-1-1&quot;, &quot;K100300540&quot;, &quot;05&quot;, &quot;98221925,刘颖&quot;, &quot;dg&quot;);">
                    <i class="ace-icon fa fa-tasks"></i>
                  </a>
                </div>
              </div>
            </td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="6_9"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="7_9"></td>
          </tr>
          <tr style="height: 58.5px;">
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,228,255,0.7);" id="0_10">第五节(19:25-20:10)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="1_10"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="2_10"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="3_10"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="4_10"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="5_10"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="6_10"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="7_10"></td>
          </tr>
          <tr style="height: 58.5px;">
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,228,255,0.7);text-align:center;" id="dj_0_11" rowspan="2">第六大节</th>
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,228,255,0.7);" id="0_11">第六节(20:25-21:10)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="1_11"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="2_11"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="3_11"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="4_11"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="5_11"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="6_11"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="7_11"></td>
          </tr>
          <tr style="height: 58.5px;">
            <th style="vertical-align:middle;width:150px;background-color:rgba(207,228,255,0.7);" id="0_12">第六节(21:20-22:05)</th>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="1_12"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="2_12"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="3_12"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="4_12"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="5_12"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="6_12"></td>
            <td style="vertical-align:middle;padding:0;height:45px !important;background-color:rgba(207,228,255,0.7);" id="7_12"></td>
          </tr>
        </tbody>
      </table>
    </div>
""".trimIndent()