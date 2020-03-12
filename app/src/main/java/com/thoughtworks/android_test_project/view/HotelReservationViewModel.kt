package com.thoughtworks.android_test_project.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thoughtworks.android_test_project.R
import com.thoughtworks.android_test_project.domain.core.ReservationsUseCase
import com.thoughtworks.android_test_project.domain.models.Reservation
import com.thoughtworks.android_test_project.extensions.endOfDay
import com.thoughtworks.android_test_project.extensions.startOfDay
import com.thoughtworks.android_test_project.extensions.toSimpleDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class HotelReservationViewModel(
    private val context: Application,
    private val reservationsUseCase: ReservationsUseCase
) :
    AndroidViewModel(context) {

    private val today = DateTime().startOfDay()
    var endDate: DateTime = today
    var startDate: DateTime = today
    var isRewardClient: Boolean = false
    val reservationData: MutableLiveData<Reservation?> = MutableLiveData()
    val messageData: MutableLiveData<String?> = MutableLiveData()
    val enableFindButton: MutableLiveData<Boolean> = MutableLiveData(false)

    private val backgroundScope by lazy {
        CoroutineScope(viewModelScope.coroutineContext + Dispatchers.IO)
    }

    fun findBestHotels() {
        backgroundScope.launch {
            messageData.postValue(context.getString(R.string.loading_message))
            bestReservationCallback(reservationsUseCase(startDate, endDate, isRewardClient))
        }
    }

    private fun bestReservationCallback(reservation: Reservation) {
        reservationData.postValue(reservation)
        messageData.postValue(null)
    }

    fun validateStartDate(day: Int, month: Int, year: Int) {
        this.startDate = parseDateTime(day, month, year).startOfDay()
        validateDates()
    }

    fun validateEndDate(day: Int, month: Int, year: Int) {
        this.endDate = parseDateTime(day, month, year).endOfDay()
        validateDates()
    }

    private fun parseDateTime(day: Int, month: Int, year: Int): DateTime {
        return "$day/${month + 1}/$year".toSimpleDateTime()
    }

    private fun validateDates() {
        val startValidDate = today.plusDays(1)

        return when {
            startDate.isBefore(startValidDate) || endDate.isBefore(startValidDate) -> {
                messageData.postValue(
                    context.getString(R.string.start_date_error_message)
                )
                enableFindButton.postValue(false)
            }
            endDate.isBefore(startDate) -> {
                messageData.postValue(
                    context.getString(R.string.wrong_range_error_message)
                )
                enableFindButton.postValue(false)
            }
            else -> {
                messageData.postValue(null)
                enableFindButton.postValue(true)
            }
        }
    }

    fun clear() {
        messageData.value = null
        reservationData.value = null
        backgroundScope.coroutineContext.cancelChildren()
    }

}
