package com.arturdevmob.tochka.presentation.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.arturdevmob.tochka.R
import com.arturdevmob.tochka.di.auth.AuthComponent
import com.arturdevmob.tochka.di.auth.AuthModule
import com.arturdevmob.tochka.presentation.App
import com.arturdevmob.tochka.presentation.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.fragment_auth.*
import javax.inject.Inject

class AuthFragment : BaseFragment() {
    private var component: AuthComponent? = null
    @Inject
    lateinit var viewModel: AuthViewModel
    lateinit var googleSignInOptions: GoogleSignInOptions
    lateinit var googleApiClient: GoogleApiClient

    companion object {
        const val SIGN_GOOGLE_REQUEST = 111
    }

    override fun onStop() {
        super.onStop()

        googleApiClient.stopAutoManage(getSingleActivity());
        googleApiClient.disconnect();
    }

    override fun onDetach() {
        super.onDetach()
        component = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        googleSignInit()

        sign_in_button.setOnClickListener {
            signInGoogle()
        }
    }

    // Обработка результата google авторизации
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SIGN_GOOGLE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

                if (result.isSuccess) {
                    result.signInAccount?.let {
                        saveAccountInfo(it)
                    }
                }
            } else {
                showMessage(resources.getString(R.string.login_not_done))
            }
        }
    }

    override fun setDi() {
        component = App.appComponent
            .createAuthComponent(AuthModule(this))
        component?.inject(this)
    }

    override fun setToolbar() {
        getSingleActivity().drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        getSingleActivity().supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun setViewModel() {
        viewModel.init()

        setAutStateObserver()
    }

    // Подписка на состояние авторизации пользователя
    private fun setAutStateObserver() {
        viewModel.authState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is AuthViewModel.AuthState.Authorized -> {
                    getSingleActivity().startSearchUsersFragment()
                }
                is AuthViewModel.AuthState.NotAuthorized -> {
                    showMessage(R.string.sign_in)
                }
                is AuthViewModel.AuthState.Error -> {
                    it.message?.let { message ->
                        showMessage(message)
                    }
                }
            }
        })
    }

    // Запуск авторизации через google
    private fun signInGoogle() {
        val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

        startActivityForResult(intent, SIGN_GOOGLE_REQUEST)
    }

    // Иницилизация компонентов для авторизации
    private fun googleSignInit() {
        googleSignInOptions = createGoogleSignInOptions()
        googleApiClient = createGoogleApiClient()
    }

    private fun createGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }

    private fun createGoogleApiClient(): GoogleApiClient {
        return GoogleApiClient.Builder(requireContext())
            .enableAutoManage(getSingleActivity(), createOnConnectionFailedListener())
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build()
    }

    private fun createOnConnectionFailedListener(): GoogleApiClient.OnConnectionFailedListener {
        return GoogleApiClient.OnConnectionFailedListener {
            throw Exception("OnConnectionFailedListener Error")
        }
    }

    // Сохранение информации об аккаунте, полученной после авторизации
    private fun saveAccountInfo(account: GoogleSignInAccount) {
        viewModel.saveAccountInfo(account.displayName, account.email, account.photoUrl.toString())
    }
}