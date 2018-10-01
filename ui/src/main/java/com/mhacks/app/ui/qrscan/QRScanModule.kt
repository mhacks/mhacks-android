package com.mhacks.app.ui.qrscan

import androidx.lifecycle.ViewModel
import com.mhacks.app.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Module for the QRScanActivity.
 */
@Module
abstract class QRScanActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(QRScanViewModel::class)
    abstract fun bindQRScanViewModel(qrScanViewModel: QRScanViewModel): ViewModel

}
