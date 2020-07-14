package com.example.tochkatest.di.app

import com.example.tochkatest.di.auth.AuthComponent
import com.example.tochkatest.di.auth.AuthModule
import com.example.tochkatest.di.logout.LogoutComponent
import com.example.tochkatest.di.logout.LogoutModule
import com.example.tochkatest.di.searchusers.SearchUsersComponent
import com.example.tochkatest.di.searchusers.SearchUsersModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun createAuthComponent(module: AuthModule): AuthComponent
    fun createSearchUsersComponent(module: SearchUsersModule): SearchUsersComponent
    fun createLogoutComponent(module: LogoutModule): LogoutComponent
}