package com.ones.assistant.domain.model.book

data class BookCover(
    val url: String
)

data class Publisher(
    val publisher_name: String
)

data class Category(
    val title: String
)

data class BooksDomainModel(
    val documentId: String,
    val title: String,
    val description: String,
    val ISBN: String,
    val page: String,
    val release: String,
    val language: String,
    val book_cover: BookCover,
    val publisher: Publisher,
    val categories: List<Category>,
    val pdfUrl: String = ""
)
