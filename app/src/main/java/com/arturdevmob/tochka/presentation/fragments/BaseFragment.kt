package com.arturdevmob.tochka.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.arturdevmob.tochka.presentation.App
import com.arturdevmob.tochka.presentation.SingleActivity
import com.arturdevmob.tochka.presentation.utils.GoogleSignInHelper
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {
    lateinit var googleSignInHelper: GoogleSignInHelper
    abstract fun setDi()
    abstract fun setToolbar()
    abstract fun setViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setDi()
        setToolbar()
        setViewModel()

        googleSignInHelper = App.appComponent.getGoogleSignInHelper()
    }

    override fun onStop() {
        super.onStop()

        googleSignInHelper.disconnect()
    }

    override fun onResume() {
        super.onResume()

        googleSignInHelper.connect()
    }

    protected fun getSingleActivity(): SingleActivity {
        return activity as SingleActivity
    }

    protected fun showMessage(messageResId: Int) {
        view?.let {
            Snackbar.make(it, messageResId, Snackbar.LENGTH_LONG).show()
        }
    }

    protected fun showMessage(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }
}