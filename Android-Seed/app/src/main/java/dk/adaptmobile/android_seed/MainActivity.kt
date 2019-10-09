package dk.adaptmobile.android_seed

import android.os.Bundle
import dk.adaptmobile.android_seed.MainActivityViewModel.Input
import dk.adaptmobile.android_seed.base.RemoteConfigActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RemoteConfigActivity() {

    private val viewModel = MainActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        super.setup(mainContainer, modalContainer, savedInstanceState)

        viewModel.input.onNext(Input.Init)
    }

    override fun showMinimumVersionDialog() {
        super.showMinimumVersionDialog()
        viewModel.input.onNext(Input.MinimumVersionDialog)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.input.onNext(Input.OnDestroy)
    }

}
