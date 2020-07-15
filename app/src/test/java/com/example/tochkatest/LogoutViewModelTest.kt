package com.example.tochkatest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.tochkatest.domain.interactors.LogoutInteractor
import com.example.tochkatest.presentation.utils.rx.SchedulerProvider
import com.example.tochkatest.presentation.viewmodels.LogoutViewModel
import com.example.tochkatest.utils.SchedulerProviderTest
import io.reactivex.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class LogoutViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var interactor: LogoutInteractor
    private lateinit var schedulerProvider: SchedulerProvider
    private lateinit var logoutViewModel: LogoutViewModel

    // Observers
    private val logoutStateObserver = mock<Observer<LogoutViewModel.LogoutState>>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = SchedulerProviderTest()
        logoutViewModel = LogoutViewModel(schedulerProvider, interactor)

        logoutViewModel.logoutState.observeForever(logoutStateObserver)
    }

    @Test
    fun `successful logging out account`() {
        val result = LogoutViewModel.LogoutState.Success

        `when`(interactor.logoutAccount())
            .thenReturn(Completable.complete())

        logoutViewModel.init()

        verify(logoutStateObserver).onChanged(result)
    }

    @Test
    fun `error logging out account`() {
        val throwable = Throwable("Error logging out")
        val result = LogoutViewModel.LogoutState.Error(throwable.message, throwable)

        `when`(interactor.logoutAccount())
            .thenReturn(Completable.error(throwable))

        logoutViewModel.init()

        verify(logoutStateObserver).onChanged(refEq(result))
    }

    private inline fun <reified T> mock() = mock(T::class.java)
}