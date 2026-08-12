class Solution {
    private var i = 0
    private var s = ""

    fun calculate(s: String): Int {
        i = 0
        this.s = s
        
        return calculateInternal()
    }

    private fun calculateInternal(): Int {
        val n = s.length

        var res = 0
        var sign = 1
        while (i < n) {
            val ch = s[i]

            if (ch == ' ') {
                i++
            } else if (ch.isDigit()) {
                var num = 0
                while (i < n && s[i].isDigit()) {
                    num = num * 10 + (s[i] - '0')
                    i++
                }

                res += sign * num
            } else if (ch == '(') {
                i++
                val num = calculateInternal()
                i++

                res += sign * num
            } else if (ch == ')') {
                break
            } else if (ch == '+') {
                sign = 1
                i++
            } else if (ch == '-') {
                sign = -1
                i++
            }
        }

        return res
    }
}

fun main() {
    val s = Solution()
    println(s.calculate("1 + (2 - 3)"))
}
