package com.arash.altafi.calculator.util.calculator.button.common

import com.arash.altafi.calculator.util.calculator.button.CalculatorButton
import com.arash.altafi.calculator.data.model.Calculation

object Decimal : CalculatorButton(".") {

    override val applier: (String) -> String = { n -> "$n$symbol" }

    override fun Calculation.perform(): Calculation {
        if (symbol in result) return this

        return copy(result = applier(result))
    }
}
