package com.mhacks.android.data.room

import com.mhacks.android.MHacksApplication
import com.mhacks.android.data.kotlin.Configuration
import com.mhacks.android.data.kotlin.RoomUser
import com.mhacks.android.data.kotlin.User
import com.mhacks.android.data.model.Login
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jeffreychang on 9/6/17.
 */
class RoomSingleton private constructor(val application: MHacksApplication) {

    @Inject lateinit var roomDatabase: MHacksDatabase

    init {
        application.roomComponent.inject(this)
    }

    fun getLogin(
            success: (Login) -> Unit,
            failure: (Throwable) -> Unit) {
        roomDatabase.loginDao()
                .getLogin()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { login -> success(login) },
                        { error -> failure(error) })
    }

    fun getLoginFlowable(): Flowable<Login> {
        return roomDatabase.loginDao().getLogin()
    }

    fun getUserFlowable(): Flowable<User> {
        return roomDatabase.userDao().getUser()
    }


    fun insertLogin(login: Login) { Observable.fromCallable({
        roomDatabase.loginDao()
                .insertLogin(login) })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun getConfiguration(
            success: (Configuration) -> Unit,
            failure: (Throwable) -> Unit) {
        roomDatabase.configDao()
                .getConfig()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { config -> success(config) },
                        { error -> failure(error) })
    }

    fun insertConfiguration(configuration: Configuration) { Observable.fromCallable({
            roomDatabase.configDao()
                .insertConfig(configuration) })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }


    // User
//    fun getUser(
//            success: (RoomUser) -> Unit,
//            failure: (Throwable) -> Unit) {
//        roomDatabase.userDao()
//                .getUser()
//                .flatMap { application.hackathonComponent. }
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe (
//                        { user -> success(user) },
//                        { error -> failure(error) })
//    }

//    fun insertLoginObservable(login: Login): Observable<Unit> {
//        return Observable.fromCallable({ roomDatabase.loginDao().insertLogin(login) })
//    }



    companion object {
        fun newInstance(application: MHacksApplication): RoomSingleton {
            return RoomSingleton(application = application)
        }
    }
}