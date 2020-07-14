package com.example.tochkatest.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tochkatest.domain.interactors.*
import com.example.tochkatest.presentation.utils.rx.SchedulerProvider
import com.example.tochkatest.presentation.viewmodels.AuthViewModel
import com.example.tochkatest.presentation.viewmodels.LogoutViewModel
import com.example.tochkatest.presentation.viewmodels.NavigationViewModel
import com.example.tochkatest.presentation.viewmodels.SearchUsersViewModel

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