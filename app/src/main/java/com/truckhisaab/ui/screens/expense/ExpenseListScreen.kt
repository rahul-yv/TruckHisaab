package com.truckhisaab.ui.screens.expense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.domain.model.ExpenseCategory
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun ExpenseListScreen(onBack: () -> Unit, onAddExpense: () -> Unit, onAnalytics: () -> Unit, viewModel: ExpenseViewModel = hiltViewModel()) {
    val expenses by viewModel.filteredExpenses.collectAsState()
    val filter by viewModel.filterCategory.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val total = expenses.sumOf { it.amount }

    Scaffold(
        topBar = { THTopBar("Expenses", onBack = onBack) { IconButton(onClick = onAnalytics) { Icon(Icons.Default.PieChart, "Analytics", tint = Color.White) } } },
        floatingActionButton = { QuickAddFab(onClick = onAddExpense) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Card(Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(containerColor = DangerRedBg)) {
                Column(Modifier.padding(12.dp)) {
                    Text("Total Kharcha", fontSize = 12.sp, color = TextSecondary)
                    Text(formatINR(total), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DangerRed)
                }
            }
            THSearchBar(query, { viewModel.updateSearch(it) }, "Expense search...", Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(8.dp))
            LazyRow(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip(selected = filter == null, onClick = { viewModel.updateFilter(null) }, label = { Text("Sab") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White)) }
                items(ExpenseCategory.entries.toList()) { cat ->
                    FilterChip(selected = filter == cat, onClick = { viewModel.updateFilter(cat) }, label = { Text(cat.hindiLabel) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(cat.colorHex), selectedLabelColor = Color.White))
                }
            }
            if (expenses.isEmpty()) {
                EmptyState(icon = Icons.Default.Receipt, title = "Koi kharcha nahi", actionLabel = "Kharcha Add", onAction = onAddExpense)
            } else {
                LazyColumn(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { Spacer(Modifier.height(8.dp)) }
                    items(expenses) { exp ->
                        THCard(Modifier.fillMaxWidth()) {
                            Row(Modifier.padding(12.dp)) {
                                StatusDot(Color(exp.category.colorHex), 10)
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(exp.category.hindiLabel, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text("${exp.truckNumber} • ${formatDate(exp.date)}", fontSize = 11.sp, color = TextSecondary)
                                    if (exp.note.isNotBlank()) Text(exp.note, fontSize = 11.sp, color = TextHint)
                                }
                                Text(formatINR(exp.amount), fontWeight = FontWeight.Bold, color = DangerRed)
                            }
                        }
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}
