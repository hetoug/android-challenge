package dk.adaptmobile.android_seed.base

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.bluelinelabs.conductor.rxlifecycle2.ControllerEvent
import com.bluelinelabs.conductor.rxlifecycle2.RxController
import dk.adaptmobile.amkotlinutil.extensions.inflate
import dk.adaptmobile.amkotlinutil.extensions.lastController
import dk.adaptmobile.amkotlinutil.extensions.openInBrowser
import dk.adaptmobile.amkotlinutil.extensions.pushView
import dk.adaptmobile.android_seed.screens.Routing
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.*


/**
 * Created by Alex on 5/7/18
 */
abstract class BaseView<T : BaseViewModel> : RxController(), LayoutContainer {

    private val listener: Controller.LifecycleListener
    open lateinit var viewModel: T

    override val containerView: View?
        get() = view

    init {
        listener = object : Controller.LifecycleListener() {

            override fun preCreateView(controller: Controller) {
                super.preCreateView(controller)
                viewModel = setViewModel()
            }

            override fun postCreateView(controller: Controller, view: View) {
                super.postCreateView(controller, view)
                activity?.let { activity ->
                    onViewBound(view, activity)
                    viewModel.routing
                            .compose(bindUntilEvent(ControllerEvent.DESTROY))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { routing ->
                                when (routing) {
                                    is Routing.External -> activity.getString(routing.link).openInBrowser(activity)
                                    else -> router.pushView(routing.controller, routing.animationType, routing.removesFromViewOnPush, routing.retain, routing.asRoot, routing.replace, routing.tag, routing.hidekeyboard)
                                }
                            }

                    viewModel.error
                            .compose(bindUntilEvent(ControllerEvent.DESTROY))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                //TODO: Handle error
                            }

                    viewModel.output
                            .compose(bindUntilEvent(ControllerEvent.DESTROY))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                handleEvent(it)
                            }
                }
            }
        }
        addLifecycleListener(listener)
    }

    fun goBack(amount: Int = 1, data: Any?) {
        if (amount <= 1) {
            router.popCurrentController()
        } else {
            val tempBackStack = router.backstack
            for (i in 1..amount) {
                tempBackStack.removeAt(tempBackStack.lastIndex)
            }
            router.setBackstack(tempBackStack, HorizontalChangeHandler())
        }

        // make callback with data on view under the top one
        data?.let {
            (router.lastController as? BaseView<*>)?.callback(it)
        }
    }

    protected abstract fun setViewModel(): T

    protected abstract fun inflateView(): Int

    protected abstract fun handleEvent(output: BaseViewModel.IOutput)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View = container.inflate(inflateView(), false)

    protected abstract fun onViewBound(view: View, activity: Activity)

    protected open fun callback(data: Any) {}

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        clearFindViewByIdCache()
    }


}

