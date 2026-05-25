package com.ones.assistant.utilities

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import okhttp3.OkHttpClient

private const val PODCAST_GRAPHQL_TOKEN =
    "155571071897d47c0a74ac7a1a34e09649ea8155931dda1ddd5d8f03bd4220cc89bc17ac309ea10745cbfd40c0bf0ad066f20989b7f42f3992138691c952da08159a2760c01313a2f8e3ae215bf86a68cfe3a2b45178ae02fb8673a9870ab746a1fcae9268c21d5d6eab5bf0f83410c3cac4930b3c9434e580eaafb2bf2babf0"

private val podcastOkHttpClient = OkHttpClient.Builder()
    .addInterceptor(PodcastAuthInterceptor(PODCAST_GRAPHQL_TOKEN))
    .build()

val apolloClientPodcast = ApolloClient.Builder()
    .serverUrl("https://podcast-cms.domrey.online/graphql")
    .okHttpClient(podcastOkHttpClient)
    .build()
