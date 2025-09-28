package com.ones.assistant.domain.repositories

import com.ones.assistant.graphql.BooksQuery
interface BookRepo {
    suspend fun getBooks(): Result<BooksQuery.Data>
}