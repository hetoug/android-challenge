package dk.adaptmobile.android_seed.screens.firstview

import android.app.Activity
import android.view.View
import com.github.ajalt.timberkt.e
import com.icapps.niddler.retrofit.NiddlerRetrofitCallInjector.inject
import dk.adaptmobile.amkotlinutil.extensions.doOnAndroidMain
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.navigation.BaseView
import dk.adaptmobile.android_seed.network.ConnectionManager
import dk.adaptmobile.android_seed.screens.firstview.FirstViewModel.*
import org.koin.core.KoinComponent
import org.koin.core.inject


class FirstView : BaseView<FirstViewModel, Output>(), KoinComponent {
    private val connectionManager: ConnectionManager by inject()
    override fun setViewModel() = FirstViewModel()

    override fun inflateView() = R.layout.view_first

    override fun onViewBound(view: View, activity: Activity) {
        connectionManager.restService.getRequest().doOnAndroidMain().subscribe()

    }

    override fun handleOutput(output: Output) {
    }
}