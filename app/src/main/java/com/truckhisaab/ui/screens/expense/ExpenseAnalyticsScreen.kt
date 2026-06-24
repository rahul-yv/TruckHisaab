package com.truckhisaab.ui.screens.expense

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun ExpenseAnalyticsScreen(onBack: () -> Unit, viewModel: ExpenseViewModel = hiltViewModel()) {
    val breakdown by viewModel.categoryBreakdown.collectAsState()
    val total = breakdown.sumOf { it.total }

    Scaffold(topBar = { THTopBar("Expense Analytics", onBack = onBack) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Category Breakdown", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.height(16.dp))
                        if (breakdown.isNotEmpty()) {
                            Box(Modifier.size(180.dp), contentAlignment = Alignment.Center) {
                                var startAngle = -90f
                                Canvas(Modifier.fillMaxSize()) {
                                    breakdown.forEach { cat ->
                                        val sweep = (cat.percentage / 100 * 360).toFloat()
                                        drawArc(Color(cat.category.colorHex), startAngle, sweep, false, style = Stroke(width = 32f), size = Size(size.width * 0.8f, size.height * 0.8f), topLeft = Offset(size.width * 0.1f, size.height * 0.1f))
                                        startAngle += sweep
                                    }
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Total", fontSize = 12.sp, color = TextSecondary)
                                    Text(formatINR(total), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }
            }
            items(breakdown.sortedByDescending { it.total }) { cat ->
                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        StatusDot(Color(cat.category.colorHex), 12)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(cat.category.hindiLabel, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            LinearProgressIndicator(progress = { (cat.percentage / 100).toFloat() }, Modifier.fillMaxWidth().height(6.dp), color = Color(cat.category.colorHex), trackColor = Color.LightGray.copy(0.3f))
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(horizontalAlignment = Alignment.End) {
                            Text(formatINR(cat.total), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("${cat.percentage.toInt()}%", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}
