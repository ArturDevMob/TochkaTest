package com.arturdevmob.tochka.di.logout

import androidx.lifecycle.ViewModelProvider
import com.arturdevmob.tochka.domain.interactors.LogoutInteractor
import com.arturdevmob.tochka.domain.repositories.AccountRepository
import com.arturdevmob.tochka.presentation.fragments.LogoutFragment
import com.arturdevmob.tochka.presentation.utils.ModelFactory
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProvider
import com.arturdevmob.tochka.presentation.viewmodels.LogoutViewModel
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
    fun provideLogoutViewModel(
        schedulerProvider: SchedulerProvider,
        interactor: LogoutInteractor
    ): LogoutViewModel {
        return ViewModelProvider(
            fragmentOwner,
            ModelFactory(schedulerProvider, interactor)
        ).get(LogoutViewModel::class.java)
    }
}