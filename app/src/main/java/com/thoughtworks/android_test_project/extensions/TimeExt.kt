package com.thoughtworks.android_test_project.extensions

import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

private const val SIMPLE_DATE_PATTERN = "dd/MM/yyyy"

private const val SATURDAY_JODA_DAY_OFF_WEEK = 6
private const val SUNDAY_JODA_DAY_OFF_WEEK = 7

private const val END_HOURS_DAY = 23
private const val END_MIN_SEC_DAY = 59
private const val END_MILLISECOND_DAY = 999

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

fun DateTime.startOfDay(): DateTime = withTime(LocalTime.MIDNIGHT)

fun DateTime.endOfDay(): DateTime {
    return withHourOfDay(END_HOURS_DAY)
        .withMinuteOfHour(END_MIN_SEC_DAY)
        .withSecondOfMinute(END_MIN_SEC_DAY)
        .withMillisOfSecond(END_MILLISECOND_DAY)
}

fun DateTime.toSimpleDatePattern(): String {
    val formatter = DateTimeFormat.forPattern(SIMPLE_DATE_PATTERN)
    return this.toString(formatter)
}