package com.example.tochkatest.data.repositories

import com.example.tochkatest.data.mappers.UserMapper
import com.example.tochkatest.data.network.UserApi
import com.example.tochkatest.domain.models.UserDomainModel
import com.example.tochkatest.domain.repositories.UserRepository
import io.reactivex.Single

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {
    // Получает список пользователей из сети соответствующих поисковому запросу
    // page - номер страницы с которой брать результат
    override fun getUsersFromQuery(query: String, page: Int): Single<List<UserDomainModel>> {
        return userApi.getUsersFromQuery(query, page)
            .map { users -> users.users.map { UserMapper.toDomainModels(it) } }
    }
}