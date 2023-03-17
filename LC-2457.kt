class Solution {
    fun getDigitsSum(n: Long): Long {
        var num = n
        var sum = 0L

        while (num != 0L) {
            sum += num % 10
            num /= 10
        }

        return sum
    }

    fun makeIntegerBeautiful(n: Long, target: Int): Long {
        var end = n
        var base = 1L

        while (getDigitsSum(end) > target) {
            end = end / 10 + 1
            base *= 10
        }

        return end * base - n
    }
}

fun main() {
    val n = 16L
    val target = 6
    val solution = Solution()
    
    println(solution.makeIntegerBeautiful(n, target))
}
