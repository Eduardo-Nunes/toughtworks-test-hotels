package com.thoughtworks.android_test_project.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        initViews()
        initListeners()
    }

    private fun initViews() {
        checkInCalendarView.date = viewModel.startDate.millis
        checkOutCalendarView.date = viewModel.endDate.millis
        rewardSwitch.isChecked = viewModel.isRewardClient
    }

    private fun initListeners() {
        bestPricesButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_checkInOuFragment_to_hotelResultFragment))

        checkInCalendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.validateStartDate(dayOfMonth, month, year)
        }
        checkOutCalendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.validateEndDate(dayOfMonth, month, year)
        }
        rewardSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isRewardClient = isChecked
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() {
//        viewModel.clear()
        viewModel.messageData.observe(viewLifecycleOwner, Observer(::bindMessage))
        viewModel.enableFindButton.observe(viewLifecycleOwner, Observer(::enableFindButton))
    }

    private fun enableFindButton(isEnable: Boolean) {
        bestPricesButton.isEnabled = isEnable
    }

    private fun bindMessage(message: String?) {

        if (message == null) {
            return
        }

        MaterialAlertDialogBuilder(context)
            .setTitle("MENSAGEM")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, which ->
                viewModel.clear()
            }
            .show()
    }
}