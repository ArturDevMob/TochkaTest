package com.arturdevmob.tochka.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arturdevmob.tochka.domain.interactors.NavigationInteractor
import com.arturdevmob.tochka.domain.models.AccountDomainModel
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class NavigationViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val interactor: NavigationInteractor
) : ViewModel() {
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
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
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