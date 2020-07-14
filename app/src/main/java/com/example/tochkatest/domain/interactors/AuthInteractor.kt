package com.example.tochkatest.domain.interactors

import com.example.tochkatest.domain.models.AccountDomainModel
import com.example.tochkatest.domain.repositories.AccountRepository
import io.reactivex.Completable
import io.reactivex.Single

class AuthInteractor(private val accountRepository: AccountRepository) : BaseInteractor() {
    fun saveAccountInfo(account: AccountDomainModel): Completable {
        return accountRepository.saveAccountInfo(account)
    }

    fun getAccountInfo(): Single<AccountDomainModel> {
        return accountRepository.getAccountInfo()
    }
}