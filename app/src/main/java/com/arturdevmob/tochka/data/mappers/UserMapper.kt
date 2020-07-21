package com.arturdevmob.tochka.data.mappers

import com.arturdevmob.tochka.data.models.UserDataModel
import com.arturdevmob.tochka.domain.models.UserDomainModel

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