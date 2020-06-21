# ScheduleX
ScheduleX Build On JetPack 更优雅的使用课程表

## 一. 介绍
ScheduleX是一个课程表应用，支持添加多个课表，极速切换课表，更支持通过不同方式导入课表
，快捷操作课程等功能。目前仅支持酷安下载。

下载链接：https://www.coolapk.com/apk/cn.surine.schedulex


## 二. 功能

#### 1. 使用各种方式导入课程 *登录教务处*，*导入文件（Json，CSV，Excel）*，*超级课程表导入*等。
- 登录教务处目前仅针对天津科技大学，但是有计划适配其他学校，如果有同学喜欢我们的App，并愿意一同发展壮大它，适配您的学校可以联系我。这边将支持客户端对接或者后端对接两种形式。
- 导入文件Json模板如下。
```
[{
        "campusName":"",
        "classDay":"1",
        "classSessions":"1",
        "classWeek":"111111111111111111111111100000",
        "classroomName":"",
        "color":"#0d629b",
        "continuingSession":"2",
        "coureName":"Android开发技术",
        "coursePropertiesName":"",
        "id":"1@70e8db61-0852-432a-afc6-d3db7aceeaba1586102085712",
        "scheduleId":1,
        "teacherName":"mr",
        "teachingBuildingName":"555",
        "weekDescription":"",
        "xf":""
    }]
    
    字段含义从上至下依此为：
    校园名（可忽略），星期几上课，第几节开始，哪几周上课（0代表本周不上课，下标从1开始，如111100 代表前四周上课，5，6周不上），教室名（可忽略），课程格子颜色，持续节次（注意不是终止节次），课程名，课程属性名（可忽略），id（可忽略），scheduleId(可忽略)，老师名，上课地点，周描述（可忽略），学分
```
- 导入文件CSV模板如下
```
campusName,classDay,classSessions,classWeek,classroomName,color,continuingSession,courseName,coursePropertiesName,id,scheduleId,teacherName,teachingBuildingName,weekDescription,xf
,1,1,111111111111111111111111100000,,#0d629b,2,Android开发技术,,1@70e8db61-0852-432a-afc6-d3db7aceeaba1586102085712,1,mr,555,,

    字段名及含义以及是否可以忽略同上文Json格式类似，您可以在JSON转CSV网站上转换（注意：由于校内接口名写错了，json的coureName字段和CSV的本字段名称不一样，在CSV中为courseName），您可以新建一个文本文件并命名为xxx.csv将本段内容复制进去，并按照首行对应字段含义来书写课程记录，每个记录占一行。    
```
或者您可以直接点击 [模板](https://github.com/Surine/ScheduleX/blob/dev/csv%E6%A8%A1%E6%9D%BF.csv) 查看
- 使用超级课程表账号导入课程，可以登录您的超级课程表账号，获取最近几学期的课程数据

#### 2.课表主界面，点击周标题切换周，左右滑动可切换
 - 在课表页面左右滑动可以快速查看其他周信息，当重新进入App或者从其他页面返回时，会重新返回当前周，更多显示元素和信息可以在课表配置页面调节，课表配置支持快捷操作。
 - 同时课程格子支持点击和长按操作。点击可以查看当前格子课程详细信息，长按可以进入批量快捷编辑模式。
 
#### 3.支持时间表配置与显示，可以按照您的需求配置时间表
 - 智能计算课程时长，只需要规定每节课上课时间和每节课持续时间即可制作属于自己的时间表。
 
#### 4.小部件计划
- 现在支持周视图和日视图两种样式啦，后面可能会花费更多时间致力于小部件的样式定制。

## 三. 加入我们或者提出意见
 欢迎加入ScheduleX反馈与吐槽QQ群-686976115
