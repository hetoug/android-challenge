package dk.adaptmobile.android_seed.screens

import com.bluelinelabs.conductor.Controller
import dk.adaptmobile.amkotlinutil.extensions.AnimationType
import dk.adaptmobile.android_seed.screens.test.TestView

sealed class Routing(
        val controller: Controller? = null,
        val animationType: AnimationType = AnimationType.Slide,
        val removesFromViewOnPush: Boolean = true,
        val retain: Boolean = false,
        open val asRoot: Boolean = false,
        open val replace: Boolean = false,
        open val tag: String? = null,
        val hidekeyboard: Boolean = true
) {
    object Test : Routing(TestView())
    data class External(val link: Int) : Routing()
}