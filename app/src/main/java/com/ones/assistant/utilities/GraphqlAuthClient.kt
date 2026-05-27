package com.ones.assistant.utilities

import com.apollographql.apollo.ApolloClient

val apolloClientAuth = ApolloClient.Builder()
    .serverUrl("https://sso-mobile.domrey.online/graphql")
    .build()
