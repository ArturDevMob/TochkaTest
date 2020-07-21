package com.arturdevmob.tochka.di.logout

import com.arturdevmob.tochka.presentation.fragments.LogoutFragment
import dagger.Subcomponent

@Subcomponent(modules = [LogoutModule::class])
@LogoutScope
interface LogoutComponent {
    fun inject(logoutFragment: LogoutFragment)
}