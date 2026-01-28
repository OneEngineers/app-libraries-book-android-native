package com.ones.assistant.utilities

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class PodcastAuthInterceptor(private val token: String): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(newRequest)
    }
}
const val graphql_token = "c7c913a53356c3ac2260098cdfe607c889542cdd0f205788b0bf4da5812070e03358b1f63d403418d2dffb7ae706f1da9c332f2dd2274d95179a1341b5e7724466055d12e751b5f74f5c32770985202644469bb7a7c6f49c4039ffedbf3ec900254dbf632e717e4933ab17fcc81e05ee4d9fabaeb724c0ccb483acf5bb66b7ef"
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(PodcastAuthInterceptor(graphql_token))
    .build()

val apolloClientBook = ApolloClient.Builder()
    .serverUrl("https://book-lms.itedev.online/graphql")
    .okHttpClient(okHttpClient)
    .build()