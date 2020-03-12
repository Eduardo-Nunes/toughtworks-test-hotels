package com.thoughtworks.android_test_project.view

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.thoughtworks.android_test_project.R
import com.thoughtworks.android_test_project.domain.core.HotelsRepository
import com.thoughtworks.android_test_project.domain.core.ReservationsUseCase
import com.thoughtworks.android_test_project.domain.models.Reservation
import com.thoughtworks.android_test_project.extensions.toSimpleDateTime
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.joda.time.DateTimeUtils
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HotelReservationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var context: Application

    @Mock
    lateinit var useCase: ReservationsUseCase

    @Mock
    lateinit var repository: HotelsRepository

    @Mock
    lateinit var reservationDataTest: Observer<Reservation?>

    @Mock
    lateinit var messageDataTest: Observer<String?>

    private lateinit var viewModel: HotelReservationViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        initContext()
        initJodaTime()
        initObservers()

        Dispatchers.setMain(mainThreadSurrogate)

        repository = mock(HotelsRepository::class.java)
        useCase = spy(ReservationsUseCase(repository))

        viewModel = HotelReservationViewModel(context, useCase).also {
            CoroutineScope(Dispatchers.Main).launch {
                it.reservationData.observeForever(reservationDataTest)
                it.messageData.observeForever(messageDataTest)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun initObservers() {
        reservationDataTest = mock(Observer::class.java) as Observer<Reservation?>
        messageDataTest = mock(Observer::class.java) as Observer<String?>
    }

    private fun initContext() {
        context = mock(Application::class.java)
        `when`(context.getString(R.string.loading_message)).thenReturn("loading")
        `when`(context.getString(R.string.start_date_error_message)).thenReturn("datas anteriores a hoje")
        `when`(context.getString(R.string.wrong_range_error_message)).thenReturn("data final errada")
    }

    private fun initJodaTime() {
        DateTimeUtils.setCurrentMillisFixed("11/03/2020".toSimpleDateTime().millis)
    }

    @After
    fun before() {
        DateTimeUtils.setCurrentMillisSystem()
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun findBestHotels_ERROR_START_BEFORE_NOW() = runBlockingTest {

        viewModel.validateStartDate(10, 2, 2020)
        viewModel.validateEndDate(11, 2, 2020)
        viewModel.isRewardClient = false

        viewModel.findBestHotels()

        verify(messageDataTest, atLeastOnce()).onChanged("datas anteriores a hoje")
    }

    @Test
    fun findBestHotels_ERROR_END_BEFORE_NOW() = runBlockingTest {

        viewModel.validateStartDate(11, 2, 2020)
        viewModel.validateEndDate(10, 2, 2020)
        viewModel.isRewardClient = false

        viewModel.findBestHotels()

        verify(messageDataTest, atLeast(1)).onChanged("datas anteriores a hoje")
    }

    @Test
    fun findBestHotels_ERROR_END_BEFORE_START() = runBlockingTest {

        viewModel.validateStartDate(18, 2, 2020)
        viewModel.validateEndDate(15, 2, 2020)
        viewModel.isRewardClient = false

        viewModel.findBestHotels()

        verify(messageDataTest, atLeast(1)).onChanged("data final errada")

    }

    @Test
    fun findBestHotels_SUCCESS() = runBlockingTest {


        viewModel.validateStartDate(16, 2, 2020)
        viewModel.validateEndDate(18, 2, 2020)
        viewModel.isRewardClient = false

        viewModel.findBestHotels()

        verify(useCase)(
            "16/03/2020".toSimpleDateTime(),
            "18/03/2020".toSimpleDateTime(),
            false
        )
        verify(messageDataTest, atLeastOnce()).onChanged("loading")
        verify(messageDataTest, atLeastOnce()).onChanged(null)
        verify(reservationDataTest, atLeastOnce()).onChanged(
            Reservation(
                hotelName = "Parque das flores",
                stars = 3,
                startDate = "16/03/2020".toSimpleDateTime(),
                endDate = "18/03/2020".toSimpleDateTime(),
                hasReward = false,
                totalPrice = 330f
            )
        )

    }
}