package com.thoughtworks.android_test_project.extensions

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

private const val SIMPLE_DATE_PATTERN = "dd/MM/yyyy"

fun String.toSimpleDateTime(): DateTime {
    return DateTime.parse(
        this,
        DateTimeFormat.forPattern(SIMPLE_DATE_PATTERN)
    )
}