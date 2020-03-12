package com.thoughtworks.android_test_project.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thoughtworks.android_test_project.R
import com.thoughtworks.android_test_project.domain.core.ReservationsUseCase
import com.thoughtworks.android_test_project.domain.models.Reservation
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

    val reservationData: MutableLiveData<Reservation?> = MutableLiveData()
    val messageData: MutableLiveData<String?> = MutableLiveData()

    private val backgroundScope by lazy {
        CoroutineScope(viewModelScope.coroutineContext + Dispatchers.IO)
    }

    fun findBestHotels(startDate: DateTime, endDate: DateTime, isRewardClient: Boolean) {
        if (!validateDates(startDate, endDate)) return

        backgroundScope.launch {
            messageData.postValue(context.getString(R.string.loading_message))
            bestReservationCallback(reservationsUseCase(startDate, endDate, isRewardClient))
        }
    }

    private fun bestReservationCallback(reservation: Reservation) {
        reservationData.postValue(reservation)
        messageData.postValue(null)
    }

    private fun validateDates(startDate: DateTime, endDate: DateTime): Boolean {
        return when {
            startDate.isBeforeNow || endDate.isBeforeNow -> {
                messageData.postValue(
                    context.getString(R.string.start_date_error_message)
                )
                false
            }
            endDate.isBefore(startDate) -> {
                messageData.postValue(
                    context.getString(R.string.wrong_range_error_message)
                )
                false
            }
            else -> {
                messageData.postValue(null)
                true
            }
        }
    }

    fun clear() {
        messageData.value = null
        reservationData.value = null
        backgroundScope.coroutineContext.cancelChildren()
    }

}
