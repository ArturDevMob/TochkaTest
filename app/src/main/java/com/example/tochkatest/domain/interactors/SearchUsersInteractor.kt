package com.example.tochkatest.domain.interactors

import com.example.tochkatest.domain.models.AccountDomainModel
import com.example.tochkatest.domain.models.UserDomainModel
import com.example.tochkatest.domain.repositories.AccountRepository
import com.example.tochkatest.domain.repositories.UserRepository
import io.reactivex.Single

class SearchUsersInteractor(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) : BaseInteractor() {
    fun getUsersFromQuery(query: String, page: Int): Single<List<UserDomainModel>> {
        return userRepository.getUsersFromQuery(query, page)
    }

    fun getAccountInfo(): Single<AccountDomainModel> {
        return accountRepository.getAccountInfo()
    }
}