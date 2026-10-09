package me.tx.composetester.base

import me.tx.composetester.biz.BizApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    // Replace this with your backend's HTTPS base URL; it must end with a slash.
    private const val BASE_URL = "https://api.example.com/"

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(ComposHttpClient.headerInterceptor)
        .build()

    val bizApi: BizApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BizApi::class.java)
}