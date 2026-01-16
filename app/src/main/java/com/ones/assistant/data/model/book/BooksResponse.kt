package com.ones.assistant.data.model.book

data class BooksResponse(
    val data: BookDataModel
)
data class BookDataModel(
    val books: List<BookDataModel>
)