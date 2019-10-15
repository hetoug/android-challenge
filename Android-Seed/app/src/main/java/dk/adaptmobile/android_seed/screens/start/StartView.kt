package dk.adaptmobile.android_seed.screens.start

import android.app.Activity
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import dk.adaptmobile.amkotlinutil.extensions.visible
import dk.adaptmobile.amkotlinutil.navigation.BaseView
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.screens.start.StartViewModel.Input
import dk.adaptmobile.android_seed.screens.start.StartViewModel.Output
import kotlinx.android.synthetic.main.view_start.*

/**
 * Created by Alex on 5/8/18
 */
class StartView : BaseView<StartViewModel, Output>() {
    override fun setViewModel() = StartViewModel()

    override fun inflateView(): Int = R.layout.view_start

    override fun onViewBound(view: View, activity: Activity) {
        viewModel.input.onNext(Input(button.clicks()))
    }

    override fun handleOutput(output: Output) {
        when (output) {
            is Output.ShowText -> text.visible()
        }
    }
}
