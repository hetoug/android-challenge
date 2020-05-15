package dk.adaptmobile.android_seed.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import com.github.ajalt.timberkt.e
import dk.adaptmobile.amkotlinutil.extensions.doOnAndroidMain
import dk.adaptmobile.amkotlinutil.extensions.minutesInMillis
import dk.adaptmobile.amkotlinutil.extensions.wait
import dk.adaptmobile.amkotlinutil.model.PostDelay
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.base.MainActivityViewModel.*
import dk.adaptmobile.android_seed.managers.ReviewManager
import dk.adaptmobile.android_seed.managers.TrackingManager

import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : RemoteConfigActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private var reviewTimer: PostDelay? = null
    private val trackingManager: TrackingManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trackingManager.subscribeTracking()

        viewModel = MainActivityViewModel()
        super.setup(mainContainer, modalContainer, savedInstanceState)

        viewModel.input.onNext(Input.Init(savedInstanceState != null))

        subscribeOutput()
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent ?: return
        viewModel.input.onNext(Input.OnNewIntent(intent))
    }

    override fun showMinimumVersionDialog() {
        super.showMinimumVersionDialog()
        viewModel.input.onNext(Input.MinimumVersionDialog)
    }

    override fun onMinimumVersionChecked() {
        super.onMinimumVersionChecked()

        reviewTimer?.cancel()
        // val waitPeriod = if (BuildConfig.DEBUG) 10.secondsInMillis else 5.minutesInMillis // for debugging review
        val waitPeriod = 5.minutesInMillis
        reviewTimer = wait(waitPeriod) {
            ReviewManager.afterFiveMinutes()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.input.onNext(Input.OnDestroy)
    }

    @SuppressLint("CheckResult")
    fun subscribeOutput() {
        viewModel.output
                .doOnAndroidMain()
                .subscribe(
                        {
                            handleOutput(it)
                        },
                        {
                            e(it) { "Error in MainActivity output" }
                        }
                )
    }

    private fun handleOutput(output: Output) {
        when (output) {
            //is Output.ShowLoading -> this.showLoadingBar(output.show)
        }
    }

    override fun onPause() {
        super.onPause()
        reviewTimer?.cancel()
    }
}
