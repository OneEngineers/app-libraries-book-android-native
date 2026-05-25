package com.ones.assistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ones.assistant.domain.usecase.podcast.GetPodcastsUseCase
import com.ones.assistant.presentation.views.podcast.PodcastItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastListViewModel @Inject constructor(
    private val getPodcastsUseCase: GetPodcastsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PodcastListUiState())
    val uiState: StateFlow<PodcastListUiState> = _uiState.asStateFlow()

    fun loadPodcasts() {
        if (_uiState.value.isLoading) return

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            val result = getPodcastsUseCase()

            _uiState.value = when {
                result.isSuccess -> {
                    val podcasts = result.getOrNull()?.map { podcast ->
                        PodcastItem(
                            id = podcast.documentId,
                            title = podcast.name,
                            creator = podcast.speakerName,
                            coverUrl = podcast.imageUrl
                        )
                    } ?: emptyList()

                    _uiState.value.copy(
                        isLoading = false,
                        podcasts = podcasts,
                        errorMessage = null
                    )
                }
                else -> {
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Failed to load podcasts"
                    )
                }
            }
        }
    }
}

data class PodcastListUiState(
    val isLoading: Boolean = false,
    val podcasts: List<PodcastItem> = emptyList(),
    val errorMessage: String? = null
)
