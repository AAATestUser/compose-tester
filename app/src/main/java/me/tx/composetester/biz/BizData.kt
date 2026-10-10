package me.tx.composetester.biz

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String = ""
)

data class LogoutResponse(
    val result: Boolean = false
)

data class User(
    val name: String = "",
    val avatar: String = "",
    val age: Int = 0,
    val phone: String = ""
)