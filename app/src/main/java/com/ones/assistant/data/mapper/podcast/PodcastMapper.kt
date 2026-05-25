package com.ones.assistant.data.mapper.podcast

import com.ones.assistant.domain.model.podcast.EpisodeDomainModel
import com.ones.assistant.domain.model.podcast.PodcastDetailDomainModel
import com.ones.assistant.domain.model.podcast.PodcastDomainModel
import com.ones.assistant.graphql.podcast.EpisodesQuery
import com.ones.assistant.graphql.podcast.PodcastDetailQuery
import com.ones.assistant.graphql.podcast.PodcastsQuery
import javax.inject.Inject

class PodcastMapper @Inject constructor() {

    fun mapToDomain(podcast: PodcastsQuery.Podcast): PodcastDomainModel {
        return PodcastDomainModel(
            documentId = podcast.documentId,
            name = podcast.name.orEmpty(),
            speakerName = podcast.speaker?.name.orEmpty(),
            imageUrl = podcast.image?.url.orEmpty()
        )
    }

    fun mapToDomainList(podcasts: List<PodcastsQuery.Podcast>): List<PodcastDomainModel> {
        return podcasts.map { mapToDomain(it) }
    }

    fun mapEpisodeToDomain(episode: EpisodesQuery.Episode): EpisodeDomainModel {
        return EpisodeDomainModel(
            documentId = episode.documentId,
            title = episode.title,
            description = stripHtml(episode.description.orEmpty()),
            durationSeconds = episode.duration?.toInt() ?: 0,
            audioUrl = episode.audio.url.orEmpty(),
            thumbnailUrl = episode.thumbnail.url.orEmpty()
        )
    }

    fun mapEpisodesToDomainList(episodes: List<EpisodesQuery.Episode>): List<EpisodeDomainModel> {
        return episodes.map { mapEpisodeToDomain(it) }
    }

    fun mapDetailToDomain(
        podcast: PodcastDetailQuery.Podcast,
        episodes: List<EpisodeDomainModel>
    ): PodcastDetailDomainModel {
        return PodcastDetailDomainModel(
            documentId = podcast.documentId,
            name = podcast.name.orEmpty(),
            description = stripHtml(podcast.description.orEmpty()),
            categoryName = podcast.category?.name.orEmpty(),
            speakerName = podcast.speaker?.name.orEmpty(),
            imageUrl = podcast.image?.url.orEmpty(),
            episodes = episodes
        )
    }

    private fun stripHtml(html: String): String {
        return html
            .replace(Regex("<[^>]*>"), "")
            .replace("&nbsp;", " ")
            .trim()
    }
}
