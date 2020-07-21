package com.arturdevmob.tochka.di.navigation

import com.arturdevmob.tochka.di.searchusers.SearchUsersComponent
import com.arturdevmob.tochka.di.searchusers.SearchUsersModule
import com.arturdevmob.tochka.presentation.fragments.NavigationFragment
import dagger.Subcomponent

@Subcomponent(modules = [NavigationModule::class])
@NavigationScope
interface NavigationComponent {
    fun inject(navigationFragment: NavigationFragment)

    fun createSearchUsersComponent(module: SearchUsersModule): SearchUsersComponent
}