package com.arturdevmob.tochka.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserListDataModel(
    @SerializedName("items")
    @Expose
    val users: List<UserDataModel>
)