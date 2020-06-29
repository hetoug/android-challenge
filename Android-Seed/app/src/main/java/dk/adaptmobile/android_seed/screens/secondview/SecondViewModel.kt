package dk.adaptmobile.android_seed.screens.secondview

import dk.adaptmobile.android_seed.navigation.BaseViewModel
import dk.adaptmobile.android_seed.screens.secondview.SecondViewModel.*
import io.reactivex.Observable

class SecondViewModel : BaseViewModel<Input, Output>() {

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