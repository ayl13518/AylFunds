package com.aylmer.aylfunds.utils

import java.text.SimpleDateFormat
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.DayOfWeek
import java.time.format.TextStyle

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    //val formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return formatter.format(Date(millis))
    //return millis.format(formatter)
}


fun getCurrentDate(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    return current.format(formatter)
}

fun convertDateForDB(dbDate: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dbDate.format(formatter)
}

fun getDayofWeek(dbDate: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val  dDay = formatter.parse(dbDate)
    val firstThreeLetters = DayOfWeek.from(dDay).getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

    return firstThreeLetters

}
