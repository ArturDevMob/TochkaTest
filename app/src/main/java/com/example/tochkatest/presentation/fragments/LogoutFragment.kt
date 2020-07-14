package com.example.tochkatest.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.tochkatest.R
import com.example.tochkatest.di.logout.LogoutModule
import com.example.tochkatest.presentation.App
import com.example.tochkatest.presentation.viewmodels.LogoutViewModel
import kotlinx.android.synthetic.main.fragment_search_users.*
import javax.inject.Inject

class LogoutFragment() : BaseFragment() {
    @Inject
    lateinit var viewModel: LogoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun setDi() {
        App.appComponent
            .createLogoutComponent(LogoutModule(this))
            .inject(this)
    }

    override fun setToolbar() {
        // В этом фрагменте toolbar не нужен
    }

    override fun setViewModel() {
        viewModel.init()

        setLogoutStateObserver()
    }

    // Устанавливает наблюдателя за состоянием разлогинивания пользователя
    private fun setLogoutStateObserver() {
        viewModel.logoutState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is LogoutViewModel.LogoutState.Success -> {
                    getSingleActivity().startAuthFragment()
                }
                is LogoutViewModel.LogoutState.Error -> {
                    it.message?.let { message ->
                        showMessage(message)
                    }
                }
            }
        })
    }
}