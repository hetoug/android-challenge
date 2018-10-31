package dk.adaptmobile.android_seed.screens.test

import android.app.Activity
import android.view.View
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.base.BaseView

/**
 * Created by Alex on 5/8/18
 */
class TestView : BaseView<TestViewModel>() {

    override fun setViewModel(): TestViewModel = TestViewModel(this)

    override fun inflateView(): Int = R.layout.view_test

    override fun onViewBound(view: View, activity: Activity) {
        bindViewModel()
    }

    private fun bindViewModel() {

    }
}