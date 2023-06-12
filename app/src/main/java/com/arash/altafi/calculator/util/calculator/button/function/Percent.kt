package com.arash.altafi.calculator.util.calculator.button.function

import com.arash.altafi.calculator.util.calculator.button.FunctionButton

object Percent : FunctionButton("%") {

    override val applier: (String) -> String = { n -> "$n ÷ 100" }
}
