package dk.adaptmobile.android_seed.base

import android.annotation.SuppressLint
import dk.adaptmobile.android_seed.model.ErrorMessage
import dk.adaptmobile.android_seed.screens.Routing
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject

/**
 * Created by Alex on 5/7/18
 */
@SuppressLint("CheckResult")
abstract class BaseViewModel {
    val disposeBag: CompositeDisposable = CompositeDisposable()

    val routing: PublishSubject<Routing> = PublishSubject.create()
    val error: PublishSubject<ErrorMessage> = PublishSubject.create()
    val output: ReplaySubject<IOutput> = ReplaySubject.create()
    val input: BehaviorSubject<IInput> = BehaviorSubject.create()

    interface IOutput
    interface IInput

    protected abstract fun handleInput(input: IInput)

    init {
        input
                .observeOn(Schedulers.computation())
                .subscribe { handleInput(it) }
                .addTo(disposeBag)
    }
}
