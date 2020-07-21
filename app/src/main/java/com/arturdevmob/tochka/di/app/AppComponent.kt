package com.arturdevmob.tochka.di.app

import com.arturdevmob.tochka.di.auth.AuthComponent
import com.arturdevmob.tochka.di.auth.AuthModule
import com.arturdevmob.tochka.di.logout.LogoutComponent
import com.arturdevmob.tochka.di.logout.LogoutModule
import com.arturdevmob.tochka.di.navigation.NavigationComponent
import com.arturdevmob.tochka.di.navigation.NavigationModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun createNavigationComponent(module: NavigationModule): NavigationComponent
    fun createAuthComponent(module: AuthModule): AuthComponent
    fun createLogoutComponent(module: LogoutModule): LogoutComponent
}