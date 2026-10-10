package me.tx.composetester.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Request
import retrofit2.HttpException
import java.io.IOException

object ComposHttpClient {
    private val requestScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val headersLock = Any()
    private val headers = linkedMapOf<String, String>()

    val headerInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        getHeaders().forEach { (name, value) ->
            requestBuilder.header(name, value)
        }
        chain.proceed(requestBuilder.build())
    }

    fun setHeader(name: String, value: String) {
        Request.Builder().header(name, value)
        synchronized(headersLock) {
            val existingName = headers.keys.firstOrNull { it.equals(name, ignoreCase = true) }
            if (existingName != null) headers.remove(existingName)
            headers[name] = value
        }
    }

    fun getHeader(name: String): String? = synchronized(headersLock) {
        headers.entries.firstOrNull { it.key.equals(name, ignoreCase = true) }?.value
    }

    fun getHeaders(): Map<String, String> = synchronized(headersLock) {
        headers.toMap()
    }

    fun <K, T> call(
        request: K,
        endpoint: suspend (K) -> ComposHttpResponse<T>,
        defaultValue: () -> T,
        onResult: (T) -> Unit,
        onFailed: (code: Int, message: String) -> Unit
    ) {
        execute(
            call = { endpoint(request) },
            defaultValue = defaultValue,
            onResult = onResult,
            onFailed = onFailed
        )
    }

    fun <T> call(
        endpoint: suspend () -> ComposHttpResponse<T>,
        defaultValue: () -> T,
        onResult: (T) -> Unit,
        onFailed: (code: Int, message: String) -> Unit
    ) {
        execute(
            call = endpoint,
            defaultValue = defaultValue,
            onResult = onResult,
            onFailed = onFailed
        )
    }

    private fun <T> execute(
        call: suspend () -> ComposHttpResponse<T>,
        defaultValue: () -> T,
        onResult: (T) -> Unit,
        onFailed: (code: Int, message: String) -> Unit
    ) {
        requestScope.launch {
            val response = try {
                call()
            } catch (exception: HttpException) {
                ComposHttpResponse(
                    code = exception.code(),
                    message = exception.message().ifBlank { "HTTP request failed" }
                )
            } catch (exception: IOException) {
                ComposHttpResponse(
                    code = -1,
                    message = "Network connection failed"
                )
            }
            withContext(Dispatchers.Main) {
                if (response.code == 200) {
                    onResult(response.data ?: defaultValue())
                } else {
                    onFailed(response.code, response.message)
                }
            }
        }
    }
}
