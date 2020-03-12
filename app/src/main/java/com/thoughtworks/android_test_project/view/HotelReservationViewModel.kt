package com.thoughtworks.android_test_project.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thoughtworks.android_test_project.R
import com.thoughtworks.android_test_project.domain.core.ReservationsUseCase
import com.thoughtworks.android_test_project.domain.models.Reservation
import kotlinx.coroutines.*
import org.joda.time.DateTime

class HotelReservationViewModel(
    private val context: Application,
    private val reservationsUseCase: ReservationsUseCase
) :
    AndroidViewModel(context) {

    val reservationData: MutableLiveData<Reservation?> = MutableLiveData()
    val messageData: MutableLiveData<String?> = MutableLiveData()

    private val backgroundScope by lazy {
        CoroutineScope(viewModelScope.coroutineContext + Dispatchers.IO)
    }

    fun findBestHotels(startDate: DateTime, endDate: DateTime, isRewardClient: Boolean) {
        validateDates(startDate, endDate)?.run {
            return@run messageData.postValue(this)
        }

        backgroundScope.launch {
            messageData.postValue(context.getString(R.string.loading_message))
            bestReservationCallback(reservationsUseCase(startDate, endDate, isRewardClient))
        }
    }

    private suspend fun bestReservationCallback(reservation: Reservation) {
        delay(context.resources.getInteger(android.R.integer.config_longAnimTime).toLong())
        reservationData.postValue(reservation)
        messageData.postValue(null)
    }

    private fun validateDates(startDate: DateTime, endDate: DateTime): String? {
        return when {
            startDate.isBeforeNow || endDate.isBeforeNow -> {
                context.getString(
                    R.string.start_date_error_message
                )
            }
            endDate.isBefore(startDate) -> {
                context.getString(
                    R.string.start_date_error_message
                )
            }
            else -> null
        }
    }

    fun clear() {
        messageData.value = null
        reservationData.value = null
        backgroundScope.coroutineContext.cancelChildren()
    }

}
