package com.assistant.libraries.data.repositories

import com.apollographql.apollo.ApolloClient
import com.assistant.libraries.domain.repositories.BookRepo
import com.assistant.libraries.graphql.BooksQuery

class BookRepoImplement(private val  apolloClient: ApolloClient) : BookRepo {
    override suspend fun getBooks(): Result<BooksQuery.Data> {
        return try {
            val response = apolloClient.query(BooksQuery()).execute()
            response.data?.let {
                Result.success(it)
                } ?: run {
                Result.failure(response.exception!!)
            }

        }catch (e: Exception){
            Result.failure(e)
        }
    }
}