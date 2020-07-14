package com.example.tochkatest.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tochkatest.domain.interactors.NavigationInteractor
import com.example.tochkatest.domain.models.AccountDomainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NavigationViewModel(private val interactor: NavigationInteractor) : ViewModel() {
    val accountInfo = MutableLiveData<AccountInfo>() // Модель с информацией об аккаунте
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }

    fun init() {
        if (accountInfo.value == null) {
            loadAccountInfo()
        }
    }

    // Загружает информацию об аккаунте авторизовавшегося пользователя
    private fun loadAccountInfo() {
        val disposable = interactor.getAccountInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                accountInfo.value = AccountInfo.Result(it)
            }, {
                accountInfo.value = AccountInfo.Error(it.message, it)
            })

        compositeDisposable.add(disposable)
    }

    sealed class AccountInfo {
        class Result(val data: AccountDomainModel) : AccountInfo()
        class Error(val message: String?, val throwable: Throwable) : AccountInfo()
    }
}