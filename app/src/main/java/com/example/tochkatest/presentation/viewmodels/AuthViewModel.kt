package com.example.tochkatest.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tochkatest.domain.interactors.AuthInteractor
import com.example.tochkatest.domain.models.AccountDomainModel
import com.example.tochkatest.presentation.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class AuthViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val interactor: AuthInteractor
) : ViewModel() {
    val authState = MutableLiveData<AuthState>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun init() {
        if (authState.value == null) {
            checkingUserAuthorization()
        }
    }

    // Сохраняет информацию об аккаунте авторизовавшегося пользователя
    fun saveAccountInfo(name: String?, email: String?, photoUri: String?) {
        val model = AccountDomainModel(name, email, photoUri)

        val disposable = interactor.saveAccountInfo(model)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({
                authState.value = AuthState.Authorized
            }, {
                authState.value = AuthState.Error(it.message, it)
            })

        compositeDisposable.add(disposable)
    }

    // Получает информацию об аккаунте пользователя
    // Если информации нет - не авторизирован, информация есть - авторизирован
    private fun checkingUserAuthorization() {
        val disposable = interactor.getAccountInfo()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({
                if (it.name == null && it.email == null && it.photoUri == null) {
                    authState.value = AuthState.NotAuthorized
                } else {
                    authState.value = AuthState.Authorized
                }
            }, {
                authState.value = AuthState.Error(it.message, it)
            })

        compositeDisposable.add(disposable)
    }

    sealed class AuthState {
        class Error(val message: String?, val throwable: Throwable) : AuthState()
        object Authorized : AuthState()
        object NotAuthorized : AuthState()
    }
}