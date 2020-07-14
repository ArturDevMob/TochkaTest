package com.example.tochkatest.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tochkatest.domain.interactors.AuthInteractor
import com.example.tochkatest.domain.interactors.BaseInteractor
import com.example.tochkatest.domain.interactors.LogoutInteractor
import com.example.tochkatest.domain.interactors.SearchUsersInteractor
import com.example.tochkatest.presentation.viewmodels.AuthViewModel
import com.example.tochkatest.presentation.viewmodels.LogoutViewModel
import com.example.tochkatest.presentation.viewmodels.SearchUsersViewModel

class ModelFactory(private val interactor: BaseInteractor) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(interactor as AuthInteractor) as T
            }
            modelClass.isAssignableFrom(SearchUsersViewModel::class.java) -> {
                SearchUsersViewModel(interactor as SearchUsersInteractor) as T
            }
            modelClass.isAssignableFrom(LogoutViewModel::class.java) -> {
                LogoutViewModel(interactor as LogoutInteractor) as T
            }
            else -> {
                throw IllegalArgumentException("Неизвестный класс модели: ${modelClass.name}")
            }
        }
    }
}