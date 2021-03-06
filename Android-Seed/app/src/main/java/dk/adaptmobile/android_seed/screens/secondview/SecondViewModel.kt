package dk.adaptmobile.android_seed.screens.secondview

import dk.adaptmobile.amkotlinutil.extensions.subscribeToInput
import dk.adaptmobile.android_seed.model.NewsApiArticle
import dk.adaptmobile.android_seed.navigation.BaseViewModel
import dk.adaptmobile.android_seed.network.FetchNewsUseCase
import dk.adaptmobile.android_seed.screens.secondview.SecondViewModel.Input
import dk.adaptmobile.android_seed.screens.secondview.SecondViewModel.Output
import dk.adaptmobile.android_seed.usecases.BaseUseCase.UseCaseResult
import io.reactivex.rxjava3.core.Observable
import org.koin.core.inject

class SecondViewModel : BaseViewModel<Input, Output>() {

    sealed class Output : BaseViewModel.IOutput() {
        object NoNews : Output()
        data class ShowNewsFeed(val data: List<NewsApiArticle>) : Output()
    }

    sealed class Input : BaseViewModel.IInput() {
        data class Events(
                val getNewsClicked: Observable<Unit>
        ) : Input()
    }

    private val fetchNewsUseCase: FetchNewsUseCase by inject()

    override fun init() {
    }

    override fun handleInput(input: Input) {
        when (input) {
            is Input.Events -> handleEvents(input)
        }
    }

    private fun handleEvents(input: Input.Events) {
        input.getNewsClicked.flatMapSingle { fetchNewsUseCase() }.subscribeToInput(disposeBag) {
            when (it) {
                is UseCaseResult.Success -> {
                    output.onNext(Output.ShowNewsFeed(it.body))
                }
                is UseCaseResult.Failure -> {
                    output.onNext(Output.NoNews)
                }
            }
        }
    }
}