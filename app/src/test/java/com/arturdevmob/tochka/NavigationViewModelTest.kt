package com.arturdevmob.tochka

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.arturdevmob.tochka.domain.interactors.NavigationInteractor
import com.arturdevmob.tochka.domain.models.AccountDomainModel
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProvider
import com.arturdevmob.tochka.presentation.viewmodels.NavigationViewModel
import com.arturdevmob.tochka.utils.SchedulerProviderTest
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class NavigationViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var interactor: NavigationInteractor
    private lateinit var schedulerProvider: SchedulerProvider
    private lateinit var navigationViewModel: NavigationViewModel

    // Observers
    private val accountInfoObserver = mock<Observer<NavigationViewModel.AccountInfo>>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = SchedulerProviderTest()
        navigationViewModel = NavigationViewModel(schedulerProvider, interactor)

        navigationViewModel.accountInfo.observeForever(accountInfoObserver)
    }

    @Test
    fun `successful loading account`() {
        val account = createAccountDomainModel()
        val result = NavigationViewModel.AccountInfo.Result(account)

        `when`(interactor.getAccountInfo())
            .thenReturn(createSingleWithAccountDomainModel())

        navigationViewModel.init()

        verify(accountInfoObserver).onChanged(refEq(result))
    }

    @Test
    fun `error loading account`() {
        val throwable = Throwable("Error loading account")
        val result = NavigationViewModel.AccountInfo.Error(throwable.message, throwable)

        `when`(interactor.getAccountInfo())
            .thenReturn(Single.error(throwable))

        navigationViewModel.init()

        verify(accountInfoObserver).onChanged(refEq(result))
    }

    private fun createSingleWithAccountDomainModel(): Single<AccountDomainModel> {
        return Single.just(createAccountDomainModel())
    }

    private fun createAccountDomainModel(): AccountDomainModel {
        return AccountDomainModel("name", "test@gmail.com", null)
    }

    private inline fun <reified T> mock() = mock(T::class.java)
}