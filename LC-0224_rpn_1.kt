class Solution {
    fun tokenize(s: String): ArrayDeque<String> {
        val tokens = ArrayDeque<String>()

        var i = 0
        while (i < s.length) {
            when {
                s[i] == ' ' -> {
                    i++
                }

                s[i] in "+-()" -> {
                    tokens.addLast(s[i].toString())
                    i++
                }

                s[i].isDigit() -> {
                    val j = i

                    while (i < s.length && s[i].isDigit()) {
                        i++
                    }

                    tokens.addLast(s.substring(j, i))
                }

                else -> {
                    throw IllegalArgumentException()
                }
            }
        }

        return tokens
    }

    fun convertToRpn(tokens: ArrayDeque<String>): ArrayDeque<String> {
        val operatorStack = ArrayDeque<String>()
        val rpn = ArrayDeque<String>()

        var expectOperand = true

        for (token in tokens) {
            when {
                token[0].isDigit() -> {
                    rpn.addLast(token)

                    while (operatorStack.isNotEmpty() && (operatorStack.last() == "NEG" || operatorStack.last() == "POS")) {
                        rpn.addLast(operatorStack.removeLast())
                    }

                    expectOperand = false
                }

                token == "(" -> {
                    operatorStack.addLast(token)
                    expectOperand = true
                }

                token == ")" -> {
                    while (operatorStack.isNotEmpty() && operatorStack.last() != "(") {
                        rpn.addLast(operatorStack.removeLast())
                    }

                    if (operatorStack.isEmpty()) {
                        throw IllegalArgumentException()
                    }
                    operatorStack.removeLast()

                    while (operatorStack.isNotEmpty() && (operatorStack.last() == "NEG" || operatorStack.last() == "POS")) {
                        rpn.addLast(operatorStack.removeLast())
                    }

                    expectOperand = false
                }

                token == "+" || token == "-" -> {
                    if (expectOperand) {
                        operatorStack.addLast(
                            if (token == "-") "NEG" else "POS"
                        )
                    } else {
                        while (operatorStack.isNotEmpty() && operatorStack.last() != "(") {
                            rpn.addLast(operatorStack.removeLast())
                        }

                        operatorStack.addLast(token)
                        expectOperand = true
                    }
                }
            }
        }

        while (operatorStack.isNotEmpty()) {
            if (operatorStack.last() == "(") {
                throw IllegalArgumentException()
            }

            rpn.addLast(operatorStack.removeLast())
        }

        return rpn
    }

    fun evaluate(rpn: ArrayDeque<String>): Long {
        val operandStack = ArrayDeque<Long>()

        for (token in rpn) {
            when {
                token[0].isDigit() -> {
                    operandStack.addLast(token.toLong())
                }

                token == "NEG" -> {
                    val operand = operandStack.removeLast()
                    operandStack.addLast(-operand)
                }

                token == "POS" -> {
                    val operand = operandStack.removeLast()
                    operandStack.addLast(operand)
                }

                token == "+" -> {
                    val right = operandStack.removeLast()
                    val left = operandStack.removeLast()

                    operandStack.addLast(left + right)
                }

                token == "-" -> {
                    val right = operandStack.removeLast()
                    val left = operandStack.removeLast()

                    operandStack.addLast(left - right)
                }
            }
        }

        return operandStack.removeLast()
    }

    fun calculate(s: String): Int {
        val tokens = tokenize(s)
        val rpn = convertToRpn(tokens)

        return evaluate(rpn).toInt()
    }
}

fun main() {
    val s = Solution()
    println(s.calculate("1 + (2 - 3)"))
}
