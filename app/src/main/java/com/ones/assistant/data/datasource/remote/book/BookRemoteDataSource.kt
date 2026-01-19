package com.ones.assistant.data.datasource.remote.book

import com.ones.assistant.graphql.BookLocalQuery
import com.ones.assistant.graphql.GetBooksQuery
import com.ones.assistant.utilities.apolloClientBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRemoteDataSource @Inject constructor() {

    suspend fun getBooks(): Result<GetBooksQuery.Data> {
        return withContext(Dispatchers.IO) {
            try {
                val query = GetBooksQuery()
                val response = apolloClientBook.query(query).execute()

                if (response.hasErrors()) {
                    Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
                } else {
                    response.data?.let { Result.success(it) }
                        ?: Result.failure(Exception("No data received"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getBookDetail(documentId: String): Result<BookLocalQuery.Data> {
        return withContext(Dispatchers.IO) {
            try {
                val query = BookLocalQuery(documentId)
                val response = apolloClientBook.query(query).execute()
                if (response.hasErrors()) {
                    Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
                } else {
                    response.data?.let { Result.success(it) }
                        ?: Result.failure(Exception("No data received"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
