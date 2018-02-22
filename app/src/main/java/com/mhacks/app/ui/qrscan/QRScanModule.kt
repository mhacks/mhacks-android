package com.mhacks.app.ui.qrscan

import com.mhacks.app.data.SharedPreferencesManager
import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.ui.qrscan.presenter.QRScanPresenter
import com.mhacks.app.ui.qrscan.presenter.QRScanPresenterImpl
import com.mhacks.app.ui.qrscan.view.QRScanView
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Module for the QRScanActivity.
 */
@Module
abstract class QRScanActivityModule {

    @Binds
    abstract fun provideQRScanView(QRActivity: QRScanActivity): QRScanView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideQRScanPresenter(qrScanView: QRScanView,
                                   mHacksService: MHacksService,
                                   mHacksDatabase: MHacksDatabase,
                                   sharedPreferencesManager: SharedPreferencesManager,
                                   authInterceptor: AuthModule.AuthInterceptor)
                : QRScanPresenter =
                QRScanPresenterImpl(
                        qrScanView,
                        mHacksService,
                        mHacksDatabase,
                        sharedPreferencesManager,
                        authInterceptor)
    }
}
