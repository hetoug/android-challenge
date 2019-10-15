package dk.adaptmobile.android_seed

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class SplashActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
        overridePendingTransition(0, 0)
    }
}
