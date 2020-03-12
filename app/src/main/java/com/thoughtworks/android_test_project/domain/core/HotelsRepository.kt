package com.thoughtworks.android_test_project.domain.core

import com.thoughtworks.android_test_project.data.models.Hotel

open class HotelsRepository {

    fun listHotels(): List<Hotel> {
        return listOf(
            Hotel("Parque das flores", 3, 110f, 80f, 90f, 80f),
            Hotel("Jardim Botânico", 4, 160f, 110f, 60f, 50f),
            Hotel("Mar Atlântico", 5, 220f, 100f, 150f, 40f)
        )
    }
}