package com.truckhisaab.domain.model

data class PnLReport(
    val period: String = "",
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val profit: Double = 0.0,
    val previousIncome: Double = 0.0,
    val previousExpense: Double = 0.0,
    val previousProfit: Double = 0.0,
    val categoryBreakdown: List<CategoryBreakdown> = emptyList(),
    val truckBreakdown: List<TruckPnL> = emptyList(),
    val partyBreakdown: List<PartyBreakdown> = emptyList(),
    val weeklyData: List<WeeklyPnL> = emptyList()
)

data class TruckPnL(
    val truckNumber: String,
    val income: Double,
    val expense: Double,
    val profit: Double,
    val trips: Int
)

data class PartyBreakdown(
    val partyName: String,
    val totalAmount: Double,
    val trips: Int
)

data class WeeklyPnL(
    val label: String,
    val income: Double,
    val expense: Double
)
