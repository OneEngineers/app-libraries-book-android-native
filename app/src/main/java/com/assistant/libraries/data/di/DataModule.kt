package com.assistant.libraries.data.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.assistant.libraries.data.repositories.BookRepoImplement
import com.assistant.libraries.domain.repositories.BookRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Singleton
    @Provides
    fun provideApolloClient(): ApolloClient {
        val okHttpClient = OkHttpClient.Builder().build()
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://book-lms.itedev.online/graphql")
            .okHttpClient(okHttpClient)
            .build()
        return apolloClient
    }

    @Provides
    fun provideBookRepo(apolloClient: ApolloClient): BookRepo {
        return BookRepoImplement(apolloClient)
    }
}