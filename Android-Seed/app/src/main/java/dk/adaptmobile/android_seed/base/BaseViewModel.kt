package dk.adaptmobile.android_seed.base

import dk.adaptmobile.android_seed.model.ErrorMessage
import dk.adaptmobile.android_seed.screens.Routing
import io.reactivex.subjects.PublishSubject

/**
 * Created by Alex on 5/7/18
 */
open class BaseViewModel {

    open val routing: PublishSubject<Routing> = PublishSubject.create()
    open val error: PublishSubject<ErrorMessage> = PublishSubject.create()

}