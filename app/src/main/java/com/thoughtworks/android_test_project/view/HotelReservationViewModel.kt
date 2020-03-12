package com.thoughtworks.android_test_project.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren

class HotelReservationViewModel(context: Application) :
    AndroidViewModel(context) {

    private val backgroundScope by lazy {
        CoroutineScope(viewModelScope.coroutineContext + Dispatchers.IO)
    }

    fun findBestHotels() {

    }

    var test = 0

    fun clear() {
        backgroundScope.coroutineContext.cancelChildren()
    }

}
