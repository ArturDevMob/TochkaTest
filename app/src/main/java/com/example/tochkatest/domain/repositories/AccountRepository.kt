package com.example.tochkatest.domain.repositories

import com.example.tochkatest.domain.models.AccountDomainModel
import io.reactivex.Completable
import io.reactivex.Single

interface AccountRepository {
    fun saveAccountInfo(account: AccountDomainModel): Completable
    fun removeAccountInfo(): Completable
    fun getAccountInfo(): Single<AccountDomainModel>
}