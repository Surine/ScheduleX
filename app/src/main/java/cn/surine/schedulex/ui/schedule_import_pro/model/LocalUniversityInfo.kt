package cn.surine.schedulex.ui.schedule_import_pro.model

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
        val name: String,
        val code: String,
        val city: String
)