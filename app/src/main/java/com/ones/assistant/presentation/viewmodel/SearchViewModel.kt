package com.ones.assistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ones.assistant.domain.usecase.book.GetBooksUseCase
import com.ones.assistant.domain.usecase.podcast.GetPodcastsUseCase
import com.ones.assistant.presentation.views.books.BookDetails
import com.ones.assistant.presentation.views.podcast.PodcastItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val getPodcastsUseCase: GetPodcastsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        if (_uiState.value.isLoading) return
        
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                // Fetch books and podcasts in parallel
                val booksResult = getBooksUseCase()
                val podcastsResult = getPodcastsUseCase()

                val books = if (booksResult.isSuccess) {
                    booksResult.getOrNull()?.map { book ->
                        BookDetails(
                            id = book.documentId,
                            title = book.title,
                            author = book.publisher.publisher_name,
                            description = book.description,
                            isbn = book.ISBN,
                            publishedYear = book.release,
                            pages = book.page.toIntOrNull() ?: 0,
                            language = book.language,
                            category = book.categories.firstOrNull()?.title ?: "Uncategorized",
                            rating = 0f,
                            totalRatings = 0,
                            availableCopies = 0,
                            totalCopies = 0,
                            coverUrl = book.book_cover.url
                        )
                    } ?: emptyList()
                } else {
                    emptyList()
                }

                val podcasts = if (podcastsResult.isSuccess) {
                    podcastsResult.getOrNull()?.map { podcast ->
                        PodcastItem(
                            id = podcast.documentId,
                            title = podcast.name,
                            creator = podcast.speakerName,
                            coverUrl = podcast.imageUrl
                        )
                    } ?: emptyList()
                } else {
                    emptyList()
                }

                val categories = listOf("All") + books.map { it.category }.filter { it.isNotEmpty() }.distinct()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        books = books,
                        podcasts = podcasts,
                        categories = categories,
                        filteredBooks = books,
                        filteredPodcasts = podcasts
                    )
                }

                filterResults()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load search data"
                    )
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterResults()
    }

    fun updateSelectedCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        filterResults()
    }

    fun updateSelectedTab(tab: SearchTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        filterResults()
    }

    private fun filterResults() {
        val currentState = _uiState.value
        val query = currentState.searchQuery.trim()
        val category = currentState.selectedCategory
        
        // Filter books
        val filteredBooks = currentState.books.filter { book ->
            val matchesQuery = query.isEmpty() || 
                    book.title.contains(query, ignoreCase = true) ||
                    book.author.contains(query, ignoreCase = true) ||
                    book.description.contains(query, ignoreCase = true)
            
            val matchesCategory = category == "All" || book.category.equals(category, ignoreCase = true)
            
            matchesQuery && matchesCategory
        }

        // Filter podcasts
        val filteredPodcasts = currentState.podcasts.filter { podcast ->
            val matchesQuery = query.isEmpty() || 
                    podcast.title.contains(query, ignoreCase = true) ||
                    podcast.creator.contains(query, ignoreCase = true)
            
            matchesQuery
        }

        _uiState.update {
            it.copy(
                filteredBooks = filteredBooks,
                filteredPodcasts = filteredPodcasts
            )
        }
    }
}

enum class SearchTab {
    ALL,
    BOOKS,
    PODCASTS
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val selectedTab: SearchTab = SearchTab.ALL,
    val books: List<BookDetails> = emptyList(),
    val podcasts: List<PodcastItem> = emptyList(),
    val categories: List<String> = listOf("All"),
    val filteredBooks: List<BookDetails> = emptyList(),
    val filteredPodcasts: List<PodcastItem> = emptyList(),
    val errorMessage: String? = null
)
