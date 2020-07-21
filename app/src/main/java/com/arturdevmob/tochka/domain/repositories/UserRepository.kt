package com.arturdevmob.tochka.domain.repositories

import com.arturdevmob.tochka.domain.models.UserDomainModel
import io.reactivex.Single

interface UserRepository {
    fun getUsersFromQuery(query: String, page: Int): Single<List<UserDomainModel>>
}