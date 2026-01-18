package com.ones.assistant.data.mapper.book

import com.ones.assistant.domain.model.book.BooksDomainModel
import com.ones.assistant.domain.model.book.BookCover
import com.ones.assistant.domain.model.book.Publisher
import com.ones.assistant.domain.model.book.Category
import com.ones.assistant.graphql.GetBooksQuery
import com.ones.assistant.graphql.BookLocalQuery
import javax.inject.Inject

class BookMapper @Inject constructor() {

    fun mapToDomain(graphqlBook: GetBooksQuery.BookLocal): BooksDomainModel {
        return BooksDomainModel(
            documentId = graphqlBook.documentId ?: "",
            title = graphqlBook.title ?: "",
            description = graphqlBook.description ?: "",
            ISBN = (graphqlBook.ISBN as? String) ?: "",
            page = (graphqlBook.page as? String) ?: "",
            release = (graphqlBook.release as? String) ?: "",
            language = graphqlBook.language?.name ?: "",
            book_cover = BookCover(url = graphqlBook.book_cover?.url ?: ""),
            publisher = Publisher(publisher_name = graphqlBook.publisher?.publisher_name ?: ""),
            categories = graphqlBook.categories?.map { Category(title = it?.title ?: "") } ?: emptyList()
        )
    }

    fun mapToDomain(graphqlBook: BookLocalQuery.BookLocal): BooksDomainModel {
        return BooksDomainModel(
            documentId = graphqlBook.documentId ?: "",
            title = graphqlBook.title ?: "",
            description = graphqlBook.description ?: "",
            ISBN = (graphqlBook.ISBN as? String) ?: "",
            page = (graphqlBook.page as? String) ?: "",
            release = (graphqlBook.release as? String) ?: "",
            language = graphqlBook.language?.name ?: "",
            book_cover = BookCover(url = graphqlBook.book_cover?.url ?: ""),
            publisher = Publisher(publisher_name = graphqlBook.publisher?.publisher_name ?: ""),
            categories = graphqlBook.categories?.map { Category(title = it?.title ?: "") } ?: emptyList()
        )
    }

    fun mapToDomainList(graphqlBooks: List<GetBooksQuery.BookLocal>): List<BooksDomainModel> {
        return graphqlBooks.map { mapToDomain(it) }
    }
}