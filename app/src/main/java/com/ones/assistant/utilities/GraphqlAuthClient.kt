package com.ones.assistant.utilities

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.apollographql.apollo.api.DefaultUpload
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getToken()
        val request = chain.request().newBuilder()
        if (token != null) {
            request.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}

fun createApolloClientAuth(tokenManager: TokenManager): ApolloClient {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenManager))
        .build()

    return ApolloClient.Builder()
        .serverUrl("https://sso-mobile.domrey.online/graphql")
        .okHttpClient(okHttpClient)
        .build()
}

// Keeping this for backward compatibility if needed, but it won't have the interceptor
val apolloClientAuth = ApolloClient.Builder()
    .serverUrl("https://sso-mobile.domrey.online/graphql")
    .build()
