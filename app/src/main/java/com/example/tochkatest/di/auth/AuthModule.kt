package com.example.tochkatest.di.auth

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tochkatest.domain.interactors.AuthInteractor
import com.example.tochkatest.domain.repositories.AccountRepository
import com.example.tochkatest.presentation.utils.ModelFactory
import com.example.tochkatest.presentation.utils.rx.SchedulerProvider
import com.example.tochkatest.presentation.viewmodels.AuthViewModel
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