package com.assistant.libraries.domain.repositories

import com.assistant.libraries.graphql.BooksQuery
interface BookRepo {
    suspend fun getBooks(): Result<BooksQuery.Data>
}