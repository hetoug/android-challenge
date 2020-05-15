package dk.adaptmobile.android_seed.screens.bottomnavigation

import android.app.Activity
import android.view.View

import dk.adaptmobile.amkotlinutil.conductor.ArgumentDelegate
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.navigation.BaseBottomNavigationView
import dk.adaptmobile.android_seed.screens.bottomnavigation.BottomNavigationViewModel.*
import kotlinx.android.synthetic.main.view_bottom_navigation.*

class BottomNavigationView() : BaseBottomNavigationView<BottomNavigationViewModel, Output>() {

    private var tabId: Int by ArgumentDelegate()

    constructor(tabId: Int) : this() {
        this.tabId = tabId
    }

    override fun setViewModel() = BottomNavigationViewModel()

    override fun inflateView(): Int = R.layout.view_bottom_navigation

    override fun onViewBound(view: View, activity: Activity) {
        super.setupRouting(tabContainer, bottomNavigation)
    }

    override fun handleOutput(output: Output) {
        when (output) {
        }
    }
}
