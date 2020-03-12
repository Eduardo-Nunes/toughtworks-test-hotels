package com.thoughtworks.android_test_project.domain.core

import android.app.Application
import com.thoughtworks.android_test_project.extensions.toSimpleDateTime
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class ReservationsUseCaseTest {

    private lateinit var useCaseTest: ReservationsUseCase

    @Mock
    lateinit var repository: HotelsRepository

    @Mock
    lateinit var context: Application

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        initContext()
        initJodaTime()
        repository = mock(HotelsRepository::class.java)
        useCaseTest = ReservationsUseCase(repository)
    }

    private fun initJodaTime() {
        DateTimeUtils.setCurrentMillisFixed(0L)
    }

    @After
    fun before() {
        DateTimeUtils.setCurrentMillisSystem()
    }

    private fun initContext() {
        context = mock(Application::class.java)
    }

    @Test
    fun getBestReservation_ENTRADA_1() = runBlocking {

        val start = "16/03/2020".toSimpleDateTime()
        val end = "18/03/2020".toSimpleDateTime()

        val bestOffer = useCaseTest(
            start, end,
            isReward = false
        )

        assert(bestOffer.hotelName == "Parque das flores")
    }


    @Test
    fun getBestReservation_ENTRADA_2() = runBlocking {

        val start = "20/03/2020".toSimpleDateTime()
        val end = "22/03/2020".toSimpleDateTime()

        val bestOffer = useCaseTest(
            start, end,
            isReward = false
        )

        assert(bestOffer.hotelName == "Jardim Botânico")
    }

    @Test
    fun getBestReservation_ENTRADA_3() = runBlocking {

        val start = "26/03/2020".toSimpleDateTime()
        val end = "28/03/2020".toSimpleDateTime()

        val bestOffer = useCaseTest(
            start, end,
            isReward = true
        )

        assert(bestOffer.hotelName == "Mar Atlântico")
    }


    @Test
    fun getDatesRange_SUCCESS() {
        val daysRange = useCaseTest.getDateRange(DateTime.now(), DateTime.now().plusDays(5))

        assert(daysRange.size == 6)
        repeat(6) {
            assert(daysRange.contains(DateTime.now().plusDays(it)))
        }
    }
}