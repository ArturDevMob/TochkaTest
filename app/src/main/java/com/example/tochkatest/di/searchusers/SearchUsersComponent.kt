package com.example.tochkatest.di.searchusers

import com.example.tochkatest.presentation.fragments.SearchUsersFragment
import dagger.Subcomponent

@Subcomponent(modules = [SearchUsersModule::class])
@SearchUsersScope
interface SearchUsersComponent {
    fun inject(searchUsersFragment: SearchUsersFragment)
}