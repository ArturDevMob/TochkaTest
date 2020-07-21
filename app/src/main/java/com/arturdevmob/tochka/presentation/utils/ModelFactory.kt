package com.arturdevmob.tochka.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arturdevmob.tochka.domain.interactors.*
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProvider
import com.arturdevmob.tochka.presentation.viewmodels.AuthViewModel
import com.arturdevmob.tochka.presentation.viewmodels.LogoutViewModel
import com.arturdevmob.tochka.presentation.viewmodels.NavigationViewModel
import com.arturdevmob.tochka.presentation.viewmodels.SearchUsersViewModel

class ModelFactory(
    private val schedulerProvider: SchedulerProvider,
    private val interactor: BaseInteractor
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(schedulerProvider, interactor as AuthInteractor) as T
            }
            modelClass.isAssignableFrom(SearchUsersViewModel::class.java) -> {
                SearchUsersViewModel(schedulerProvider, interactor as SearchUsersInteractor) as T
            }
            modelClass.isAssignableFrom(LogoutViewModel::class.java) -> {
                LogoutViewModel(schedulerProvider, interactor as LogoutInteractor) as T
            }
            modelClass.isAssignableFrom(NavigationViewModel::class.java) -> {
                NavigationViewModel(schedulerProvider, interactor as NavigationInteractor) as T
            }
            else -> {
                throw IllegalArgumentException("Неизвестный класс модели: ${modelClass.name}")
            }
        }
    }
}