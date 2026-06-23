package com.truckhisaab.ui.screens.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.data.model.Expense
import com.truckhisaab.data.model.ExpenseCategory
import com.truckhisaab.ui.components.EmptyState
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.components.formatINR
import com.truckhisaab.ui.components.formatTimeAgo
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.TextHint
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    onBack: () -> Unit,
    onAddExpense: () -> Unit,
    onAnalytics: () -> Unit,
    viewModel: ExpenseViewModel = viewModel()
) {
    val filterState by viewModel.filter.collectAsState()
    var showSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            THTopBar(title = "Kharcha", onBack = onBack, actions = {
                IconButton(onClick = { showSearch = !showSearch }) { Icon(Icons.Default.Search, "Search", tint = Color.White) }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExpense, containerColor = TruckRed, contentColor = Color.White) {
                Icon(Icons.Default.Add, "Naya Kharcha")
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Card(Modifier.fillMaxWidth().padding(16.dp, 8.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = DangerRed.copy(0.1f))) {
                Row(Modifier.padding(14.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column { Text("Aaj", fontSize = 12.sp, color = TextHint); Text(formatINR(viewModel.getTodayExpense()), fontWeight = FontWeight.Bold, color = DangerRed) }
                    Column { Text("Hafta", fontSize = 12.sp, color = TextHint); Text(formatINR(viewModel.getWeekExpense()), fontWeight = FontWeight.Bold) }
                    Column { Text("Mahina", fontSize = 12.sp, color = TextHint); Text(formatINR(viewModel.getMonthExpense()), fontWeight = FontWeight.Bold) }
                    Text("📊 Analytics", Modifier.clickable { onAnalytics() }.align(Alignment.CenterVertically), color = TruckRed, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            if (showSearch) {
                OutlinedTextField(value = filterState.searchQuery, onValueChange = viewModel::setSearch, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), placeholder = { Text("Search kharcha...") }, shape = RoundedCornerShape(12.dp), singleLine = true, leadingIcon = { Icon(Icons.Default.Search, null) })
            }

            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp).horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = filterState.category == null, onClick = { viewModel.setFilterCategory(null) }, label = { Text("Sab") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White))
                ExpenseCategory.entries.take(6).forEach { cat ->
                    FilterChip(selected = filterState.category == cat, onClick = { viewModel.setFilterCategory(cat) }, label = { Text(cat.label) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(cat.colorHex), selectedLabelColor = Color.White))
                }
            }

            Spacer(Modifier.height(8.dp))

            val filtered = viewModel.filteredExpenses()
            if (filtered.isEmpty()) {
                EmptyState(icon = Icons.Default.Receipt, title = "Koi kharcha nahi", actionLabel = "Naya Kharcha", onAction = onAddExpense)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(filtered) { expense -> ExpenseItem(expense) }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).clip(CircleShape).background(Color(expense.category.colorHex).copy(0.15f)), contentAlignment = Alignment.Center) {
                Text(expense.category.label.first().toString(), color = Color(expense.category.colorHex), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(expense.category.label, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                if (expense.note.isNotBlank()) Text(expense.note, fontSize = 12.sp, color = TextSecondary, maxLines = 1)
                Text(formatTimeAgo(expense.date), fontSize = 11.sp, color = TextHint)
            }
            Text(formatINR(expense.amount), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DangerRed)
        }
    }
}
