package dk.adaptmobile.android_seed.screens.`$3thirdview`

import dk.adaptmobile.android_seed.navigation.BaseViewModel
import dk.adaptmobile.android_seed.screens.`$1firstview`.FirstViewModel
import dk.adaptmobile.android_seed.screens.`$3thirdview`.ThirdViewModel.*
import io.reactivex.Observable

class ThirdViewModel : BaseViewModel<Input, Output>() {

    sealed class Output : BaseViewModel.IOutput() {
        //data class ShowNewsFeed(val news: List<NewsViewData>) : Output()
    }

    data class Input(
            val closeButtonClick: Observable<Unit>
    ) : BaseViewModel.IInput()

    override fun init() {
    }

    override fun handleInput(input: Input) {
    }
}