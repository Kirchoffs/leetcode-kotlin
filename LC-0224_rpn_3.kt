class Solution {
    enum class Associativity {
        LEFT,
        RIGHT
    }

    data class Operator(
        val precedence: Int,
        val associativity: Associativity,
        val arity: Int
    )

    val operators = mapOf(
        "+" to Operator(1, Associativity.LEFT, 2),
        "-" to Operator(1, Associativity.LEFT, 2),

        "POS" to Operator(2, Associativity.RIGHT, 1),
        "NEG" to Operator(2, Associativity.RIGHT, 1)
    )

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

    fun convertToRpn(
        tokens: ArrayDeque<String>
    ): ArrayDeque<String> {
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
                    while (
                        operatorStack.isNotEmpty() &&
                        operatorStack.last() != "("
                    ) {
                        rpn.addLast(
                            operatorStack.removeLast()
                        )
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
                            if (rawToken == "-") {
                                "NEG"
                            } else {
                                "POS"
                            }
                        } else {
                            rawToken
                        }

                    while (
                        operatorStack.isNotEmpty() &&
                        operatorStack.last() != "(" &&
                        shouldPop(
                            stackOp = operatorStack.last(),
                            incomingOp = token
                        )
                    ) {
                        rpn.addLast(
                            operatorStack.removeLast()
                        )
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

            rpn.addLast(
                operatorStack.removeLast()
            )
        }

        return rpn
    }

    fun shouldPop(
        stackOp: String,
        incomingOp: String
    ): Boolean {
        val stackOperator = operators[stackOp]!!
        val incomingOperator = operators[incomingOp]!!

        return if (
            incomingOperator.associativity == Associativity.RIGHT
        ) {
            stackOperator.precedence >
                    incomingOperator.precedence
        } else {
            stackOperator.precedence >=
                    incomingOperator.precedence
        }
    }

    fun evaluate(
        rpn: ArrayDeque<String>
    ): Long {
        val operandStack = ArrayDeque<Long>()

        for (token in rpn) {
            if (token[0].isDigit()) {
                operandStack.addLast(
                    token.toLong()
                )
                continue
            }

            val operator = operators[token]!!

            if (operator.arity == 1) {
                val operand =
                    operandStack.removeLast()

                val result =
                    when (token) {
                        "NEG" -> -operand
                        "POS" -> operand

                        else ->
                            throw IllegalArgumentException()
                    }

                operandStack.addLast(result)
            } else if (operator.arity == 2) {
                val right =
                    operandStack.removeLast()

                val left =
                    operandStack.removeLast()

                val result =
                    when (token) {
                        "+" -> left + right
                        "-" -> left - right

                        else ->
                            throw IllegalArgumentException()
                    }

                operandStack.addLast(result)
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
