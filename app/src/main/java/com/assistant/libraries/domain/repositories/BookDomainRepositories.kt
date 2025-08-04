package com.assistant.libraries.domain.repositories

import com.assistant.libraries.graphql.BooksQuery

interface BookDomainRepositories {
    suspend fun getQueryBook(): Result<BooksQuery.Book>
}
