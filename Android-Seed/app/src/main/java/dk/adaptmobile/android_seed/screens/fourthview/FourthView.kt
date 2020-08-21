package dk.adaptmobile.android_seed.screens.fourthview

import android.app.Activity
import android.view.View
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.navigation.BaseView
import dk.adaptmobile.android_seed.screens.fourthview.FourthViewModel.*


class FourthView : BaseView<FourthViewModel, Output>() {
    override fun setViewModel() = FourthViewModel()

    override fun inflateView() = R.layout.view_fourth

    override fun onViewBound(view: View, activity: Activity) {}

    override fun handleOutput(output: Output) {
    }
}
