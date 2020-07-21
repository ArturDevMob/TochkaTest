package com.arturdevmob.tochka

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.arturdevmob.tochka.domain.interactors.SearchUsersInteractor
import com.arturdevmob.tochka.domain.models.UserDomainModel
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProvider
import com.arturdevmob.tochka.presentation.viewmodels.SearchUsersViewModel
import com.arturdevmob.tochka.utils.SchedulerProviderTest
import io.reactivex.Flowable
import io.reactivex.Single
import junit.framework.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class SearchUsersViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var interactor: SearchUsersInteractor
    private lateinit var schedulerProvider: SchedulerProvider
    private lateinit var searchUsersViewModel: SearchUsersViewModel

    // Observers
    private val usersSearchObserver = mock<Observer<SearchUsersViewModel.SearchUsers>>()
    private val lastQueryObserver = mock<Observer<String>>()
    private val lastPageObserver = mock<Observer<Int>>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = SchedulerProviderTest()
        searchUsersViewModel = SearchUsersViewModel(schedulerProvider, interactor)

        searchUsersViewModel.usersSearch.observeForever(usersSearchObserver)
        searchUsersViewModel.lastQuery.observeForever(lastQueryObserver)
        searchUsersViewModel.lastPage.observeForever(lastPageObserver)
    }

    @Test
    fun `loading users from query with result`() {
        val resultLoading = SearchUsersViewModel.SearchUsers.Loading
        val result = SearchUsersViewModel.SearchUsers.Result::class.java

        `when`(interactor.getUsersFromQuery(anyString(), anyInt()))
            .thenReturn(createSingleWithListUserDomainModel())

        searchUsersViewModel.loadUsersFromQuery(createFlowableWithSearchQuery())

        verify(usersSearchObserver).onChanged(resultLoading)
        verify(usersSearchObserver).onChanged(any(result))

        val usersSearchActual = searchUsersViewModel.usersSearch.value

        if (usersSearchActual is SearchUsersViewModel.SearchUsers.Result) {
            if (usersSearchActual.data.isEmpty()) {
                fail("usersSearchActual: List в Result не может быть пустым")
            }
        }
    }

    @Test
    fun `loading users from query with empty result`() {
        val resultLoading = SearchUsersViewModel.SearchUsers.Loading
        val resultEmpty = SearchUsersViewModel.SearchUsers.Empty

        `when`(interactor.getUsersFromQuery(anyString(), anyInt()))
            .thenReturn(createSingleEmptyWithListUserDomainModel())

        searchUsersViewModel.loadUsersFromQuery(createFlowableWithSearchQuery())

        verify(usersSearchObserver).onChanged(resultLoading)
        verify(usersSearchObserver).onChanged(resultEmpty)
    }

    @Test
    fun `error loading users from query`() {
        val throwable = Throwable("Error loading users")
        val resultLoading = SearchUsersViewModel.SearchUsers.Loading
        val resultError = SearchUsersViewModel.SearchUsers.Error(throwable.message, throwable)

        `when`(interactor.getUsersFromQuery(anyString(), anyInt()))
            .thenReturn(Single.error(throwable))

        searchUsersViewModel.loadUsersFromQuery(createFlowableWithSearchQuery())

        verify(usersSearchObserver).onChanged(resultLoading)
        verify(usersSearchObserver).onChanged(refEq(resultError))
    }


    @Test
    fun `loading next page users from query with result`() {
        val resultLoading = SearchUsersViewModel.SearchUsers.Loading
        val result = SearchUsersViewModel.SearchUsers.Result::class.java

        `when`(interactor.getUsersFromQuery(anyString(), anyInt()))
            .thenReturn(createSingleWithListUserDomainModel())

        searchUsersViewModel.loadUsersFromQuery(createFlowableWithSearchQuery())
        searchUsersViewModel.loadUsersNextFromQuery("query", 2)

        verify(usersSearchObserver).onChanged(resultLoading)
        verify(usersSearchObserver, times(2)).onChanged(any(result))

        val usersSearchActual = searchUsersViewModel.usersSearch.value

        if (usersSearchActual is SearchUsersViewModel.SearchUsers.Result) {
            if (usersSearchActual.data.size < 2) {
                fail("usersSearchActual: List в Result содержит меньше элементов, чем должно быть")
            }
            if (usersSearchActual.data.isEmpty()) {
                fail("usersSearchActual: List в Result не может быть пустым")
            }
        }
    }

    @Test
    fun `loading next page users from query with result empty`() {
        val resultLoading = SearchUsersViewModel.SearchUsers.Loading
        val result = SearchUsersViewModel.SearchUsers.Result::class.java

        `when`(interactor.getUsersFromQuery(anyString(), anyInt()))
            .thenReturn(createSingleWithListUserDomainModel())

        searchUsersViewModel.loadUsersFromQuery(createFlowableWithSearchQuery())

        `when`(interactor.getUsersFromQuery(anyString(), anyInt()))
            .thenReturn(createSingleEmptyWithListUserDomainModel())

        searchUsersViewModel.loadUsersNextFromQuery("query", 2)

        verify(usersSearchObserver).onChanged(resultLoading)
        verify(usersSearchObserver, times(2)).onChanged(any(result))

        val usersSearchActual = searchUsersViewModel.usersSearch.value

        if (usersSearchActual is SearchUsersViewModel.SearchUsers.Result) {
            if (usersSearchActual.data.size > 1) {
                fail("usersSearchActual: Список в Result содержит больше элементов, чем должно быть")
            }
            if (usersSearchActual.data.isEmpty()) {
                fail("usersSearchActual: List в Result не может быть пустым")
            }
        }
    }

    @Test
    fun `error loading next page users from query`() {
        val throwable = Throwable("Error loading next page users")
        val resultLoading = SearchUsersViewModel.SearchUsers.Loading
        val resultError = SearchUsersViewModel.SearchUsers.Error(throwable.message, throwable)

        `when`(interactor.getUsersFromQuery(anyString(), anyInt()))
            .thenReturn(createSingleWithListUserDomainModel())

        searchUsersViewModel.loadUsersFromQuery(createFlowableWithSearchQuery())

        `when`(interactor.getUsersFromQuery(anyString(), anyInt()))
            .thenReturn(Single.error(throwable))

        searchUsersViewModel.loadUsersNextFromQuery("query", 2)

        verify(usersSearchObserver).onChanged(resultLoading)
        verify(usersSearchObserver).onChanged(refEq(resultError))
    }

    private fun createSingleEmptyWithListUserDomainModel(): Single<List<UserDomainModel>> {
        return Single.just(emptyList())
    }

    private fun createSingleWithListUserDomainModel(): Single<List<UserDomainModel>> {
        return Single.just(createListUserDomainModel())
    }

    private fun createListUserDomainModel(): List<UserDomainModel> {
        return listOf(createUserDomainModel())
    }

    private fun createUserDomainModel(): UserDomainModel {
        return UserDomainModel(1, "query", "url photo")
    }

    private fun createFlowableWithSearchQuery(): Flowable<String> {
        return Flowable.just("query")
    }

    private inline fun <reified T> mock() = mock(T::class.java)
}