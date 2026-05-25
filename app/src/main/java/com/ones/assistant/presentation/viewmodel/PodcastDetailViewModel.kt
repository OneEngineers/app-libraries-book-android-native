package com.ones.assistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ones.assistant.domain.usecase.podcast.GetPodcastDetailUseCase
import com.ones.assistant.presentation.views.podcast.PodcastDetailUi
import com.ones.assistant.presentation.views.podcast.PodcastEpisodeUi
import com.ones.assistant.utilities.PodcastAudioPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastDetailViewModel @Inject constructor(
    private val getPodcastDetailUseCase: GetPodcastDetailUseCase,
    private val audioPlayer: PodcastAudioPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(PodcastDetailUiState())
    val uiState: StateFlow<PodcastDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            audioPlayer.playbackState.collect { playback ->
                _uiState.update {
                    it.copy(
                        playingEpisodeId = playback.episodeId,
                        isPlaying = playback.isPlaying,
                        playbackError = playback.errorMessage
                    )
                }
            }
        }
    }

    fun loadPodcastDetail(documentId: String) {
        if (_uiState.value.isLoading) return

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            val result = getPodcastDetailUseCase(documentId)

            _uiState.value = when {
                result.isSuccess -> {
                    val detail = result.getOrNull()
                    _uiState.value.copy(
                        isLoading = false,
                        podcast = detail?.toUi(),
                        errorMessage = null
                    )
                }
                else -> {
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Failed to load podcast"
                    )
                }
            }
        }
    }

    fun onEpisodeClick(episode: PodcastEpisodeUi) {
        audioPlayer.togglePlayback(episode.id, episode.audioUrl)
    }

    fun clearPlaybackError() {
        _uiState.update { it.copy(playbackError = null) }
    }

    override fun onCleared() {
        audioPlayer.stop()
        super.onCleared()
    }
}

data class PodcastDetailUiState(
    val isLoading: Boolean = false,
    val podcast: PodcastDetailUi? = null,
    val errorMessage: String? = null,
    val playingEpisodeId: String? = null,
    val isPlaying: Boolean = false,
    val playbackError: String? = null
)

private fun com.ones.assistant.domain.model.podcast.PodcastDetailDomainModel.toUi(): PodcastDetailUi {
    return PodcastDetailUi(
        documentId = documentId,
        title = name,
        description = description.ifBlank {
            "Listen to episodes from $name."
        },
        category = categoryName.ifBlank { "Podcast" },
        coverUrl = imageUrl,
        episodes = episodes.map { episode ->
            PodcastEpisodeUi(
                id = episode.documentId,
                title = episode.title,
                durationSeconds = episode.durationSeconds,
                thumbnailUrl = episode.thumbnailUrl,
                audioUrl = episode.audioUrl
            )
        }
    )
}
