package dk.adaptmobile.android_seed.screens.thirdview

import dk.adaptmobile.android_seed.navigation.BaseViewModel
import dk.adaptmobile.android_seed.screens.thirdview.ThirdViewModel.*
import io.reactivex.rxjava3.core.Observable


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