package com.app.task_reminder.utilities

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val dateFormat = "MMM dd, yyyy"
const val timeFormat = "hh:mm a"
const val date1Format = "dd\nMMM"

fun dateTimeStringToLong(dateTimeString: String): Long {
    val dateFormat = SimpleDateFormat("$dateFormat $timeFormat", Locale.getDefault())

    return try {
        val date = dateFormat.parse(dateTimeString)
        date?.time ?: -1L
    } catch (e: Exception) {
        e.printStackTrace()
        -1L
    }
}

fun formatDate(calendar: Calendar): String {
    val dateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    return dateFormat.format(calendar.time)
}

fun formatTime(calendar: Calendar): String {
    val timeFormat = SimpleDateFormat(timeFormat, Locale.getDefault())
    return timeFormat.format(calendar.time)
}

fun longToDateString(dateTimeInMillis: Long): String {
    val dateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateTimeInMillis
    return dateFormat.format(calendar.time)
}

fun longToTimeString(dateTimeInMillis: Long): String {
    val dateFormat = SimpleDateFormat(timeFormat, Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateTimeInMillis
    return dateFormat.format(calendar.time)
}

fun longToDateTimeString(dateTimeInMillis: Long): String {
    val dateFormat = SimpleDateFormat("$dateFormat $timeFormat", Locale.getDefault())
    val date = Date(dateTimeInMillis)
    return dateFormat.format(date)
}

fun longToDate1String(dateTimeInMillis: Long): String {
    val dateFormat = SimpleDateFormat(date1Format, Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateTimeInMillis
    return dateFormat.format(calendar.time)
}
