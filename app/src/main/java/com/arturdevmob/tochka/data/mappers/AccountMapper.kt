package com.arturdevmob.tochka.data.mappers

import com.arturdevmob.tochka.domain.models.AccountDomainModel

class AccountMapper {
    companion object {
        fun toDomainModel(name: String?, email: String?, photoUri: String?): AccountDomainModel {
            return AccountDomainModel(name, email, photoUri)
        }
    }
}