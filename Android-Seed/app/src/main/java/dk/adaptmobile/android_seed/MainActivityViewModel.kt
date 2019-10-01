package dk.adaptmobile.android_seed

import dk.adaptmobile.android_seed.screens.Routing
import io.reactivex.subjects.BehaviorSubject

class MainActivityViewModel {
    val firstView: BehaviorSubject<Routing> = BehaviorSubject.create()

    init {
        firstView.onNext(Routing.Start)
    }
}