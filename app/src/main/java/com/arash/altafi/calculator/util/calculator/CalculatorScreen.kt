package com.arash.altafi.calculator.util.calculator

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.arash.altafi.calculator.R
import com.arash.altafi.calculator.util.calculator.NumberPadState.BUTTONS_LAYOUT_COLUMNS_COUNT
import com.arash.altafi.calculator.util.calculator.NumberPadState.BUTTONS_LAYOUT_ROW_COUNT
import com.arash.altafi.calculator.util.calculator.NumberPadState.color
import com.arash.altafi.calculator.util.calculator.NumberPadState.widthRatio
import com.arash.altafi.calculator.util.calculator.button.CalculatorButton
import com.arash.altafi.calculator.util.calculator.button.calculatorButtons
import com.arash.altafi.calculator.util.calculator.button.common.AllClear
import com.arash.altafi.calculator.util.calculator.button.common.Digit
import com.arash.altafi.calculator.util.calculator.button.function.Equals
import com.arash.altafi.calculator.util.calculator.button.function.NumSign
import com.arash.altafi.calculator.util.calculator.button.function.Percent
import com.arash.altafi.calculator.util.calculator.button.operator.Add
import com.arash.altafi.calculator.util.calculator.button.operator.Div
import com.arash.altafi.calculator.util.calculator.button.operator.Mul
import com.arash.altafi.calculator.util.calculator.button.operator.Sub
import com.arash.altafi.calculator.data.model.Calculation
import com.arash.altafi.calculator.ui.component.CorneredFlatIconButton
import com.arash.altafi.calculator.ui.component.NeuButton
import com.arash.altafi.calculator.ui.layout.Grid
import com.arash.altafi.calculator.ui.theme.SiliconeCalculatorTheme
import com.arash.altafi.calculator.util.formatNumbers

@Composable
fun CalculatorScreen(
    uiState: CalculatorUiState,
    onCalculatorButtonClick: (CalculatorButton) -> Unit,
    onHistoryNav: () -> Unit,
    onThemeToggle: () -> Unit,
) {
    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
    ) {
        CalculatorTopBar(
            onThemeToggle = onThemeToggle,
            onHistoryNav = onHistoryNav,
        )
        CalculatorContent(
            onCalculatorButtonClick = onCalculatorButtonClick,
            mathExpression = uiState.calculation.expression,
            evaluationResult = uiState.calculation.result
        )
    }
}

@Composable
fun CalculatorTopBar(
    onThemeToggle: () -> Unit,
    onHistoryNav: () -> Unit,
) {
    Row(
        modifier = Modifier.layoutId("top_bar"),
        horizontalArrangement = Arrangement.spacedBy(10.dp,
            alignment = Alignment.Start)
    ) {
        val baseModifier = Modifier.aspectRatio(1.25f)

        CorneredFlatIconButton(
            modifier = baseModifier,
            onClick = onThemeToggle,
            icon = if (MaterialTheme.colors.isLight) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
            contentDescription = stringResource(R.string.theme_changer)
        )
        CorneredFlatIconButton(
            modifier = baseModifier,
            onClick = onHistoryNav,
            icon = Icons.Outlined.History,
            contentDescription = stringResource(R.string.calculations_history)
        )
    }
}

@Composable
fun CalculatorContent(
    onCalculatorButtonClick: (CalculatorButton) -> Unit,
    mathExpression: String,
    evaluationResult: String,
) {
    Display(
        mathExpression = mathExpression,
        evaluationResult = evaluationResult,
    )
    KeyLayout(
        onButtonClick = onCalculatorButtonClick
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun Display(
    mathExpression: String,
    evaluationResult: String,
) {
    Column(
        modifier = Modifier
            .layoutId("display")
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            modifier = Modifier
                .horizontalScroll(
                    state = rememberScrollState(),
                    reverseScrolling = true
                )
                .padding(horizontal = 20.dp)
                .alpha(ContentAlpha.medium)
                .testTag("calculator:expression"),
            text = mathExpression.formatNumbers(),
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Light,
            ),
            textAlign = TextAlign.Center,
        )

        val customTextSelectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colors.secondary,
            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.4f)
        )
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            SelectionContainer {
                Text(
                    modifier = Modifier
                        .horizontalScroll(
                            state = rememberScrollState(),
                            reverseScrolling = true
                        )
                        .padding(horizontal = 20.dp)
                        .testTag("calculator:result"),
                    text = evaluationResult.formatNumbers(),
                    style = MaterialTheme.typography.h3.copy(
                        fontWeight = FontWeight.Normal,
                        platformStyle = PlatformTextStyle(false),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    ),
                )
            }
        }
    }
}

