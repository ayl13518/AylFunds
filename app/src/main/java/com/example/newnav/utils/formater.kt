package com.example.newnav.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


fun getCurrentDate(): String {

    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    return current.format(formatter)
}