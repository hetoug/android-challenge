package dk.adaptmobile.android_seed.screens.start

import android.app.Activity
import android.view.View
import dk.adaptmobile.amkotlinutil.extensions.visible
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.base.BaseView
import dk.adaptmobile.android_seed.base.BaseViewModel
import kotlinx.android.synthetic.main.view_start.*

/**
 * Created by Alex on 5/8/18
 */
class StartView : BaseView<StartViewModel>() {
    override fun setViewModel() = StartViewModel(this)

    override fun inflateView(): Int = R.layout.view_start

    override fun onViewBound(view: View, activity: Activity) {
        button.setOnClickListener {
            viewModel.input.onNext(StartViewModel.Input.ButtonClicked)
        }
    }

    override fun handleEvent(output: BaseViewModel.IOutput) {
        when (output) {
            is StartViewModel.Output.ShowText -> text.visible()
        }
    }
}
