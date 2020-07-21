package com.arturdevmob.tochka

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.arturdevmob.tochka.domain.interactors.AuthInteractor
import com.arturdevmob.tochka.domain.models.AccountDomainModel
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProvider
import com.arturdevmob.tochka.presentation.viewmodels.AuthViewModel
import com.arturdevmob.tochka.utils.SchedulerProviderTest
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class AuthViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var interactor: AuthInteractor
    private lateinit var schedulerProvider: SchedulerProvider
    private lateinit var authViewModel: AuthViewModel

    // Observers
    private val authStateObserver = mock<Observer<AuthViewModel.AuthState>>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = SchedulerProviderTest()
        authViewModel = AuthViewModel(schedulerProvider, interactor)

        authViewModel.authState.observeForever(authStateObserver)
    }

    @Test
    fun `successful authorization`() {
        val result = AuthViewModel.AuthState.Authorized

        `when`(interactor.getAccountInfo())
            .thenReturn(createSingleWithAccountDomainModel())

        authViewModel.init()

        verify(authStateObserver).onChanged(result)
    }

    @Test
    fun `failed authorization`() {
        val result = AuthViewModel.AuthState.NotAuthorized

        `when`(interactor.getAccountInfo())
            .thenReturn(createSingleWithEmptyAccountDomainModel())

        authViewModel.init()

        verify(authStateObserver).onChanged(result)
    }

    @Test
    fun `error authorization`() {
        val throwable = Throwable("Error authorization")
        val result = AuthViewModel.AuthState.Error(throwable.message, throwable)

        `when`(interactor.getAccountInfo())
            .thenReturn(Single.error { throwable })

        authViewModel.init()

        verify(authStateObserver).onChanged(refEq(result))
    }

    @Test
    fun `saving authorization`() {
        val account = createAccountDomainModel()
        val result = AuthViewModel.AuthState.Authorized

        `when`(interactor.saveAccountInfo(account))
            .thenReturn(Completable.complete())

        authViewModel.saveAccountInfo(account.name, account.email, account.photoUri)

        verify(authStateObserver).onChanged(result)
    }

    @Test
    fun `error saving authorization`() {
        val throwable = Throwable("Error")
        val account = createAccountDomainModel()
        val result = AuthViewModel.AuthState.Error(throwable.message, throwable)

        `when`(interactor.saveAccountInfo(account))
            .thenReturn(Completable.error(throwable))

        authViewModel.saveAccountInfo(account.name, account.email, account.photoUri)

        verify(authStateObserver).onChanged(refEq(result))
    }

    private fun createAccountDomainModel(): AccountDomainModel {
        return AccountDomainModel("Name", "test@gmail.ru", null)
    }

    private fun createSingleWithAccountDomainModel(): Single<AccountDomainModel> {
        return Single.just(createAccountDomainModel())
    }

    private fun createSingleWithEmptyAccountDomainModel(): Single<AccountDomainModel> {
        return Single.just(AccountDomainModel(null, null, null))
    }

    private inline fun <reified T> mock() = Mockito.mock(T::class.java)
}