package org.mhacks.app.qrscan.di

import dagger.Component
import org.mhacks.app.core.di.BaseActivityComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.qrscan.di.module.QRScanDataModule
import org.mhacks.app.qrscan.di.module.QRScanModule
import org.mhacks.app.qrscan.ui.QRScanActivity

/**
 * Component binding injections for the :qrscan feature module.
 */
@Component(
        modules = [QRScanModule::class, QRScanDataModule::class],
        dependencies = [CoreComponent::class]
)
@FeatureScope
interface QRScanComponent : BaseActivityComponent<QRScanActivity> {

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder
        fun build(): QRScanComponent
    }
}