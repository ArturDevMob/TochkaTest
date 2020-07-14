package com.example.tochkatest.domain.interactors

import com.example.tochkatest.domain.repositories.AccountRepository
import io.reactivex.Completable

class LogoutInteractor(private val accountRepository: AccountRepository) : BaseInteractor() {
    fun logoutAccount(): Completable {
        return accountRepository.removeAccountInfo()
    }
}