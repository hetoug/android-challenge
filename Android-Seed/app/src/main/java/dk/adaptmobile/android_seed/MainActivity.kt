package dk.adaptmobile.android_seed

import android.os.Bundle
import com.bluelinelabs.conductor.Conductor
import dk.adaptmobile.amkotlinutil.navigation.NavManager
import dk.adaptmobile.android_seed.base.RemoteConfigActivity
import dk.adaptmobile.android_seed.screens.Routing
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RemoteConfigActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainRouter = Conductor.attachRouter(this, mainContainer, savedInstanceState)
        modalRouter = Conductor.attachRouter(this, modalContainer, savedInstanceState)
        modalRouter.setPopsLastView(true) //We want it to be possible to pop the last view on the modal controller only

        handleRouting()

        NavManager.open(Routing.Start())
    }

    override fun showMinimumVersionDialog() {
        super.showMinimumVersionDialog()
//        NavManager.openModally(Routing.MinimumVersionDialog())
    }

}
