package com.arturdevmob.tochka.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arturdevmob.tochka.domain.interactors.SearchUsersInteractor
import com.arturdevmob.tochka.domain.models.UserDomainModel
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProvider
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class SearchUsersViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val interactor: SearchUsersInteractor
) : ViewModel() {
    val usersSearch = MutableLiveData<SearchUsers>() // Модель с пользователями
    var lastQuery = MutableLiveData<String>() // Поисковой запрос, чтобы пережить поворот
    var lastPage = MutableLiveData<Int>() // Номер текущей страницы, чтобы пережить поворот
    private lateinit var usersFromQuery: Disposable
    private lateinit var nextUsersFromQuery: Disposable
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }

    // Первая загрузка пользователей по поисковому запросу
    // Принимает поток содержащий строку с поисковым запросом
    fun loadUsersFromQuery(flowableQuery: Flowable<String>) {
        clearUsersFromQueryDisposable()

        usersFromQuery = flowableQuery
            .doOnNext {
                usersSearch.postValue(SearchUsers.Loading)
                lastQuery.postValue(it)
            }
            .flatMapSingle { interactor.getUsersFromQuery(it, 1) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({
                if (it.isNotEmpty()) {
                    usersSearch.value = SearchUsers.Result(it)
                } else {
                    usersSearch.value = SearchUsers.Empty
                }
            }, {
                usersSearch.value = SearchUsers.Error(it.message, it)
            })

        compositeDisposable.add(usersFromQuery)
    }

    // Подгружает новых пользователей
    // Принимает строку с поисковым запросом и номер страницы требующей данные
    fun loadUsersNextFromQuery(query: String, page: Int) {
        if (query.isNotEmpty()) {
            lastPage.value = page
        }

        clearNextUsersFromQueryDisposable()

        nextUsersFromQuery = interactor.getUsersFromQuery(
            lastQuery.value ?: query, lastPage.value ?: page
        )
            .map {
                // К прошлому листу с пользователями, добавляются пользователи из нового листа
                val oldList = (usersSearch.value as SearchUsers.Result).data.toMutableList()
                oldList.addAll(it)
                oldList
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({
                usersSearch.value = SearchUsers.Result(it)
            }, {
                usersSearch.value = SearchUsers.Error(it.message, it)
            })

        compositeDisposable.add(nextUsersFromQuery)
    }

    // Очищает подписку на предыдущий поток
    // Иначе подписки будут накапливаться
    private fun clearUsersFromQueryDisposable() {
        if (this::usersFromQuery.isInitialized) {
            usersFromQuery.dispose()
        }
    }

    // Аналогично
    private fun clearNextUsersFromQueryDisposable() {
        if (this::nextUsersFromQuery.isInitialized) {
            nextUsersFromQuery.dispose()
        }
    }

    sealed class SearchUsers {
        class Result(val data: List<UserDomainModel>) : SearchUsers()
        class Error(val message: String?, val throwable: Throwable) : SearchUsers()
        object Empty : SearchUsers()
        object Loading : SearchUsers()
    }
}