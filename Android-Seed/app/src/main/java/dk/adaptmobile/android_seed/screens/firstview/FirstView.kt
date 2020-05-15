package dk.adaptmobile.android_seed.screens.firstview

import android.app.Activity
import android.view.View
import com.github.ajalt.timberkt.e
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.navigation.BaseView
import dk.adaptmobile.android_seed.screens.`$1firstview`.FirstViewModel
import java.lang.NullPointerException
import java.lang.RuntimeException

class FirstView : BaseView<FirstViewModel, FirstViewModel.Output>() {
    override fun setViewModel() = FirstViewModel()

    override fun inflateView() = R.layout.view_first

    override fun onViewBound(view: View, activity: Activity) {
        //e { "Crash from error"}

    }

    override fun handleOutput(output: FirstViewModel.Output) {
    }
}