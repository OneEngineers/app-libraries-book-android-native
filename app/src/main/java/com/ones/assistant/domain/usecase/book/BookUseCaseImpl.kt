package com.ones.assistant.domain.usecase.book

import com.ones.assistant.data.repository.BookRepository
import com.ones.assistant.domain.model.book.BooksDomainModel
import com.ones.assistant.domain.repositories.book.BookRepositories
import javax.inject.Inject

class GetBooksUseCaseImpl @Inject constructor(
    private val bookRepository: BookRepositories
) : GetBooksUseCase {

    override suspend fun invoke(): Result<List<BooksDomainModel>> {
        return bookRepository.getBooks()
    }
}

class GetBookDetailUseCaseImpl @Inject constructor(
    private val bookRepository: BookRepositories
) : GetBookDetailUseCase {

    override suspend fun invoke(documentId: String): Result<BooksDomainModel> {
        return bookRepository.getBookDetail(documentId)
    }
}