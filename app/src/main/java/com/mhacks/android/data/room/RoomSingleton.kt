package com.mhacks.android.data.room

import com.mhacks.android.MHacksApplication
import com.mhacks.android.data.model.Login
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by jeffreychang on 9/6/17.
 */
class RoomSingleton private constructor(application: MHacksApplication) {

    @Inject lateinit var roomDatabase: MHacksDatabase

    init {
        application.roomComponent.inject(this)
    }

//    fun getLogin(success: (List<Login>) -> Unit,
//                    failure: (Throwable) -> Unit) {
//        roomDatabase.loginDao()
//                .getLogin()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe (
//                        { response -> success(response) },
//                        { error    -> failure(error) }
//                )
//    }

    fun insertLogin(login: Login) {
        Observable.fromCallable({
            roomDatabase.loginDao().insertLogin(login) })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Timber.d("Login inserted.") })
    }
    companion object {
        fun newInstance(application: MHacksApplication): RoomSingleton {
            return RoomSingleton(application = application)
        }
    }
}