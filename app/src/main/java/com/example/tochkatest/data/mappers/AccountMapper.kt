package com.example.tochkatest.data.mappers

import com.example.tochkatest.domain.models.AccountDomainModel

class AccountMapper {
    companion object {
        fun toDomainModel(name: String?, email: String?, photoUri: String?): AccountDomainModel {
            return AccountDomainModel(name, email, photoUri)
        }
    }
}