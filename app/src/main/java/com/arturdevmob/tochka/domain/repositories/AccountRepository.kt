package com.arturdevmob.tochka.domain.repositories

import com.arturdevmob.tochka.domain.models.AccountDomainModel
import io.reactivex.Completable
import io.reactivex.Single

interface AccountRepository {
    fun saveAccountInfo(account: AccountDomainModel): Completable
    fun removeAccountInfo(): Completable
    fun getAccountInfo(): Single<AccountDomainModel>
}