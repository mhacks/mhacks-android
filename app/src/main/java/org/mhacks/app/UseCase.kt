package org.mhacks.app

abstract class UseCase<P, Q> {

    abstract suspend fun execute(parameters: P): Result<Q>

    suspend operator fun invoke(parameters: P) = execute(parameters)

}