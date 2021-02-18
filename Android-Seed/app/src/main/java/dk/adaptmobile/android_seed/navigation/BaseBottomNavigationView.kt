package dk.adaptmobile.android_seed.navigation

import android.annotation.SuppressLint
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler
import com.bluelinelabs.conductor.rxlifecycle2.ControllerEvent
import com.github.ajalt.timberkt.e
import com.google.android.material.bottomnavigation.BottomNavigationView
import dk.adaptmobile.amkotlinutil.extensions.*
import dk.adaptmobile.android_seed.base.FirstTab
import dk.adaptmobile.android_seed.base.FourthTab
import dk.adaptmobile.android_seed.base.SecondTab
import dk.adaptmobile.android_seed.base.ThirdTab
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

@SuppressLint("CheckResult")
abstract class BaseBottomNavigationView<T : BaseViewModel<*, T2>, T2 : BaseViewModel.IOutput> : BaseView<T, T2>() {
    private lateinit var tabRouter: Router

    fun setupRouting(tabContainer: ChangeHandlerFrameLayout, bottomNavigation: CustomBottomNavigation) {

        tabRouter = getChildRouter(tabContainer)

        NavManager.tabRouting
                .doOnAndroidMain()
                .subscribeBy(
                        onNext = {
                            when (it) {
                                is BaseRouting.GoToRoot -> tabRouter.popToRoot(SimpleSwapChangeHandler(false))
                                is BaseRouting.SetTabRoot -> {
                                    NavManager.setCurrentTabState(tabRouter.getStateAsBundle())
                                    tabRouter.pushView(it.view, AnimationType.None, asRoot = true)
                                }
                                is BaseRouting.OpenTab -> tabRouter.restoreState(it.state)
                                is BaseRouting.Back -> tabRouter.goBack(it.amount, it.data)
                                is BaseRouting.MarkTabAsSelected -> bottomNavigation.selectedItemId = it.menuId
                                else -> tabRouter.pushView(it.controller, it.animationType, asRoot = it.asRoot)
                            }
                        },
                        onError = { e(it) { "Error routing in BaseBottomNavigationView" } }
                )
                .addTo(viewModel.disposeBag)

        bottomNavigation.setOnNavigationItemSelectedListener {
            NavManager.tabSelected(it)
            true
        }

        if (tabRouter.isEmpty()) { // Fresh app state
            NavManager.openTab(SecondTab)
        } else { // Restored app state
            bottomNavigation.onRestoreCallback = {
                when (bottomNavigation.selectedItemId) {
                    FirstTab.id -> NavManager.setupInitialState(FirstTab)
                    SecondTab.id -> NavManager.setupInitialState(SecondTab)
                    ThirdTab.id -> NavManager.setupInitialState(ThirdTab)
                    FourthTab.id -> NavManager.setupInitialState(FourthTab)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NavManager.clearTabState()
    }

    override fun callback(data: Any?) {
        super.callback(data)
        tabRouter.lastController?.let {
            (it as BaseView<*, *>).callback(data)
        }
    }
}
