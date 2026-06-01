package com.ones.assistant.utilities

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

private const val AUTH_GRAPHQL_URL = "https://sso-mobile.domrey.online/graphql"

private fun normalizeBearerToken(rawToken: String): String {
    val trimmed = rawToken.trim()
    return if (trimmed.startsWith("Bearer ", ignoreCase = true)) {
        trimmed.removePrefix("Bearer ").removePrefix("bearer ").trim()
    } else {
        trimmed
    }
}

private class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val safeToken = normalizeBearerToken(token)
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $safeToken")
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(newRequest)
    }
}

fun apolloClientAuth(token: String? = null): ApolloClient {
    val builder = ApolloClient.Builder()
        .serverUrl(AUTH_GRAPHQL_URL)

    if (!token.isNullOrBlank()) {
        val okHttp = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .build()
        builder.okHttpClient(okHttp)
    }

    return builder.build()
}

// Backward compatible client (no auth header)
val apolloClientAuth = apolloClientAuth(token = null)
