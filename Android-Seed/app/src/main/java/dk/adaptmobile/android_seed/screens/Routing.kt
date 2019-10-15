package dk.adaptmobile.android_seed.screens

import dk.adaptmobile.amkotlinutil.extensions.AnimationType
import dk.adaptmobile.amkotlinutil.navigation.BaseRouting
import dk.adaptmobile.amkotlinutil.navigation.BaseView
import dk.adaptmobile.android_seed.screens.start.StartView

sealed class Routing(
    controller: BaseView<*, *>? = null,
    animationType: AnimationType = AnimationType.Slide,
    retain: Boolean = false,
    asRoot: Boolean = false
) : BaseRouting(controller, animationType, asRoot, retain) {
    class Start : Routing(StartView())
}
