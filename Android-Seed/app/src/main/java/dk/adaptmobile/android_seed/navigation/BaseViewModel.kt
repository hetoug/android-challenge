package dk.adaptmobile.android_seed.navigation

import com.github.ajalt.timberkt.e
import dk.adaptmobile.amkotlinutil.extensions.subscribeOnAndroidMain
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
import io.reactivex.rxjava3.subjects.Subject

import org.koin.core.KoinComponent

abstract class BaseViewModel<T : BaseViewModel.IInput, T2 : BaseViewModel.IOutput> : KoinComponent {
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


}
