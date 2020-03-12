package com.thoughtworks.android_test_project.extensions

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

private const val SIMPLE_DATE_PATTERN = "dd/MM/yyyy"

private const val SATURDAY_JODA_DAY_OFF_WEEK = 6
private const val SUNDAY_JODA_DAY_OFF_WEEK = 7

fun String.toSimpleDateTime(): DateTime {
    return DateTime.parse(
        this,
        DateTimeFormat.forPattern(SIMPLE_DATE_PATTERN)
    )
}

fun DateTime.isWeekendDay(): Boolean {
    return when (dayOfWeek) {
        SUNDAY_JODA_DAY_OFF_WEEK, SATURDAY_JODA_DAY_OFF_WEEK -> true
        else -> false
    }
}