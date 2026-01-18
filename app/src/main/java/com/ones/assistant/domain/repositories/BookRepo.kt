package com.ones.assistant.domain.repositories

import com.ones.assistant.graphql.BookLocalQuery
import com.ones.assistant.graphql.GetBooksQuery

interface BookRepo {
    suspend fun getBooks(): Result<GetBooksQuery.Data>
    suspend fun getBookDetail(documentId: String): Result<BookLocalQuery.Data>
}