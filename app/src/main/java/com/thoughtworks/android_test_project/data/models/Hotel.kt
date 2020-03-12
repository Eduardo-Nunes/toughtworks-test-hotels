package com.thoughtworks.android_test_project.data.models

import com.thoughtworks.android_test_project.data.interfaces.RewardsVocation

data class Hotel(
    val name: String,
    override val stars: Int,
    val regularDayPrice: Float,
    val rewardDayPrice: Float,
    val regularWeekendPrice: Float,
    val rewardWeekendPrice: Float
) : RewardsVocation {
    override fun getDayPrice(isReward: Boolean): Float {
        return if (isReward) {
            rewardDayPrice
        } else {
            regularDayPrice
        }
    }

    override fun getWeekendPrice(isReward: Boolean): Float {
        return if (isReward) {
            rewardWeekendPrice
        } else {
            regularWeekendPrice
        }
    }
}