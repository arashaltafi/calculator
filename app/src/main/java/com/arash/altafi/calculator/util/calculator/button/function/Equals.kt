package com.arash.altafi.calculator.util.calculator.button.function

import com.arash.altafi.calculator.util.calculator.button.FunctionButton
import com.arash.altafi.calculator.data.model.Calculation

object Equals : FunctionButton("=") {

    override val applier: (String) -> String = { it }

    override fun Calculation.perform(): Calculation {
        if (!isComplete) return this

        val amendedExpression = if (result == "0")
            expression.substringBeforeLast(lastOperator)
        else
            expression.plus(result)

        evaluator.expression = applier(amendedExpression)

        return copy(expression = evaluator.expression, result = evaluator.eval())
    }
}
