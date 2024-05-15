package com.example.zrprint

fun removeTrailingBackslash(text: String): String {
    val lastIndex = text.lastIndexOf('\\')
    return if (lastIndex >= 0 && lastIndex == text.length - 1) {
        text.substring(0, lastIndex)
    } else {
        text
    }
}

fun main() {
    val inputText = "时度力\\揣歪捏怪\\\\"
    val modifiedText = removeTrailingBackslash(inputText)
    println(modifiedText)
}