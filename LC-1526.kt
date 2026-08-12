class Solution {
    fun minNumberOperations(target: IntArray): Int {
        var res = target[0]
        for (i in 1 until target.size) {
            res += maxOf(target[i] - target[i - 1], 0)
        }
        return res
    }
}

fun main() {
    val solution = Solution()
    println(solution.minNumberOperations(intArrayOf(3, 1, 5, 4, 2)))
}