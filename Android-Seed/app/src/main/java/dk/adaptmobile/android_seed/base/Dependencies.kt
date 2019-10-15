package dk.adaptmobile.android_seed.base

import io.reactivex.subjects.PublishSubject

object Dependencies {
    val noNetworkSubject = PublishSubject.create<Boolean>()
    val loadingSubject = PublishSubject.create<Boolean>()
}
