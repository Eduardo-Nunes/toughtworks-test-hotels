package com.thoughtworks.android_test_project

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        initJodaTime()
    }

    private fun initKoin() {
    }

    private fun initJodaTime() {
        JodaTimeAndroid.init(this)
    }
}