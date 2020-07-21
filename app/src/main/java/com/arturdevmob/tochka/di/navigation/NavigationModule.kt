package com.arturdevmob.tochka.di.navigation

import androidx.lifecycle.ViewModelProvider
import com.arturdevmob.tochka.domain.interactors.NavigationInteractor
import com.arturdevmob.tochka.domain.repositories.AccountRepository
import com.arturdevmob.tochka.presentation.fragments.NavigationFragment
import com.arturdevmob.tochka.presentation.utils.ModelFactory
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProvider
import com.arturdevmob.tochka.presentation.viewmodels.NavigationViewModel
import dagger.Module
import dagger.Provides

@Module
class NavigationModule(private val fragmentOwner: NavigationFragment) {
    @Provides
    @NavigationScope
    fun provideNavigationInteractor(accountRepository: AccountRepository): NavigationInteractor {
        return NavigationInteractor(accountRepository)
    }

    @Provides
    @NavigationScope
    fun provideNavigationViewModel(
        schedulerProvider: SchedulerProvider,
        navigationInteractor: NavigationInteractor
    ): NavigationViewModel {
        return ViewModelProvider(
            fragmentOwner,
            ModelFactory(schedulerProvider, navigationInteractor)
        ).get(
            NavigationViewModel::class.java
        )
    }
}