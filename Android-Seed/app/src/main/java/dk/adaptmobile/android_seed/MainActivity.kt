package dk.adaptmobile.android_seed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import dk.adaptmobile.amkotlinutil.extensions.AnimationType
import dk.adaptmobile.amkotlinutil.extensions.pushView
import dk.adaptmobile.android_seed.base.SuperActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : SuperActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var router: Router
    private lateinit var viewModel: MainActivityViewModel
    private val disposeBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = MainActivityViewModel()
        router = Conductor.attachRouter(this, container, savedInstanceState)
        viewModel.firstView.subscribe {
            if (!router.hasRootController()) {
                router.pushView(it.controller, AnimationType.None, asRoot = true)
            }
        }.addTo(disposeBag)
    }

    override fun onBackPressed() {
        if (!router.handleBack()) super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposeBag.isDisposed) {
            disposeBag.dispose()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        router.onActivityResult(requestCode, resultCode, data)
    }
}
