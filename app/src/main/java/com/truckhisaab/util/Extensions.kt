package com.truckhisaab.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val inrFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
    maximumFractionDigits = 0
}

private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("en", "IN"))
private val timeFormat = SimpleDateFormat("hh:mm a", Locale("en", "IN"))

fun Double.formatINR(): String = inrFormat.format(this)

fun Long.formatDate(): String = dateFormat.format(Date(this))

fun Long.formatTime(): String = timeFormat.format(Date(this))

fun Long.formatTimeAgo(): String {
    val diff = System.currentTimeMillis() - this
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return when {
        days > 30 -> "${days / 30} mahine pehle"
        days > 0 -> "$days din pehle"
        hours > 0 -> "$hours ghante pehle"
        minutes > 0 -> "$minutes min pehle"
        else -> "Abhi"
    }
}

fun Long.daysUntil(): Int =
    ((this - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()

fun Long.startOfDay(): Long {
    val cal = java.util.Calendar.getInstance()
    cal.timeInMillis = this
    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
    cal.set(java.util.Calendar.MINUTE, 0)
    cal.set(java.util.Calendar.SECOND, 0)
    cal.set(java.util.Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun startOfToday(): Long = System.currentTimeMillis().startOfDay()

fun startOfWeek(): Long {
    val cal = java.util.Calendar.getInstance()
    cal.set(java.util.Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
    cal.set(java.util.Calendar.MINUTE, 0)
    cal.set(java.util.Calendar.SECOND, 0)
    cal.set(java.util.Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun startOfMonth(): Long {
    val cal = java.util.Calendar.getInstance()
    cal.set(java.util.Calendar.DAY_OF_MONTH, 1)
    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
    cal.set(java.util.Calendar.MINUTE, 0)
    cal.set(java.util.Calendar.SECOND, 0)
    cal.set(java.util.Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun startOfYear(): Long {
    val cal = java.util.Calendar.getInstance()
    cal.set(java.util.Calendar.DAY_OF_YEAR, 1)
    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
    cal.set(java.util.Calendar.MINUTE, 0)
    cal.set(java.util.Calendar.SECOND, 0)
    cal.set(java.util.Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}
