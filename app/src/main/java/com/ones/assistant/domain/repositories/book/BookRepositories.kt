package com.ones.assistant.domain.repositories.book

import com.ones.assistant.domain.model.book.BooksDomainModel

interface BookRepositories {
    suspend fun getBooks(): Result<List<BooksDomainModel>>
    suspend fun getBookDetail(documentId: String): Result<BooksDomainModel>
}