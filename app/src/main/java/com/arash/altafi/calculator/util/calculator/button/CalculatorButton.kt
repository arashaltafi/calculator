package com.arash.altafi.calculator.util.calculator.button

import com.arash.altafi.calculator.util.calculator.button.common.AllClear
import com.arash.altafi.calculator.util.calculator.button.common.Decimal
import com.arash.altafi.calculator.util.calculator.button.common.Digit
import com.arash.altafi.calculator.util.calculator.button.function.Equals
import com.arash.altafi.calculator.util.calculator.button.function.NumSign
import com.arash.altafi.calculator.util.calculator.button.function.Percent
import com.arash.altafi.calculator.util.calculator.button.operator.Add
import com.arash.altafi.calculator.util.calculator.button.operator.Div
import com.arash.altafi.calculator.util.calculator.button.operator.Mul
import com.arash.altafi.calculator.util.calculator.button.operator.Sub
import com.arash.altafi.calculator.data.model.Calculation
import com.arash.altafi.calculator.util.Evaluator

abstract class CalculatorButton(val symbol: String) {
    open val applier = { n: String -> "$n $symbol " }
    abstract fun Calculation.perform(): Calculation
}

open class FunctionButton(symbol: String) : CalculatorButton(symbol) {

    protected val evaluator = Evaluator()

    override fun Calculation.perform(): Calculation {
        if (result == "0") return this

        evaluator.expression = applier(result)

        return copy(result = evaluator.eval())
    }
}

open class OperatorButton(symbol: String) : CalculatorButton(symbol) {

    override fun Calculation.perform(): Calculation {
        if (expression.isEmpty() && result == "0") return this

        val amendedExpression = when {
            result == "0" -> expression.substringBeforeLast(lastOperator)
            expression.endsWith(lastOperator) -> expression.plus(result)
            else -> result
        }

        return copy(
            expression = applier(amendedExpression),
            result = "0"
        )
    }
}

val calculatorButtons = listOf(
    AllClear,
    NumSign,
    Percent,
    Div,
    Digit('7'),
    Digit('8'),
    Digit('9'),
    Mul,
    Digit('4'),
    Digit('5'),
    Digit('6'),
    Sub,
    Digit('1'),
    Digit('2'),
    Digit('3'),
    Add,
    Digit('0'),
    Decimal,
    Equals,
)
