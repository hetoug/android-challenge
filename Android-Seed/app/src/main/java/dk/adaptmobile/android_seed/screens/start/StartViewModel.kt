package dk.adaptmobile.android_seed.screens.start

import dk.adaptmobile.android_seed.base.BaseViewModel

/**
 * Created by Alex on 5/8/18
 */
class StartViewModel(val view: StartView) : BaseViewModel() {
    sealed class Output : IOutput {
        object ShowText : Output()
    }

    sealed class Input : IInput {
        object ButtonClicked : Input()
    }

    override fun handleInput(input: IInput) {
        when (input) {
            is Input.ButtonClicked -> output.onNext(Output.ShowText)
        }
    }
}