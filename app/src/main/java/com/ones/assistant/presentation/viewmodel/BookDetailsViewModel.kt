package com.ones.assistant.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ones.assistant.data.repository.BookRepository
import com.ones.assistant.presentation.views.books.BookDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    private val bookRepository: BookRepository = BookRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDetailsUiState())
    val uiState: StateFlow<BookDetailsUiState> = _uiState.asStateFlow()

    fun loadBookDetail(documentId: String) {
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            Log.d("BookDetailsViewModel", "Loading book with ID: $documentId")
            val result = bookRepository.getBookDetail(documentId)

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { bookData ->
                        Log.d("BookDetailsViewModel", "Raw data received: ${bookData.bookLocal}")
                        
                        val book = bookData.bookLocal?.let { book ->
                            BookDetails(
                                id = documentId,
                                title = book.title ?: "",
                                author = book.publisher?.publisher_name ?: "",
                                description = book.description ?: "",
                                isbn = book.ISBN ?: "",
                                publishedYear = book.release?.toString() ?: "",
                                pages = book.page?.toIntOrNull() ?: 0,
                                language = book.language?.name ?: "",
                                category = book.categories.joinToString { it?.title ?: "" },
                                rating = 0f,
                                totalRatings = 0,
                                availableCopies = 0,
                                totalCopies = 0,
                                coverUrl = book.book_cover?.url ?: ""
                            )
                        }

                        Log.d("BookDetailsViewModel", "Mapped book details: $book")

                        currentState.copy(
                            isLoading = false,
                            book = book,
                            errorMessage = if (book == null) "Book not found" else null
                        )
                    },
                    onFailure = { error ->
                        Log.e("BookDetailsViewModel", "Error fetching book", error)
                        currentState.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load book details"
                        )
                    }
                )
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

data class BookDetailsUiState(
    val isLoading: Boolean = false,
    val book: BookDetails? = null,
    val errorMessage: String? = null
)
