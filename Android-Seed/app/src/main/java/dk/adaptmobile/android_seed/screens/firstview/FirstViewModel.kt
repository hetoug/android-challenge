package dk.adaptmobile.android_seed.screens.firstview

import dk.adaptmobile.amkotlinutil.extensions.subscribeToInput
import dk.adaptmobile.android_seed.navigation.BaseViewModel
import dk.adaptmobile.android_seed.screens.firstview.FirstViewModel.Input
import dk.adaptmobile.android_seed.screens.firstview.FirstViewModel.Output
import dk.adaptmobile.android_seed.usecases.BaseUseCase.UseCaseResult
import dk.adaptmobile.android_seed.usecases.FetchJsonUseCase
import io.reactivex.rxjava3.core.Observable
import org.koin.core.inject

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
                .flatMapSingle { fetchJsonUseCase() }
                .subscribeToInput(disposeBag) {
                    when (it) {
                        is UseCaseResult.Success -> output.onNext(Output.UpdateText(it.body))
                        is UseCaseResult.Failure -> it.message.statusCode
                    }
                }
    }
}
