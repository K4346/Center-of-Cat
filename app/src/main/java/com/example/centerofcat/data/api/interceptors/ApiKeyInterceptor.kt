package com.example.centerofcat.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor() : Interceptor {
    private val apiKey: String = "2d63512c-1c5f-496b-8250-71b91514da66"
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        original = original.newBuilder().addHeader("x-api-key", apiKey).build()
        return chain.proceed(original)
    }
}