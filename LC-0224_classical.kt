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
        var num = 0
        var op = '+'
        while (i < n) {
            val ch = s[i]

            if (ch.isDigit()) {
                num = num * 10 + (ch - '0')
            }

            if (ch == '(') {
                i++
                num = calculateInternal()
            }

            if ((!ch.isDigit() && ch != ' ') || i == n - 1) {
                if (op == '+') {
                    res += num
                } else if (op == '-') {
                    res -= num
                }

                num = 0
                op = ch
            }

            if (ch == ')') {
                break
            }

            i++
        }

        return res
    }
}

fun main() {
    val s = Solution()
    println(s.calculate("1 + (2 - 3)"))
}
