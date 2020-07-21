package com.arturdevmob.tochka.domain.interactors

import com.arturdevmob.tochka.domain.repositories.AccountRepository
import io.reactivex.Completable

class LogoutInteractor(private val accountRepository: AccountRepository) : BaseInteractor() {
    fun logoutAccount(): Completable {
        return accountRepository.removeAccountInfo()
    }
}