package com.thoughtworks.android_test_project.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.thoughtworks.android_test_project.R
import com.thoughtworks.android_test_project.domain.models.Reservation
import com.thoughtworks.android_test_project.extensions.toSimpleDatePattern
import kotlinx.android.synthetic.main.hotel_result_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class HotelResultFragment : Fragment() {

    private val viewModel: HotelReservationViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.hotel_result_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() = with(viewModel) {
        reservationData.observe(viewLifecycleOwner, Observer(::bindReservation))
        messageData.observe(viewLifecycleOwner, Observer(::bindMessage))
        findBestHotels()
    }

    private fun bindMessage(message: String?) {
        if (message == null) {
            messageTextView.visibility = GONE
            return
        }

        messageTextView.visibility = VISIBLE
        messageTextView.text = message
    }

    private fun bindReservation(reservation: Reservation?) {
        if (reservation == null) {
            resultView.visibility = GONE
            return
        }

        resultView.visibility = VISIBLE
        with(reservation) {
            reservationName.text = hotelName
            val currency = Currency.getInstance("BRL")
            reservationPrice.text = "${currency.symbol} ${totalPrice}"
            starCount.text = stars.toString()
            reservationStart.text = startDate.toSimpleDatePattern()
            reservationEnd.text = endDate.toSimpleDatePattern()
        }
    }
}