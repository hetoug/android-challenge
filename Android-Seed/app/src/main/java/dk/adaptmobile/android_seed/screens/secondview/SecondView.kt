package dk.adaptmobile.android_seed.screens.secondview

import android.app.Activity
import android.view.View
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.navigation.BaseView
import dk.adaptmobile.android_seed.screens.secondview.SecondViewModel.*


class SecondView : BaseView<SecondViewModel, Output>() {
    override fun setViewModel() = SecondViewModel()

    override fun inflateView() = R.layout.view_second

    override fun onViewBound(view: View, activity: Activity) {}

    override fun handleOutput(output: Output) {
    }
}