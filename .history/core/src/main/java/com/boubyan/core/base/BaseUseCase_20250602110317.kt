package com.boubyan.core.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Base class for use cases that require parameters.
 * Handles coroutine dispatching and resource wrapping.
 *
 * @param P The type of the parameters required by this use case
 * @param R The type of the result returned by this use case
 * @param coroutineDispatcher The dispatcher to use for coroutines (defaults to IO)
 */
abstract class BaseUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    /**
     * Invokes the use case with the given parameters.
     * @param parameters The parameters required by this use case
     * @return A Flow of Resource containing the result
     */
    operator fun invoke(parameters: P): Flow<Resource<R>> = execute(parameters)
        .flowOn(coroutineDispatcher)

    /**
     * Abstract method to execute the use case logic.
     * Must be implemented by concrete use cases.
     * @param parameters The parameters required by this use case
     * @return A Flow of Resource containing the result
     */
    protected abstract fun execute(parameters: P): Flow<Resource<R>>
}

/**
 * Base class for use cases that don't require parameters.
 * Handles coroutine dispatching and resource wrapping.
 *
 * @param R The type of the result returned by this use case
 * @param coroutineDispatcher The dispatcher to use for coroutines (defaults to IO)
 */
abstract class BaseUseCaseNoParams<R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    /**
     * Invokes the use case.
     * @return A Flow of Resource containing the result
     */
    operator fun invoke(): Flow<Resource<R>> = execute()
        .flowOn(coroutineDispatcher)

    /**
     * Abstract method to execute the use case logic.
     * Must be implemented by concrete use cases.
     * @return A Flow of Resource containing the result
     */
    protected abstract fun execute(): Flow<Resource<R>>
}