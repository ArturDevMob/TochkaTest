package com.arturdevmob.tochka.di.searchusers

import androidx.lifecycle.ViewModelProvider
import com.arturdevmob.tochka.domain.interactors.SearchUsersInteractor
import com.arturdevmob.tochka.domain.repositories.UserRepository
import com.arturdevmob.tochka.presentation.adapters.UsersAdapters
import com.arturdevmob.tochka.presentation.fragments.SearchUsersFragment
import com.arturdevmob.tochka.presentation.utils.ModelFactory
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProvider
import com.arturdevmob.tochka.presentation.viewmodels.SearchUsersViewModel
import dagger.Module
import dagger.Provides

@Module
class SearchUsersModule(private val fragmentOwner: SearchUsersFragment) {
    @Provides
    @SearchUsersScope
    fun provideSearchUsersInteractor(userRepository: UserRepository): SearchUsersInteractor {
        return SearchUsersInteractor(userRepository)
    }

    @Provides
    @SearchUsersScope
    fun provideSearchUsersViewModel(
        schedulerProvider: SchedulerProvider,
        interactor: SearchUsersInteractor
    ): SearchUsersViewModel {
        return ViewModelProvider(
            fragmentOwner,
            ModelFactory(schedulerProvider, interactor)
        ).get(SearchUsersViewModel::class.java)
    }

    @Provides
    @SearchUsersScope
    fun provideUsersAdapters(): UsersAdapters {
        return UsersAdapters()
    }
}