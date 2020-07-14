package com.example.tochkatest.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tochkatest.domain.interactors.LogoutInteractor
import com.example.tochkatest.presentation.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class LogoutViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val interactor: LogoutInteractor
) : ViewModel() {
    val logoutState = MutableLiveData<LogoutState>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun init() {
        logoutAccount()
    }

    // Удаляет информацию об аккаунте пользователя
    // тем самым сбрасывая его авторизацию
    private fun logoutAccount() {
        val disposable = interactor.logoutAccount()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({
                logoutState.value = LogoutState.Success
            }, {
                logoutState.value = LogoutState.Error(it.message, it)
            })

        compositeDisposable.add(disposable)
    }

    sealed class LogoutState {
        object Success : LogoutState()
        class Error(val message: String?, val throwable: Throwable) : LogoutState()
    }
}