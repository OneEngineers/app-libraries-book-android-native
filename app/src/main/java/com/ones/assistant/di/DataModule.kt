package com.ones.assistant.di

import com.ones.assistant.data.datasource.remote.book.BookRemoteDataSource
import com.ones.assistant.data.datasource.remote.podcast.PodcastRemoteDataSource
import com.ones.assistant.data.mapper.book.BookMapper
import com.ones.assistant.data.mapper.podcast.PodcastMapper
import com.ones.assistant.data.repository.BookRepository
import com.ones.assistant.data.repository.PodcastRepository
import com.ones.assistant.domain.repositories.book.BookRepositories
import com.ones.assistant.domain.repositories.podcast.PodcastRepositories
import com.ones.assistant.domain.usecase.book.GetBookDetailUseCase
import com.ones.assistant.domain.usecase.book.GetBookDetailUseCaseImpl
import com.ones.assistant.domain.usecase.book.GetBooksUseCase
import com.ones.assistant.domain.usecase.book.GetBooksUseCaseImpl
import com.ones.assistant.domain.usecase.podcast.GetPodcastDetailUseCase
import com.ones.assistant.domain.usecase.podcast.GetPodcastDetailUseCaseImpl
import com.ones.assistant.domain.usecase.podcast.GetPodcastsUseCase
import com.ones.assistant.domain.usecase.podcast.GetPodcastsUseCaseImpl
import android.content.Context
import com.ones.assistant.data.repository.UserRepositoryImpl
import com.ones.assistant.domain.repositories.user.UserRepository
import com.ones.assistant.domain.usecase.user.UpdateProfileUseCase
import com.ones.assistant.utilities.TokenManager
import com.ones.assistant.utilities.createApolloClientAuth
import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    @Named("AuthClient")
    fun provideApolloClientAuth(tokenManager: TokenManager): ApolloClient {
        return createApolloClientAuth(tokenManager)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        @Named("AuthClient") apolloClient: ApolloClient
    ): UserRepository {
        return UserRepositoryImpl(apolloClient)
    }

    @Provides
    fun provideUpdateProfileUseCase(userRepository: UserRepository): UpdateProfileUseCase {
        return UpdateProfileUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideBookRemoteDataSource(): BookRemoteDataSource {
        return BookRemoteDataSource()
    }

    @Provides
    @Singleton
    fun provideBookMapper(): BookMapper {
        return BookMapper()
    }

    @Provides
    @Singleton
    fun provideBookRepository(
        bookRemoteDataSource: BookRemoteDataSource,
        bookMapper: BookMapper
    ): BookRepositories {
        return BookRepository(bookRemoteDataSource, bookMapper)
    }

    @Provides
    fun provideGetBooksUseCase(bookRepository: BookRepositories): GetBooksUseCase {
        return GetBooksUseCaseImpl(bookRepository)
    }

    @Provides
    fun provideGetBookDetailUseCase(bookRepository: BookRepositories): GetBookDetailUseCase {
        return GetBookDetailUseCaseImpl(bookRepository)
    }

    @Provides
    @Singleton
    fun providePodcastRemoteDataSource(): PodcastRemoteDataSource {
        return PodcastRemoteDataSource()
    }

    @Provides
    @Singleton
    fun providePodcastMapper(): PodcastMapper {
        return PodcastMapper()
    }

    @Provides
    @Singleton
    fun providePodcastRepository(
        podcastRemoteDataSource: PodcastRemoteDataSource,
        podcastMapper: PodcastMapper
    ): PodcastRepositories {
        return PodcastRepository(podcastRemoteDataSource, podcastMapper)
    }

    @Provides
    fun provideGetPodcastsUseCase(podcastRepository: PodcastRepositories): GetPodcastsUseCase {
        return GetPodcastsUseCaseImpl(podcastRepository)
    }

    @Provides
    fun provideGetPodcastDetailUseCase(podcastRepository: PodcastRepositories): GetPodcastDetailUseCase {
        return GetPodcastDetailUseCaseImpl(podcastRepository)
    }
}