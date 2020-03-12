package com.thoughtworks.android_test_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.thoughtworks.android_test_project.view.HotelReservationViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val hotelViewModel: HotelReservationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        setContentView(R.layout.activity_main)
        setupNavigation()
    }

    private fun setupNavigation() {
        NavigationUI.setupWithNavController(mainToolbar, mainRootNavigation.findNavController())
    }

    override fun onBackPressed() {
        if (!mainRootNavigation.findNavController().popBackStack()) {
            super.onBackPressed()
        } else {
            hotelViewModel.clear()
        }
    }
}
