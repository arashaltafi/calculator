package com.arash.altafi.calculator.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arash.altafi.calculator.data.model.Calculation
import com.arash.altafi.calculator.data.model.History
import com.arash.altafi.calculator.data.model.previewHistoryItems
import com.arash.altafi.calculator.util.formatNumbers

@Composable
fun HistoryItem(
    calculations: List<Calculation>,
    onCalculationClick: (Calculation) -> Unit,
    date: String,
) {
    for (calculation in calculations) {
        key(calculation.hashCode()) {
            CalculationItem(
                calculation = calculation,
                onCalculationClick = onCalculationClick
            )
        }
    }

    Text(
        modifier = Modifier.padding(
            vertical = 12.dp,
            horizontal = 28.dp
        ),
        text = date,
        style = MaterialTheme.typography.subtitle1.copy(
            fontWeight = FontWeight.Medium,
        )
    )
}

@Composable
fun CalculationItem(
    calculation: Calculation,
    onCalculationClick: (Calculation) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onCalculationClick(calculation) })
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .horizontalScroll(rememberScrollState(), reverseScrolling = true)
                .padding(horizontal = 16.dp),
            text = calculation.expression.formatNumbers(),
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Light
            )
        )
        Text(
            modifier = Modifier
                .horizontalScroll(rememberScrollState(), reverseScrolling = true)
                .padding(horizontal = 16.dp),
            text = calculation.result.formatNumbers(),
            style = MaterialTheme.typography.h5,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryItemPreview() {
    MaterialTheme {
        HistoryItem(
            calculations = previewHistoryItems.map(History::calculation).take(3),
            onCalculationClick = { },
            date = "Today"
        )
    }
}
