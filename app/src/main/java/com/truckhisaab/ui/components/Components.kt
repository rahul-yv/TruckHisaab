package com.truckhisaab.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.ui.theme.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ==================== TOP BAR ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun THTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    containerColor: Color = TruckRed,
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

// ==================== BUTTONS ====================

@Composable
fun THPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = TruckRed,
    loading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled && !loading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, disabledContainerColor = color.copy(alpha = 0.4f))
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
        } else {
            Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun THSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        if (icon != null) {
            Icon(icon, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
        }
        Text(text, fontSize = 14.sp)
    }
}

@Composable
fun QuickAddFab(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = TruckRed,
        contentColor = Color.White,
        shape = CircleShape
    ) {
        Icon(Icons.Default.Add, "Quick Add", Modifier.size(28.dp))
    }
}

// ==================== CARDS ====================

@Composable
fun THCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) { content() }
}

@Composable
fun HeroCard(
    title: String,
    value: String,
    subtitle: String = "",
    valueColor: Color = TextPrimary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TruckRed)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(title, color = Color.White.copy(0.8f), fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            if (subtitle.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(subtitle, color = Color.White.copy(0.7f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    change: String = "",
    changePositive: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, fontSize = 11.sp, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            if (change.isNotEmpty()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    change,
                    fontSize = 11.sp,
                    color = if (changePositive) SuccessGreen else DangerRed,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ==================== STATUS INDICATORS ====================

@Composable
fun StatusDot(color: Color, size: Int = 8) {
    Box(modifier = Modifier.size(size.dp).clip(CircleShape).background(color))
}

@Composable
fun StatusBadge(label: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = color)
    }
}

@Composable
fun OfflineBanner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(WarningOrange)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.WifiOff, null, tint = Color.White, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text("Offline - Data sync hoga jab internet aaye", color = Color.White, fontSize = 12.sp)
    }
}

// ==================== FEEDBACK STATES ====================

@Composable
fun EmptyState(
    icon: ImageVector = Icons.Default.Inbox,
    title: String,
    subtitle: String = "",
    actionLabel: String = "",
    onAction: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, modifier = Modifier.size(72.dp), tint = TextHint)
        Spacer(Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, color = TextSecondary)
        if (subtitle.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, color = TextHint)
        }
        if (actionLabel.isNotEmpty()) {
            Spacer(Modifier.height(24.dp))
            THPrimaryButton(text = actionLabel, onClick = onAction)
        }
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = TruckRed)
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.ErrorOutline, null, modifier = Modifier.size(72.dp), tint = DangerRed)
        Spacer(Modifier.height(16.dp))
        Text(message, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, color = TextSecondary)
        if (onRetry != null) {
            Spacer(Modifier.height(16.dp))
            THPrimaryButton(text = "Phir se try karein", onClick = onRetry)
        }
    }
}

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    height: Dp = 16.dp
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(animation = tween(1200, easing = LinearEasing)),
        label = "shimmer"
    )
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(ShimmerBase, ShimmerHighlight, ShimmerBase),
                    start = Offset(translateAnim - 200, 0f),
                    end = Offset(translateAnim, 0f)
                )
            )
    )
}

// ==================== DIALOGS ====================

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String = "Haan",
    dismissText: String = "Nahi",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmColor: Color = DangerRed
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.ErrorOutline, null, tint = confirmColor) },
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = confirmColor)) {
                Text(confirmText)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(dismissText) } }
    )
}

// ==================== INPUTS ====================

@Composable
fun THTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onDone: () -> Unit = {},
    error: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = if (placeholder.isNotEmpty()) {{ Text(placeholder, color = TextHint) }} else null,
        leadingIcon = if (leadingIcon != null) {{ Icon(leadingIcon, null) }} else null,
        isError = error != null,
        supportingText = if (error != null) {{ Text(error, color = DangerRed) }} else null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        singleLine = singleLine,
        maxLines = maxLines,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun THNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    prefix: String = "₹",
    error: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newVal -> if (newVal.all { it.isDigit() || it == '.' }) onValueChange(newVal) },
        label = { Text(label) },
        prefix = { Text(prefix, fontWeight = FontWeight.Bold) },
        isError = error != null,
        supportingText = if (error != null) {{ Text(error, color = DangerRed) }} else null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun THSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search...",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(placeholder, color = TextHint) },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, "Clear")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    )
}

// ==================== FORMATTING UTILITIES ====================

fun formatINR(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    format.maximumFractionDigits = 0
    return format.format(amount)
}

fun formatDate(millis: Long, pattern: String = "dd MMM yyyy"): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(Date(millis))

fun formatTimeAgo(millis: Long): String {
    val diff = System.currentTimeMillis() - millis
    val minutes = diff / 60000
    val hours = diff / 3600000
    val days = diff / 86400000
    return when {
        minutes < 1 -> "Abhi"
        minutes < 60 -> "${minutes}m pehle"
        hours < 24 -> "${hours}h pehle"
        days < 7 -> "${days}d pehle"
        else -> formatDate(millis, "dd MMM")
    }
}

fun daysUntil(millis: Long): Long = (millis - System.currentTimeMillis()) / 86400000L

fun statusColor(daysRemaining: Long): Color = when {
    daysRemaining < 15 -> DangerRed
    daysRemaining < 30 -> WarningOrange
    else -> SuccessGreen
}
