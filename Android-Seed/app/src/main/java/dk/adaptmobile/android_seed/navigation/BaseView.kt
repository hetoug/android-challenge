package dk.adaptmobile.android_seed.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.bluelinelabs.conductor.rxlifecycle2.ControllerEvent
import com.bluelinelabs.conductor.rxlifecycle2.RxRestoreViewOnCreateController
import com.github.ajalt.timberkt.e
import dk.adaptmobile.amkotlinutil.extensions.disposeSafe
import dk.adaptmobile.amkotlinutil.extensions.doOnAndroidMain
import dk.adaptmobile.amkotlinutil.extensions.inflate
import dk.adaptmobile.amkotlinutil.extensions.lastController
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.*

@SuppressLint("CheckResult")
abstract class BaseView<T : BaseViewModel<*, T2>, T2 : BaseViewModel.IOutput> : RxRestoreViewOnCreateController(), LayoutContainer {

    private var outputDisposable: Disposable? = null
    open var savedViewState: Bundle? = null
    private val listener: LifecycleListener
    open lateinit var viewModel: T

    override val containerView: View?
        get() = view

    init {
        listener = object : LifecycleListener() {

            override fun preCreateView(controller: Controller) {
                super.preCreateView(controller)

                viewModel = setViewModel()
                initViewModel()
                        .subscribeOn(Schedulers.computation())
                        .subscribe()
            }

            private fun initViewModel(): Completable {
                return Completable.create {
                    viewModel.init()
                    it.onComplete()
                }
            }

            override fun onChangeEnd(controller: Controller, changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
                super.onChangeEnd(controller, changeHandler, changeType)

                // Skip when navigating away from view (Prevents events being dispatched twice)
                if (!changeType.isEnter) {
                    return
                }

                if (outputDisposable != null) return

                outputDisposable = viewModel.output
                        .compose(bindUntilEvent(ControllerEvent.DESTROY))
                        .doOnAndroidMain()
                        .subscribe(
                                {
                                    if (controller.isAttached) {
                                        handleOutput(it)
                                    }
                                },
                                {
                                    e(it) { "Error in BaseView output" }
                                }
                        )
            }

            override fun postCreateView(controller: Controller, view: View) {
                super.postCreateView(controller, view)
                activity?.let { activity ->
                    onViewBound(view, activity)
                }
            }
        }
        addLifecycleListener(listener)
    }

    protected abstract fun setViewModel(): T

    protected abstract fun inflateView(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        this.savedViewState = savedViewState
        return container.inflate(inflateView(), false)
    }

    protected abstract fun onViewBound(view: View, activity: Activity)

    protected abstract fun handleOutput(output: T2)

    open fun callback(data: Any?) {}

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        clearFindViewByIdCache()
        viewModel.disposeBag.disposeSafe()
        outputDisposable?.dispose()
        outputDisposable = null
    }
}

fun Router.goBack(amount: Int = 1, data: Any? = null) {
    if (amount <= 1 && backstackSize > 1) {
        popCurrentController()
    } else {
        val tempBackStack = backstack
        for (i in 1..amount) {
            tempBackStack.removeAt(tempBackStack.lastIndex)
        }
        setBackstack(tempBackStack, HorizontalChangeHandler())
    }

    // make callback with data on view under the top one
    data?.let {
        (lastController as? BaseView<*, *>)?.callback(it)
    }
}
