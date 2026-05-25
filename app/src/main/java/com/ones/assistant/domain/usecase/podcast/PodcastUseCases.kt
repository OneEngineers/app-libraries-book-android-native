package com.ones.assistant.domain.usecase.podcast

import com.ones.assistant.domain.model.podcast.PodcastDetailDomainModel
import com.ones.assistant.domain.model.podcast.PodcastDomainModel

interface GetPodcastsUseCase {
    suspend operator fun invoke(): Result<List<PodcastDomainModel>>
}

interface GetPodcastDetailUseCase {
    suspend operator fun invoke(documentId: String): Result<PodcastDetailDomainModel>
}
