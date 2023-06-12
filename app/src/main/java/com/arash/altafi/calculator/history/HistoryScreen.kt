@file:OptIn(ExperimentalMaterialApi::class)

package com.arash.altafi.calculator.history

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.arash.altafi.calculator.R
import com.arash.altafi.calculator.data.model.Calculation
import com.arash.altafi.calculator.data.model.History
import com.arash.altafi.calculator.data.model.previewHistoryItems
import com.arash.altafi.calculator.ui.component.CorneredFlatButton
import com.arash.altafi.calculator.ui.component.CorneredFlatIconButton
import com.arash.altafi.calculator.ui.component.OutlinedCorneredFlatButton
import com.arash.altafi.calculator.ui.theme.SiliconeCalculatorTheme
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onBackPress: () -> Unit,
    onHistoryClear: () -> Unit,
    onCalculationClick: (Calculation) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val clearHistoryBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    BackHandler {
        if (clearHistoryBottomSheetState.isVisible) {
            coroutineScope.launch { clearHistoryBottomSheetState.hide() }
        } else {
            onBackPress()
        }
    }
    ModalBottomSheetLayout(
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetState = clearHistoryBottomSheetState,
        sheetShape = MaterialTheme.shapes.large.copy(
            bottomEnd = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        ),
        scrimColor = Color.Black.copy(0.32f),
        sheetContent = {
            ClearHistoryBottomSheetContent(
                onCancelClick = {
                    coroutineScope.launch { clearHistoryBottomSheetState.hide() }
                },
                onClearClick = {
                    coroutineScope.launch {
                        onHistoryClear()
                        onBackPress()
                    }
                }
            )
        }
    ) {
        ConstraintLayout(
            constraintSet = constraintSet,
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
        ) {
            HistoryTopBar(
                onBackPress = onBackPress,
                onHistoryClear = { coroutineScope.launch { clearHistoryBottomSheetState.show() } }
            )
            HistoryItemsList(
                historyItems = uiState.historyItems,
                onCalculationClick = onCalculationClick
            )
        }
    }
}

@Composable
fun ColumnScope.ClearHistoryBottomSheetContent(
    onCancelClick: () -> Unit,
    onClearClick: () -> Unit,
) {
    Spacer(modifier = Modifier.height(28.dp))

    Column(
        modifier = Modifier.align(CenterHorizontally),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.clear),
            style = MaterialTheme.typography.h6,
        )
        Text(
            text = stringResource(R.string.clear_history_now),
            style = MaterialTheme.typography.body1
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentWidth()
            .height(48.dp)
            .align(CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedCorneredFlatButton(
            modifier = Modifier.aspectRatio(2.5f),
            onClick = onCancelClick
        ) {
            Text(text = stringResource(R.string.cancel))
        }

        CorneredFlatButton(
            modifier = Modifier
                .aspectRatio(2.5f)
                .testTag("history:clear"),
            onClick = onClearClick
        ) {
            Text(text = stringResource(R.string.clear))
        }
    }

    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeContent))
}

@Composable
fun HistoryTopBar(
    onBackPress: () -> Unit,
    onHistoryClear: () -> Unit,
) {
    Row(
        modifier = Modifier
            .layoutId("top_bar"),
        horizontalArrangement = Arrangement.spacedBy(10.dp,
            alignment = Alignment.Start)
    ) {
        val baseModifier = Modifier.aspectRatio(1.25f)
        CorneredFlatIconButton(
            modifier = baseModifier,
            onClick = onBackPress,
            icon = Icons.Outlined.ArrowBack,
            contentDescription = stringResource(R.string.back_to_calculator)
        )
        CorneredFlatIconButton(
            modifier = baseModifier,
            onClick = onHistoryClear,
            icon = Icons.Outlined.ClearAll,
            contentDescription = stringResource(R.string.clear_history)
        )
    }
}

@Composable
fun HistoryItemsList(
    historyItems: List<History>,
    onCalculationClick: (Calculation) -> Unit,
) {
    val historyItemsByDate = remember(historyItems) {
        historyItems.groupBy(
            keySelector = History::date,
            valueTransform = History::calculation
        )
    }

    Box(
        modifier = Modifier.layoutId("history_list"),
    ) {
        if (historyItemsByDate.isEmpty()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.nothing_to_show)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .testTag("history:items"),
                state = rememberLazyListState(
                    initialFirstVisibleItemIndex = historyItemsByDate.size * 2
                ),
            ) {
                historyItemsByDate.onEachIndexed { index, (date, calculations) ->
                    item {
                        HistoryItem(
                            calculations = calculations,
                            onCalculationClick = onCalculationClick,
                            date = date
                        )
                    }
                    item {
                        val isLastItem = remember { index == historyItemsByDate.size - 1 }
                        if (!isLastItem) {
                            Divider(
                                modifier = Modifier.padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

val constraintSet = ConstraintSet {
    val topBarRef = createRefFor("top_bar")
    val historyList = createRefFor("history_list")

    val topGuideline1 = createGuidelineFromTop(0.01f)
    val topGuideline9 = createGuidelineFromTop(0.09f)
    val topGuideline10 = createGuidelineFromTop(0.11f)

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
    constrain(historyList) {
        linkTo(
            top = topGuideline10,
            start = parent.start,
            bottom = parent.bottom,
            end = parent.end,
        )

        width = Dimension.fillToConstraints
        height = Dimension.fillToConstraints
    }
}

@Preview(
    name = "Light theme with items",
    showBackground = true,
)
@Preview(
    name = "Dark theme with items",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun HistoryScreenWithItemsPreview() {
    SiliconeCalculatorTheme {
        Surface(color = MaterialTheme.colors.background) {
            val calculations = previewHistoryItems.toMutableList()

            HistoryScreen(
                uiState = HistoryUiState(calculations),
                onBackPress = { },
                onHistoryClear = { calculations.clear() },
                onCalculationClick = { }
            )
        }
    }
}
