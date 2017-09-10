//package com.mhacks.android.data.network
//
//import com.mhacks.android.MHacksApplication
//import com.mhacks.android.data.kotlin.Config
//import com.mhacks.android.data.kotlin.RoomUser
//import com.mhacks.android.data.kotlin.User
//import com.mhacks.android.data.model.Login
//import com.mhacks.android.data.network.services.HackathonApiService
//import com.mhacks.android.data.room.RoomSingleton
//import io.reactivex.Observable
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//import javax.inject.Inject
//
///**
// * Created by jeffreychang on 9/3/17.
// */
//class NetworkSingleton (val application: MHacksApplication) {
//
//    @Inject lateinit var hackathonAPIService: HackathonApiService
//
//    init {
//        application.hackathonComponent.inject(this)
//    }
//
//    fun getConfiguration(): Observable<Config> {
//        return hackathonAPIService.getConfiguration()
//
//    }
//
////    fun getLoginObservable(email: String, password: String,
////                             success: (response: Login) -> Unit,
////                             failure: (failure: Throwable) -> Unit) {
////        hackathonAPIService.getLogin(email, password)
////                .subscribeOn(Schedulers.newThread())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe (
////                    { response -> success(response) },
////                    { error    -> failure(error) }
////                )
////    }
//
//    fun getLoginObservable(email: String, password: String,
//                           success: (response: Login) -> Unit,
//                           failure: (failure: Throwable) -> Unit) {
//        hackathonAPIService.getLogin(email, password)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe (
//                        { response -> success(response) },
//                        { error    -> failure(error) }
//                )
//    }
//
//    fun getUserObservable(): Observable<User> {
//        return hackathonAPIService.getUser("Bearer " + "sdfasdf")
//
//    }
//
//    companion object {
//        fun newInstance(application: MHacksApplication): NetworkSingleton {
//            return NetworkSingleton(application)
//        }
//    }
//
//
//}