package com.arash.altafi.calculator.util

val String.encodeReservedChars
    get() = replace(
        """[!*'();:@&=+$,/?%#\[\]]""".toRegex()
    ) { "%${it.value.single().code.toString(16)}" }
