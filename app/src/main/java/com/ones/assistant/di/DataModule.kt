package com.ones.assistant.di

import com.ones.assistant.data.datasource.remote.book.BookRemoteDataSource
import com.ones.assistant.data.mapper.book.BookMapper
import com.ones.assistant.data.repository.BookRepository
import com.ones.assistant.domain.repositories.book.BookRepositories
import com.ones.assistant.domain.usecase.book.GetBookDetailUseCase
import com.ones.assistant.domain.usecase.book.GetBookDetailUseCaseImpl
import com.ones.assistant.domain.usecase.book.GetBooksUseCase
import com.ones.assistant.domain.usecase.book.GetBooksUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

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
}