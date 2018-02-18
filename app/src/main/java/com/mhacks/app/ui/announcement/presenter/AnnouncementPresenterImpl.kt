package com.mhacks.app.ui.announcement.presenter

import com.mhacks.app.data.kotlin.Announcement
import com.mhacks.app.data.kotlin.AnnouncementResponse
import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.announcement.view.AnnouncementView
import com.mhacks.app.ui.common.BasePresenterImpl
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by jeffreychang on 2/16/18.
 */

class AnnouncementPresenterImpl(private val announcementView: AnnouncementView,
                                private val mHacksService: MHacksService,
                                private val mHacksDatabase: MHacksDatabase)
    : AnnouncementPresenter, BasePresenterImpl() {


    override fun loadAnnouncements() {
        compositeDisposable?.add(
                mHacksDatabase.announcementDao().getAnnouncements()
                        .flatMap { if (it.isEmpty())
                            getAnnouncementResponseFromAPI()
                            else Single.just(it)
                        }
                        .doOnSuccess {
                            Observable.fromCallable {
                                mHacksDatabase.announcementDao().updateAnnouncements(it)
                            }
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe()
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            announcementView.onGetAnnouncementsSuccess(it)
                        }, {
                            announcementView.onGetAnnouncementsFailure(it)
                        })
        )
    }

    private fun getAnnouncementResponseFromAPI(): Single<List<Announcement>> {
        return mHacksService.getAnnouncementResponse()
                .map { it.announcements }
    }
}