package dk.adaptmobile.android_seed.screens.browser

import dk.adaptmobile.amkotlinutil.extensions.subscribeToInput
import dk.adaptmobile.android_seed.base.Dependencies
import dk.adaptmobile.android_seed.extensions.*
import dk.adaptmobile.android_seed.navigation.BaseViewModel
import dk.adaptmobile.android_seed.navigation.NavManager
import dk.adaptmobile.android_seed.screens.browser.BrowserViewModel.*
import io.reactivex.rxjava3.core.Observable

class BrowserViewModel(val url: String) : BaseViewModel<Input, Output>() {
    sealed class Output : BaseViewModel.IOutput() {
        data class LoadUrl(val url: String) : Output()
    }

    sealed class Input : BaseViewModel.IInput() {
        data class Events(
                val backClicked: Observable<Unit>,
                val webviewEvents: Observable<WebViewEvent>
        ) : Input()
    }

    override fun init() {
        Dependencies.loadingSubject.onNext(true)
        output.onNext(Output.LoadUrl(this.url))
    }

    override fun handleInput(input: Input) {
        when (input) {
            is Input.Events -> handleEvents(input)
        }
    }

    private fun handleEvents(input: Input.Events) {
        input.backClicked
                .subscribeToInput(disposeBag) {
                    NavManager.goBack()
                }
        input.webviewEvents
                .subscribeToInput(disposeBag) {
                    when (it) {
                        is WebViewEvent.OnPageFinished -> Dependencies.loadingSubject.onNext(false)
                    }
                }
    }
}
