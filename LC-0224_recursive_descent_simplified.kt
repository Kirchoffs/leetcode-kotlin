class Solution {
    sealed class Expr

    data class NumberExpr(
        val value: Long
    ) : Expr()

    data class BinaryExpr(
        val left: Expr,
        val operator: String,
        val right: Expr
    ) : Expr()

    data class UnaryExpr(
        val operator: String,
        val expr: Expr
    ) : Expr()


    // =========================
    // Lexer
    // =========================

    private fun tokenize(s: String): ArrayDeque<String> {
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
                    val start = i

                    while (i < s.length && s[i].isDigit()) {
                        i++
                    }

                    tokens.addLast(s.substring(start, i))
                }

                else -> {
                    throw IllegalArgumentException()
                }
            }
        }

        return tokens
    }


    // =========================
    // Parser
    // =========================

    private fun parse(tokens: ArrayDeque<String>): Expr {
        return expression(tokens)
    }

    /*
     * expression ->
     *     unary (("+" | "-") unary)*
     */
    private fun expression(tokens: ArrayDeque<String>): Expr {
        var left = unary(tokens)

        while (
            tokens.firstOrNull() == "+" ||
            tokens.firstOrNull() == "-"
        ) {
            val operator = tokens.removeFirst()
            val right = unary(tokens)

            left = BinaryExpr(left, operator, right)
        }

        return left
    }

    /*
     * unary ->
     *     ("+" | "-") unary |
     *     primary
     */
    private fun unary(tokens: ArrayDeque<String>): Expr {
        if (
            tokens.first() == "+" ||
            tokens.first() == "-"
        ) {
            val operator = tokens.removeFirst()
            return UnaryExpr(operator, unary(tokens))
        }

        return primary(tokens)
    }

    /*
     * primary ->
     *     NUMBER |
     *     "(" expression ")"
     */
    private fun primary(tokens: ArrayDeque<String>): Expr {
        val token = tokens.first()
            ?: throw IllegalArgumentException()

        if (token[0].isDigit()) {
            tokens.removeFirst()
            return NumberExpr(token.toLong())
        }

        if (token == "(") {
            tokens.removeFirst()

            val expr = expression(tokens)

            if (tokens.removeFirst() != ")") {
                throw IllegalArgumentException()
            }

            return expr
        }

        throw IllegalArgumentException()
    }


    // =========================
    // Evaluator
    // =========================

    private fun evaluate(expr: Expr): Long {
        return when (expr) {
            is NumberExpr -> expr.value

            is UnaryExpr -> {
                val value = evaluate(expr.expr)

                when (expr.operator) {
                    "+" -> value
                    "-" -> -value
                    else -> throw IllegalArgumentException()
                }
            }

            is BinaryExpr -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)

                when (expr.operator) {
                    "+" -> left + right
                    "-" -> left - right
                    else -> throw IllegalArgumentException()
                }
            }
        }
    }


    // =========================
    // Calculator
    // =========================

    fun calculate(s: String): Int {
        val tokens = tokenize(s)
        val ast = parse(tokens)

        return evaluate(ast).toInt()
    }
}


fun main() {
    val s = Solution()
    println(s.calculate("1 + (2 - 3)"))
}
