package com.example.tochkatest.presentation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tochkatest.R
import com.example.tochkatest.di.searchusers.SearchUsersModule
import com.example.tochkatest.domain.models.AccountDomainModel
import com.example.tochkatest.domain.models.UserDomainModel
import com.example.tochkatest.presentation.App
import com.example.tochkatest.presentation.adapters.UsersAdapters
import com.example.tochkatest.presentation.utils.RxSearch
import com.example.tochkatest.presentation.viewmodels.SearchUsersViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_search_users.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject

class SearchUsersFragment : BaseFragment() {
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
        App.appComponent
            .createSearchUsersComponent(SearchUsersModule(this))
            .inject(this)
    }

    override fun setToolbar() {
        getSingleActivity().drawerLayout.setDrawerLockMode(
            DrawerLayout.LOCK_MODE_UNLOCKED
        )
    }

    override fun setViewModel() {
        viewModel.init()

        setUserSearchObserver()
        setAccountInfoObserver()
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

    // Устанавливает наблюдателя за информацией об авторизовавшимся пользователе
    private fun setAccountInfoObserver() {
        viewModel.accountInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SearchUsersViewModel.AccountInfo.Result -> {
                    updateUiNavigationDrawer(it.data)
                }
                is SearchUsersViewModel.AccountInfo.Error -> {
                    it.message?.let { message ->
                        showMessage(message)
                    }
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

    // Обновляет боковую панель информацией об аккаунте авторизовавшегося пользователя
    private fun updateUiNavigationDrawer(account: AccountDomainModel) {
        val navigationView = getSingleActivity().navigationView.getHeaderView(0)

        with(navigationView) {
            user_login_text.text =
                account.name ?: resources.getString(R.string.account_info_no_name)
            user_email_text.text =
                account.email ?: resources.getString(R.string.account_info_no_email)

            Picasso.get()
                .load(Uri.parse(account.photoUri))
                .error(R.drawable.ic_no_avatar)
                .into(user_avatar_image)
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
        search_users_recycler_view.visibility = if (show) View.VISIBLE else View.GONE
        usersAdapters.users = users.toMutableList()
    }

    private fun showProgressbar(show: Boolean) {
        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showMessageEmptySearchUsers(show: Boolean) {
        message_empty_search_users_text.visibility = if (show) View.VISIBLE else View.GONE
    }
}