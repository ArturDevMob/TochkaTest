package com.example.tochkatest.di.logout

import com.example.tochkatest.presentation.fragments.LogoutFragment
import dagger.Subcomponent

@Subcomponent(modules = [LogoutModule::class])
@LogoutScope
interface LogoutComponent {
    fun inject(logoutFragment: LogoutFragment)
}