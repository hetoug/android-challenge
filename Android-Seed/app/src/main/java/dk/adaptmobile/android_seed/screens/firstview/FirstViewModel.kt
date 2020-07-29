package dk.adaptmobile.android_seed.screens.firstview

import dk.adaptmobile.amkotlinutil.extensions.doOnIO
import dk.adaptmobile.amkotlinutil.extensions.subscribeToInput
import dk.adaptmobile.android_seed.model.TestRequest
import dk.adaptmobile.android_seed.model.TestResponse
import dk.adaptmobile.android_seed.navigation.BaseViewModel
import dk.adaptmobile.android_seed.network.ConnectionManager
import dk.adaptmobile.android_seed.screens.firstview.FirstViewModel.Input
import dk.adaptmobile.android_seed.screens.firstview.FirstViewModel.Output
import io.reactivex.rxjava3.core.Observable
import org.koin.core.inject
import kotlin.random.Random

class FirstViewModel : BaseViewModel<Input, Output>() {

    sealed class Output : BaseViewModel.IOutput() {
        data class UpdateText(val text: String) : Output()
    }

    sealed class Input : IInput() {
        data class Events(
                val buttonClicked: Observable<Unit>
        ) : Input()
    }

    private val fetchJsonUseCase: FetchJsonUseCase by inject()

    override fun init() {

    }

    override fun handleInput(input: Input) {
        when (input) {
            is Input.Events -> handleEvents(input)
        }
    }

    private fun handleEvents(input: Input.Events) {
        input.buttonClicked
                .doOnIO()
                .flatMap { fetchJsonUseCase() }
                .subscribeToInput(disposeBag) {
                    output.onNext(Output.UpdateText(it))
                }

    }
}

class FetchJsonUseCase(
        private val connectionManager: ConnectionManager
) {
    operator fun invoke(): Observable<String> {
        return connectionManager.restService
                .postRequest(TestRequest("Test"))
                .map { it.json.test }
    }
}