package com.example.tochkatest.domain.interactors

import com.example.tochkatest.domain.models.AccountDomainModel
import com.example.tochkatest.domain.repositories.AccountRepository
import io.reactivex.Single

class NavigationInteractor(private val accountRepository: AccountRepository) : BaseInteractor() {
    fun getAccountInfo(): Single<AccountDomainModel> {
        return accountRepository.getAccountInfo()
    }
}