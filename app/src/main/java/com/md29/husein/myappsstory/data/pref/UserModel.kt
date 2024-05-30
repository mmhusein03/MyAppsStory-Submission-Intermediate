package com.md29.husein.myappsstory.data.pref

data class UserModel(
    val token: String,
    val userId: String,
    val name: String,
    val isLogin: Boolean = false
)