package dk.adaptmobile.android_seed.base

import android.annotation.SuppressLint
import android.content.Intent
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import dk.adaptmobile.amkotlinutil.extensions.DisposeBag
import dk.adaptmobile.amkotlinutil.extensions.disposeSafe
import dk.adaptmobile.amkotlinutil.extensions.wait
import dk.adaptmobile.android_seed.managers.PrefsManager
import dk.adaptmobile.android_seed.navigation.NavManager
import dk.adaptmobile.android_seed.screens.Routing
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject

import org.koin.core.KoinComponent
import org.koin.core.inject

@SuppressLint("CheckResult")
class MainActivityViewModel : KoinComponent {
    private val prefsManager: PrefsManager by inject()
    private val disposeBag = DisposeBag()
    val output: PublishSubject<Output> = PublishSubject.create()
    val input: PublishSubject<Input> = PublishSubject.create()

    sealed class Output {
        data class ShowLoading(val show: Boolean) : Output()
    }

    sealed class Input {
        data class OnNewIntent(val intent: Intent) : Input()
        data class Init(val hasSavedInstanceState: Boolean) : Input()
        object OnDestroy : Input()
        object MinimumVersionDialog : Input()
    }

    init {
        input.subscribe {
            when (it) {
                is Input.Init -> init(it.hasSavedInstanceState)
                is Input.OnDestroy -> disposeBag.disposeSafe()
                is Input.MinimumVersionDialog -> {} //NavManager.openModally(Routing.ForceUpdateDialog())
                is Input.OnNewIntent -> handleNewIntent(it.intent)
            }
        }.addTo(disposeBag)
    }

    private fun handleNewIntent(intent: Intent) {

    }


    private fun init(hasSavedInstanceState: Boolean) {
        if (!hasSavedInstanceState) {
            NavManager.open(Routing.BottomNavigation())
//            wait(500) {
//                if (!prefsManager.hasOnboardingShown) {
//                    NavManager.openModally(Routing.Onboarding())
//                }
//            }
        }

        Dependencies.loadingSubject.subscribe {
            output.onNext(Output.ShowLoading(it))
        }.addTo(disposeBag)
    }


}
