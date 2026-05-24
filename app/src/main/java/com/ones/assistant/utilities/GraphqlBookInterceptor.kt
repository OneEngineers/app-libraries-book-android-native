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
const val graphql_token = "a97170d27afc45687a5fa7980bdb706f2175c2093ecc0ca17c98f81a8c318848590bc59f24def0b482ea24d76039b352e3965486db5d6bc467f49050f1d58dddc8ed74f7aa50cd0b9817463c8557686626fb0f454aa8ee1c262cafd503165e7d0e012d1d00d8a210c15f2d82aa050a988dcfb8736fecdb9294790ba05e3810f5"
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(PodcastAuthInterceptor(graphql_token))
    .build()

val apolloClientBook = ApolloClient.Builder()
    .serverUrl("https://book-lms.domrey.online/graphql")
    .okHttpClient(okHttpClient)
    .build()