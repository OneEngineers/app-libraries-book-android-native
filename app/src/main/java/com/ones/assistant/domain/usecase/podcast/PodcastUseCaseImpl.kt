package com.ones.assistant.domain.usecase.podcast

import com.ones.assistant.domain.repositories.podcast.PodcastRepositories
import javax.inject.Inject

class GetPodcastsUseCaseImpl @Inject constructor(
    private val podcastRepository: PodcastRepositories
) : GetPodcastsUseCase {

    override suspend fun invoke() = podcastRepository.getPodcasts()
}

class GetPodcastDetailUseCaseImpl @Inject constructor(
    private val podcastRepository: PodcastRepositories
) : GetPodcastDetailUseCase {

    override suspend fun invoke(documentId: String) = podcastRepository.getPodcastDetail(documentId)
}
