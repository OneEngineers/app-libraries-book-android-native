package com.ones.assistant.utilities

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

data class PodcastPlaybackState(
    val episodeId: String? = null,
    val isPlaying: Boolean = false,
    val errorMessage: String? = null
)

@Singleton
class PodcastAudioPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null

    private val _playbackState = MutableStateFlow(PodcastPlaybackState())
    val playbackState: StateFlow<PodcastPlaybackState> = _playbackState.asStateFlow()

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _playbackState.update { it.copy(isPlaying = isPlaying, errorMessage = null) }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_ENDED) {
                _playbackState.update { it.copy(isPlaying = false) }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            _playbackState.update {
                it.copy(
                    isPlaying = false,
                    errorMessage = error.message ?: "Failed to play audio"
                )
            }
        }
    }

    fun togglePlayback(episodeId: String, audioUrl: String) {
        if (audioUrl.isBlank()) {
            _playbackState.update {
                it.copy(
                    episodeId = episodeId,
                    isPlaying = false,
                    errorMessage = "No audio available for this episode"
                )
            }
            return
        }

        val player = getOrCreatePlayer()
        val current = _playbackState.value

        if (current.episodeId == episodeId) {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
            return
        }

        _playbackState.update {
            it.copy(episodeId = episodeId, isPlaying = false, errorMessage = null)
        }

        player.setMediaItem(MediaItem.fromUri(audioUrl))
        player.prepare()
        player.play()
    }

    fun stop() {
        exoPlayer?.run {
            stop()
            clearMediaItems()
        }
        _playbackState.value = PodcastPlaybackState()
    }

    fun release() {
        exoPlayer?.run {
            removeListener(playerListener)
            release()
        }
        exoPlayer = null
        _playbackState.value = PodcastPlaybackState()
    }

    private fun getOrCreatePlayer(): ExoPlayer {
        return exoPlayer ?: ExoPlayer.Builder(context).build().also { player ->
            player.addListener(playerListener)
            exoPlayer = player
        }
    }
}
