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
        val precedence = mapOf(
            "+" to 1,
            "-" to 1,
            "POS" to 2,
            "NEG" to 2
        )

        val rightAssociative = setOf(
            "POS",
            "NEG"
        )

        val operatorStack = ArrayDeque<String>()
        val rpn = ArrayDeque<String>()

        var expectOperand = true

        for (rawToken in tokens) {
            when {
                rawToken[0].isDigit() -> {
                    rpn.addLast(rawToken)
                    expectOperand = false
                }

                rawToken == "(" -> {
                    operatorStack.addLast(rawToken)
                    expectOperand = true
                }

                rawToken == ")" -> {
                    while (operatorStack.isNotEmpty() && operatorStack.last() != "(") {
                        rpn.addLast(operatorStack.removeLast())
                    }

                    if (operatorStack.isEmpty()) {
                        throw IllegalArgumentException()
                    }
                    operatorStack.removeLast()

                    expectOperand = false
                }

                rawToken == "+" || rawToken == "-" -> {
                    val token =
                        if (expectOperand) {
                            if (rawToken == "-") "NEG" else "POS"
                        } else {
                            rawToken
                        }

                    while (
                        operatorStack.isNotEmpty() &&
                        operatorStack.last() != "(" &&
                        shouldPop(
                            stackOp = operatorStack.last(),
                            incomingOp = token,
                            precedence = precedence,
                            rightAssociative = rightAssociative
                        )
                    ) {
                        rpn.addLast(operatorStack.removeLast())
                    }

                    operatorStack.addLast(token)

                    expectOperand = true
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

    fun shouldPop(
        stackOp: String,
        incomingOp: String,
        precedence: Map<String, Int>,
        rightAssociative: Set<String>
    ): Boolean {
        val stackPrecedence = precedence[stackOp]!!
        val incomingPrecedence = precedence[incomingOp]!!

        return if (incomingOp in rightAssociative) {
            stackPrecedence > incomingPrecedence
        } else {
            stackPrecedence >= incomingPrecedence
        }
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

