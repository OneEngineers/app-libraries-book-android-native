package com.ones.assistant.domain.repositories.podcast

import com.ones.assistant.domain.model.podcast.PodcastDetailDomainModel
import com.ones.assistant.domain.model.podcast.PodcastDomainModel

interface PodcastRepositories {
    suspend fun getPodcasts(): Result<List<PodcastDomainModel>>
    suspend fun getPodcastDetail(documentId: String): Result<PodcastDetailDomainModel>
}
