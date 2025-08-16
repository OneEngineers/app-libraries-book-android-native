package com.assistant.libraries.domain.ues_cases

import com.assistant.libraries.domain.repositories.BookRepo
import com.assistant.libraries.graphql.BooksQuery
import com.assistant.libraries.utilities.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchBooksUseCases @Inject constructor(private val bookRepo: BookRepo){
    operator fun invoke(): Flow<NetworkResult<BooksQuery.Data>> = flow {
        emit(NetworkResult.Loading())
        val response = bookRepo.getBooks()
        if (response.isSuccess) {
            emit(NetworkResult.Success(response.getOrThrow()))
        } else {
            emit(NetworkResult.Error(response.exceptionOrNull()?.message))
        }
    }.catch {
        emit(NetworkResult.Error(it.message))

    }.flowOn(Dispatchers.IO)
}