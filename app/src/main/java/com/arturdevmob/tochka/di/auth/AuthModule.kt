package com.arturdevmob.tochka.di.auth

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arturdevmob.tochka.domain.interactors.AuthInteractor
import com.arturdevmob.tochka.domain.repositories.AccountRepository
import com.arturdevmob.tochka.presentation.utils.ModelFactory
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProvider
import com.arturdevmob.tochka.presentation.viewmodels.AuthViewModel
import dagger.Module
import dagger.Provides

@Module
class AuthModule(private val fragmentOwner: Fragment) {
    @Provides
    @AuthScope
    fun provideAuthInteractor(accountRepository: AccountRepository): AuthInteractor {
        return AuthInteractor(accountRepository)
    }

    @Provides
    @AuthScope
    fun provideAuthViewModel(
        schedulerProvider: SchedulerProvider,
        interactor: AuthInteractor
    ): AuthViewModel {
        return ViewModelProvider(
            fragmentOwner,
            ModelFactory(schedulerProvider, interactor)
        ).get(AuthViewModel::class.java)
    }
}