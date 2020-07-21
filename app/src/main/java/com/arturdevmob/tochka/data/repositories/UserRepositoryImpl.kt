package com.arturdevmob.tochka.data.repositories

import com.arturdevmob.tochka.data.mappers.UserMapper
import com.arturdevmob.tochka.data.network.UserApi
import com.arturdevmob.tochka.domain.models.UserDomainModel
import com.arturdevmob.tochka.domain.repositories.UserRepository
import io.reactivex.Single

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {
    // Получает список пользователей из сети соответствующих поисковому запросу
    // page - номер страницы с которой брать результат
    override fun getUsersFromQuery(query: String, page: Int): Single<List<UserDomainModel>> {
        return userApi.getUsersFromQuery(query, page)
            .map { users -> users.users.map { UserMapper.toDomainModels(it) } }
    }
}