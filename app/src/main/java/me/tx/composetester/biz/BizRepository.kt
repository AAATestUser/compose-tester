package me.tx.composetester.biz

import me.tx.composetester.base.ApiClient
import me.tx.composetester.base.ComposHttpClient

object BizRepository {
    fun login(loginRequest: LoginRequest, onResult: (LoginResponse) -> Unit) {
        ComposHttpClient.call(
            request = loginRequest,
            endpoint = ApiClient.bizApi::login,
            defaultValue = ::LoginResponse,
            onResult = onResult
        )
    }

    fun logout(onResult: (LogoutResponse) -> Unit) {
        ComposHttpClient.request(
            call = ApiClient.bizApi::logout,
            onResult = { response ->
                onResult(response.data ?: LogoutResponse())
            }
        )
    }
}