package com.assistant.libraries.data.repositories

import com.assistant.libraries.domain.repositories.BookDomainRepositories
import com.assistant.libraries.graphql.BooksQuery

class BookDataRepositories : BookDomainRepositories {
    override suspend fun getQueryBook(): Result<BooksQuery.Book> {
        TODO("Not yet implemented")
    }
}
