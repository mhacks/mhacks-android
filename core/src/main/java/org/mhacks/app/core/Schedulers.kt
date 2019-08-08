package org.mhacks.app.core

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

data class DispatcherProvider(
        val main: Scheduler,
        val computation: Scheduler,
        val io: Scheduler
) {

    @Inject
    constructor() : this(AndroidSchedulers.mainThread(), Schedulers.computation(), Schedulers.io())
}
