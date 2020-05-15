package dk.adaptmobile.android_seed.navigation

import android.os.Bundle
import dk.adaptmobile.amkotlinutil.extensions.AnimationType

abstract class BaseRouting(
    val controller: BaseView<*, *>? = null,
    val animationType: AnimationType = AnimationType.Slide,
    open val asRoot: Boolean = false,
    val retain: Boolean = false
) {
    // Generic routing
    class Back(val amount: Int = 1, val data: Any? = null) : BaseRouting()
    class CloseAll(val data: Any? = null) : BaseRouting()

    // Tab routing
    class OpenTab(val state: Bundle) : BaseRouting()
    class SetTabRoot(val view: BaseView<*, *>) : BaseRouting()
    class GoToRoot : BaseRouting()
    class MarkTabAsSelected(val menuId: Int) : BaseRouting()
}
