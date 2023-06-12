package com.arash.altafi.calculator.util.calculator.button.common

import com.arash.altafi.calculator.util.calculator.button.CalculatorButton
import com.arash.altafi.calculator.data.model.Calculation

object AllClear : CalculatorButton("AC") {

    override val applier: (String) -> String = { "" }

    override fun Calculation.perform(): Calculation {
        return copy(
            expression = applier(expression),
            result = "0"
        )
    }
}
