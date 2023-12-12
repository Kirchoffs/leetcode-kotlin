class Solution {
    fun nthUglyNumber(n: Int): Int {
        val uglyNumbers = IntArray(n + 1) { 1 }
        
        var (i2, i3, i5) = intArrayOf(1, 1, 1)
        for (i in 2 .. n) {
            val (n2, n3, n5) = intArrayOf(
                uglyNumbers[i2] * 2,
                uglyNumbers[i3] * 3,
                uglyNumbers[i5] * 5,
            )

            val nextUglyNumber = minOf(n2, minOf(n3, n5))

            if (nextUglyNumber == n2) i2++
            if (nextUglyNumber == n3) i3++
            if (nextUglyNumber == n5) i5++

            uglyNumbers[i] = nextUglyNumber
        }

        return uglyNumbers[n]
    }
}

fun main() {
    val n = 10
    val solution = Solution()
    println(solution.nthUglyNumber(n))
}
