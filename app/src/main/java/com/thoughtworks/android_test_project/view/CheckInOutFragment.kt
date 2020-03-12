package com.thoughtworks.android_test_project.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.thoughtworks.android_test_project.R
import kotlinx.android.synthetic.main.fragment_check_in_out.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CheckInOutFragment : Fragment() {

    private val viewModel: HotelReservationViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_in_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        bestPricesButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_checkInOuFragment_to_hotelResultFragment2))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() {
        viewModel.test = 10
    }
}