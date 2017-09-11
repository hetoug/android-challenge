package dk.adaptmobile.android_seed

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import dk.adaptmobile.amkotlinutil.util.bindView
import dk.adaptmobile.amutil.misc.AMUtil

class MainActivity : AppCompatActivity() {

    private lateinit var router: Router
    private val container: ChangeHandlerFrameLayout by bindView(R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        AMUtil.isFirstLaunch(this)

        router = Conductor.attachRouter(this, container, savedInstanceState)

        if (!router.hasRootController()) {
//            router.setRoot(RouterTransaction.with(OnboardingView()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        router.onActivityResult(requestCode, resultCode, data)
    }

}
