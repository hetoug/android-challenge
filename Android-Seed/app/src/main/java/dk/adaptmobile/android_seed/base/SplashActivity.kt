package dk.adaptmobile.android_seed.base

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.github.ajalt.timberkt.e
import com.google.firebase.FirebaseApp
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dk.adaptmobile.amkotlinutil.extensions.DisposeBag
import dk.adaptmobile.amkotlinutil.extensions.disposeSafe
import dk.adaptmobile.amkotlinutil.extensions.doOnAndroidMain
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.managers.PrefsManager
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.core.KoinComponent
import org.koin.core.inject

@SuppressLint("CheckResult")
class SplashActivity : FragmentActivity(), KoinComponent {

    private val prefsManager: PrefsManager by inject()
    private val disposeBag = DisposeBag()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        FirebaseApp.initializeApp(this)

        startMainActivity()

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    // Get deep link from result (may be null if no link is found)
                    var deepLink: Uri? = null
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.link
                    }
                }
                .addOnFailureListener(this) { e(it) { "Error reading dynamic link" } }
    }

    private fun startMainActivity() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.putExtras(getIntent())
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onStop() {
        super.onStop()
        disposeBag.disposeSafe()
    }
}
