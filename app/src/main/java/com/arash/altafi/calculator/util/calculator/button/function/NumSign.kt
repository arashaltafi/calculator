package com.arash.altafi.calculator.util.calculator.button.function

import com.arash.altafi.calculator.util.calculator.button.FunctionButton

object NumSign : FunctionButton("±") {

    override val applier: (String) -> String = { n -> "-$n" }
}
