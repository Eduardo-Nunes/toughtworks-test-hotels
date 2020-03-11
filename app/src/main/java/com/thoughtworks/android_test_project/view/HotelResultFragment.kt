package com.thoughtworks.android_test_project.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.thoughtworks.android_test_project.R
import kotlinx.android.synthetic.main.hotel_result_fragment.*

class HotelResultFragment : Fragment() {

    private val viewModel: HotelReservationViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(HotelReservationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.hotel_result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        textView.text = "aguardando dados"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() {
        textView.text = viewModel.test.toString()
    }


}
