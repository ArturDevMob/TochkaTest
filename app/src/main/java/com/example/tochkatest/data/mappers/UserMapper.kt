package com.example.tochkatest.data.mappers

import com.example.tochkatest.data.models.UserDataModel
import com.example.tochkatest.domain.models.UserDomainModel

class UserMapper {
    companion object {
        fun toDomainModels(model: UserDataModel): UserDomainModel {
            return UserDomainModel(
                model.id,
                model.login,
                model.avatarUrl
            )
        }
    }
}