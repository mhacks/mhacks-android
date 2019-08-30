//package org.mhacks.app.org.mhacks.app.eventlibrary.di.component
//
//import android.app.Application
//import com.mhacks.app.MHacksApplication
//import com.mhacks.app.data.DataModule
//import org.mhacks.app.org.mhacks.app.eventlibrary.di.ViewModelModule
//import org.mhacks.app.org.mhacks.app.eventlibrary.di.module.*
//import dagger.BindsInstance
//import dagger.Component
//import dagger.android.AndroidInjector
//import dagger.android.support.AndroidSupportInjectionModule
//import dagger.android.support.DaggerApplication
//import javax.inject.Singleton
//
///**
// * Component that injects into Android members (e.g. Activities and Fragments) with various
// * modules that provide tasks such as networking and caching in a database.
// */
//@Singleton
//@Component(
//        modules = [
//            AndroidSupportInjectionModule::class,
//            AppModule::class,
//            ActivityBuilder::class,
//            ViewModelModule::class,
//            RoomModule::class,
//            DataModule::class,
//            AuthModule::class,
//            RetrofitModule::class,
//            SharedPreferencesModule::class
//        ])
//interface AppComponent : AndroidInjector<DaggerApplication> {
//
//    override fun inject(instance: DaggerApplication)
//
//    fun inject(application: MHacksApplication)
//
//    @Component.Builder
//    interface Builder {
//
//        @BindsInstance
//        fun application(application: Application): Builder
//
//        fun roomModule(roomModule: RoomModule): Builder
//
//        fun authModule(authModule: AuthModule): Builder
//
//        fun retrofitModule(retrofitModule: RetrofitModule): Builder
//
//        fun build(): AppComponent
//    }
//}
//
