package dk.adaptmobile.android_seed

import android.annotation.SuppressLint
import dk.adaptmobile.amkotlinutil.extensions.DisposeBag
import dk.adaptmobile.amkotlinutil.extensions.disposeSafe
import dk.adaptmobile.amkotlinutil.navigation.NavManager
import dk.adaptmobile.android_seed.base.Dependencies
import dk.adaptmobile.android_seed.screens.Routing
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject

@SuppressLint("CheckResult")
class MainActivityViewModel {
    private val disposeBag = DisposeBag()
    val output: PublishSubject<Output> = PublishSubject.create()
    val input: PublishSubject<Input> = PublishSubject.create()

    sealed class Output {
        data class ShowLoading(val show: Boolean) : Output()
        data class ShowNoNetworkError(val show: Boolean) : Output()
    }

    sealed class Input {
        object Init : Input()
        object OnDestroy : Input()
        object MinimumVersionDialog : Input()
    }

    init {
        input.subscribe {
            when (it) {
                Input.Init -> init()
                Input.OnDestroy -> disposeBag.disposeSafe()
                Input.MinimumVersionDialog -> TODO("Handle minium version dialog")
            }
        }.addTo(disposeBag)
    }

    private fun init() {
        NavManager.open(Routing.Start())

        Dependencies.loadingSubject.subscribe {
            output.onNext(Output.ShowLoading(it))
        }.addTo(disposeBag)

        Dependencies.noNetworkSubject.subscribe {
            output.onNext(Output.ShowNoNetworkError(it))
        }.addTo(disposeBag)
    }
}
