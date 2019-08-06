package org.mhacks.app.core.di.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import org.mhacks.app.core.di.MHacksViewModelFactory

@Module
abstract class ViewModelModule {

//    @Singleton
//    abstract fun provideMHacksViewModelFactory() = MHacksViewModelFactory()

    @Binds
    abstract fun bindViewModelFactory(factory: MHacksViewModelFactory): ViewModelProvider.Factory

}
