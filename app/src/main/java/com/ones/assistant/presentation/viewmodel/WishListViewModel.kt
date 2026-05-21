package com.ones.assistant.presentation.views.feature

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class PodcastItem(
    val id: String,
    val title: String,
    val author: String,
    val imageRes: Int
)

class WishListViewModel : ViewModel() {
    private val _favorites = mutableStateListOf<PodcastItem>()
    val favorites: List<PodcastItem> get() = _favorites

    fun addFavorite(podcast: PodcastItem) {
        if (_favorites.none { it.id == podcast.id }) {
            _favorites.add(podcast)
        }
    }

    fun removeFavorite(podcastId: String) {
        _favorites.removeAll { it.id == podcastId }
    }
}
