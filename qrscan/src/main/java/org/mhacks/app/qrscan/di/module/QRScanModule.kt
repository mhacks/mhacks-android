package org.mhacks.app.qrscan.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.mhacks.app.core.di.ViewModelKey
import org.mhacks.app.qrscan.QRScanViewModel

/**
 * Module for the QRScanActivity.
 */
@Module
abstract class QRScanModule {

    @Binds
    @IntoMap
    @ViewModelKey(QRScanViewModel::class)
    abstract fun bindQRScanViewModel(qrScanViewModel: QRScanViewModel): ViewModel

}
