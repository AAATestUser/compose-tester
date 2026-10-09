package me.tx.composetester.biz

import me.tx.composetester.base.ComposHttpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface BizApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): ComposHttpResponse<LoginResponse>

    @POST("api/auth/logout")
    suspend fun logout(): ComposHttpResponse<LogoutResponse>
}