package com.ones.assistant.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ones.assistant.domain.model.book.BooksDomainModel
import com.ones.assistant.domain.usecase.book.GetBookDetailUseCase
import com.ones.assistant.domain.usecase.book.GetBookDetailUseCaseImpl
import com.ones.assistant.presentation.views.books.BookDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookDetailsViewModel @Inject constructor(
    private val getBookDetailUseCase: GetBookDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDetailsUiState())
    val uiState: StateFlow<BookDetailsUiState> = _uiState.asStateFlow()

    fun loadBookDetail(documentId: String) {
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            Log.d("BookDetailsViewModel", "Loading book with ID: $documentId")
            val result = getBookDetailUseCase(documentId)

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { bookDomain ->
                        Log.d("BookDetailsViewModel", "Domain data received: $bookDomain")
                        
                        val book = BookDetails(
                            id = bookDomain.documentId,
                            title = bookDomain.title,
                            author = bookDomain.publisher.publisher_name,
                            description = bookDomain.description,
                            isbn = bookDomain.ISBN,
                            publishedYear = bookDomain.release,
                            pages = bookDomain.page.toIntOrNull() ?: 0,
                            language = bookDomain.language,
                            category = bookDomain.categories.firstOrNull()?.title ?: "",
                            rating = 0f,
                            totalRatings = 0,
                            availableCopies = 0,
                            totalCopies = 0,
                            coverUrl = ""
                        )

                        Log.d("BookDetailsViewModel", "Mapped book details: $book")

                        currentState.copy(
                            isLoading = false,
                            book = book,
                            errorMessage = null
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
