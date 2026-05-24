package com.ones.assistant.presentation.views.podcast

data class PodcastEpisodeUi(
    val id: String,
    val title: String,
    val durationSeconds: Int,
    val thumbnailUrl: String = "",
    val thumbnailRes: Int? = null,
    val audioUrl: String = ""
)

data class PodcastDetailUi(
    val documentId: String,
    val title: String,
    val description: String,
    val category: String,
    val coverUrl: String = "",
    val coverRes: Int? = null,
    val episodes: List<PodcastEpisodeUi>
) {
    val episodeCount: Int get() = episodes.size
}

fun formatEpisodeDuration(seconds: Int): String {
    if (seconds <= 0) return "0 Second"
    if (seconds < 60) return if (seconds == 1) "1 Second" else "$seconds Seconds"
    val minutes = seconds / 60
    val remaining = seconds % 60
    return if (remaining == 0) {
        if (minutes == 1) "1 Minute" else "$minutes Minutes"
    } else {
        "$minutes min $remaining sec"
    }
}
