package cn.surine.schedulex.ui.schedule_import_pro.model

import cn.surine.schedulex.ui.schedule_import_pro.util.ParseData

/**
 * Intro：
 *  本地列表实体类
 * @author sunliwei
 * @date 10/7/20 17:18
 */

data class LocalUniversityInfo(
        val province: String,
        val schools: List<LocalUniversity>
)

data class LocalUniversity(
        var name: String,
        val code: String,
        val city: String = "",
        val jwSystem: String = ""
) {
    fun isHtmlSystem() = if (ParseData.commonHtmlSystem.contains(jwSystem)) 3 else 1
}