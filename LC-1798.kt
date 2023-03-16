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