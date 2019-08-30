//package org.mhacks.app.events.di.component
//
//import dagger.Component
//import org.mhacks.app.core.di.BaseFragmentComponent
//import org.mhacks.app.core.di.CoreComponent
//import org.mhacks.app.core.di.module.FeatureScope
//import org.mhacks.app.events.di.module.EventDataModule
//import org.mhacks.app.events.di.module.EventModule
//import org.mhacks.app.events.widget.EventFragment
//
///**
// * Component binding injections for the :events feature module.
// */
//@Component(
//        modules = [EventModule::class, EventDataModule::class],
//        dependencies = [CoreComponent::class]
//)
//@FeatureScope
//interface EventComponent : BaseFragmentComponent<EventFragment> {
//
//    @Component.Builder
//    interface Builder {
//        fun coreComponent(component: CoreComponent): Builder
//        fun build(): EventComponent
//
//    }
//}