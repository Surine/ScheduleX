# ScheduleX

ScheduleX Build On JetPack 更优雅的使用课程表

![](https://img.shields.io/github/issues/Surine/ScheduleX)
![](https://img.shields.io/github/forks/Surine/ScheduleX)
![](https://img.shields.io/github/stars/Surine/ScheduleX)
![](https://img.shields.io/badge/lang-Kotln%20%26Java-green)

## 什么东西？

ScheduleX是一个课程表应用，支持添加多个课表，极速切换课表，更支持通过不同方式导入课表，快捷操作课程等功能。并且正在变强大。

Schedulex秉承以下原则：

- 操作高效，响应迅速 （得益于Jetpack的架构，已经做到了）
- 更好看的UI（发挥了一个大前端程序猿毕生审美）
- 尽可能的减少导入的困难（虽然现在不是那么完美，但是会做到的）

下载链接：https://www.coolapk.com/apk/cn.surine.schedulex
当然，如果你是一个乐于钻研的好孩子，你可以尝试自己编译一下（dev分支）。



## 有什么功能

导入课程当然是最重要的部分啦。

#### 目前支持的导入方式：

- 手动添加 （虽然手动添加不太完美……但是新版在做了，进度1%）
- 教务系统 （适配了没几个学校，可怜）

- 超级课程表导入 （这是一个很帅的功能）

- Json导入（极客最爱的导入方式）

- CSV导入（即使没有excel导入，您也可以通过将excel保存成csv导入）

- 小爱课程表导入（全网首发）



#### 教务系统支持：

正方教务

新正方教务

强智教务

新强智教务

树维教务

天津科技大学（校内网）@提供接口资源和测试的童鞋们

武汉纺织大学 @花生酱啊

北京大学 @花生酱啊

西南科技大学

华南农业大学@Grey阿灰

北方工业大学

浙大宁波理工学院

湖南工学院

重庆科技学院

中南林业科技大学

中国科学技术大学

中南大学

大连工业大学



#### 文件导入模板

1. json模板（也可以在app内直接下载）

   ```json
   //为了方便解释字段含义，我直接在字段后面使用双斜杠作为注释，使用前请将注释去掉。
   [
       {
           "day":1,       //星期几？取值1-7
           "name":"课程名",  //课程名
           "position":"上课地点",  //上课地点
           "sectionContinue":2,    //课程持续几小节（注意不是终止节次，而是持续）
           "sectionStart":1,    //课程从第几节开始的
           "teacher":"教师名",   //教师名
           "week":[
               1,
               3,
               5,
               7,
               9
           ]   //上课周列表
       }
   ]
   ```

2. csv模板（也可以在app内直接下载）

   csv可以通过excel，wps来新建表格编辑后保存。使用方法同excel一样，但是保存的时候保存为csv格式即可。

   ```
   name,teacher,position,sectionStart,sectionContinue,day,week
   "课程名",教师,位置,1,2,1,1 2 3 4 5
   ```

   释义同json，不再单独解释。

#### 别人有的我们也要有，没有的也要有

- 导出到日历

- 简单备忘录

- 考试安排

- 批量处理

- 周视图，日视图小部件

- 色卡

- 时间表

- 各种高级配置

  

##  加入我们或者提出意见

 欢迎加入ScheduleX反馈与吐槽QQ群-686976115

点个Star再走吧。

Fork一下发个pr的同学都很棒。

微信支付宝赞助一下我也可以。

开始你的奇妙旅程吧~
