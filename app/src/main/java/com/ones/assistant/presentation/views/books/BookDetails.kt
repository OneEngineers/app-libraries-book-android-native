package com.ones.assistant.presentation.views.books

data class BookDetails(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val isbn: String,
    val publishedYear: String,
    val pages: Int,
    val language: String,
    val category: String,
    val rating: Float,
    val totalRatings: Int,
    val availableCopies: Int,
    val totalCopies: Int,
    val coverUrl: String = "",
    val pdfUrl: String = ""
)