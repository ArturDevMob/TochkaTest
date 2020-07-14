package com.example.tochkatest.domain.interactors

import com.example.tochkatest.domain.models.UserDomainModel
import com.example.tochkatest.domain.repositories.UserRepository
import io.reactivex.Single

class SearchUsersInteractor(private val userRepository: UserRepository) : BaseInteractor() {
    fun getUsersFromQuery(query: String, page: Int): Single<List<UserDomainModel>> {
        return userRepository.getUsersFromQuery(query, page)
    }
}