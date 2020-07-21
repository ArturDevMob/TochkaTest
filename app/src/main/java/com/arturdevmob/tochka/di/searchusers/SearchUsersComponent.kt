package com.arturdevmob.tochka.di.searchusers

import com.arturdevmob.tochka.presentation.fragments.SearchUsersFragment
import dagger.Subcomponent

@Subcomponent(modules = [SearchUsersModule::class])
@SearchUsersScope
interface SearchUsersComponent {
    fun inject(searchUsersFragment: SearchUsersFragment)
}