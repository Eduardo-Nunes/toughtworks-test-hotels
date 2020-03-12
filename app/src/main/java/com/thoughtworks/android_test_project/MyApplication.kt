package com.thoughtworks.android_test_project

import android.app.Application
import com.thoughtworks.android_test_project.domain.core.HotelsRepository
import com.thoughtworks.android_test_project.domain.core.ReservationsUseCase
import com.thoughtworks.android_test_project.view.HotelReservationViewModel
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {

    private val mainModule = module {
        single { ReservationsUseCase(HotelsRepository()) }
        viewModel { HotelReservationViewModel(get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initJodaTime()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@MyApplication)
            modules(
                mainModule
            )
        }
    }

    private fun initJodaTime() {
        JodaTimeAndroid.init(this)
    }
}