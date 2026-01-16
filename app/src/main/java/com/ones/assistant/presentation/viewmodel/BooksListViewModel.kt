package com.ones.assistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ones.assistant.data.repository.BookRepository
import com.ones.assistant.presentation.views.books.BookDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooksListViewModel(
    private val bookRepository: BookRepository = BookRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BooksListUiState())
    val uiState: StateFlow<BooksListUiState> = _uiState.asStateFlow()

    fun loadBooks() {
        if (_uiState.value.isLoading) return

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            val result = bookRepository.getBooks()

            _uiState.value = when {
                result.isSuccess -> {
                    val booksData = result.getOrNull()
                    val books = booksData?.bookLocals?.map { book ->
                        BookDetails(
                            id = book?.documentId ?: "",
                            title = book?.title ?: "",
                            author = book?.publisher?.publisher_name ?: "",
                            description = book?.description ?: "",
                            isbn = book?.ISBN ?: "",
                            publishedYear = book?.release?.toString() ?: "",
                            pages = book?.page?.toIntOrNull() ?: 0,
                            language = book?.language?.name ?: "",
                            category = book?.categories?.joinToString(", ") { it?.title ?: "" } ?: "",
                            rating = 0f, // Not available in API
                            totalRatings = 0, // Not available in API
                            availableCopies = 0, // Not available in API
                            totalCopies = 0, // Not available in API
                            coverUrl = book?.book_cover?.url ?: ""
                        )
                    } ?: emptyList()

                    _uiState.value.copy(
                        isLoading = false,
                        books = books,
                        errorMessage = null
                    )
                }
                else -> {
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Failed to load books"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class BooksListUiState(
    val isLoading: Boolean = false,
    val books: List<BookDetails> = emptyList(),
    val errorMessage: String? = null
)