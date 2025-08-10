package com.assistant.libraries.utilities

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
class PodcastAuthInterceptor(private val token: String): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(PodcastAuthInterceptor("YOUR_JWT_TOKEN"))
    .build()

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://book-lms.itedev.online/graphql")
    .okHttpClient(okHttpClient)
    .build()