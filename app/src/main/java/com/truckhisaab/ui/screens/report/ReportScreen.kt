package com.truckhisaab.ui.screens.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.components.formatINR
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.InfoBlue
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.SuccessGreenBg
import com.truckhisaab.ui.theme.TextHint
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed
import com.truckhisaab.ui.theme.WarningOrange
import com.truckhisaab.ui.theme.WhatsAppGreen

@Composable
fun ReportScreen(onBack: () -> Unit, viewModel: ReportViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            THTopBar(title = "P&L Hisaab", onBack = onBack, actions = {
                IconButton(onClick = { }) { Icon(Icons.Default.Share, "Share", tint = Color.White) }
            })
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = if (state.monthProfit >= 0) SuccessGreen else DangerRed)) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Is Mahine ka P&L", color = Color.White.copy(0.8f), fontSize = 14.sp)
                    Text(formatINR(state.monthProfit), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text(if (state.monthProfit >= 0) "PROFIT" else "LOSS", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Income", color = Color.White.copy(0.7f), fontSize = 12.sp); Text(formatINR(state.monthIncome), color = Color.White, fontWeight = FontWeight.Bold) }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Expense", color = Color.White.copy(0.7f), fontSize = 12.sp); Text(formatINR(state.monthExpense), color = Color.White, fontWeight = FontWeight.Bold) }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PeriodCard("Hafta", state.weekIncome, state.weekExpense, InfoBlue, Modifier.weight(1f))
                PeriodCard("Total", state.totalIncome, state.totalExpense, TruckRed, Modifier.weight(1f))
            }

            if (state.categoryBreakdown.isNotEmpty()) {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Kharcha Breakdown", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        val max = state.categoryBreakdown.values.maxOrNull() ?: 1.0
                        state.categoryBreakdown.entries.sortedByDescending { it.value }.forEach { (cat, amount) ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(10.dp).clip(CircleShape).background(Color(cat.colorHex)))
                                Spacer(Modifier.width(8.dp))
                                Text(cat.label, Modifier.width(70.dp), fontSize = 12.sp)
                                Box(Modifier.weight(1f).height(14.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFFF5F5F5))) {
                                    Box(Modifier.fillMaxWidth((amount / max).toFloat()).height(14.dp).clip(RoundedCornerShape(4.dp)).background(Color(cat.colorHex)))
                                }
                                Spacer(Modifier.width(8.dp))
                                Text(formatINR(amount), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            if (state.truckBreakdown.isNotEmpty()) {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Truck-wise P&L", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        state.truckBreakdown.forEach { (truck, pair) ->
                            val (inc, exp) = pair
                            val p = inc - exp
                            Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text(truck, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    Text("Income: ${formatINR(inc)} | Expense: ${formatINR(exp)}", fontSize = 11.sp, color = TextSecondary)
                                }
                                Text(formatINR(p), fontWeight = FontWeight.Bold, color = if (p >= 0) SuccessGreen else DangerRed)
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }

            if (state.partyBreakdown.isNotEmpty()) {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Party-wise Income", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        state.partyBreakdown.entries.sortedByDescending { it.value }.forEach { (party, amount) ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(party, fontSize = 14.sp)
                                Text(formatINR(amount), fontWeight = FontWeight.SemiBold, color = SuccessGreen)
                            }
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                com.truckhisaab.ui.components.THPrimaryButton("PDF Download", { }, Modifier.weight(1f), color = TruckRed)
                com.truckhisaab.ui.components.THPrimaryButton("WhatsApp", { }, Modifier.weight(1f), color = WhatsAppGreen)
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun PeriodCard(label: String, income: Double, expense: Double, color: Color, modifier: Modifier) {
    val profit = income - expense
    Card(modifier, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = color.copy(0.1f))) {
        Column(Modifier.padding(14.dp)) {
            Text(label, fontSize = 12.sp, color = TextSecondary)
            Text(formatINR(profit), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = if (profit >= 0) SuccessGreen else DangerRed)
            Spacer(Modifier.height(4.dp))
            Text("In: ${formatINR(income)}", fontSize = 11.sp, color = TextHint)
            Text("Out: ${formatINR(expense)}", fontSize = 11.sp, color = TextHint)
        }
    }
}
