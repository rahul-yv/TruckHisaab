package com.truckhisaab.ui.screens.report

import androidx.lifecycle.ViewModel
import com.truckhisaab.data.AppContainer
import com.truckhisaab.data.model.ExpenseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ReportState(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val weekIncome: Double = 0.0,
    val weekExpense: Double = 0.0,
    val monthIncome: Double = 0.0,
    val monthExpense: Double = 0.0,
    val categoryBreakdown: Map<ExpenseCategory, Double> = emptyMap(),
    val truckBreakdown: Map<String, Pair<Double, Double>> = emptyMap(),
    val partyBreakdown: Map<String, Double> = emptyMap()
) {
    val totalProfit get() = totalIncome - totalExpense
    val weekProfit get() = weekIncome - weekExpense
    val monthProfit get() = monthIncome - monthExpense
}

class ReportViewModel : ViewModel() {
    private val tripRepo = AppContainer.tripRepository
    private val expenseRepo = AppContainer.expenseRepository

    private val _state = MutableStateFlow(ReportState())
    val state: StateFlow<ReportState> = _state.asStateFlow()

    init { refresh() }

    fun refresh() {
        val trips = tripRepo.trips.value
        val expenses = expenseRepo.expenses.value

        val totalIncome = trips.sumOf { it.freightAmount }
        val totalExpense = expenses.sumOf { it.amount }

        val truckBreakdown = mutableMapOf<String, Pair<Double, Double>>()
        trips.groupBy { it.truckNumber }.forEach { (truck, tList) ->
            val inc = tList.sumOf { it.freightAmount }
            val exp = expenses.filter { it.truckId == truck }.sumOf { it.amount }
            truckBreakdown[truck] = inc to exp
        }

        val partyBreakdown = trips.groupBy { it.partyName }.mapValues { (_, tList) -> tList.sumOf { it.freightAmount } }

        _state.value = ReportState(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            weekIncome = tripRepo.getWeekIncome(),
            weekExpense = expenseRepo.getWeekExpense(),
            monthIncome = tripRepo.getMonthIncome(),
            monthExpense = expenseRepo.getMonthExpense(),
            categoryBreakdown = expenseRepo.getCategoryBreakdown(),
            truckBreakdown = truckBreakdown,
            partyBreakdown = partyBreakdown
        )
    }
}
