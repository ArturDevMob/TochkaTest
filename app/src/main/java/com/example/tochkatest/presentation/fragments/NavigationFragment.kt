package com.example.tochkatest.presentation.fragments

import android.net.Uri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.example.tochkatest.R
import com.example.tochkatest.di.navigation.NavigationComponent
import com.example.tochkatest.di.navigation.NavigationModule
import com.example.tochkatest.domain.models.AccountDomainModel
import com.example.tochkatest.presentation.App
import com.example.tochkatest.presentation.viewmodels.NavigationViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject

// Родитель для фрагментов содержащих Navigation Drawer меню
abstract class NavigationFragment : BaseFragment() {
    private var component: NavigationComponent? = null
    @Inject
    lateinit var navigationViewModel: NavigationViewModel

    override fun onDetach() {
        super.onDetach()

        component = null
    }

    override fun setDi() {
        component = App.appComponent
            .createNavigationComponent(NavigationModule(this))
        component?.inject(this)
    }

    override fun setToolbar() {
        getSingleActivity().drawerLayout.setDrawerLockMode(
            DrawerLayout.LOCK_MODE_UNLOCKED
        )
    }

    override fun setViewModel() {
        navigationViewModel.init()
        setAccountInfoObserver()
    }

    // Устанавливает наблюдателя за информацией об авторизовавшимся пользователе
    private fun setAccountInfoObserver() {
        navigationViewModel.accountInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NavigationViewModel.AccountInfo.Result -> {
                    updateUiNavigationDrawer(it.data)
                }
                is NavigationViewModel.AccountInfo.Error -> {
                    it.message?.let { message ->
                        showMessage(message)
                    }
                }
            }
        })
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
}