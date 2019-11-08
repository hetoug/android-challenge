package dk.adaptmobile.android_seed.screens.start

import dk.adaptmobile.amkotlinutil.extensions.subscribeToInput
import dk.adaptmobile.amkotlinutil.navigation.BaseViewModel
import dk.adaptmobile.android_seed.screens.start.StartViewModel.Input
import dk.adaptmobile.android_seed.screens.start.StartViewModel.Output
import io.reactivex.Observable

class StartViewModel : BaseViewModel<Input, Output>() {
    sealed class Output : BaseViewModel.IOutput() {
        object ShowText : Output()
    }

    data class Input(
        val buttonClicked: Observable<Unit>
    ) : BaseViewModel.IInput()

    override fun handleInput(input: Input) {
        input.buttonClicked.subscribeToInput(disposeBag) {
            output.onNext(Output.ShowText)
        }
    }
}
