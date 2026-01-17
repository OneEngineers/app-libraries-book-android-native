package com.ones.assistant.domain.repositories.book

import com.ones.assistant.graphql.BookLocalQuery
import com.ones.assistant.graphql.GetBooksQuery

interface BookRepositories {
    suspend fun getBooks(): Result<GetBooksQuery.Data>
    suspend fun getBookDetail(documentId: String): Result<BookLocalQuery.Data>
}