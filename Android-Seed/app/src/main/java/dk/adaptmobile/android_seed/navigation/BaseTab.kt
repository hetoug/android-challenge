package dk.adaptmobile.android_seed.navigation

import android.os.Bundle
import androidx.annotation.IdRes

abstract class BaseTab(@IdRes val id: Int, var initalView: () -> BaseView<*, *>, var state: Bundle? = null)
