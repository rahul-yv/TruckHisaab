package com.truckhisaab.ui.screens.expense

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.components.formatINR
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.TextHint
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed

@Composable
fun ExpenseAnalyticsScreen(onBack: () -> Unit, viewModel: ExpenseViewModel = viewModel()) {
    val breakdown = viewModel.getCategoryBreakdown()
    val total = breakdown.values.sum()
    val monthExpense = viewModel.getMonthExpense()
    val weekExpense = viewModel.getWeekExpense()

    Scaffold(topBar = { THTopBar(title = "Kharcha Analytics", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard("Is Hafte", formatINR(weekExpense), DangerRed, Modifier.weight(1f))
                SummaryCard("Is Mahine", formatINR(monthExpense), TruckRed, Modifier.weight(1f))
            }

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Category Breakdown", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    Box(Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                        val colors = breakdown.keys.map { Color(it.colorHex) }
                        val values = breakdown.values.toList()
                        Canvas(Modifier.size(200.dp)) {
                            var startAngle = -90f
                            values.forEachIndexed { i, v ->
                                val sweep = if (total > 0) (v / total * 360).toFloat() else 0f
                                drawArc(colors[i], startAngle, sweep, false, topLeft = Offset(20f, 20f), size = Size(size.width - 40, size.height - 40), style = Stroke(40f, cap = StrokeCap.Round))
                                startAngle += sweep
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total", fontSize = 12.sp, color = TextHint)
                            Text(formatINR(total), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    breakdown.entries.sortedByDescending { it.value }.forEach { (cat, amount) ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(12.dp).clip(CircleShape).background(Color(cat.colorHex)))
                            Spacer(Modifier.width(8.dp))
                            Text(cat.label, Modifier.weight(1f), fontSize = 14.sp)
                            Text(formatINR(amount), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Spacer(Modifier.width(8.dp))
                            val pct = if (total > 0) (amount / total * 100).toInt() else 0
                            Text("$pct%", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
            }

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Bar Chart - Category", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    val max = breakdown.values.maxOrNull() ?: 1.0
                    breakdown.entries.sortedByDescending { it.value }.forEach { (cat, amount) ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(cat.label, Modifier.width(80.dp), fontSize = 12.sp, color = TextSecondary)
                            Box(Modifier.weight(1f).height(16.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFFF5F5F5))) {
                                Box(Modifier.fillMaxWidth((amount / max).toFloat()).height(16.dp).clip(RoundedCornerShape(4.dp)).background(Color(cat.colorHex)))
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(formatINR(amount), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(0.1f))) {
                Column(Modifier.padding(16.dp)) {
                    Text("💡 Insights", fontWeight = FontWeight.Bold, color = SuccessGreen)
                    Spacer(Modifier.height(8.dp))
                    val topCat = breakdown.maxByOrNull { it.value }
                    if (topCat != null) {
                        Text("• Sabse zyada kharcha: ${topCat.key.label} (${formatINR(topCat.value)})", fontSize = 13.sp)
                    }
                    if (breakdown.size > 1) {
                        val minCat = breakdown.minByOrNull { it.value }
                        if (minCat != null) {
                            Text("• Sabse kam kharcha: ${minCat.key.label} (${formatINR(minCat.value)})", fontSize = 13.sp)
                        }
                    }
                    Text("• Daily average: ${formatINR(monthExpense / 30)}", fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun SummaryCard(label: String, value: String, color: Color, modifier: Modifier) {
    Card(modifier, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = color.copy(0.1f))) {
        Column(Modifier.padding(14.dp)) {
            Text(label, fontSize = 12.sp, color = TextSecondary)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = color)
        }
    }
}
