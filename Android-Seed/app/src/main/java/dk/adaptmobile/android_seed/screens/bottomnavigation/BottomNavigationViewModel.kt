package dk.adaptmobile.android_seed.screens.bottomnavigation

import android.annotation.SuppressLint
import dk.adaptmobile.android_seed.navigation.BaseViewModel
import dk.adaptmobile.android_seed.screens.bottomnavigation.BottomNavigationViewModel.*


@SuppressLint("CheckResult")
class BottomNavigationViewModel : BaseViewModel<Input, Output>() {

    class Input : IInput()

    sealed class Output : IOutput()

    override fun init() {}

    override fun handleInput(input: Input) {
    }
}
