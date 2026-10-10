package me.tx.composetester.biz

import me.tx.composetester.base.ApiClient
import me.tx.composetester.base.ComposHttpClient

object BizRepository {
    fun login(
        loginRequest: LoginRequest,
        onResult: (LoginResponse) -> Unit,
        onFailed: (code: Int, message: String) -> Unit
    ) {
        ComposHttpClient.call(
            request = loginRequest,
            endpoint = ApiClient.bizApi::login,
            defaultValue = ::LoginResponse,
            onResult = onResult,
            onFailed = onFailed
        )
    }

    fun logout(
        onResult: (LogoutResponse) -> Unit,
        onFailed: (code: Int, message: String) -> Unit
    ) {
        ComposHttpClient.call(
            endpoint = ApiClient.bizApi::logout,
            defaultValue = ::LogoutResponse,
            onResult = onResult,
            onFailed = onFailed
        )
    }

    fun userList(
        page: Int,
        size: Int = 20,
        onResult: (List<User>) -> Unit,
        onFailed: (code: Int, message: String) -> Unit
    ) {
        ComposHttpClient.call(
            request = page to size,
            endpoint = { (pageNumber, pageSize) ->
                ApiClient.bizApi.userList(pageNumber, pageSize)
            },
            defaultValue = { emptyList() },
            onResult = onResult,
            onFailed = onFailed
        )
    }
}