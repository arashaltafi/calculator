package com.arash.altafi.calculator.util.calculator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.arash.altafi.calculator.util.calculator.button.CalculatorButton
import com.arash.altafi.calculator.util.calculator.button.common.AllClear
import com.arash.altafi.calculator.data.model.Calculation
import com.arash.altafi.calculator.data.repository.HistoryRepository
import com.arash.altafi.calculator.navigation.SiliconeCalculatorDestinationsArg.EXPRESSION_ARG
import com.arash.altafi.calculator.navigation.SiliconeCalculatorDestinationsArg.RESULT_ARG
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val historyRepository: HistoryRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _calculation = MutableStateFlow(Calculation())
    private val currentCalculation get() = _calculation.value

    private var previousExpression = currentCalculation.expression

    val uiState = _calculation
        .map(::CalculatorUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CalculatorUiState()
        )

    init {
        updateCalculatorDisplay(
            expression = savedStateHandle[EXPRESSION_ARG],
            result = savedStateHandle[RESULT_ARG]
        )
    }

    private fun updateCalculatorDisplay(expression: String?, result: String?) {
        if (expression == null || result == null) return

        _calculation.update {
            it.copy(expression = expression, result = result)
        }

        previousExpression = expression
    }

    fun performCalculatorButton(calculatorButton: CalculatorButton) {
        if (currentCalculation.resultIsInvalid && calculatorButton != AllClear) return

        viewModelScope.launch {
            _calculation.update {
                withContext(defaultDispatcher) {
                    with(calculatorButton) { it.perform() }
                }
            }
        }
    }

    fun saveCalculationInHistory() = with(currentCalculation) {
        if (isNotEvaluated || resultIsInvalid) return

        viewModelScope.launch {
            historyRepository.saveCalculation(calculation = this@with)
        }

        previousExpression = expression
    }

    private val Calculation.isNotEvaluated
        get() = expression.endsWith(lastOperator) || expression.isEmpty() || expression == previousExpression
}
