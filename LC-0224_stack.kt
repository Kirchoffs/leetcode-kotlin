class Solution {
    fun calculate(s: String): Int {
        val resStack = ArrayDeque<Long>()
        val sgnStack = ArrayDeque<Int>()

        var i = 0
        var res = 0L
        var sgn = 1
        while (i < s.length) {
            if (s[i] == ' ') {
                i++
                continue
            }

            if (s[i].isDigit()) {
                var num = 0L
                while (i < s.length && s[i].isDigit()) {
                    num = num * 10 + (s[i] - '0')
                    i++
                }

                res += sgn * num
                sgn = 1
            } else if (s[i] == '+') {
                sgn = 1
                i++
            } else if (s[i] == '-') {
                sgn = -1
                i++
            } else if (s[i] == '(') {
                resStack.addLast(res)
                sgnStack.addLast(sgn)

                res = 0
                sgn = 1
                i++
            } else if (s[i] == ')') {
                res = sgnStack.removeLast() * res + resStack.removeLast()
                sgn = 1
                i++
            }
        }

        return res.toInt()
    }
}

fun main() {
    val s = Solution()
    println(s.calculate("1 + (2 - 3)"))
}
