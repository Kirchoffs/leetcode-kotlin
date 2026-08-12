@Suppress("NAME_SHADOWING")
class Solution {
    fun minOperations(n: Int): Int {
        val digits = mutableListOf<Int>()

        var n = n
        while (n != 0) {
            digits.add(n % 2)
            n /= 2
        }

        var res = 0
        var idx = 0
        while (idx < digits.size) {
            while (idx < digits.size && digits[idx] == 0) {
                idx++
            }

            if (idx < digits.size) {
                res++

                var cnt = 0
                while (idx < digits.size && digits[idx] == 1) {
                    idx++
                    cnt++
                }

                if (cnt > 1) {
                    if (idx < digits.size) {
                        digits[idx] = 1
                    } else {
                        res++
                    }
                }
            }
        }

        return res
    }
}

fun main() {
    val n = 39
    val solution = Solution()
    
    println(solution.minOperations(n))
}
