package com.mhacks.app.ui.map.presenter

import com.mhacks.app.data.models.MapFloor
import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.common.BasePresenterImpl
import com.mhacks.app.ui.map.view.MapView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Implementation of map view fragment.
 */
class MapViewFragmentPresenterImpl(private val mapView: MapView,
                                   private val mHacksService: MHacksService,
                                   private val mHacksDatabase: MHacksDatabase)
    : MapViewFragmentPresenter, BasePresenterImpl() {

    override fun getMapFloor() {
        compositeDisposable?.add(
                mHacksDatabase.mapFloorDao().getFloors()
                        .flatMap {
                            if (it.isEmpty())
                                getFloorsFromAPI()
                                        .doOnSuccess {
                                            mHacksDatabase.mapFloorDao()
                                                    .deleteAndUpdateMapFloors(it)
                                        }
                            else Single.just(it) }
                        .delay(400, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            mapView.onGetMapFloorsSuccess(it)
                        }, {
                            mapView.onGetMapFloorsFailure(it)
                        })
        )
    }
    private fun getFloorsFromAPI(): Single<List<MapFloor>>
            = mHacksService.getFloorResponse().map { it.floors}
}