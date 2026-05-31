package com.ones.assistant.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ones.assistant.presentation.views.books.BookDetails

data class HistoryItem(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String,
    val readAt: Long = System.currentTimeMillis()
)

class ReadingHistoryViewModel : ViewModel() {
    private val _history = mutableStateListOf<HistoryItem>()
    val history: List<HistoryItem> get() = _history

    fun addToHistory(book: BookDetails) {
        // Remove existing entry if it exists to move it to the top
        _history.removeAll { it.id == book.id }
        
        _history.add(0, HistoryItem(
            id = book.id,
            title = book.title,
            author = book.author,
            coverUrl = book.coverUrl
        ))
    }

    fun clearHistory() {
        _history.clear()
    }
}
