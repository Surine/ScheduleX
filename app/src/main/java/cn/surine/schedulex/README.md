# 结构
项目中的MVVM架构如下规划：
每个M，VM模块相对独立，项目中目前分的模块有
Login（登录）、Course（课程）、Schedule（课表）、EmptyRoom（空教室）、Score（分数）、Timer（时间管理器）等、Ui（UI模块）
所有调度都需要放在View层控制，由ViewModel的数据变化通知View层。View层可根据需求来随意组合VM，如下面的几个操作

1、获取课表操作为 ：Login -> getCourse 
2、获取成绩操作为 ：Login -> getScore
3、获取空教室操作为：Login -> getClassRoom
4、获取当前周，每次打开app时，进行Login -> getCourse 操作来更新，此操作同时会获取cookie，方便后期调用。

登录主要是用来获取cookie，Login单独抽离ViewModel/Repository，管理登录状态。
外界通过状态来决定UI的变动或者下一步的操作。

详细点理解1的数据流程为
    V_LOGIN[触发登录]-> VM_LOGIN[开始登录] -> RE_LOGIN[仓库：开始登录获取数据] -> VM_LOGIN[成功登录] -> V_LOGIN[收到成功监听]
    -> V_LOGIN[触发获取课程表]-> VM_COURSE[开始获取课程表]-> RE_COURSE[仓库：开始获取课表并获取数据]->VM_COURSE[获取课表成功]
    -> V_LOGIN[触发保存课表]-> VM_COURSE[开始保存课表]-> RE_COURSE[仓库：开始保存课表并成功保存]->VM_COURSE[保存课表成功]
    -> V_LOGIN[提示成功]
    
上述的流程包含三个操作，通过LoginView组织的事件链可以有条不紊的完成这个工作，而且如果想断链也可以按照事件（登录，获取，保存）来控制
删除哪个步骤。