package dk.adaptmobile.android_seed.screens.browser

import android.app.Activity
import android.view.View
import com.jakewharton.rxbinding4.view.clicks
import dk.adaptmobile.amkotlinutil.conductor.ArgumentDelegate
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.extensions.events
import dk.adaptmobile.android_seed.navigation.BaseView
import dk.adaptmobile.android_seed.screens.browser.BrowserViewModel.*
import kotlinx.android.synthetic.main.view_browser.*

class BrowserView() : BaseView<BrowserViewModel, Output>() {

    private var url: String by ArgumentDelegate()

    constructor(url: String) : this() {
        this.url = url
    }

    override fun setViewModel() = BrowserViewModel(url)

    override fun inflateView() = R.layout.view_browser

    override fun onViewBound(view: View, activity: Activity) {
        viewModel.input.onNext(
                Input.Events(
                        backClicked = browserBackButton.clicks(),
                        webviewEvents = browserWebView.events()
                )
        )

        browserWebView.settings.javaScriptEnabled = true
    }

    override fun handleOutput(output: Output) {
        when (output) {
            is Output.LoadUrl -> browserWebView.loadUrl(output.url)
        }
    }
}