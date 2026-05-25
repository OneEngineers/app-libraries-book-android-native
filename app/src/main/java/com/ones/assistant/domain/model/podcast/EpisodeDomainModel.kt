package com.ones.assistant.domain.model.podcast

data class EpisodeDomainModel(
    val documentId: String,
    val title: String,
    val description: String,
    val durationSeconds: Int,
    val audioUrl: String,
    val thumbnailUrl: String
)

data class PodcastDetailDomainModel(
    val documentId: String,
    val name: String,
    val description: String,
    val categoryName: String,
    val speakerName: String,
    val imageUrl: String,
    val episodes: List<EpisodeDomainModel>
)
