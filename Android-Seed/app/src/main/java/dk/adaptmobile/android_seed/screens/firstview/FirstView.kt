package dk.adaptmobile.android_seed.screens.firstview

import android.app.Activity
import android.view.View
import com.jakewharton.rxbinding4.view.clicks
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.navigation.BaseView
import dk.adaptmobile.android_seed.screens.firstview.FirstViewModel.*
import kotlinx.android.synthetic.main.view_first.*
import org.koin.core.KoinComponent


class FirstView : BaseView<FirstViewModel, Output>() {
    override fun setViewModel() = FirstViewModel()

    override fun inflateView() = R.layout.view_first

    override fun onViewBound(view: View, activity: Activity) {
        viewModel.input.onNext(Input.Events(firstButton.clicks()))
    }

    override fun handleOutput(output: Output) {
        when (output) {
            is Output.UpdateText -> firstTextView.text = output.text
        }
    }
}