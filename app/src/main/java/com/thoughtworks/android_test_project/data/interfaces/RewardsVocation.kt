package com.thoughtworks.android_test_project.data.interfaces

interface RewardsVocation {
    val stars: Int
    fun getDayPrice(isReward: Boolean): Float
    fun getWeekendPrice(isReward: Boolean): Float
}