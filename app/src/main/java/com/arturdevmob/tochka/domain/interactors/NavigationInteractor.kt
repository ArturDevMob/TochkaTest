package com.arturdevmob.tochka.domain.interactors

import com.arturdevmob.tochka.domain.models.AccountDomainModel
import com.arturdevmob.tochka.domain.repositories.AccountRepository
import io.reactivex.Single

class NavigationInteractor(private val accountRepository: AccountRepository) : BaseInteractor() {
    fun getAccountInfo(): Single<AccountDomainModel> {
        return accountRepository.getAccountInfo()
    }
}