package dk.adaptmobile.android_seed.extensions

import com.github.ajalt.timberkt.e
import dk.adaptmobile.amkotlinutil.extensions.doOnComputation
import dk.adaptmobile.amkotlinutil.extensions.subscribeOnAndroidMain
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.subscribeToInput(disposeBag: CompositeDisposable, callback: (value: T) -> Unit) {
    doOnComputation()
            .subscribeOnAndroidMain()
            .subscribe(
                    {
                        callback(it)
                    },
                    {
                        e { "Error: $it" }
                    }
            ).addTo(disposeBag)

}

fun <T> Single<T>.subscribeToInput(disposeBag: CompositeDisposable, callback: (value: T) -> Unit) {
    doOnComputation()
            .subscribeOnAndroidMain()
            .subscribe(
                    {
                        callback(it)
                    },
                    {
                        e { "Error: $it" }
                    }
            ).addTo(disposeBag)

}

fun <T> Single<T>.doOnAndroidMain(): Single<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.doOnIO(): Single<T> {
    return this.observeOn(Schedulers.io())
}

fun <T> Single<T>.doOnComputation(): Single<T> {
    return this.observeOn(Schedulers.computation())
}

fun <T> Single<T>.doOnNewThread(): Single<T> {
    return this.observeOn(Schedulers.newThread())
}

fun <T> Single<T>.subscribeOnAndroidMain(): Single<T> {
    return this.subscribeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.subscribeOnComputation(): Single<T> {
    return this.subscribeOn(Schedulers.computation())
}

fun <T> Single<T>.subscribeOnIo(): Single<T> {
    return this.subscribeOn(Schedulers.io())
}

fun <T> Single<T>.subscribeOnNewThread(): Single<T> {
    return this.subscribeOn(Schedulers.newThread())
}