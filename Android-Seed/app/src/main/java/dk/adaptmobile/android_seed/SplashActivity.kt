package dk.adaptmobile.android_seed;

import android.content.Intent
import android.os.Bundle
import dk.adaptmobile.android_seed.base.SuperActivity

class SplashActivity : SuperActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
        overridePendingTransition(0, 0)
    }

}