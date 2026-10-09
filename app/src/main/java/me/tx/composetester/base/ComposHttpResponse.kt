package me.tx.composetester.base

data class ComposHttpResponse<T>(
    val data: T? = null,
    val code: Int = 0,
    val message: String = ""
)
