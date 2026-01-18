package com.ones.assistant.data.repository

import com.ones.assistant.data.datasource.remote.book.BookRemoteDataSource
import com.ones.assistant.data.mapper.book.BookMapper
import com.ones.assistant.domain.model.book.BooksDomainModel
import com.ones.assistant.domain.repositories.book.BookRepositories
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookRemoteDataSource: BookRemoteDataSource,
    private val bookMapper: BookMapper
) : BookRepositories {

    override suspend fun getBooks(): Result<List<BooksDomainModel>> {
        return bookRemoteDataSource.getBooks().mapCatching { data ->
            data.bookLocals?.let { bookMapper.mapToDomainList(it.filterNotNull()) } ?: emptyList()
        }
    }

    override suspend fun getBookDetail(documentId: String): Result<BooksDomainModel> {
        return bookRemoteDataSource.getBookDetail(documentId).mapCatching { data ->
            data.bookLocal?.let { bookMapper.mapToDomain(it) }
                ?: throw Exception("Book not found")
        }
    }
}
