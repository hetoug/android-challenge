package dk.adaptmobile.android_seed.navigation

import com.github.ajalt.timberkt.e
import dk.adaptmobile.amkotlinutil.extensions.subscribeOnAndroidMain

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject

abstract class BaseViewModel<T : BaseViewModel.IInput, T2 : BaseViewModel.IOutput> {
    val disposeBag: CompositeDisposable = CompositeDisposable()
    val output: Subject<T2>
    val input: BehaviorSubject<T> = BehaviorSubject.create()

    open class IOutput
    open class IInput

    abstract fun init()
    protected abstract fun handleInput(input: T)

    init {
        val replaySubject = ReplaySubject.create<T2>()
        output = replaySubject.toSerialized() // We want to use a SerializedSubject since that allows calling onNext from any thread. Reference: https://stackoverflow.com/questions/31841809/is-serializedsubject-necessary-for-thread-safety-in-rxjava

        input
                .observeOn(Schedulers.computation())
                .subscribe(
                        { handleInput(it) },
                        { e(it) { "Error subscring to input" } }
                )
                .addTo(disposeBag)
    }

    protected fun <T> subscribeToInput(observable: Observable<T>, callback: (value: T) -> Unit) {
        observable
                .subscribeOnAndroidMain()
                .subscribe(
                        {
                            callback(it)
                        },
                        {
                            e(it) { "Error" }
                        }
                ).addTo(disposeBag)
    }

    protected fun <T> subscribeToInput(observable: Single<T>, callback: (value: T) -> Unit) {
        observable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            callback(it)
                        },
                        {
                            e(it) { "Error" }
                        }
                ).addTo(disposeBag)
    }
}
