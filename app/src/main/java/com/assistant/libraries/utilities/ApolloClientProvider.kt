package com.assistant.libraries.utilities

import com.apollographql.apollo.ApolloClient

object ApolloClientProvider {
    val apolloClient = ApolloClient.Builder()
        .serverUrl("https://book-lms.itedev.online/graphql")
        .build()
}