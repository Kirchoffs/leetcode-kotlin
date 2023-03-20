import java.util.Arrays

class Solution {
    fun maxNumOfMarkedIndices(nums: IntArray): Int {
        if (nums.size < 2) {
            return 0
        }
        Arrays.sort(nums)

        var res = 0
        var left = 0
        var right = (nums.size + 1) / 2
        while (right < nums.size) {
            if (nums[left] * 2 <= nums[right]) {
                left++
                res += 2
            }
            right++
        }

        return res
    }
}

fun main() {
    val nums = intArrayOf(9, 2, 5, 4)
    val solution = Solution()
    
    println(solution.maxNumOfMarkedIndices(nums))
}
