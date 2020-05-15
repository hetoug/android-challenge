package dk.adaptmobile.android_seed.base

import dk.adaptmobile.android_seed.managers.TrackingEvent
import dk.adaptmobile.android_seed.managers.TrackingScreen
import io.reactivex.subjects.PublishSubject

object Dependencies {
    val noNetworkSubject = PublishSubject.create<Boolean>()
    val loadingSubject = PublishSubject.create<Boolean>()
    var screenTracking: PublishSubject<TrackingScreen> = PublishSubject.create()
    var eventTracking: PublishSubject<TrackingEvent> = PublishSubject.create()
}
