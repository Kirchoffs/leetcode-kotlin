class Solution {
    enum class TokenType {
        NUMBER,
        PLUS,
        MINUS,
        LPAREN,
        RPAREN
    }

    data class Token(
        val type: TokenType,
        val value: Long? = null
    )


    sealed class Expr

    data class NumberExpr(
        val value: Long
    ) : Expr()

    data class BinaryExpr(
        val left: Expr,
        val operator: TokenType,
        val right: Expr
    ) : Expr()

    data class UnaryExpr(
        val operator: TokenType,
        val expr: Expr
    ) : Expr()


    // =========================
    // Lexer
    // =========================

    private fun tokenize(s: String): ArrayDeque<Token> {
        val tokens = ArrayDeque<Token>()
        var i = 0

        while (i < s.length) {
            when (s[i]) {
                ' ' -> {
                    i++
                }

                '+' -> {
                    tokens.addLast(Token(TokenType.PLUS))
                    i++
                }

                '-' -> {
                    tokens.addLast(Token(TokenType.MINUS))
                    i++
                }

                '(' -> {
                    tokens.addLast(Token(TokenType.LPAREN))
                    i++
                }

                ')' -> {
                    tokens.addLast(Token(TokenType.RPAREN))
                    i++
                }

                in '0'..'9' -> {
                    var num = 0L

                    while (i < s.length && s[i] in '0'..'9') {
                        num = num * 10 + (s[i] - '0')
                        i++
                    }

                    tokens.addLast(
                        Token(TokenType.NUMBER, num)
                    )
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

    private fun parse(tokens: ArrayDeque<Token>): Expr {
        return expression(tokens)
    }

    /*
     * expression -> 
     *     unary (("+" | "-") unary)*
     */
    private fun expression(tokens: ArrayDeque<Token>): Expr {
        var left = unary(tokens)

        while (
            check(tokens, TokenType.PLUS) ||
            check(tokens, TokenType.MINUS)
        ) {
            val operator = tokens.removeFirst().type
            val right = unary(tokens)

            left = BinaryExpr(
                left,
                operator,
                right
            )
        }

        return left
    }

    /*
     * unary -> 
     *     ("+" | "-") unary | 
     *     primary
     */
    private fun unary(tokens: ArrayDeque<Token>): Expr {
        if (
            check(tokens, TokenType.PLUS) ||
            check(tokens, TokenType.MINUS)
        ) {
            val operator = tokens.removeFirst().type
            val expr = unary(tokens)

            return UnaryExpr(
                operator,
                expr
            )
        }

        return primary(tokens)
    }

    /*
     * primary -> 
     *     NUMBER |
     *     "(" expression ")"
     */
    private fun primary(tokens: ArrayDeque<Token>): Expr {
        if (check(tokens, TokenType.NUMBER)) {
            val token = tokens.removeFirst()

            return NumberExpr(token.value!!)
        }

        if (check(tokens, TokenType.LPAREN)) {
            tokens.removeFirst()

            val expr = expression(tokens)

            consume(tokens, TokenType.RPAREN)

            return expr
        }

        throw IllegalArgumentException()
    }


    private fun check(
        tokens: ArrayDeque<Token>,
        type: TokenType
    ): Boolean {
        return tokens.firstOrNull()?.type == type
    }

    private fun consume(
        tokens: ArrayDeque<Token>,
        type: TokenType
    ) {
        if (!check(tokens, type)) {
            throw IllegalArgumentException()
        }

        tokens.removeFirst()
    }


    // =========================
    // Evaluator
    // =========================

    private fun evaluate(expr: Expr): Long {
        return when (expr) {
            is NumberExpr -> {
                expr.value
            }

            is UnaryExpr -> {
                val value = evaluate(expr.expr)

                when (expr.operator) {
                    TokenType.PLUS -> value
                    TokenType.MINUS -> -value
                    else -> throw IllegalArgumentException()
                }
            }

            is BinaryExpr -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)

                when (expr.operator) {
                    TokenType.PLUS -> left + right
                    TokenType.MINUS -> left - right
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
