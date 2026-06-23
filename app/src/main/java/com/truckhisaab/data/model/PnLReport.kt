package com.truckhisaab.data.model

data class PnLReport(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val netProfit: Double = 0.0,
    val periodLabel: String = "",
    val changePercent: Double = 0.0,
    val weeklyData: List<WeeklyPnL> = emptyList(),
    val categoryBreakdown: List<CategoryBreakdown> = emptyList(),
    val partyBreakdown: List<PartyBreakdown> = emptyList()
)

data class WeeklyPnL(
    val label: String,
    val income: Double,
    val expense: Double
)

data class CategoryBreakdown(
    val category: String,
    val amount: Double,
    val percent: Double
)

data class PartyBreakdown(
    val partyName: String,
    val totalTrips: Int,
    val totalIncome: Double,
    val pending: Double
)
