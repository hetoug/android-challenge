package dk.adaptmobile.android_seed

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import dk.adaptmobile.amutil.misc.AMUtil
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        AMUtil.isFirstLaunch(this)

        router = Conductor.attachRouter(this, container, savedInstanceState)
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
