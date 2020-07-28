package dk.adaptmobile.android_seed.screens

import dk.adaptmobile.amkotlinutil.extensions.AnimationType
import dk.adaptmobile.android_seed.navigation.BaseRouting
import dk.adaptmobile.android_seed.navigation.BaseView
import dk.adaptmobile.android_seed.screens.bottomnavigation.BottomNavigationView



sealed class Routing(
        controller: BaseView<*, *>? = null,
        animationType: AnimationType = AnimationType.Slide,
        retain: Boolean = false,
        asRoot: Boolean = false
) : BaseRouting(controller, animationType, asRoot, retain) {

    class BottomNavigation : Routing(BottomNavigationView(), animationType = AnimationType.None)

}
