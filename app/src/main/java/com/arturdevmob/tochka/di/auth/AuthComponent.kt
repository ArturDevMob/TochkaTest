package com.arturdevmob.tochka.di.auth

import com.arturdevmob.tochka.presentation.fragments.AuthFragment
import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
@AuthScope
interface AuthComponent {
    fun inject(authFragment: AuthFragment)
}