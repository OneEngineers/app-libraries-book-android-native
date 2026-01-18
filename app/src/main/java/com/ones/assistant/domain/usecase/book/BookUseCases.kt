package com.ones.assistant.domain.usecase.book

import com.ones.assistant.domain.model.book.BooksDomainModel

interface GetBooksUseCase {
    suspend operator fun invoke(): Result<List<BooksDomainModel>>
}

interface GetBookDetailUseCase {
    suspend operator fun invoke(documentId: String): Result<BooksDomainModel>
}