package com.example.tochkatest.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tochkatest.domain.interactors.LogoutInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LogoutViewModel(private val interactor: LogoutInteractor) : ViewModel() {
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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