package com.example.tochkatest.di.navigation

import com.example.tochkatest.di.searchusers.SearchUsersComponent
import com.example.tochkatest.di.searchusers.SearchUsersModule
import com.example.tochkatest.presentation.fragments.NavigationFragment
import dagger.Subcomponent

@Subcomponent(modules = [NavigationModule::class])
@NavigationScope
interface NavigationComponent {
    fun inject(navigationFragment: NavigationFragment)

    fun createSearchUsersComponent(module: SearchUsersModule): SearchUsersComponent
}