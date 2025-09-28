package com.ones.assistant.utilities

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class BookAuthInterceptor(private val token: String): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
val ReportHttpClient = OkHttpClient.Builder()
    .addInterceptor(BookAuthInterceptor("YOUR_JWT_TOKEN"))
    .build()

val reportClient = ApolloClient.Builder()
    .serverUrl("https://report-lms.itedev.online/graphql")
    .okHttpClient(ReportHttpClient)
    .build()