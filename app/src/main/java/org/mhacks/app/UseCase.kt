package org.mhacks.app

abstract class UseCase<P, Q> {

    abstract fun execute(parameters: P): Q

    operator fun invoke(parameters: P) = execute(parameters)

}