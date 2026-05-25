package com.ones.assistant.data.repository

import com.ones.assistant.data.datasource.remote.podcast.PodcastRemoteDataSource
import com.ones.assistant.data.mapper.podcast.PodcastMapper
import com.ones.assistant.domain.model.podcast.PodcastDetailDomainModel
import com.ones.assistant.domain.model.podcast.PodcastDomainModel
import com.ones.assistant.domain.repositories.podcast.PodcastRepositories
import javax.inject.Inject

class PodcastRepository @Inject constructor(
    private val podcastRemoteDataSource: PodcastRemoteDataSource,
    private val podcastMapper: PodcastMapper
) : PodcastRepositories {

    override suspend fun getPodcasts(): Result<List<PodcastDomainModel>> {
        return podcastRemoteDataSource.getPodcasts().mapCatching { data ->
            data.podcasts?.filterNotNull()?.let { podcastMapper.mapToDomainList(it) } ?: emptyList()
        }
    }

    override suspend fun getPodcastDetail(documentId: String): Result<PodcastDetailDomainModel> {
        return podcastRemoteDataSource.getPodcastDetailWithEpisodes(documentId).mapCatching { (podcastData, episodesData) ->
            val podcast = podcastData.podcast
                ?: throw Exception("Podcast not found")
            val episodes = episodesData.episodes
                ?.filterNotNull()
                ?.let { podcastMapper.mapEpisodesToDomainList(it) }
                ?: emptyList()
            podcastMapper.mapDetailToDomain(podcast, episodes)
        }
    }
}
