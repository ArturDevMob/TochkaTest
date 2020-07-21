package com.arturdevmob.tochka.data.network

import com.arturdevmob.tochka.data.models.UserListDataModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {
    companion object {
        private const val DEFAULT_AMOUNT_USERS = 30
    }

    @GET("/search/users")
    fun getUsersFromQuery(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") count: Int = DEFAULT_AMOUNT_USERS
    ): Single<UserListDataModel>
}