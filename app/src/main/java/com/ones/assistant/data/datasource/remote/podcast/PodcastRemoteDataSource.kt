package com.ones.assistant.data.datasource.remote.podcast

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.ones.assistant.graphql.podcast.EpisodesQuery
import com.ones.assistant.graphql.podcast.PodcastDetailQuery
import com.ones.assistant.graphql.podcast.PodcastsQuery
import com.ones.assistant.utilities.apolloClientPodcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PodcastRemoteDataSource @Inject constructor() {

    suspend fun getPodcasts(): Result<PodcastsQuery.Data> = executeQuery {
        apolloClientPodcast.query(PodcastsQuery()).execute()
    }

    suspend fun getPodcastDetail(documentId: String): Result<PodcastDetailQuery.Data> = executeQuery {
        apolloClientPodcast.query(PodcastDetailQuery(documentId)).execute()
    }

    suspend fun getEpisodes(): Result<EpisodesQuery.Data> = executeQuery {
        apolloClientPodcast.query(EpisodesQuery()).execute()
    }

    private suspend fun <D : Operation.Data> executeQuery(
        block: suspend () -> ApolloResponse<D>
    ): Result<D> {
        return withContext(Dispatchers.IO) {
            try {
                val response = block()

                if (response.hasErrors()) {
                    Result.failure(
                        Exception(response.errors?.firstOrNull()?.message ?: "Unknown error")
                    )
                } else {
                    response.data?.let { Result.success(it) }
                        ?: Result.failure(Exception("No data received"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getPodcastDetailWithEpisodes(
        documentId: String
    ): Result<Pair<PodcastDetailQuery.Data, EpisodesQuery.Data>> {
        return withContext(Dispatchers.IO) {
            try {
                val podcastDeferred = async { getPodcastDetail(documentId) }
                val episodesDeferred = async { getEpisodes() }

                val podcastResult = podcastDeferred.await()
                val episodesResult = episodesDeferred.await()

                if (podcastResult.isFailure) {
                    return@withContext Result.failure(
                        podcastResult.exceptionOrNull() ?: Exception("Failed to load podcast")
                    )
                }
                if (episodesResult.isFailure) {
                    return@withContext Result.failure(
                        episodesResult.exceptionOrNull() ?: Exception("Failed to load episodes")
                    )
                }

                val podcastData = podcastResult.getOrThrow()
                val episodesData = episodesResult.getOrThrow()

                if (podcastData.podcast == null) {
                    return@withContext Result.failure(Exception("Podcast not found"))
                }

                Result.success(podcastData to episodesData)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
