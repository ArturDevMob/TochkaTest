package com.arturdevmob.tochka.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.arturdevmob.tochka.R
import com.arturdevmob.tochka.di.logout.LogoutComponent
import com.arturdevmob.tochka.di.logout.LogoutModule
import com.arturdevmob.tochka.presentation.App
import com.arturdevmob.tochka.presentation.viewmodels.LogoutViewModel
import javax.inject.Inject

class LogoutFragment : BaseFragment() {
    private var component: LogoutComponent? = null
    @Inject
    lateinit var viewModel: LogoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onDetach() {
        super.onDetach()
        component = null
    }

    override fun setDi() {
        component = App.appComponent
            .createLogoutComponent(LogoutModule(this))
        component?.inject(this)
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
                    signOutAccountGoogle()
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

    private fun signOutAccountGoogle() {
        googleSignInHelper.signOutAccountGoogle()
    }
}