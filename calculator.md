# Notes
## Solution 1 Classical Template
The key to this approach is to first process the current number, and then handle how it gets added to result.

### LC-224 Basic Calculator: Plus / Minus / Parenthesis
```kotlin
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
```

## Solution 2 Intuitive Calculation
The core idea of this approach is to process tokens on the fly: as soon as a digit is encountered, it computes the full number; when an open parenthesis is met, it evaluates the nested expression immediately via recursion; and upon reaching a closing parenthesis, it simply breaks.

### LC-224 Basic Calculator: Plus / Minus / Parenthesis
```kotlin
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
```

## Solution 3 Recursive Descent Parsing
Recursive Descent Parsing

### LC-224 Basic Calculator: Plus / Minus / Parenthesis
```kotlin
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
```

## Solution 4 Reverse Polish Notation
Reverse Polish Notation (RPN)

The core idea is to tokenize the expression, then construct and evaluate the Reverse Polish Notation (RPN).

## Solution 5 Stacks
The core idea is simulating DFS with two stacks: one tracking previous results and the other storing outer coefficients.
