package dk.adaptmobile.android_seed.base

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import androidx.multidex.MultiDexApplication
import com.github.ajalt.timberkt.e
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dk.adaptmobile.android_seed.BuildConfig
import dk.adaptmobile.android_seed.managers.PrefsManager
import dk.adaptmobile.android_seed.managers.TrackingManager
import dk.adaptmobile.android_seed.navigation.NavManager
import dk.adaptmobile.android_seed.network.ConnectionManager
import dk.adaptmobile.android_seed.util.CrashlyticsTree
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber


@Suppress("ConstantConditionIf")
@SuppressLint("CheckResult")
class ApplicationController : MultiDexApplication() {

    // We need to keep a reference to the current
    var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityDestroyed(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

            override fun onActivityPaused(activity: Activity) {
                currentActivity = null
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }
        })

        setupLogging()
        setupTabs()
        setupDI()
    }

    private fun setupTabs() {
        val tabs = listOf(FirstTab, SecondTab, ThirdTab, FourthTab)
        NavManager.initTabs(tabs)
    }

    private fun setupLogging() {
        Timber.plant(CrashlyticsTree())

        if (BuildConfig.DEBUG) {
            Timber.plant(LinkingDebugTree())
        }

        //Error handler that will be called when onError is not set.
        RxJavaPlugins.setErrorHandler { e(it) }

        //val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
        throw RuntimeException("CRASH")
    }

    private fun setupDI() {
        val module = module {
            single { PrefsManager(get()) }
            single { ConnectionManager() }
            single { NotificationManagerCompat.from(get()) }
            single { TrackingManager(this@ApplicationController, get()) }
            factory { FirebaseAnalytics.getInstance(get()) }
        }

        startKoin {
            androidContext(this@ApplicationController.applicationContext)
            modules(module)
        }
    }
}

class LinkingDebugTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
        // because Robolectric runs them on the JVM but on Android the elements are different.
        val stackTrace = Throwable().stackTrace
        if (stackTrace.size <= CALL_STACK_INDEX) {
            throw IllegalStateException(
                    "Synthetic stacktrace didn't have enough elements: are you using proguard?")
        }
        val clazz = extractClassName(stackTrace[CALL_STACK_INDEX])
        val lineNumber = stackTrace[CALL_STACK_INDEX].lineNumber
        val logMessage = ".($clazz.kt:$lineNumber) - $message"
        super.log(priority, tag, logMessage, t)
    }

    /**
     * Extract the class name without any anonymous class suffixes (e.g., `Foo$1`
     * becomes `Foo`).
     */
    private fun extractClassName(element: StackTraceElement): String {
        var tag = element.className
        tag = tag.split("$")[0]
        return tag.substring(tag.lastIndexOf('.') + 1)
    }

    companion object {
        private const val CALL_STACK_INDEX = 5
    }
}
