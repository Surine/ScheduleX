package cn.surine.schedulex.ui.schedule_import_pro.util

//提供一个处理周的工具类
//希望达到一种效果，传入字符串之后可以匹配不同的形式

//目前几种频繁出现的形式
//1-2周 done
//1-2(单周) done
//1-2(双周) done
//1-2(全部) done
//1-2周(单) done
//1-2周(双) done
//1-2周 done
//1-2周单 done
//1-2周双 done
//1-2周 done
//1-2周单周 done
//1-3周双周 done
//第1-2周 done
//单周(1-2) done
//双周(1-2) done
//周(1-2) done

//1,2,3,4,5,6(周) done
//1(周) done
//1周 done
//1-2,4-5(周) done
//1,2,3-6(周) done
//7-13,15(周) done
//第1-2周|单周  done

//再出现补充即可

//嘘，小点声，价值连城的代码～
//头发散落一地
object WeekUtilsV2 {
    fun parse(str: String): List<Int> {
        val list = mutableListOf<Int>()
        when {
            str.contains(",") -> {
                //对应混合模式的（包含逗号和破折号）
                Regex("""[[\d+]*[\d+-\\d+]*[,]*]*""").find(str)?.value?.split(",")?.forEach {
                    list.addAll(parse(it))
                }
            }
            str.contains("-") -> {
                //目前短破折号要么自己出现，要么搭配逗号出现,自己出现的时候仅有一个范围段
                Regex("""\d+-\d+""").find(str)?.value?.split("-")?.let { result ->
                    when {
                        str.contains("单") -> list.addAll((result[0].toInt()..result[1].toInt()).filterIndexed { index, _ -> index % 2 == 0 })
                        str.contains("双") -> list.addAll((result[0].toInt()..result[1].toInt()).filterIndexed { index, _ -> index % 2 == 1 })
                        else -> list.addAll(result[0].toInt()..result[1].toInt())
                    }
                }
            }
            else -> {
                //单个元素
                Regex("""\d+""").find(str)?.value?.let {
                    list.add(it.toInt())
                }
            }
        }
        return list
    }
}


//测试
fun main() {
    val map = hashMapOf(
            "1-10周" to listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            "1-10(单周)" to listOf(1, 3, 5, 7, 9),
            "1-10(双周)" to listOf(2, 4, 6, 8, 10),
            "1-10(全部)" to listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            "1-10(单)" to listOf(1, 3, 5, 7, 9),
            "1-10(双)" to listOf(2, 4, 6, 8, 10),
            "1-10单" to listOf(1, 3, 5, 7, 9),
            "1-10双" to listOf(2, 4, 6, 8, 10),
            "1-10周单" to listOf(1, 3, 5, 7, 9),
            "1-10周双" to listOf(2, 4, 6, 8, 10),
            "1-10周单周" to listOf(1, 3, 5, 7, 9),
            "1-10周双周" to listOf(2, 4, 6, 8, 10),
            "1-10周全部" to listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            "第1-10周" to listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            "第1-10周单周" to listOf(1, 3, 5, 7, 9),
            "第1-10周双周" to listOf(2, 4, 6, 8, 10),
            "第1-10周单" to listOf(1, 3, 5, 7, 9),
            "第1-10周双" to listOf(2, 4, 6, 8, 10),
            "单周(1-10)" to listOf(1, 3, 5, 7, 9),
            "双周(1-10)" to listOf(2, 4, 6, 8, 10),
            "周(1-10)" to listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            "1,2,3,4,5,6(周)" to listOf(1, 2, 3, 4, 5, 6),
            "1,2,3,4,5,6周" to listOf(1, 2, 3, 4, 5, 6),
            "4,6周" to listOf(4, 6),
            "6周" to listOf(6),
            "7" to listOf(7),
            "1-3,7-10(周)" to listOf(1, 2, 3, 7, 8, 9, 10),
            "1,2,4-5(周)" to listOf(1, 2, 4, 5),
            "1,2,4-5,8(周)" to listOf(1, 2, 4, 5, 8),
            "1,2,4-5,8,10-13(周)" to listOf(1, 2, 4, 5, 8, 10, 11, 12, 13),
            "1-5,10(周)" to listOf(1, 2, 3, 4, 5, 10),
            "1-5,10" to listOf(1, 2, 3, 4, 5, 10)
    )
    for ((k, v) in map) {
        println(WeekUtilsV2.parse(k) == v)
    }
}