package com.mhacks.app.ui.common

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by jeffreychang on 2/17/18.
 */

abstract class BasePresenterImpl: BasePresenter {

    var compositeDisposable: CompositeDisposable? = null

    override fun onAttach() {
        compositeDisposable = CompositeDisposable()
    }

    override fun onDetach() {
        compositeDisposable?.dispose()
    }
}