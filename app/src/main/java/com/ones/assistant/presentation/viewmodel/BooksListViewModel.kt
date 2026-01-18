package com.ones.assistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ones.assistant.domain.usecase.book.GetBooksUseCase
import com.ones.assistant.presentation.views.books.BookDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksListViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase
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
            val result = getBooksUseCase()

            _uiState.value = when {
                result.isSuccess -> {
                    val booksDomain = result.getOrNull() ?: emptyList()
                    val books = booksDomain.map { book ->
                        BookDetails(
                            id = book.documentId,
                            title = book.title,
                            author = book.publisher.publisher_name,
                            description = book.description,
                            isbn = book.ISBN,
                            publishedYear = book.release,
                            pages = book.page.toIntOrNull() ?: 0,
                            language = book.language,
                            category = book.categories.firstOrNull()?.title ?: "",
                            rating = 0f,
                            totalRatings = 0,
                            availableCopies = 0,
                            totalCopies = 0,
                            coverUrl = book.book_cover.url
                        )
                    }

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
