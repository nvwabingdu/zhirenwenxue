



//fun groupNumbersAndGetRanges(numbers: List<Int>, groupSize: Int = 100): List<String> {
//    val ranges = mutableListOf<String>()
//    for (i in numbers.indices.step(groupSize)) {
//        val group = numbers.subList(i, minOf(i + groupSize, numbers.size))
//        val min = group.minOf { it }
//        val max = group.maxOf { it }
//        ranges.add("$min-$max")
//    }
//    return ranges
//}
//
//fun main() {
//    val numbers = (0..0).toList()
//    val result = groupNumbersAndGetRanges(numbers)
//    println(result) // 输出 ["1-5", "6-10", "11-15", "16-20", "21-25", "26-30", "31-35"]
//}