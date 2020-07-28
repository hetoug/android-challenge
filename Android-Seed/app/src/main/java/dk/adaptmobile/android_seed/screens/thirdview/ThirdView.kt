package dk.adaptmobile.android_seed.screens.thirdview

import android.app.Activity
import android.view.View
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.navigation.BaseView
import dk.adaptmobile.android_seed.screens.thirdview.ThirdViewModel.*


class ThirdView : BaseView<ThirdViewModel, Output>() {
    override fun setViewModel() = ThirdViewModel()

    override fun inflateView() = R.layout.view_third

    override fun onViewBound(view: View, activity: Activity) {}

    override fun handleOutput(output: Output) {
    }
}