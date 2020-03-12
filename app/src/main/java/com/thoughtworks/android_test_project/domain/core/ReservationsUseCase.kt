package com.thoughtworks.android_test_project.domain.core

import androidx.annotation.VisibleForTesting
import com.thoughtworks.android_test_project.data.interfaces.RewardsVocation
import com.thoughtworks.android_test_project.data.models.Hotel
import com.thoughtworks.android_test_project.domain.models.Reservation
import com.thoughtworks.android_test_project.extensions.isWeekendDay
import org.joda.time.DateTime
import org.joda.time.Days
import java.lang.reflect.Modifier.PRIVATE

class ReservationsUseCase(repository: HotelsRepository) {

    private val hotelsList: List<Hotel> by lazy {
        repository.listHotels()
    }

    fun getBestReservation(
        startDate: DateTime,
        endDate: DateTime,
        isReward: Boolean
    ): Reservation {

        val reservationList =
            getReservationsOptions(startDate, endDate, isReward)
                .sortedBy { it.totalPrice }
                .toMutableList()

        val firstOption = reservationList.removeAt(0)

        val hasSamePrice = reservationList.any { it.totalPrice == firstOption.totalPrice }

        return if (!hasSamePrice) {
            firstOption
        } else {
            resolveDrawedOptions(reservationList, firstOption)
        }
    }

    private fun resolveDrawedOptions(
        options: List<Reservation>,
        firstOption: Reservation
    ): Reservation {
        val allDrawedOptions = options
            .filter { it.totalPrice == firstOption.totalPrice }
            .toMutableList()
            .plus(firstOption)
            .sortedByDescending { it.stars }

        return allDrawedOptions.first()
    }

    private fun getReservationsOptions(
        startDate: DateTime,
        endDate: DateTime,
        isReward: Boolean
    ): List<Reservation> =
        hotelsList.map { hotel ->
            Reservation(
                hotelName = hotel.name,
                stars = hotel.stars,
                startDate = startDate,
                endDate = endDate,
                hasReward = isReward,
                totalPrice = getReservationTotalPrice(
                    hotel,
                    getDateRange(startDate, endDate),
                    isReward
                )
            )
        }

    private fun getReservationTotalPrice(
        hotel: RewardsVocation,
        dates: List<DateTime>,
        isReward: Boolean
    ): Float {
        var totalPrice = 0f

        dates.forEach { day ->
            val sumPrice = if (day.isWeekendDay()) {
                hotel.getWeekendPrice(isReward)
            } else {
                hotel.getDayPrice(isReward)
            }

            totalPrice += sumPrice
        }

        return totalPrice
    }

    @VisibleForTesting(otherwise = PRIVATE)
    fun getDateRange(startDate: DateTime, endDate: DateTime): List<DateTime> {
        val dates: MutableList<DateTime> = mutableListOf()

        val days = Days.daysBetween(startDate, endDate).days

        for (i in 0..days) {
            dates.add(startDate.plusDays(i))
        }

        return dates
    }
}