package com.example.tochkatest.di.logout

import androidx.lifecycle.ViewModelProvider
import com.example.tochkatest.domain.interactors.LogoutInteractor
import com.example.tochkatest.domain.repositories.AccountRepository
import com.example.tochkatest.presentation.fragments.LogoutFragment
import com.example.tochkatest.presentation.utils.ModelFactory
import com.example.tochkatest.presentation.viewmodels.LogoutViewModel
import dagger.Module
import dagger.Provides

@Module
class LogoutModule(private val fragmentOwner: LogoutFragment) {
    @Provides
    @LogoutScope
    fun provideLogoutInteractor(accountRepository: AccountRepository): LogoutInteractor {
        return LogoutInteractor(accountRepository)
    }

    @Provides
    @LogoutScope
    fun provideLogoutViewModel(interactor: LogoutInteractor): LogoutViewModel {
        return ViewModelProvider(
            fragmentOwner,
            ModelFactory(interactor)
        ).get(LogoutViewModel::class.java)
    }
}