@Composable
private fun KeyLayout(
    calculatorState: NumberPadState = rememberNumberPadState(),
    onButtonClick: (CalculatorButton) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .aspectRatio(BUTTONS_LAYOUT_COLUMNS_COUNT / BUTTONS_LAYOUT_ROW_COUNT.toFloat())
            .layoutId("key_layout")
    ) {
        Grid(
            columns = BUTTONS_LAYOUT_COLUMNS_COUNT,
            modifier = Modifier.fillMaxSize(),
        ) {
            val buttonSizeWithoutSpacing = maxWidth / BUTTONS_LAYOUT_COLUMNS_COUNT
            val spaceBetweenButtons =
                NumberPadState.calculateButtonSpacing(buttonSizeWithoutSpacing)

            for (button in calculatorButtons) {
                val hapticFeedback = LocalHapticFeedback.current
                NeuButton(
                    modifier = Modifier
                        .span(
                            columns = button.widthRatio,
                            rows = 1,
                        )
                        .padding(spaceBetweenButtons)
                        .testTag("calculator:${button.symbol}"),
                    lightColor = button.color,
                    onClick = {
                        onButtonClick(button)
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                ) {
                    Text(
                        text = button.symbol,
                        color = contentColorFor(backgroundColor = button.color),
                        style = MaterialTheme.typography.button.copy(
                            fontSize = with(LocalDensity.current) {
                                (maxHeight * 0.33f).toSp()
                            },
                            fontWeight = FontWeight.Light,
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

val constraintSet = ConstraintSet {
    val topBarRef = createRefFor("top_bar")
    val display = createRefFor("display")
    val keyLayout = createRefFor("key_layout")

    val topGuideline1 = createGuidelineFromTop(0.01f)
    val topGuideline9 = createGuidelineFromTop(0.09f)
    val topGuideline40 = createGuidelineFromTop(0.4f)
    val bottomGuideline3 = createGuidelineFromBottom(0.02f)

    constrain(topBarRef) {
        linkTo(
            top = topGuideline1,
            start = parent.start,
            bottom = topGuideline9,
            end = parent.end,
            startMargin = 20.dp
        )

        width = Dimension.fillToConstraints
        height = Dimension.fillToConstraints
    }
    constrain(display) {
        bottom.linkTo(keyLayout.top, margin = 16.dp)
        end.linkTo(parent.end)

        width = Dimension.wrapContent
        height = Dimension.fillToConstraints
    }
    constrain(keyLayout) {
        linkTo(
            top = topGuideline40,
            start = parent.start,
            bottom = bottomGuideline3,
            end = parent.end,
            verticalBias = 1f
        )

        height = Dimension.preferredWrapContent
        width = Dimension.preferredWrapContent
    }
}

@Stable
object NumberPadState {

    const val BUTTONS_LAYOUT_COLUMNS_COUNT = 4
    const val BUTTONS_LAYOUT_ROW_COUNT = 5

    fun calculateButtonSpacing(buttonWidth: Dp): Dp {
        return buttonWidth * 0.04f
    }

    val CalculatorButton.widthRatio: Int
        get() = if (this == Digit('0')) 2 else 1

    val CalculatorButton.color
        @Composable
        get() = when (this) {
            in listOf(Div, Mul, Sub, Add, Equals) -> MaterialTheme.colors.secondary
            in listOf(AllClear, NumSign, Percent) -> MaterialTheme.colors.primaryVariant
            else -> MaterialTheme.colors.primary
        }
}

@Composable
fun rememberNumberPadState() = remember { NumberPadState }

@ExperimentalMaterialApi
@Preview(
    name = "Light theme",
    showBackground = true,
)
@Preview(
    name = "Dark theme",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
)
@Composable
fun CalculatorScreenPreview() {
    SiliconeCalculatorTheme {
        Surface(color = MaterialTheme.colors.background) {
            CalculatorScreen(
                uiState = CalculatorUiState(
                    Calculation(
                        expression = "4,900 + 15,910",
                        result = "20,810"
                    )
                ),
                onCalculatorButtonClick = { },
                onHistoryNav = { },
                onThemeToggle = { }
            )
        }
    }
}
