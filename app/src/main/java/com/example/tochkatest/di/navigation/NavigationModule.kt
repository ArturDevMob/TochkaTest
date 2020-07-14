package com.example.tochkatest.di.navigation

import androidx.lifecycle.ViewModelProvider
import com.example.tochkatest.domain.interactors.NavigationInteractor
import com.example.tochkatest.domain.repositories.AccountRepository
import com.example.tochkatest.presentation.fragments.NavigationFragment
import com.example.tochkatest.presentation.utils.ModelFactory
import com.example.tochkatest.presentation.utils.rx.SchedulerProvider
import com.example.tochkatest.presentation.viewmodels.NavigationViewModel
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