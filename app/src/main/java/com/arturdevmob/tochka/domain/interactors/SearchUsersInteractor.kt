package com.arturdevmob.tochka.domain.interactors

import com.arturdevmob.tochka.domain.models.UserDomainModel
import com.arturdevmob.tochka.domain.repositories.UserRepository
import io.reactivex.Single

class SearchUsersInteractor(private val userRepository: UserRepository) : BaseInteractor() {
    fun getUsersFromQuery(query: String, page: Int): Single<List<UserDomainModel>> {
        return userRepository.getUsersFromQuery(query, page)
    }
}