package com.arash.altafi.calculator.util.calculator.button.common

import com.arash.altafi.calculator.util.calculator.button.CalculatorButton
import com.arash.altafi.calculator.data.model.Calculation

data class Digit(val digit: Char) : CalculatorButton("$digit") {

    override val applier: (String) -> String = { n -> "$n$digit" }

    override fun Calculation.perform(): Calculation {
        val amendedResult = result.takeUnless { it == "0" }.orEmpty()

        return copy(result = applier(amendedResult))
    }
}
