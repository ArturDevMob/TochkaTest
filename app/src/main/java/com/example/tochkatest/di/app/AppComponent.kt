package com.example.tochkatest.di.app

import com.example.tochkatest.di.auth.AuthComponent
import com.example.tochkatest.di.auth.AuthModule
import com.example.tochkatest.di.logout.LogoutComponent
import com.example.tochkatest.di.logout.LogoutModule
import com.example.tochkatest.di.navigation.NavigationComponent
import com.example.tochkatest.di.navigation.NavigationModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun createNavigationComponent(module: NavigationModule): NavigationComponent
    fun createAuthComponent(module: AuthModule): AuthComponent
    fun createLogoutComponent(module: LogoutModule): LogoutComponent
}