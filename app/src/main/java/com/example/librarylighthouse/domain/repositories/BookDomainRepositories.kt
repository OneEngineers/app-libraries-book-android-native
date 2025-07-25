package com.example.librarylighthouse.domain.repositories

import com.example.librarylighthouse.graphql.BooksQuery

interface BookDomainRepositories {
    suspend fun getQueryBook(): Result<BooksQuery.Book>
}
