package dk.adaptmobile.android_seed.screens.start

import android.app.Activity
import android.view.View
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.base.BaseView

/**
 * Created by Alex on 5/8/18
 */
class StartView : BaseView<StartViewModel>() {

    override fun setViewModel(): StartViewModel = StartViewModel(this)

    override fun inflateView(): Int = R.layout.view_start

    override fun onViewBound(view: View, activity: Activity) {

    }

}