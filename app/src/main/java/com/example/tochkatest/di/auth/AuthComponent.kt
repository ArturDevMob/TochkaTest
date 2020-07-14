package com.example.tochkatest.di.auth

import com.example.tochkatest.presentation.fragments.AuthFragment
import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
@AuthScope
interface AuthComponent {
    fun inject(authFragment: AuthFragment)
}