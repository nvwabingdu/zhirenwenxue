package com.example.zrwenxue.app

import kotlin.random.Random


/**
 * @Author qiwangi
 * @Date 2023/8/19
 * @TIME 09:51
 */
object Single {
    fun generateRandomColors(): Triple<String, String, String> {
        val colorSet = HashSet<Int>()
        val colors = arrayListOf<String>()

        fun getRandomColor(): String {
            val red = Random.nextInt(0, 256)
            val green = Random.nextInt(0, 256)
            val blue = Random.nextInt(0, 256)
            return "#%02X%02X%02X".format(red, green, blue)
        }

        fun getColorDist(color: String, existingColors: List<String>): Int {
            val colorInt = color.substring(1).toInt(16)
            var minDist = Int.MAX_VALUE
            for (existingColor in existingColors) {
                val existingColorInt = existingColor.substring(1).toInt(16)
                val dist = Math.abs(colorInt - existingColorInt)
                if (dist < minDist) {
                    minDist = dist
                }
            }
            return minDist
        }

        while (colors.size < 3) {
            val color = getRandomColor()
            val minDist = getColorDist(color, colors)
            if (!colorSet.contains(minDist) && minDist > 100000) {
                colors.add(color)
                colorSet.add(minDist)
            }
        }

        return Triple(colors[0], colors[1], colors[2])
    }


}