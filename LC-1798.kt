class Solution {
    fun getMaximumConsecutive(coins: IntArray): Int {
        coins.sort()
        var res = 0
        for (coin in coins) {
            if (coin > res + 1) {
                return res + 1
            } else {
                res += coin
            }
        }
        return res + 1
    }
}

fun main() {
    val coins = intArrayOf(1, 3)
    val solution = Solution()

    println(solution.getMaximumConsecutive(coins))
}
