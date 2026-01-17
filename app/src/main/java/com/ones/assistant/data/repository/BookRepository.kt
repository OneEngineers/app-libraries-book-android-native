package com.ones.assistant.data.repository

import com.ones.assistant.domain.repositories.book.BookRepositories;
import com.ones.assistant.graphql.BookLocalQuery
import com.ones.assistant.graphql.GetBooksQuery
import com.ones.assistant.utilities.apolloClientBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository : BookRepositories {

    override suspend fun getBooks(): Result<GetBooksQuery.Data> {
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

    override suspend fun getBookDetail(documentId: String): Result<BookLocalQuery.Data> {
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
