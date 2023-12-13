import kotlin.math.pow

class Solution {
    fun nthUglyNumber(n: Int, a: Int, b: Int, c: Int): Int {
        var (aL, bL, cL) = listOf(a.toLong(), b.toLong(), c.toLong())

        fun gcd(x: Long, y: Long): Long = if (y == 0L) x else gcd(y, x % y)
        fun lcm(x: Long, y: Long): Long = x / gcd(x, y) * y

        fun countElements(m: Long): Int {
            var res = 0L

            res += m / aL + m / bL + m / cL
            res -= m / lcm(aL, bL) + m / lcm(aL, cL) + m / lcm(bL, cL)
            res += m / lcm(aL, lcm(bL, cL))

            return res.toInt()
        }

        var (l, r) = listOf(1L, 2 * 10.toDouble().pow(9).toLong() + 1)
        while (l < r) {
            val m = l + (r - l) / 2
            if (countElements(m) < n) {
                l = m + 1
            } else {
                r = m
            }
        }

        return l.toInt()
    }
}

fun main() {
    val s = Solution()
    println(s.nthUglyNumber(3, 2, 3, 5))
}
