package dk.adaptmobile.android_seed.screens.firstview

import dk.adaptmobile.android_seed.navigation.BaseViewModel
import dk.adaptmobile.android_seed.screens.firstview.FirstViewModel.*
import io.reactivex.Observable

class FirstViewModel : BaseViewModel<Input, Output>() {

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