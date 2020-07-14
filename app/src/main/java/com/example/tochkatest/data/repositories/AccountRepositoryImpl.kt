package com.example.tochkatest.data.repositories

import android.content.SharedPreferences
import com.example.tochkatest.data.mappers.AccountMapper
import com.example.tochkatest.domain.models.AccountDomainModel
import com.example.tochkatest.domain.repositories.AccountRepository
import io.reactivex.Completable
import io.reactivex.Single

class AccountRepositoryImpl(private val preferences: SharedPreferences) : AccountRepository {
    companion object {
        const val ACCOUNT_NAME_KEY = "account_name_key"
        const val ACCOUNT_EMAIL_KEY = "account_email_key"
        const val ACCOUNT_PHOTO_URI_KEY = "account_photo_uri_key"
    }

    // Сохраняет информацию об аккаунте авторизовавшегося пользователя
    override fun saveAccountInfo(account: AccountDomainModel): Completable {
        return Completable.fromAction {
            val editor = preferences.edit()

            editor.putString(ACCOUNT_NAME_KEY, account.name)
            editor.putString(ACCOUNT_EMAIL_KEY, account.email)
            editor.putString(ACCOUNT_PHOTO_URI_KEY, account.photoUri.toString())

            editor.apply()
        }
    }

    // Удаляет всю информацию об аккаунте авторизовавшегося пользователя
    // Удаляя информацию об аккаунте, мы разлогиниваем пользователя
    override fun removeAccountInfo(): Completable {
        return Completable.fromAction {
            val editor = preferences.edit()

            editor.remove(ACCOUNT_NAME_KEY)
            editor.remove(ACCOUNT_EMAIL_KEY)
            editor.remove(ACCOUNT_PHOTO_URI_KEY)

            editor.apply()
        }
    }

    // Возвращает модель аккаунта авторизовавшегося пользователя
    // Информация об аккаунте является подтверждением, что пользователь авторизовывался
    override fun getAccountInfo(): Single<AccountDomainModel> {
        return Single.fromCallable {
            AccountMapper.toDomainModel(
                preferences.getString(ACCOUNT_NAME_KEY, null),
                preferences.getString(ACCOUNT_EMAIL_KEY, null),
                preferences.getString(ACCOUNT_PHOTO_URI_KEY, null)
            )
        }
    }
}