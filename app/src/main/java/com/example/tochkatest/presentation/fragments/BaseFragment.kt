package com.example.tochkatest.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tochkatest.presentation.SingleActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {
    abstract fun setDi()
    abstract fun setToolbar()
    abstract fun setViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setDi()
        setToolbar()
        setViewModel()
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