class Solution {
    fun generateAbbreviations(word: String): List<String> {
        val res = mutableListOf<String>()
        val len = word.length
        for (i in 0 until (1 shl len)) {
            res.add(processMaskToString(word, i))
        }
        return res
    }

    @Suppress("NAME_SHADOWING")
    private fun processMaskToString(word: String, mask: Int): String {
        val sb = StringBuilder()
        var i = 0
        var mutableMask = mask
        while (i < word.length) {
            val bit = mutableMask and 1
            if (bit == 0) {
                sb.append(word[i])
                mutableMask = mutableMask shr 1
                i++
            } else {
                var cnt = 0
                while (i < word.length) {
                    val bit = mutableMask and 1
                    if (bit == 1) {
                        cnt++
                        mutableMask = mutableMask shr 1
                        i++
                    } else {
                        break
                    }
                }
                sb.append(cnt)
            }
        }
        return sb.toString()
    }
}

fun main() {
    val solution = Solution()
    println(solution.generateAbbreviations("word"))
}
