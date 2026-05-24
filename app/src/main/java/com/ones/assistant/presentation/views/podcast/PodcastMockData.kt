package com.ones.assistant.presentation.views.podcast

import com.ones.assistant.R

object PodcastMockData {

    private val chhayaTalk = PodcastDetailUi(
        documentId = "chhaya-talk",
        title = "Chhaya Talk",
        description = "ជាកម្មវិធីផ្សាយផ្ទាល់របស់លោក ច័យា សម្រាប់ចែករំលែកចំណេះដឹង និងបទពិសោធន៍ក្នុងការអភិវឌ្ឍខ្លួនឯង។",
        category = "Education",
        coverRes = R.drawable.dear_to_lead,
        episodes = listOf(
            PodcastEpisodeUi(
                id = "ep1",
                title = "តើខ្ញុំគួររៀនជំនាញអ្វីបន្តទៀតនៅសាកលវិទ្យាល័យ?",
                durationSeconds = 0,
                thumbnailRes = R.drawable.for_the_record
            ),
            PodcastEpisodeUi(
                id = "ep2",
                title = "Test",
                durationSeconds = 0,
                thumbnailRes = R.drawable.the321
            ),
            PodcastEpisodeUi(
                id = "ep3",
                title = "testing upload audio 2",
                durationSeconds = 0,
                thumbnailRes = R.drawable.strategies
            )
        )
    )

    private val defaultPodcast = PodcastDetailUi(
        documentId = "default",
        title = "15 Minute English",
        description = "IELTS grammar requires a blend of accuracy and range, focusing on using simple and complex sentences.",
        category = "Language",
        coverRes = R.drawable.dear_to_lead,
        episodes = List(5) { index ->
            PodcastEpisodeUi(
                id = "ep-$index",
                title = "IELTS grammar requires a blend of accuracy and range, focusing on using simple and complex sentences.",
                durationSeconds = 0,
                thumbnailRes = R.drawable.for_the_record
            )
        }
    )

    fun getById(podcastId: String): PodcastDetailUi {
        val normalized = podcastId.trim().lowercase()
        return when {
            normalized.contains("chhaya") ||
                normalized == "chhaya talk" -> chhayaTalk.copy(documentId = podcastId)
            normalized == chhayaTalk.documentId -> chhayaTalk
            else -> defaultPodcast.copy(
                documentId = podcastId,
                title = podcastId.ifBlank { defaultPodcast.title }
            )
        }
    }
}
