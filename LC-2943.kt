class Solution {
    fun maximizeSquareHoleArea(@Suppress("UNUSED_PARAMETER") n: Int, @Suppress("UNUSED_PARAMETER") m: Int, hBars: IntArray, vBars: IntArray): Int {
        val side = minOf(getMaxConsecitiveLength(hBars), getMaxConsecitiveLength(vBars))
        return side * side
    }

    fun getMaxConsecitiveLength(bars: IntArray): Int {
        val sortedBars = bars.sorted().toIntArray()

        var res = -1
        var i = 0
        while (i < sortedBars.size) {
            var j = i + 1
            while (j < sortedBars.size && sortedBars[j] - sortedBars[j - 1] == 1) {
                j += 1
            }

            res = maxOf(res, j - i)
            i = j
        }

        return res + 1
    }
}

fun main() {
    val n = 2
    val m = 1
    val hBars = intArrayOf(2, 3)
    val vBars = intArrayOf(2)

    val solution = Solution()
    println(solution.maximizeSquareHoleArea(n, m, hBars, vBars))
}
