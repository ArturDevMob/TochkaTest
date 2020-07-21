package com.arturdevmob.tochka.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.arturdevmob.tochka.R
import com.arturdevmob.tochka.di.navigation.NavigationModule
import com.arturdevmob.tochka.di.searchusers.SearchUsersComponent
import com.arturdevmob.tochka.di.searchusers.SearchUsersModule
import com.arturdevmob.tochka.domain.models.UserDomainModel
import com.arturdevmob.tochka.presentation.App
import com.arturdevmob.tochka.presentation.adapters.UsersAdapters
import com.arturdevmob.tochka.presentation.utils.RxSearch
import com.arturdevmob.tochka.presentation.utils.UserDiffUtilCallback
import com.arturdevmob.tochka.presentation.viewmodels.SearchUsersViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search_users.*
import javax.inject.Inject

class SearchUsersFragment : NavigationFragment() {
    private var component: SearchUsersComponent? = null
    private var diffResultDisposable: Disposable? = null

    @Inject
    lateinit var viewModel: SearchUsersViewModel

    @Inject
    lateinit var usersAdapters: UsersAdapters
    lateinit var usersSearchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_users, container, false)
    }

    override fun onDetach() {
        super.onDetach()

        diffResultDisposable?.dispose()
        component = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        setRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.search_users, menu)

        usersSearchView = menu.findItem(R.id.search_users_action).actionView as SearchView

        setSearchView(usersSearchView)
        setLastQuerySearchObserver()
    }

    override fun setDi() {
        super.setDi()

        component = App.appComponent
            .createNavigationComponent(NavigationModule(this))
            .createSearchUsersComponent(SearchUsersModule(this))
        component?.inject(this)
    }

    override fun setViewModel() {
        super.setViewModel()

        setUserSearchObserver()
        setLastPageObserver()
    }

    // Устанавливает набл состояния модели с пользователями
    private fun setUserSearchObserver() {
        viewModel.usersSearch.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SearchUsersViewModel.SearchUsers.Result -> {
                    showUsers(it.data)
                }
                is SearchUsersViewModel.SearchUsers.Error -> {
                    showProgressbar(false)
                    showMessageEmptySearchUsers(false)
                    it.message?.let { message -> showMessage(message) }
                }
                is SearchUsersViewModel.SearchUsers.Empty -> {
                    showEmptyUsers()
                }
                is SearchUsersViewModel.SearchUsers.Loading -> {
                    showLoading()
                }
            }
        })
    }

    // Устанавливает наблюдателя за изменениями поискового запроса для поиска пользователей
    // Поисковой запрос сохраняется в viewmodel чтобы пережить поворот
    private fun setLastQuerySearchObserver() {
        viewModel.lastQuery.observe(viewLifecycleOwner, Observer {
            usersSearchView.setQuery(it, false)
        })
    }

    // Устанавливает наблюдателя за изменениями номера страницы с которой получать пользователей
    // Номер страницы сохраняется в viewmodel чтобы пережить поворот
    private fun setLastPageObserver() {
        viewModel.lastPage.observe(viewLifecycleOwner, Observer {
            usersAdapters.currentPage = it
        })
    }

    private fun setSearchView(searchView: SearchView) {
        // Создается поток содержащий поисковой запрос и передается вью-модели
        val flowableQuery = RxSearch.from(searchView)

        searchView.setOnSearchClickListener {
            viewModel.loadUsersFromQuery(flowableQuery)
        }
    }

    private fun setRecyclerView() {
        usersAdapters.callback = object : UsersAdapters.Callback {
            // Адаптер достиг конца списка и просит данные для следующей страницы
            override fun onLoadUsersForNextPage(nextPage: Int) {
                viewModel.loadUsersNextFromQuery(usersSearchView.query.toString(), nextPage)
            }
        }

        search_users_recycler_view.layoutManager = LinearLayoutManager(requireContext())
        search_users_recycler_view.setHasFixedSize(true)
        search_users_recycler_view.adapter = usersAdapters
    }

    // Показывает список пользователей
    // Скрывает прогресс-бар и уведомление о пустом списке пользователей
    private fun showUsers(users: List<UserDomainModel>) {
        showProgressbar(false)
        showMessageEmptySearchUsers(false)
        showSearchUsers(true, users)
    }

    // Показывает уведомление о пустом списке пользователей
    // Скрывает список пользователей и прогресс-бар
    private fun showEmptyUsers() {
        showProgressbar(false)
        showMessageEmptySearchUsers(true)
        showSearchUsers(false)
    }

    // Показывает прогресс-бар
    // Скрывает список пользователей и уведомление о пустом списке пользователей
    private fun showLoading() {
        showProgressbar(true)
        showMessageEmptySearchUsers(false)
        showSearchUsers(false)
    }

    private fun showSearchUsers(show: Boolean, users: List<UserDomainModel> = emptyList()) {
        updateDataUserAdapter(users)
        search_users_recycler_view.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showProgressbar(show: Boolean) {
        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showMessageEmptySearchUsers(show: Boolean) {
        message_empty_search_users_text.visibility = if (show) View.VISIBLE else View.GONE
    }

    // Обновление списка адаптера с пользователями через DiffUtil
    // Работа DiffUtil помещена в фоновый поток, т.к. на больших списках могут появится тормоза
    private fun updateDataUserAdapter(users: List<UserDomainModel>) {
        val userDiffUtilCallback = UserDiffUtilCallback(usersAdapters.users, users)

        diffResultDisposable?.dispose()

        diffResultDisposable = Single.fromCallable { DiffUtil.calculateDiff(userDiffUtilCallback) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.dispatchUpdatesTo(usersAdapters)
                usersAdapters.users = users
            }, {
                it.message?.let { message -> showMessage(message) }
            })
    }
}