package com.mhacks.app.ui.events.presenter

import com.mhacks.app.data.models.Event
import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.common.BasePresenterImpl
import com.mhacks.app.ui.events.view.EventsView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Implementation of events presenter
 */

class EventsFragmentPresenterImpl(private val eventsView: EventsView,
                                  private val mHacksService: MHacksService,
                                  private val mHacksDatabase: MHacksDatabase)
    : EventsFragmentPresenter, BasePresenterImpl() {

    override fun getEvents() {
        compositeDisposable?.add(
                mHacksDatabase.eventDao().getEvents()
                        .flatMap {
                            if (it.isEmpty())
                                getEventsFromAPI()
                                        .doOnSuccess {
                                            mHacksDatabase.eventDao()
                                                    .deleteAndUpdateEvents(it)
                                    }
                            else Single.just(it) }
                        .delay(400, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            eventsView.onGetEventsSuccess(it)
                        }, {
                            eventsView.onGetEventsFailure(it)
                        })
        )
    }
    private fun getEventsFromAPI(): Single<List<Event>>
            = mHacksService.getEventResponse().map { it.events }
}