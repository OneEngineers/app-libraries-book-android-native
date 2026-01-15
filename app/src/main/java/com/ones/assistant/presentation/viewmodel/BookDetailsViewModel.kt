package com.ones.assistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ones.assistant.data.repository.BookRepository
import com.ones.assistant.presentation.views.books.BookDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    private val bookRepository: BookRepository = BookRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(BookDetailsUiState())
    val uiState: StateFlow<BookDetailsUiState> = _uiState.asStateFlow()
    
    fun loadBookDetail(documentId: String) {
        if (_uiState.value.isLoading) return
        
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        
        viewModelScope.launch {
            val result = bookRepository.getBookDetail(documentId)
            
            _uiState.value = when {
                result.isSuccess -> {
                    val bookData = result.getOrNull()
                    val book = bookData?.bookLocal?.let { book ->
                        BookDetails(
                            id = documentId,
                            title = book.title ?: "",
                            author = book.publisher?.publisher_name ?: "",
                            description = book.description ?: "",
                            isbn = book.ISBN ?: "",
                            publishedYear = book.release ?.toString() ?: "",
                            pages = book.page?.toIntOrNull() ?: 0,
                            language = book.language?.name ?: "",
                            category = book.categories.toString(),
                            rating = 0f, // Not available in API
                            totalRatings = 0, // Not available in API
                            availableCopies = 0, // Not available in API
                            totalCopies = 0, // Not available in API
                            coverUrl = book.book_cover?.url ?: ""
                        )
                    }

                    _uiState.value.copy(
                        isLoading = false,
                        book = book,
                        errorMessage = if (book == null) "Book not found" else null
                    )
                }
                else -> {
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Failed to load book details"
                    )
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class BookDetailsUiState(
    val isLoading: Boolean = false,
    val book: BookDetails? = null,
    val errorMessage: String? = null
)
