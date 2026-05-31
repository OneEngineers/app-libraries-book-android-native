package com.ones.assistant.presentation.views.feature

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

sealed class WishlistItem {
    abstract val id: String
    abstract val title: String
    abstract val author: String

    data class Podcast(
        override val id: String,
        override val title: String,
        override val author: String,
        val imageRes: Int
    ) : WishlistItem()

    data class Book(
        override val id: String,
        override val title: String,
        override val author: String,
        val coverUrl: String
    ) : WishlistItem()
}

class WishListViewModel : ViewModel() {
    private val _favorites = mutableStateListOf<WishlistItem>()
    val favorites: List<WishlistItem> get() = _favorites

    fun addFavorite(item: WishlistItem) {
        if (_favorites.none { it.id == item.id }) {
            _favorites.add(item)
        }
    }

    fun removeFavorite(itemId: String) {
        _favorites.removeAll { it.id == itemId }
    }
}
