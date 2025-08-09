package com.assistant.libraries

import com.apollographql.apollo.ApolloClient

object ApolloClientProvider {
    val apolloClient = ApolloClient.Builder()
        .serverUrl("https://book-lms.itedev.online/graphql")
        .build()
}