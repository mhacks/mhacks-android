package com.mhacks.app.di

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

abstract class UseCase {

    val disposables = CompositeDisposable()

    fun execute(observer: DisposableObserver<*>) {
        disposables.add(observer)
    }

    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

}