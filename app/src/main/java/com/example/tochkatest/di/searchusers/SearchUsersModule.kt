package com.example.tochkatest.di.searchusers

import androidx.lifecycle.ViewModelProvider
import com.example.tochkatest.domain.interactors.SearchUsersInteractor
import com.example.tochkatest.domain.repositories.UserRepository
import com.example.tochkatest.presentation.adapters.UsersAdapters
import com.example.tochkatest.presentation.fragments.SearchUsersFragment
import com.example.tochkatest.presentation.utils.ModelFactory
import com.example.tochkatest.presentation.viewmodels.SearchUsersViewModel
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
    fun provideSearchUsersViewModel(interactor: SearchUsersInteractor): SearchUsersViewModel {
        return ViewModelProvider(
            fragmentOwner,
            ModelFactory(interactor)
        ).get(SearchUsersViewModel::class.java)
    }

    @Provides
    @SearchUsersScope
    fun provideUsersAdapters(): UsersAdapters {
        return UsersAdapters()
    }
}