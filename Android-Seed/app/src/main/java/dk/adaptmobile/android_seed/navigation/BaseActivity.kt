package dk.adaptmobile.android_seed.navigation

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.*
import com.github.ajalt.timberkt.e
import com.greysonparrelli.permiso.Permiso
import dk.adaptmobile.amkotlinutil.extensions.doOnAndroidMain
import dk.adaptmobile.amkotlinutil.extensions.isNotEmpty
import dk.adaptmobile.amkotlinutil.extensions.lastController
import dk.adaptmobile.amkotlinutil.extensions.pushView
import dk.adaptmobile.android_seed.managers.PrefsManager
import dk.adaptmobile.android_seed.screens.Routing
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseActivity : AppCompatActivity(), KoinComponent {
    private lateinit var mainRouter: Router
    private lateinit var modalRouter: Router
    private var modalData: Any? = null
    private val disposeBag = CompositeDisposable()

    val prefsManager: PrefsManager by inject()

    protected fun setup(mainContainer: ChangeHandlerFrameLayout, modalContainer: ChangeHandlerFrameLayout, savedInstanceState: Bundle?) {
        mainRouter = Conductor.attachRouter(this, mainContainer, savedInstanceState, null)
        modalRouter = Conductor.attachRouter(this, modalContainer, savedInstanceState) {
            if (it.backstackSize > 0) {
                NavManager.setModalRoutingAsCurrent()
            }
        }

        modalRouter.setPopsLastView(true) // We want it to be possible to pop the last view on the modal controller only

        subscribeToRouting(NavManager.mainRouting, mainRouter)
        subscribeToRouting(NavManager.modalRouting, modalRouter)

        // We listen to changes on the modal router. When the router is empty we
        // 1) clear the routing state in NavManager
        // 2) Sends callback data from the modal router to the main router, in order to update the views below the modal router
        modalRouter.addChangeListener(object : ControllerChangeHandler.ControllerChangeListener {
            override fun onChangeStarted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup, handler: ControllerChangeHandler) {}

            override fun onChangeCompleted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup, handler: ControllerChangeHandler) {
                if (!isPush && modalRouter.backstackSize == 0) {
                    NavManager.clearCurrentRouting()

                    val mainController = mainRouter.lastController as? BaseView<*, *>
                    mainController?.callback(modalData)
                    modalData = null

                    if (prefsManager.reviewShouldShow) {
                        //NavManager.openModally(Routing.ReviewDialog())
                        prefsManager.reviewShouldShow = false
                    }
                }
            }
        })
    }

    private fun subscribeToRouting(routing: PublishSubject<BaseRouting>, router: Router) {
        routing
                .doOnAndroidMain()
                .subscribe(
                        {
                            when (it) {
                                is BaseRouting.Back -> {
                                    if (routing == NavManager.modalRouting) {
                                        modalData = when (router.backstackSize) {
                                            1 -> it.data
                                            else -> null
                                        }
                                    }
                                    if (!router.handleBack()) {
                                        router.goBack(it.amount, it.data)
                                    }
                                }
                                is BaseRouting.CloseAll -> {
                                    if (router.isNotEmpty()) {
                                        modalData = it.data
                                        router.goBack(router.backstackSize, it.data)
                                    }
                                }

                                else -> {
                                    if (modalRouter.backstackSize <= 0 || it.controller?.javaClass != modalRouter.lastController?.javaClass) {
                                        router.pushView(it.controller, it.animationType, asRoot = it.asRoot, retain = it.retain)
                                    }
                                }
                            }
                        },
                        {
                            e(it) { "Error subscribing" }
                        }
                )
                .addTo(disposeBag)
    }

    override fun onBackPressed() {
        if (modalRouter.backstackSize > 1) {
            modalRouter.handleBack()
        } else if (modalRouter.backstackSize == 1) {
            // If the current controller has handleBack == true, we should NOT close the modal view,
            // since it allows the controller to close itself
            if (modalRouter.lastController?.handleBack() == false) {
                NavManager.closeModal()
            }
        } else if (!mainRouter.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Permiso.getInstance().setActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        NavManager.activityDestroyed()
        if (!disposeBag.isDisposed) {
            disposeBag.dispose()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // onActivityResult should be only called on the mainRouter. Adding more router adds more onActivityResult callbacks to the Conductor views.
        // Adding more router introduced a problem logging in with facebook by doubling the network calls.
        mainRouter.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults)
    }
}
