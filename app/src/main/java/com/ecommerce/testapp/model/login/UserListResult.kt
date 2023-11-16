package com.ecommerce.testapp

import com.google.gson.annotations.SerializedName

data class UserListResult(val userList: List<UserList>? = null)

data class UserList(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("userName") val userName: String? = null,
    @SerializedName("password")  val password: String? = null,
    @SerializedName("role")  val role: String? = null
)