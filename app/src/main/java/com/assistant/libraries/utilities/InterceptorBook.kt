package com.assistant.libraries.utilities

import com.apollographql.apollo.ApolloClient

object InterceptorBook {
    val bookClient = ApolloClient.Builder()
        .serverUrl("https://book-lms.itedev.online/graphql")
        .build()

}