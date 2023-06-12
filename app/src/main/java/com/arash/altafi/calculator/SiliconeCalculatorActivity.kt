package com.arash.altafi.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.arash.altafi.calculator.navigation.SiliconeCalculatorNavHost
import com.arash.altafi.calculator.ui.animation.CircularReveal
import com.arash.altafi.calculator.ui.theme.SiliconeCalculatorTheme

@ExperimentalComposeUiApi
@AndroidEntryPoint
class SiliconeCalculatorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isSystemDark = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(isSystemDark) }
            val onThemeToggle = { darkTheme = !darkTheme }

            val navController = rememberNavController()
            CircularReveal(
                targetState = darkTheme,
                animationSpec = tween(500)
            ) { isDark ->
                SiliconeCalculatorTheme(darkTheme = isDark) {
                    Surface(
                        modifier = Modifier.semantics { testTagsAsResourceId = true },
                        color = MaterialTheme.colors.background
                    ) {
                        SiliconeCalculatorNavHost(
                            navController = navController,
                            onThemeToggle = onThemeToggle
                        )
                    }
                }
            }
        }
    }
}
