package com.thoughtworks.android_test_project.domain.models

import org.joda.time.DateTime

data class Reservation(
    val hotelName: String,
    val stars: Int,
    val startDate: DateTime,
    val endDate: DateTime,
    val hasReward: Boolean,
    val totalPrice: Float
)