package com.example.zrtool.utils

/**
 * @Author qiwangi
 * @Date 2023/8/13
 * @TIME 18:11
 */
object HtmlUtils {

    /**
     * 去除HTML标签<*>
     */
    fun removeAllTags(text: String): String {
        val regex = Regex("<.*?>")
        return text.replace(regex, "")
    }

    /**
     * 删除指定的标签对<div>dadadada</div>
     */
    fun removeSomeTags(html: String, vararg tags: String): String {
        var result = html
        for (tag in tags) {
            result = result.replace(Regex("<$tag.*?>.*?</$tag>", RegexOption.DOT_MATCHES_ALL), "")
        }
        return result
    }

    /**
     * 仅保留指定的标签对<div>dadadada</div>
     */
    fun keepSomeTags(html: String, vararg tags: String): String {
        val allowedTags = tags.joinToString("|")
        return html.replace(Regex("<(?!$allowedTags).*?>", RegexOption.DOT_MATCHES_ALL), "")
    }



}