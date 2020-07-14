package com.example.tochkatest.domain.repositories

import com.example.tochkatest.domain.models.UserDomainModel
import io.reactivex.Single

interface UserRepository {
    fun getUsersFromQuery(query: String, page: Int): Single<List<UserDomainModel>>
}